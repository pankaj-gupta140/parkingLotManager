package dbClients.dbClass;

import Admin.*;
import mainClassFile.ParkingLotManager;
import java.io.BufferedReader;
import java.util.Scanner;

public class dbBaseClass {


    public void updateParkingLot(){
        System.out.println("Database class");

    }

    public boolean checkCarInParkingLot(String str){
        return true;
    }

    public String generateEntryTicket(String regNum, String color){
        return "DataBase Class";
    }

    public String exitTheTicket(String id){
        return "Database Class" ;
    }

    public void callAdminService() {
//                        <------------------------   IN MEMORY ---------------------------->
        if(ParkingLotManager.Database.equalsIgnoreCase("inMemory")){
            new InMemoryAdmin().service();
        }

//            <-----------------------------    MONGO DATABASE  --------------------->
        else if(ParkingLotManager.Database.equalsIgnoreCase("MongoDB")){
            new MongoAdmin().service();
        }

//            <-----------------------------   REDIS DATABASE  ------------------------------->
        else if(ParkingLotManager.Database.equalsIgnoreCase("Redis")){
            new RedisAdmin().service();
        }

//            <-----------------------------   ELASTIC SEARCH DATABASE  ------------------------------->
        else if(ParkingLotManager.Database.equalsIgnoreCase("ElasticSearch")){
            new ElasticSearchAdmin().service();
        }

//          <----------------------   MYSQL DATABASE  ------------------------>
        else if(ParkingLotManager.Database.equalsIgnoreCase("MySql")){
            new MySqlAdmin().service();
        }
        else{
            System.out.println("** ADD Your DataBase Name in dbBaseClass **");
        }
    }


    public void service(){
        try {
            BufferedReader br = ParkingLotManager.br;
            Scanner sc = ParkingLotManager.sc;

            boolean flag = true;

            while(flag) {
                System.out.println("\n\t\t******* Welcome To Parking *******");
                System.out.println("\t\t******* "+ ParkingLotManager.Database +" DATABASE *******");
                System.out.println("\n\t\t Please Select an Option number ");
                System.out.println("\n\t\t 1. GENERATE ENTRY TICKET\n\t\t 2. EXIT THE TICKET\n\t\t " +
                        "3. ADMIN ACCESS\n\t\t 4. EXIT FROM THE SYSTEM \n");
                while (!sc.hasNextInt()) {
                    System.out.println("\tError... Please Enter Valid Choice (A Number)!!");
                    sc.next();
                }
                int ch=sc.nextInt();
                System.out.flush();
                String res;
                switch(ch){
                    case 3:
                        System.out.println("\t\t* ADMIN ACCESS ");

                        callAdminService();

                        break;


                    case 1:
                        System.out.println("\t\t*  GENERATE ENTRY TICKET");
                        System.out.println("\t\t Enter Your Car Registration Number\n");
                        String regNum = br.readLine().trim().toUpperCase();
                        System.out.println("\t\t Enter Your Car Color\n");
                        String color = br.readLine().trim().toUpperCase();
                        res = generateEntryTicket(regNum,color);
                        System.out.println("\n\t\t\t****** "+res+" ******");
                        System.out.println("\t\t* < Enter any character >");
                        br.readLine();
                        break;
                    case 2:
                        System.out.println("\t\t*  EXIT THE TICKET");
                        System.out.println("\t\t* Enter Your Ticket ID \n");
                        String id = br.readLine().trim().toUpperCase();
                        res = exitTheTicket(id);
                        System.out.println("\n\t\t\t****** "+res+" ******");
                        System.out.println("\t\t* < Enter any character >");
                        br.readLine();
                        break;
                    case 4:
                        System.out.println("\n\t\t ******* THANK YOU FOR USING THE PARKING SYSTEM *******");
                        flag=false;
                        break;
                    default:
                        System.out.println("\n\t Enter the valid option");

                }

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
