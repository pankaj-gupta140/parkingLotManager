package dbClients;


import core.Car;
import core.Parking;
import mainClassFile.ParkingLotManager;
import dbClients.dbClass.dbBaseClass;

import java.io.FileReader;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;


public class MySqlDB extends dbBaseClass {

    ResultSet resultset = null;
    // Database connection fields
    public static String driver = "";
    public static Connection connection=null;
    static String databaseName="parkingLotManager";
    static String tableName = "parking";
    public static String url="";
    public static Statement st=null;
    public static String username="";
    public static String password="";
    public static FileReader reader = null;


    SimpleDateFormat time = new SimpleDateFormat ("HH:mm:ss");
    SimpleDateFormat date = new SimpleDateFormat ("dd/MM/yyyy");

    @Override
    public void updateParkingLot(){
        try{

            ResultSet resultset=st.executeQuery("select * from parking where exitDate = ''");
            while(resultset.next()){
                ParkingLotManager.parkingLot[resultset.getInt(2)-1][resultset.getInt(3)-1]=1;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }

    }

    public static void createDatabase() {
        try{
            Class.forName(driver);

            connection= DriverManager.getConnection(url, username, password);
            st=connection.createStatement();
            st.executeUpdate("CREATE DATABASE IF NOT EXISTS "+databaseName);

            url= url + databaseName;
            connection=DriverManager.getConnection(url, username, password);
            st=connection.createStatement();


            String sql = "CREATE TABLE IF NOT EXISTS "+tableName+
                    "(uniqueId VARCHAR(255) not null, " +
                    "pfloor INTEGER, " +
                    "prow INTEGER, " +
                    "RegNum VARCHAR(255)," +
                    "color VARCHAR(255)," +
                    "entryDate VARCHAR(255)," +
                    "entryTime VARCHAR(255)," +
                    "exitDate VARCHAR(255)," +
                    "exitTime VARCHAR(255)," +
                    "PRIMARY KEY(uniqueId))";

            st.executeUpdate(sql);
            System.out.println("Connection is successful to the database: "+url);


        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public boolean checkCarInParkingLot(String regNum) {
        // Iterate the List to check ticket is exist or not
        try {
            resultset = st.executeQuery("select * from parking where RegNum = '" + regNum + "' and exitDate = ''");

            return resultset.next();
        }catch (Exception e){
            e.printStackTrace();
            return true;
        }
    }

    @Override
    public String generateEntryTicket(String regNum, String color)  {
        try {
            /*
             * CHECK IF THIS CAR ALREADY IN OUR PARKING LOT.
             */
            if(checkCarInParkingLot(regNum)){
                return "The Car with the Registration Number '"+regNum+"' already in the Parking Lot";
            }

            Parking park = new Parking();
            // Assigning core.Parking Variables for our Database.

            SimpleDateFormat d = new SimpleDateFormat("yyMM");
            String qry = "SELECT count(*) From parking";
            resultset=st.executeQuery(qry);

            // Set core.Parking UniqueID
            while(resultset.next()){
                park.setUniqueId("WLPK" + d.format(new Date()) + resultset.getInt(1));
            }

            int floor = 0, row = 0;
            boolean flag=true;
            for (int f = 0; f < ParkingLotManager.getFLOOR() ; f++) {
                for (int r = 0; r < ParkingLotManager.getROW(); r++) {
                    if (ParkingLotManager.parkingLot[f][r] == 0) {
                        ParkingLotManager.parkingLot[f][r] = 1;
                        floor = f;
                        row = r;
                        flag = false;
                        break;
                    }

                }
                if (!flag) { break; }

            }


            if (flag) {
                return "SORRY OUR PARKING IS FULL";
            }

            // Setting allotted parking floor and row.
            park.setFloor(floor+1);
            park.setRow(row+1);

            // Setting core.Car Registration Number and Color
            Car car = new Car();
            car.setRegistrationNumber(regNum);
            car.setColor(color);

            /*
             * CHECK IF THIS CAR ALREADY IN OUR PARKING LOT.
             */

            //Setting core.Car core.Parking Entry date and time.
            park.setCar(car);
            park.setEntryDate(date.format(new Date()));
            park.setEntryTime(time.format(new Date()));

            String sql="insert into parking values"+
                    "('"+park.getUniqueId()+"','"+park.getFloor()+"','"+park.getRow()+"','"+
                    car.getRegistrationNumber()+"','"+car.getColor()+"','"+park.getEntryDate()+
                    "','"+park.getEntryTime()+"','"+park.getExitDate()+"','"+park.getExitTime()+"')";
            st.executeUpdate(sql);

            // Generating Ticket
            System.out.println("\n\t\t* Parking Ticket");
            System.out.println("\t* Ticket ID: "+park.getUniqueId());
            System.out.println("\t* Parking Floor: "+park.getFloor());
            System.out.println("\t* Parking Row: "+park.getRow());
            System.out.println("\t* Car Reg. Number: "+car.getRegistrationNumber());
            System.out.println("\t* Car Color: "+car.getColor());
            System.out.println("\t* Entry Date: "+park.getEntryDate());
            System.out.println("\t* Entry Time: "+park.getEntryTime());
            System.out.println("\t* Exit Date: "+park.getExitDate());
            System.out.println("\t* Exit Time: "+park.getExitTime());



            return "Success";
        }
        catch(Exception e) {

            return e.getMessage();
        }

    }

    @Override
    public String exitTheTicket(String id){
        try{
            String exitD=date.format(new Date()) ,exitT=time.format(new Date());

            //Exit the car entering Ticket ID
            resultset=st.executeQuery("select * from parking where uniqueId = '"+id+"'");

            if(resultset.next()){
                if(resultset.getString(8).equalsIgnoreCase("")){

                    ParkingLotManager.parkingLot[resultset.getInt(2)-1][resultset.getInt(3)-1]=0;
                    // Generating Ticket
                    System.out.println("\n\t\t* Parking Ticket");
                    System.out.println("\t* Ticket ID: "+resultset.getString(1));
                    System.out.println("\t* Parking Floor: "+resultset.getInt(2));
                    System.out.println("\t* Parking Row: "+resultset.getInt(3));
                    System.out.println("\t* Car Reg. Number: "+resultset.getString(4));
                    System.out.println("\t* Car Color: "+resultset.getString(5));
                    System.out.println("\t* Entry Date: "+resultset.getString(6));
                    System.out.println("\t* Entry Time: "+resultset.getString(7));
                    System.out.println("\t* Exit Date: "+exitD);
                    System.out.println("\t* Exit Time: "+exitT);
                    st.executeUpdate("UPDATE parking SET exitDate = '"+exitD+"' where uniqueId = '"+id+"'");
                    st.executeUpdate("UPDATE parking SET exitTime = '"+exitT+"' where uniqueId = '"+id+"'");
                    System.out.println("\t\t* Thank you for Using Parking");
                    return "Success";
                }
                else{
                    return "Ticket Is Already Expired";
                }
            }
            else {
                return "Ticket ID is Not Valid";
            }


        }
        catch(Exception e) {

            return e.getMessage();
        }


    }

    public void MysqlService(){
        try{
            reader = new FileReader("src/main/resources/database.conf");
            Properties properties = new Properties();
            properties.load(reader);

            driver = properties.getProperty("driver");
            url = properties.getProperty("url");
            username = properties.getProperty("username");
            password = properties.getProperty("password");

            // To check if database exist or not
            createDatabase();

            // TO update parkingLot
            updateParkingLot();

            // TO call Base class method.

            super.service();


        }
        catch(Exception e) {
            System.out.println(e.getMessage());

        }
        finally {
            try{
                if(st !=null){
                    connection.close();
                    connection = null;
                    st = null;
                }

            }
            catch (SQLException e){
                e.printStackTrace();
            }
            try{
                if(connection !=null){
                    connection.close();
                    connection = null;
                    st = null;
                }
            }
            catch (SQLException e){
                e.printStackTrace();
            }
            try{
                if(reader!=null) {
                    reader.close();
                    reader = null;
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }


    }



}
