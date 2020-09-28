package dbClients;

import core.Car;
import core.Parking;
import mainClassFile.ParkingLotManager;
import dbClients.dbClass.dbBaseClass;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


import java.io.FileReader;

import java.text.SimpleDateFormat;
import java.util.*;

public class RedisDB extends dbBaseClass {
    static SimpleDateFormat time = new SimpleDateFormat ("HH:mm:ss");
    static SimpleDateFormat date = new SimpleDateFormat ("dd/MM/yyyy");

    static String redisHost = null;
    static Integer redisPort;

    static JedisPool pool = null;
    public static Jedis jedis=null;
    public static FileReader reader = null;



    static int dbSizeCount =0;


    @Override
    public void updateParkingLot(){


        // Updating the parking Lot if any car still in parking.
        try{

            for (String key : jedis.keys("WLPK*")) {
                Map<String,String> mp = jedis.hgetAll(key);
                if(mp.get("exitDate").equalsIgnoreCase(""))
                    ParkingLotManager.parkingLot[Integer.parseInt(mp.get("floor")) - 1][Integer.parseInt(mp.get( "row")) - 1] = 1;
                dbSizeCount++;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public boolean checkCarInParkingLot(String regNum){

        for (String key : jedis.keys("WLPK*")) {
            Map<String,String> mp = jedis.hgetAll(key);

            if(mp.get("regNum").equalsIgnoreCase(regNum) && mp.get("exitDate").equalsIgnoreCase("")){
                return true;
            }
        }
        return false;
    }

    @Override
    public String generateEntryTicket(String regNum, String color) {

        /*
         * CHECK IF THIS CAR ALREADY IN OUR PARKING LOT.
         */
        if(checkCarInParkingLot(regNum)) {
            return "The Car with the Registration Number '" + regNum + "' already in the Parking Lot";
        }

            SimpleDateFormat d = new SimpleDateFormat("yyMM");
        Parking park = new Parking();

        // Set core.Parking UniqueID

        park.setUniqueId("WLPK" + d.format(new Date()) + dbSizeCount);


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


        Map<String,String> mp = new HashMap<>();
        mp.put("floor",Integer.toString(park.getFloor()));
        mp.put("row",Integer.toString(park.getRow()));
        mp.put("regNum",car.getRegistrationNumber());
        mp.put("color", car.getColor());
        mp.put("entryDate",park.getEntryDate());
        mp.put("entryTime", park.getEntryTime());
        mp.put("exitDate",park.getExitDate());
        mp.put("exitTime",park.getExitTime());

//        Inserting Values in Redis DB
        jedis.hmset(park.getUniqueId(),mp);
        dbSizeCount++;


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

    @Override
    public String exitTheTicket(String id) {
        try{
            String exitD=date.format(new Date()) ,exitT=time.format(new Date());

            for (String key : jedis.keys("WLPK*")) {
                Map<String,String> mp = jedis.hgetAll(key);

                if(id.equalsIgnoreCase(key)){
                    if(mp.get("exitDate").equalsIgnoreCase("")){
                        ParkingLotManager.parkingLot[Integer.parseInt(mp.get("floor")) - 1][Integer.parseInt(mp.get( "row")) - 1] = 0;
                        // Generating Ticket
                        System.out.println("\n\t\t* Parking Ticket");
                        System.out.println("\t* Ticket ID: "+key);
                        System.out.println("\t* Parking Floor: "+Integer.parseInt(mp.get("floor")));
                        System.out.println("\t* Parking Row: "+Integer.parseInt(mp.get("row")));
                        System.out.println("\t* Car Reg. Number: "+mp.get("regNum"));
                        System.out.println("\t* Car Color: "+mp.get("color"));
                        System.out.println("\t* Entry Date: "+mp.get("entryDate"));
                        System.out.println("\t* Entry Time: "+mp.get("entryTime"));
                        System.out.println("\t* Exit Date: "+exitD);
                        System.out.println("\t* Exit Time: "+exitT);

                        mp.put("exitDate",exitD);
                        mp.put("exitTime",exitT);
                        jedis.hmset(key,mp);
                        System.out.println("\t\t* Thank you for Using Parking");
                        return "Success";
                    }
                    else{
                        return "Ticket Is Already Expired";
                    }
                }

            }
            return "Ticket ID is Not Valid";

        }
        catch(Exception e) {

            return e.getMessage();
        }
    }

    public void RedisService() {

        try {

            reader = new FileReader("src/main/resources/database.conf");
            Properties properties = new Properties();
            properties.load(reader);
            redisHost = properties.getProperty("redisHost");
            redisPort = Integer.parseInt(properties.getProperty("redisPort"));

            pool = new JedisPool(redisHost, redisPort);
            jedis = pool.getResource();

            System.out.println("<------    Connection Successful    ------>");
            System.out.println("The server is running " + jedis.ping());



            // To Update ParkingLot
            updateParkingLot();

            // TO call Base class method.
            super.service();

        }

        catch(Exception e){
            e.printStackTrace();
            if (null != jedis) {
                jedis.close();
                pool.close();
            }

        }
        finally{
            try{
                if(null!=jedis) {
                    jedis.close();
                    pool.close();

                }
            }catch (Exception e){
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
