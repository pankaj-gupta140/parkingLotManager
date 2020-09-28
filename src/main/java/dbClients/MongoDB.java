package dbClients;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import core.Car;
import core.Parking;
import mainClassFile.ParkingLotManager;
import dbClients.dbClass.dbBaseClass;
import org.bson.Document;


import java.io.FileReader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;



public class MongoDB extends dbBaseClass {

    static SimpleDateFormat time = new SimpleDateFormat ("HH:mm:ss");
    static SimpleDateFormat date = new SimpleDateFormat ("dd/MM/yyyy");

    static String mongoHost = null;
    static int mongoPort;
    static MongoClient mongoClient = null;
    static MongoDatabase database = null;



    public static MongoCollection<Document> collection =null;
    public static FileReader reader = null;

    @Override
    public void updateParkingLot(){
        FindIterable<Document> iteratorDoc = collection.find(Filters.eq("exitDate",""));
        // Getting the iterator

        for (Document d : iteratorDoc) {
            ParkingLotManager.parkingLot[d.getInteger("floor") - 1][d.getInteger("row") - 1] = 1;

        }

    }

    @Override
    public boolean checkCarInParkingLot(String regNum){
        FindIterable<Document> iteratorDoc = collection.find(Filters.and(Filters.eq("regNum",regNum),Filters.eq("exitDate","")));

        Iterator<Document> it = iteratorDoc.iterator();
        return it.hasNext();
    }

    @Override
    public String generateEntryTicket(String regNum, String color){

        /*
         * CHECK IF THIS CAR ALREADY IN OUR PARKING LOT.
         */
        if(checkCarInParkingLot(regNum)){
            return "The Car with the Registration Number '"+regNum+"' already in the Parking Lot";
        }

        SimpleDateFormat d = new SimpleDateFormat("yyMM");
        Parking park = new Parking();

        // Set core.Parking UniqueID

        park.setUniqueId("WLPK" + d.format(new Date()) + collection.count());


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


        Document document = new Document("uniqueId",park.getUniqueId())
                .append("floor",park.getFloor())
                .append("row",park.getRow())
                .append("regNum",car.getRegistrationNumber())
                .append("color",car.getColor())
                .append("entryDate",park.getEntryDate())
                .append("entryTime",park.getEntryTime())
                .append("exitDate",park.getExitDate())
                .append("exitTime",park.getExitTime());

        collection.insertOne(document);

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
            String exitD=date.format(new Date()) ,exitT=time.format(new Date());

            //Exit the car entering Ticket ID
//            FindIterable<Document> iteratorDoc = collection.find(Filters.and(Filters.eq("uniqueId",id),Filters.eq("exitDate","")));

            FindIterable<Document> iteratorDoc = collection.find(Filters.eq("uniqueId",id));
            // Getting the iterator
            Iterator<Document> it = iteratorDoc.iterator();
            if(it.hasNext()) {
                Document d = it.next();
                if(d.getString("exitDate").equalsIgnoreCase("")){
                    ParkingLotManager.parkingLot[d.getInteger("floor")-1][d.getInteger("row")-1]=0;
                    // Generating Ticket
                    System.out.println("\n\t\t* Parking Ticket");
                    System.out.println("\t* Ticket ID: "+d.getString("uniqueId"));
                    System.out.println("\t* Parking Floor: "+d.getInteger("floor"));
                    System.out.println("\t* Parking Row: "+d.getInteger("row"));
                    System.out.println("\t* Car Reg. Number: "+d.getString("regNum"));
                    System.out.println("\t* Car Color: "+d.getString("color"));
                    System.out.println("\t* Entry Date: "+d.getString("entryDate"));
                    System.out.println("\t* Entry Time: "+d.getString("entryTime"));
                    System.out.println("\t* Exit Date: "+exitD);
                    System.out.println("\t* Exit Time: "+exitT);
                    collection.updateOne(Filters.eq("uniqueId", id), Updates.set("exitDate", exitD));
                    collection.updateOne(Filters.eq("uniqueId", id), Updates.set("exitTime", exitT));
                    System.out.println("\t\t* Thank you for Using core.Parking");
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


    public void MongoService(){

        try{

            reader = new FileReader("src/main/resources/database.conf");
            Properties properties = new Properties();
            properties.load(reader);
            mongoHost = properties.getProperty("mongoHost");
            mongoPort = Integer.parseInt(properties.getProperty("mongoPort"));

//            credential = MongoCredential.createCredential("user", "parkingLotManager","password".toCharArray());
            mongoClient = new MongoClient(mongoHost,mongoPort);
            database = mongoClient.getDatabase("parkingLotManager");
            if(database == null)
                System.out.println("first create {Database :'parkingLotManager'} ");

            assert database != null;
            collection = database.getCollection("parking");
            //Creating a collection
            if(collection.count()<=0)
            {
                database.createCollection("parking");
                collection = database.getCollection("parking");
            }


            // To Update ParkingLot
            updateParkingLot();

            // TO call Base class method.

            super.service();


        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            try{
                if(mongoClient!=null){
                    mongoClient.close();
                    database =null;
                    collection = null;
                }
            }
            catch (Exception e){
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
