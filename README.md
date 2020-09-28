# ParkingLotManager  
A famous Competitive question of multi-level Parking system
#### Problem Statement:  
There is a shopping mall in the city of "Wallalong". This shopping mall has 5 floors to park cars, each floor has 20 car parking spaces. 
Each slot is given a number starting at 1 increasing with increasing distance from the entry point in steps of one. When a car enters the parking lot, 
a ticket should be issued to the driver. The ticket issuing process includes documenting the registration number (number plate) and the color of the car 
and allocating an available parking slot to the car before actually handing over a ticket to the driver 
(we assume that our customers are nice enough to always park in the slots allocated to them). 
The customer should be allocated a parking slot which is nearest to the entry. At the exit the customer returns the ticket which then marks the slot they were 
using as being available.  
You are tasked to write an automated ticketing system that allows customers to use the parking lot without human intervention in your favorite programming language. Requirements: -  
Follow OOPS concepts - Write documentation as per Java doc standards. - Write Unit tests (If possible)  
Due to government regulation, the system should provide me with the ability to find out:  
a) Registration numbers of all cars of a particular color.  
b) Slot number in which a car with a given registration number is parked.  
c) Slot numbers of all slots where a car of a particular color is parked.  
We interact with the system via a simple set of commands which produce a specific output in the console. 
The system should allow input in an interactive command prompt based shell.

#### Requirements:  
* JDK: version 11.0.8  
* IDE: IntelliJ IDEA 2020.2.2 (community version)  
* Apache Maven 3.6.0  

#### To Run the Project  
* Open the IntelliJ IDEA and clone the master branch.  
* Open ‘DataBase.conf’ file inside src/main/resources folder and choose your Database {Database = InMemory/ MySql/MongoDB/ Redis/ ElasticSearch}, change the corresponding connection configuration settings.  
  * _For MongoDB/ Redis/ ElasticSearch used non-protected configuration._  
* **mainClassFile.ParkingLotManager.java** contains the main function to run the program Or you can use **mainClassFile.ParkingLotManager-1.0-SNAPSHOT.jar** inside the target folder to run the program.  

#### To access InMemory  
* Change Database = InMemory inside DataBase.conf file.  
* Run the .jar file  

#### To Access MySQL Database  
* Install and configure MySQL 8.0.21 on your OS.  
* Change Database = MySql and change its connection configurations (ex: username = ‘root’ & password = ‘root’ ) inside Database.conf file.  
* Run the .jar file  
* The program will handle the creation of the Database and table.  

#### FlyWay Migration for MySQL database (*Optional*)  
_Flyway migration to migrate the table schema. you can open src/main/resources/flyway.conf to edit configuration settings, then go to the top-right of Intellij maven->parkingLotManger->plugins->flyway:migrate and double click it to migrate the database changes. Double click to flyway:info for the status._  

* *There is test file inside /src/test' named as "ParkingLotManagerTest.java" to run/check all Mysql Admin.APIs. Use IntelliJ IDEA to run the test file.*

#### To Access MongoDB  
* Install and configure MongoDB 4.4.1 on your OS.  
* open the terminal and type following commands  
  * sudo service mongod start  (to start the MongoDB server)  
  * mongo   
  * use parkingLotManager  
  * exit  
* Change Database = MongoDB and change its connection configurations (ex: hostname = ‘localhost & port = 27017) inside DataBase.conf file.  
* Run the .jar file  
* Don’t forget to stop the service by typing, sudo service mongod stop.  
* check the status using, sudo service mongod status  

#### To Access Redis Database  
* Install and configure Redis 6.0.8 on your OS.  
* open the terminal and type following commands  
  * redis-server  (to start the Redis server)  
* Change Database = Redis and change its connection configurations (ex: hostname = ‘localhost & port = 6379) inside DataBase.conf file.  
* Run the .jar file  
* Don’t forget to stop the service by typing ‘ctrl+z’ on terminal  

#### To Access ElasticSearch Database  
* Install and configure Elastic Search 7.9.0 on your OS.  
* open the terminal and type following commands  
  * sudo service elasticsearch start  (to start the server)  
* Change Database = ElasticSearch and change its connection configurations (ex: hostname = ‘localhost & port = 9200) inside DataBase.conf.  
* Run the .jar file  
* Don’t forget to stop the service by typing, sudo service elasticsearch stop.  
* check the status using, sudo service elasticsearch status  


#### To Execute on Terminal  
* Download the zip-file and extract to Desktop  
* Open terminal and type below commands  
* cd Desktop/mainClassFile.ParkingLotManager/src/main/java/core  
* javac mainClassFile.ParkingLotManager.java  
* java mainClassFile.ParkingLotManager  


#### To Execute on Terminal By .JAR file  
* Download the zip-file and extract to Desktop  
* Open terminal and type below commands  
* cd Desktop/mainClassFile.ParkingLotManager/target  
* java -jar mainClassFile.ParkingLotManager-1.0-SNAPSHOT.jar  

