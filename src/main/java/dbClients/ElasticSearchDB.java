package dbClients;


import core.Car;
import core.Parking;
import dbClients.dbClass.dbBaseClass;
import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;

import org.elasticsearch.action.update.UpdateRequest;

import org.elasticsearch.client.*;
import org.elasticsearch.client.indices.GetIndexRequest;
import mainClassFile.*;

import java.io.FileReader;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.*;

public class ElasticSearchDB extends dbBaseClass {

    static SimpleDateFormat time = new SimpleDateFormat ("HH:mm:ss");
    static SimpleDateFormat date = new SimpleDateFormat ("dd/MM/yyyy");


    static String ElasticSearchHost = null;
    static Integer ElasticSearchPort;


    public static RestHighLevelClient restHighLevelClient = null;
    public static FileReader reader = null;


    static String INDEX = "parking";
    static int totalTicketCount=0;



    public boolean checkIfIndexExists() throws IOException {
        GetIndexRequest request = new GetIndexRequest(INDEX);
        return restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);

    }

// Updating parkingList Array and ParkingLot 2D-Array
    @Override
    public void updateParkingLot(){
        try{
            GetRequest getRequest = new GetRequest(INDEX);
//            Total ticket ID
            getRequest.id("idCount");
            GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
            totalTicketCount = (int) getResponse.getSource().get("Count");
            Parking parking;
            Car car;
            for(int i=0;i<totalTicketCount;i++){
                getRequest.id("WLPK"+i);
                parking = new Parking();
                car = new Car();
                getResponse = restHighLevelClient.get(getRequest,RequestOptions.DEFAULT);

                parking.setUniqueId(getResponse.getId());
                parking.setFloor((int)getResponse.getSource().get("floor"));
                parking.setRow((int)getResponse.getSource().get("row"));
                car.setRegistrationNumber(getResponse.getSource().get("regNum").toString());
                car.setColor(getResponse.getSource().get("color").toString());
                parking.setCar(car);
                parking.setEntryDate(getResponse.getSource().get("entryDate").toString());
                parking.setEntryTime(getResponse.getSource().get("entryTime").toString());
                parking.setExitDate(getResponse.getSource().get("exitDate").toString());

                if(parking.getExitDate().equalsIgnoreCase("")){
                    ParkingLotManager.parkingLot[parking.getFloor()-1][parking.getRow()-1]=1;
                }

                parking.setExitTime(getResponse.getSource().get("exitTime").toString());
                ParkingLotManager.parkingList.add(parking);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }


    }
    @Override
    public boolean checkCarInParkingLot(String regNum){
        // Iterate the List to check ticket is exist or not
        for (Parking parking : ParkingLotManager.parkingList) {
            if (parking.getCar().getRegistrationNumber().equalsIgnoreCase(regNum) && parking.getExitDate().equalsIgnoreCase("")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String generateEntryTicket(String regNum, String color){

        /*
         * CHECK IF THIS CAR ALREADY IN OUR PARKING LOT.
         */
        if(checkCarInParkingLot(regNum)){
            return "The Car with the Registration Number '"+regNum+"' already in the Parking Lot";
        }

        Parking park = new Parking();

        // Set core.Parking UniqueID

        park.setUniqueId("WLPK" + totalTicketCount);


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


        Map<String,Object> mp = new HashMap<>();
        mp.put("floor",park.getFloor());
        mp.put("row",park.getRow());
        mp.put("regNum",car.getRegistrationNumber());
        mp.put("color", car.getColor());
        mp.put("entryDate",park.getEntryDate());
        mp.put("entryTime", park.getEntryTime());
        mp.put("exitDate",park.getExitDate());
        mp.put("exitTime",park.getExitTime());

//        Inserting Values in Elastic Search DB
        IndexRequest indexRequest = new IndexRequest(INDEX).id(park.getUniqueId()).source(mp);
        try{
            restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);

        }catch (IOException e){
            e.printStackTrace();
        }
        ParkingLotManager.parkingList.add(park);

//        Insert total ticket count in DB
        totalTicketCount++;
        mp = new HashMap<>();
        mp.put("Count",totalTicketCount);
        if(totalTicketCount==1){
            indexRequest = new IndexRequest(INDEX).id("idCount").source(mp);
            try{
                restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);

            }catch (IOException e){
                e.printStackTrace();
            }
        }
        else{
            UpdateRequest request = new UpdateRequest(INDEX, "idCount").doc(mp);
            try {
                restHighLevelClient.update(request, RequestOptions.DEFAULT);
            } catch(IOException e) {
                 e.printStackTrace();
            }
        }

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
    public String exitTheTicket(String id){
        try{
            Parking park ;
            Iterator<Parking> parkIt= ParkingLotManager.parkingList.iterator();

            //Exit the car entering Ticket ID

            boolean flag = true;

            // Iterate the List to check ticket is exist or not
            while(parkIt.hasNext()){
                park = parkIt.next();
                String unq = park.getUniqueId();
                if(id.equalsIgnoreCase(unq)) {
                    if(park.getExitDate().equalsIgnoreCase("")){
                        park.setExitDate(date.format(new Date()));
                        park.setExitTime(time.format(new Date()));

                        // freeing the space in parking lot
                        ParkingLotManager.parkingLot[park.getFloor()-1][park.getRow()-1]=0;
                        // Generating Ticket
                        System.out.println("\n\t\t* Parking Ticket");
                        System.out.println("\t* Ticket ID: "+park.getUniqueId());
                        System.out.println("\t* Parking Floor: "+park.getFloor());
                        System.out.println("\t* Parking Row: "+park.getRow());
                        System.out.println("\t* Car Reg. Number: "+park.getCar().getRegistrationNumber());
                        System.out.println("\t* Car Color: "+park.getCar().getColor());
                        System.out.println("\t* Entry Date: "+park.getEntryDate());
                        System.out.println("\t* Entry Time: "+park.getEntryTime());
                        System.out.println("\t* Exit Date: "+park.getExitDate());
                        System.out.println("\t* Exit Time: "+park.getExitTime());

                        Map<String,Object> mp = new HashMap<>();
                        mp.put("exitDate",park.getExitDate());
                        mp.put("exitTime",park.getExitTime());
                        UpdateRequest request = new UpdateRequest(INDEX, park.getUniqueId()).doc(mp);
                        try {
                            restHighLevelClient.update(request, RequestOptions.DEFAULT);
                        } catch(IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("\t\t* Thank you for Using Parking");
                    }
                    // Expired Ticket is used
                    else {
                        return "Ticket Is Already Expired";
                    }
                    flag = false;
                    break;
                }
            }
            // Ticket does not exist.
            if(flag){
                return "Ticket ID is Not Valid";

            }
            return "Success";


        }
        catch(Exception e) {

            return e.getMessage();
        }
    }


    public void ElasticSearchService(){
        try{

            reader = new FileReader("src/main/resources/database.conf");
            Properties properties = new Properties();
            properties.load(reader);
            ElasticSearchHost = properties.getProperty("ElasticSearchHost");
            ElasticSearchPort = Integer.parseInt(properties.getProperty("ElasticSearchPort"));

            if(restHighLevelClient == null) {
                restHighLevelClient = new RestHighLevelClient(
                        RestClient.builder(
                                new HttpHost(ElasticSearchHost, ElasticSearchPort, "http")));
            }



            // To Update ParkingLot
            if(checkIfIndexExists())
                updateParkingLot();
            System.out.println("Connection to Elastic Search Server Successful");


            // TO call Base class method.

            super.service();


        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            try {
                if (restHighLevelClient != null) {
                    restHighLevelClient.close();
                    restHighLevelClient = null;
                }
                if (reader != null) {
                    reader.close();
                    reader = null;
                }
            }
            catch(IOException e){
                e.printStackTrace();
            }

        }

    }
}
