package uk.ac.ed.inf.powergrab;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.mapbox.geojson.Point;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.LineString;

public class App {
   	
//main class for the project, where stuff is actually run
	
	public static void main (String args[]) throws MalformedURLException, IOException {
		
		System.out.println("Initialising powergrab...");
		
		// parse out arguments
	    String d = args[0];
	    String m = args[1];
	    String y = args[2];
	    Double lat = Double.parseDouble(args[3]);
	    Double lon = Double.parseDouble(args[4]);
	    Position startPos = new Position(lat, lon);
		int seed = Integer.parseInt(args[5]);
		String version = args[6];
		
		
	   
		List<String> txtOutput = new ArrayList<String>(); //prepare txt output container
		
		//get map data from web-server
		String mapSource = getMapSource(d,m,y);	
		FeatureCollection mapJson = FeatureCollection.fromJson(mapSource);
		
		List<Feature> features = mapJson.features(); // list of features that will be written to the json
		ArrayList<Station> allStations = new ArrayList<Station>(); //list of all the stations in the game
		allStations = unpackFeatures(features);
		
		Drone drone = initDrone(startPos, seed, version, allStations);	
		
		
		//initialise critical variables for the game
	    int  moves = 250;
	    double coins = 0;
	    double power = 250;
		 
	    System.out.println("Running PowerGrab...");
	    
		// --------------------BEGIN MAIN GAME LOOP-------------------------
		while (moves > 0 && power > 1.25) {	
			
			Station destination = null;
		
			Position currentPos = drone.getPosition();
			Direction nextMove = null;
			
			//-----STATELESS DRONE ------
			if (drone instanceof StatelessDrone) {
				nextMove = ((StatelessDrone) drone).calculateDirection(allStations);
			}
			
			
			//-----STATEFUL DRONE -----
			else {
				
				//if the drone has already visited all the stations then it just goes back and 
				//	forth to avoid danger
				if (((StatefulDrone) drone).getOrderedStations().isEmpty()) {
					
					destination = new Station (null,currentPos,0,0,null);
					
				} else {
					
					destination = ((StatefulDrone) drone).getOrderedStations().get(0);			    
				}
	
				nextMove = ((StatefulDrone) drone).decideDirection(destination);			
			}
			
			//update drones position and 
			drone.setPosition(drone.getPosition().nextPosition(nextMove));
			Position nextPos = drone.getPosition();
		
			
			//-----CHARGING BLOCK-----
			//when a drone charges from a station it drains the station of its coins and power
			Station chargeStation = getStationToChargeFrom(drone.getPosition(), allStations);
			
			// if there is a nearest station to charge from, charge from it and update the map
			if (chargeStation.getId().equals("") == false) {
				
				// if the station the drone is charging from is the target of the stateful drone, then remove it 
				// from the list of stations to visit
				if (chargeStation == destination) {
					((StatefulDrone) drone).getOrderedStations().remove(destination);
				}
				
				coins += chargeStation.getCoins();
				power += chargeStation.getPower();
				
				//this block updates the station that has been charged from
				allStations = updateMap(chargeStation, allStations);
			}
			
			
			//-----GENERATE OUTPUT-----
			//.txt output
			txtOutput.add(
					Double.toString(currentPos.getLatitude())+
					","+Double.toString(currentPos.getLongitude())+
					","+nextMove.toString()+
					","+Double.toString(nextPos.getLatitude())+
					","+Double.toString(nextPos.getLongitude())+
					","+coins+
					","+power);
			//geo-json output
			features.add(makeLine(currentPos, nextPos));
			
			
			//some stuff to help debug
			System.out.println("moves left:" + moves);
			System.out.println(txtOutput.get(250-moves));
			System.out.println();
			
			
			moves--;
			power -= 1.25;
		
		}
		//---------------END OF MAIN LOOP ----------------------
		
		//messages to confirm game is finished
		System.out.println("PowerGrab Complete!");
		System.out.println("Creating output files...");
		
		
		
		//---------------Create Output Files---------------------
		
		//Path jsonFile = Paths.get(version+"-"+d+"-"+m+"-"+y+".geojson");
		Path txtFile  = Paths.get(version+"-"+d+"-"+m+"-"+y+".txt");
								
		// turns features into a list of strings so it can be written to a file
		FeatureCollection jsonOut = FeatureCollection.fromFeatures(features);
		String jsonString = jsonOut.toJson();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(version+"-"+d+"-"+m+"-"+y+".geojson"));
		writer.write(jsonString);
		writer.close();
		
		System.out.println(jsonString);
		//Files.write(jsonFile, (String)jsonString, Charset.defaultCharset());
		Files.write(txtFile,txtOutput, Charset.defaultCharset());
		
