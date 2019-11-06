package uk.ac.ed.inf.powergrab;

import java.io.*;
import  java.net.*;
import org.apache.commons.io.IOUtils;

public class App {
	
//main class for the project, where stuff is actually run
	
	//------Basic TODO-------
	//1. get json from webserver[x]
	//2. parse it to get all the features into a data structure[]
	//3. Init drone (stateless or not stateless)[]
	//4.start drone game loop[]
	//5. run game[]
	//6. build txt and geojson files with the data from the game
	//7. write them
	//8. yaaay done
	
	
	public static void main (String args[]) throws MalformedURLException, IOException {
		// split args into own variables to aid reabability
		
		//variable to hold URL string for the map  
		String mapString = "http://www.homepages.inf.ed.ac.uk/stg/powergrab/2019/01/01/powergrabmap.geojson";
		
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
		connHttp.connect(); // finally connects to the URL
		
		//read json data from url
		InputStream in = connHttp.getInputStream(); //get input stream from url
		StringWriter writer = new StringWriter();  
		IOUtils.copy(in, writer, "UTF-8"); 
		String mapSource = writer.toString();
		
		
		
		
		
		
		
		
		
	}
	
	
	

}
