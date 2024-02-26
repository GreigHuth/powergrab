# Powergrab - A Drone Simulator


## Overview:

Powergrab was a 3rd year university project where I was tasked with programming a drone simulator. The goal of this project was to implement two autonomousn agents to navigate a GeoJson map and collect points. The first was a "stupid" drone with limited awareness of the map and the second was a "smart" drone that had a complete view of the map. The project was implemented in Java and used the Mapbox GeoJson library. The aim was to program drones to “fly” around a GeoJson map downloaded from a server and collect points from all of the good stations without touching any the bad stations and before the drone runs out of power. Every move the drone could make cost power and the drone was only allowed to make a maximum of 250 moves. The efficiency of the path the drone takes was not important, only its ability to path around bad stations while ensuring it touched all the good ones. My drone was able to achieve a perfect score on 9/10 maps.

You can see the path it takes here.

The whole project was implemented from scratch, I was given no boilerplate and limited direction for how each drone should specifically operate. I implemented systems to manage the Stations, control the Drones, and represent the Position of each drone on the map.

As well as the code I was required to produce a comprehensive report detailing our implementation, you can have a look here.

For this coursework i was awarded 89/100 with the strongest parts of my submission being my efficient and clean code as well as my detailed and concise report.

## How it works:

The drones inherited standard functions from a Drone interface, outlining some basic functions of each of the drones, such as calculating legal moves and initialising the position.

The stateless drone was only able to lookahead to where any moves from it current position would take it. It could prioritse good stations or no station over bad ones but if there were no stations in site it was only able to move randomly, it wasnt allowed to see the whole map.

The Stateful Drone was able to look at the whole map when deciding its path around the map, this would allow it to efficiently traverse the entire map while also ensuring it could reach all the stations it needed to. When the drone is initialised, before the maingame loop has even begun, it decides what order to visit all the good stations in. This order is based on the current position of the station the drone will have just visited. For example: If the drone were to start at position (1,1) and the nearest station was at (2,2) then this will be the first station the drone visits. Then it bases its next destination what station is closest to the station it just visited. So it would find the station closest to the coordinate (2,2). It does this until it has an ordered list of all the good stations the drone needs to visit. Then the main game loop can begin and the drone then needs to decide on what directions to move in. After the stateful drone has exhausted the list of stations to visit, it will then make its way to the original starting position and stay in the area of it. This is to ensure that the drone does not stray unexpectedly into range of a danger station. It does this for the remainder of the moves left in the game.

After a move has been completed, the game then checks if the drone is in range of any station, and charges from them if it is. If the station it is in range of is the destination station, then it removes it from the list of station to visit, so it doesn’t try to keep going to the same one forever.

After the game is over it generates text detailing all the moves made and an annotated GeoJson map showing the path taken by the drone.

By the end of the project my drone was able to get a perfect score on all of the provided maps.

## What i Learned:

This was the first piece of coursework i was given that had no boilerplate code, all students had to start from scratch, this was an intimidating prospect at the time but i quickly got started and began to appreciete the open-endedness of the task.

I learned about the strengths of object oriented design, Java is not my favourite language but the fact its so strictly object oriented made it ideal for this project and an excellent learning tool.

Learning to build a project to support two different mode of operation (the “stateful drone” and “stateless drone”) was an interesting and valuable excercise in building flexible and extensible code.

We had to generate GeoJSON maps so it was fun getting to use a real world API to generate actual JSON maps.
