package uk.ac.ed.inf.powergrab;

import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarStyle;

import java.io.*;
import java.net.*;
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
	
//main class for the project, where stuff is actually run
	
	//------Basic TODO-------
	//1. get json from webserver[x]
	//2. parse it to get all the features into a data structure[]
	//3. Init drone (stateless or not stateless)[]
	//4. start drone game loop[]
	//5. run game[]
	//6. build txt and geo-json files with the data from the game
	//7. write them
	//8. yaaay done
	
	
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
		
		//get map data from web-server
		String mapSource = getMapSource(d,m,y);	
		FeatureCollection map = FeatureCollection.fromJson(mapSource);
		List<Feature> features = map.features();
		
		//create output files
		Path jsonFile = Paths.get(version+"-"+d+"-"+m+"-"+y+".geojson");
		Path txtFile  = Paths.get(version+"-"+d+"-"+m+"-"+y+".txt");
		
		//prepare txt output container
		List<String> txtOutput = new ArrayList<String>();
		
		// initialise drone to be stateless or stateful
		Drone drone = initDrone(startPos, seed, version);
		
		//just some UI stuff to help usability and debugging
		ProgressBar pb = new ProgressBar("Running PowerGrab",100,ProgressBarStyle.ASCII).start();
		
		// main game loop
		while (drone.getMoves() > 0) {	
			//ensure stateless and stateful drones have the same method names to make this code cleaner
			
			Position currPos = drone.getPos();
			
			
			//calculate and verify next move
			//      updating power, coins and moves are dealt with in this method
			
			
			Position nextPos = drone.getPos(); //these will be different if all goes well :D
			
			//.txt output
			
			
			//geo-json output
			features.add(makeLine(currPos, nextPos));
			pb.step();
			
		}
		
		pb.stop();
		
		// turns features into a list of strings so it can be written to a file
		List<String> featuresString = featureToString(features);	
		Files.write(jsonFile, featuresString, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
		
		
		Files.write(txtFile,txtOutput, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
		
		
		
		
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
			featuresString.add(i,  features.get(i).toJson());
		}
		
		return featuresString;
	}
	
	
	public static Drone initDrone(Position startPos, int seed, String version) {
		Drone drone;
		if (version == "stateless") { 
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
