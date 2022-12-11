# IoT_System_Project
##### IoT System Project 2022 - V1.3

This is the "lower" version version (rated for grade of 3). It will be presented if I don't manage to do the "higher" version (rated for grade of 4).


## Base ressources
- arrowhead-f /sos-examples-spring
- arrowhead-f/client-skeleton-java-spring

## Progress

### Major Update
- Now the system works with 3 providers, 2 consumers and 1 that do both.
- Now the system works with a provider chain for the tray consumer.

### Currently working
- Common
- Thermometer
- Heater
- Detector 1 and 2
- Robot Arm
- Tray

### To do (Maybe)
- Upgrade the system to use EventHandler

## The Local Cloud Architecture

![Alt text](/documentation/LocalCloud_lower.png)



## How to use (a,b,c are alternative methods, you only need to do one of them)
- 1 : Clone the repo, or download and unzip somewhere on a folder on your computer.
- 2 : Open terminal in the folder and execute "mvn clean install"
- 3 : Clean your database with the sql script "Clean_database" (Optional)
- 4 a : Start manually the Arrowhead core systems (Service Registry, Authorization, Orchestrator).
- 4 b  : (Works on windows, not sure about other OS) Copy the "start_coresystems_factoryLine_doNotExecuteHere" scripts on your Arrowhead-core script folder, and execute it from there.
- 5 a : Start the providers manually.
- 5 b : (Works on windows, not sure about other OS) Start the providers with "Start_core_providers" script and start the roboticarm-provider manually (you may want to see the output)
- 5 c : Start all the providers with "Start_all_providers" script (you will miss some information)
- 6 a : Execute "Setup_from_fresh_database" sql script. (Only with a clean db)
- 6 b : Manually register the consumers and services.
- 7 : Start the consumer
- 8 : See the system working
- 9 : Realize that it’s all meaningless, and that despite all effort humanity is doomed to extinction like every other species on this ridiculously small pebble lost in the unforgiving and inaccessible immensity  of the universe.
- 10 : Stop the systems when you're done. (You may have to use the task manager)
