# Powergrab - A Drone Simulator


## Overview:

Powergrab was a 3rd year university project where I was tasked with programming a drone simulator. The goal of this project was to implement two autonomousn agents to navigate a GeoJson map and collect points. The first was a "stupid" drone with limited awareness of the map and the second was a "smart" drone that had a complete view of the map. The project was implemented in Java and used the Mapbox GeoJson library. The aim was to program drones to “fly” around a GeoJson map downloaded from a server and collect points from all of the good stations without touching any the bad stations and before the drone runs out of power. Every move the drone could make cost power and the drone was only allowed to make a maximum of 250 moves. The efficiency of the path the drone takes was not important, only its ability to path around bad stations while ensuring it touched all the good ones. My drone was able to achieve a perfect score on 9/10 maps.
The whole project was implemented from scratch, I was given no boilerplate and limited direction for how each drone should specifically operate. I implemented systems to manage the Stations, control the Drones, and represent the Position of each drone on the map.
As well as the code I was required to produce a comprehensive report detailing our implementation, you can have a look here.

## How it works:

The drones inherited standard functions from a Drone interface, outlining some basic functions of each of the drones, such as calculating legal moves and initialising the position.
The stateless drone was only able to lookahead to one move and decide its move only based on that. It could prioritse good stations or no station over bad ones but if there were no stations visible it was only able to move randomly, it wasnt allowed to see the whole map.
The Stateful Drone was able to look at the whole map and decide its path beforehand. Mine worked by finding a list of all the good stations in the map and ordering them based on closeness to the previous one. The drone then used this list to inform its direction and did the checking for dangerous stations at runtime. If the station it is in range of is the destination , then it removes it from the list of station to visit, so it doesn’t try to keep going to the same one forever.
In both cases, after a move has been completed the game then checks if the drone is in range of any station and charges from them if it is. After the game is over it generates text detailing all the moves made and an annotated GeoJson map showing the path taken by the drone.

## What i Learned:

This was the first piece of coursework i was given that had no boilerplate code, all students had to start from scratch, this was an intimidating prospect at the time but i quickly got started and began to appreciete the open-endedness of the task.

I learned about the strengths of object oriented design, Java is not my favourite language but the fact its so strictly object oriented made it ideal for this project and an excellent learning tool.

Learning to build a project to support two different mode of operation (the “stateful drone” and “stateless drone”) was an interesting and valuable excercise in building flexible and extensible code.

We had to generate GeoJSON maps so it was fun getting to use a real world API to generate actual JSON maps.
