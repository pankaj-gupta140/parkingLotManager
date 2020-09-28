package mainClassFile;

import core.Parking;
import dbClients.*;


import java.io.BufferedReader;
import java.io.FileReader;

import java.io.InputStreamReader;
import java.util.*;

// It's a main function of execution.

public class ParkingLotManager {
    /*
    * Creating Array list of all parking till the time.
    * parkingLot is 2d variable having 5 floors and 20 rows for car parking.
     */
    public static ArrayList<Parking> parkingList = new ArrayList<>();
    static int FLOOR = 5;
    static int ROW = 20;
    public static int[][] parkingLot=new int[FLOOR][ROW];
    public static String Database;
    public static BufferedReader br=null;
    public static Scanner sc=null;


    public static int getFLOOR() {
        return FLOOR;
    }

    public static void setFLOOR(int FLOOR) {
        ParkingLotManager.FLOOR = FLOOR;
    }

    public static int getROW() {
        return ROW;
    }

    public static void setROW(int ROW) {
        ParkingLotManager.ROW = ROW;
    }
    public static FileReader reader = null;


    public static void main(String [] args){
        try{
            br=new BufferedReader(new InputStreamReader(System.in));
            sc=new Scanner(System.in);
            reader = new FileReader("src/main/resources/database.conf");
            Properties properties = new Properties();
            properties.load(reader);

            Database = properties.getProperty("Database");

//          <------------------------   IN MEMORY ---------------------------->
            if(Database.equalsIgnoreCase("inMemory")){
                new InMemoryDB().InMemoryService();
            }

//            <-----------------------------    MONGO DATABASE  --------------------->
            else if(Database.equalsIgnoreCase("MongoDB")){
                new MongoDB().MongoService();
            }

//            <-----------------------------   REDIS DATABASE  ------------------------------->
            else if(Database.equalsIgnoreCase("Redis")){
                new RedisDB().RedisService();
            }

//            <-----------------------------   ELASTIC SEARCH DATABASE  ------------------------------->
            else if(Database.equalsIgnoreCase("ElasticSearch")){
                new ElasticSearchDB().ElasticSearchService();
            }

//          <----------------------   MYSQL DATABASE  ------------------------>
            else if(Database.equalsIgnoreCase("MySql")){
                new MySqlDB().MysqlService();
            }
            else{
                System.out.println("Please Check 'database.conf' file and select right database name from the first commented line ");
            }


        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
        finally {
                System.out.println("<---------- EXITING FROM PARKING LOT MANAGER SYSTEM  ------------>");
                try{
                    if(reader!=null) {
                        reader.close();
                        reader = null;
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            try {
                if (br != null) {
                    br.close();
                    br =null;
                }
                if (sc != null) {
                    sc.close();
                    sc = null;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}
