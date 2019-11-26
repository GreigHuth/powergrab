package uk.ac.ed.inf.powergrab;

import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarStyle;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.javatuples.*;

import com.mapbox.geojson.Point;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.LineString;

public class App {
    
    // global variables
   
	
//main class for the project, where stuff is actually run
	
	//------Basic TODO-------
	//1. finish stateless drone
	//2. start thinking about stateful drone/ start writing report
	//3. dont die
	
	
	
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
		
		//create output files
		Path jsonFile = Paths.get(version+"-"+d+"-"+m+"-"+y+".geojson");
		Path txtFile  = Paths.get(version+"-"+d+"-"+m+"-"+y+".txt");
						
	   
		List<String> txtOutput = new ArrayList<String>(); //prepare txt output container
		
		//get map data from web-server
		String mapSource = getMapSource(d,m,y);	
		FeatureCollection mapJson = FeatureCollection.fromJson(mapSource);
		
		List<Feature> features = mapJson.features(); // list of features that will be written to the json
		ArrayList<Station> map = new ArrayList<Station>(); //  map to use for the game
		
		map = unpackFeatures(features);
		
		Drone drone = initDrone(startPos, seed, version);	
		System.out.println("Running PowerGrab...");

	
		 int  moves = 250;
		 double coins = 0.0;
		 double power = 250.0;
		// main game loop
		while (moves > 0 && power > 1.25) {	
		
			Position currPos = drone.getPos();
			Direction nextMove = null;
			
			//block for stateless drone
			if (drone instanceof StatelessDrone) {
				drone = (StatelessDrone) drone;
				nextMove = ((StatelessDrone) drone).calcMove(map);
				//updates the drones position
				drone.position = drone.position.nextPosition(nextMove);
		
			}
			
			//block for stateful droneHAHAHAHAHAHAHAHAH
			else {
				
			}
			
			Position nextPos = drone.getPos();
			
			//this block charges the drone
			Station chargeStation = getCharge(drone.position, map);// returns the station to charge from
			
			// if there is a nearest station to charge from, charge from it and update the map
			if (chargeStation.id.equals("") == false) {
				coins += chargeStation.coins;
				power += chargeStation.power;
				
				//this block updates the map
				map = updateMap(chargeStation, map);
			}
			
			
			
			//.txt output
			txtOutput.add(
					Double.toString(currPos.latitude)+","+Double.toString(currPos.longitude)+
					","+nextMove.toString()+
					","+Double.toString(nextPos.latitude)+","+Double.toString(nextPos.longitude)+
					","+coins+
					","+power);
			
			//geo-json output
			features.add(makeLine(currPos, nextPos));
			
			System.out.println("moves left:" + moves);
			System.out.println(txtOutput.get(250-moves));
			System.out.println();
			moves--;
			power -= 1.25;
			
			
			
			
		}
		System.out.println("PowerGrab Complete!");
		System.out.println("Creating files...");
		
		
		
		
		// turns features into a list of strings so it can be written to a file
	
		
		FeatureCollection jsonOut = FeatureCollection.fromFeatures(features);
		String jsonString = jsonOut.toJson();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(version+"-"+d+"-"+m+"-"+y+".geojson"));
		writer.write(jsonString);
		writer.close();
		
		System.out.println(jsonString);
		//Files.write(jsonFile, (String)jsonString, Charset.defaultCharset());
		Files.write(txtFile,txtOutput, Charset.defaultCharset());
		
		
	
		
	}

	
	
	//updates the info on the map with the station  =
	public static ArrayList<Station> updateMap (Station chargeStation, ArrayList<Station> map){
		
		for (Station station : map) {
			if (station.id.equals(chargeStation.id)) {
				station.power = 0;
				station.coins = 0;
			}
		}
		return map;
	}
	
	
	
	public static Station getCharge(Position dronePos, ArrayList<Station> map){
		
	    double stationRange = 0.00025;// aoe of the charging stations
	    
	    Position dummy = new Position (100,100);
	    Station closest = new Station("", dummy, 0, 0, "");
	    for (Station current : map) {
	    	double nextDist = distance(dronePos, current.position);//distance to next drone to compare to
	    	double currentDist = distance(dronePos, closest.position);
	    	
	    	if ( nextDist < stationRange && nextDist < currentDist) {
	    		closest = current;
	    	}
	    }    
	   return closest;  
	}
	
	
	
	public static double distance(Position pos1, Position pos2) {       
        return Math.sqrt( Math.pow((pos1.latitude - pos2.latitude),2) + Math.pow((pos1.longitude - pos2.longitude),2) );    
    }
	
	
	
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
	
    
    
	public static Feature makeLine(Position currPos, Position nextPos) {
		List<Point> linePoints = new ArrayList<Point>(); //initialise structure to contain the points to draw the line on the geojson
		linePoints.add(Point.fromLngLat(currPos.longitude, currPos.latitude));
		linePoints.add(Point.fromLngLat(nextPos.longitude, nextPos.latitude));		
		Feature newLine = Feature.fromGeometry(LineString.fromLngLats(linePoints));
		
		return newLine;
	}
	
	
	
	public static List<String> featureToString(List<Feature> features){
		
		List<String> featuresString = new ArrayList<String>(); 
		for ( int i = 0; i < features.size(); i++){
			featuresString.add(i,  features.get(i).toJson()+",");
		}
		
		return featuresString;
	}
	
	
	
	public static Drone initDrone(Position startPos, int seed, String version) {
		Drone drone;
		if (version.equals("stateless")) { 
			drone = new StatelessDrone(startPos, seed);
		}else {
		    drone = new StatefulDrone(startPos, seed);
		}
		
		return drone;
	}
	
	
	
	public static String getMapSource(String d,String m, String y) throws MalformedURLException, IOException {
	    String mapString = "http://www.homepages.inf.ed.ac.uk/stg/powergrab/"+y+"/"+m+"/"+d+"/powergrabmap.geojson";
        
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