		System.out.println("Output files created, check the source folder.");
	}

	
	
	//returns a new map with the given stations "coins" and "power" attributes updated
	public static ArrayList<Station> updateMap (Station chargeStation, ArrayList<Station> allStations){
		
		for (Station station : allStations) {
			if (station.getId().equals(chargeStation.getId())) {
				station.setPower(0);
				station.setCoins(0);
			}
		}
		return allStations;
	}
	
	
	// if the given position is in range of any stations, it charges from the closest one, else it returns a dummy value
	public static Station getStationToChargeFrom(Position dronePos, ArrayList<Station> allStations){
		
	    double stationRange = 0.00025;// aoe of the charging stations
	    
	    Position dummy = new Position (100,100);
	    Station closest = new Station("", dummy, 0, 0, "");
	    
	    for (Station current : allStations) {
	    	
	    	double nextDist = distance(dronePos, current.getPosition());
	    	
	    	double currentDist = distance(dronePos, closest.getPosition());
	    	
	    	if ( nextDist < stationRange && nextDist < currentDist) {
	    		closest = current;
	    	}
	    }    
	   return closest;  
	}
	
	
	//calculates the distance between the given positions
	public static double distance(Position pos1, Position pos2) {       
        return Math.sqrt( 
        		Math.pow((pos1.getLatitude() - pos2.getLatitude()),2) 
        		+ Math.pow((pos1.getLongitude() - pos2.getLongitude()),2) );    
    }
	
	
	// returns a list representation of all the features in the geo-json file
	public static ArrayList<Station> unpackFeatures(List<Feature> features) {
		
		ArrayList<Station> stations = new ArrayList<Station>();
		
		for (Feature feature : features ) {
			String id = feature.getProperty("id").getAsString();
			Position pos = unpackPosition(feature);
			double coins = feature.getProperty("coins").getAsDouble();
			double power = feature.getProperty("power").getAsDouble();
			String marker = feature.getProperty("marker-symbol").getAsString();
			
			stations.add(new Station(id, pos, coins, power, marker));
		
		}
		return stations;
	}

	
	//returns the position associated with the given feature (if its a Point)
    public static Position unpackPosition(Feature feature) {
		
		Point point ;
		if (feature.geometry() instanceof Point) { // makes sure we actually get an object thats a Point
			point = (Point) feature.geometry(); //cast to a point
		}
		else {
			return null;
		}
		Position station = new Position(point.latitude(), point.longitude());
		return station;
	}
	
    
    //returns a new feature representing a line between the given points
	public static Feature makeLine(Position currPos, Position nextPos) {
		List<Point> linePoints = new ArrayList<Point>(); //initialise structure to contain the points to draw the line on the geojson
		linePoints.add(Point.fromLngLat(currPos.getLongitude(), currPos.getLatitude()));
		linePoints.add(Point.fromLngLat(nextPos.getLongitude(), nextPos.getLatitude()));		
		Feature newLine = Feature.fromGeometry(LineString.fromLngLats(linePoints));
		
		return newLine;
	}
	
	
	//returns an initialised drone based on what version the game is to run (stateless or stateful)
	public static Drone initDrone(Position startPos, int seed, String version, ArrayList<Station> allStations) {
		Drone drone;
		if (version.equals("stateless")) { 
			drone = new StatelessDrone(startPos, seed);
		}else {
		    drone = new StatefulDrone(startPos, allStations);
		}
		
		return drone;
	}
	
	
	// fetches the geo-json file from the webserver and then returns it as a string
	public static String getMapSource(String d,String m, String y) throws MalformedURLException, IOException {
	    String mapString = "http://www.homepages.inf.ed.ac.uk/stg/powergrab/"
	    						+y+"/"+m+"/"+d+"/powergrabmap.geojson";
        
        //create URL
        URL mapURL = new URL(mapString);
        //open URL connection
        URLConnection conn = mapURL.openConnection();
        //cast to a http URL
        HttpURLConnection connHttp = (HttpURLConnection) conn;
        
        //setup URL connection
        connHttp.setReadTimeout(10000); //milliseconds
        connHttp.setConnectTimeout(15000); // milliseconds
        connHttp.setRequestMethod("GET"); //sets Http method to GET
        connHttp.setDoInput(true); //set URL connection type to input
        connHttp.connect(); 
        
        //read json data from url
        InputStream in = connHttp.getInputStream(); //get input stream from url
        StringWriter writer = new StringWriter();  
        IOUtils.copy(in, writer, "UTF-8"); 
        String mapSource = writer.toString();
        
        return mapSource;
	    
	}
	
	
	
}
