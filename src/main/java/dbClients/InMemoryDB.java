package dbClients;


import core.Car;
import core.Parking;
import mainClassFile.*;
import dbClients.dbClass.dbBaseClass;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;


public class InMemoryDB extends dbBaseClass {

    SimpleDateFormat time = new SimpleDateFormat ("HH:mm:ss");
    SimpleDateFormat date = new SimpleDateFormat ("dd/MM/yyyy");

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
    public String generateEntryTicket(String regNum, String color) {
        try {

            /*
             * CHECK IF THIS CAR ALREADY IN OUR PARKING LOT.
             */
            if(checkCarInParkingLot(regNum)){
                return "The Car with the Registration Number '"+regNum+"' already in the Parking Lot";
            }

            Parking park = new Parking();
            // Assigning Parking Variables for our Database.

            SimpleDateFormat d = new SimpleDateFormat("yyMM");
            // Set Parking UniqueID
            park.setUniqueId("WLPK" + d.format(new Date()) + ParkingLotManager.parkingList.size());

            int floor = 0, row = 0;
            boolean flag=true;
            for (int f = 0; f < ParkingLotManager.getFLOOR(); f++) {
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


            //Setting core.Car core.Parking Entry date and time.
            park.setCar(car);
            park.setEntryDate(date.format(new Date()));
            park.setEntryTime(time.format(new Date()));
            ParkingLotManager.parkingList.add(park);

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
                        System.out.println("\t\t* Thank you for Using core.Parking");
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


    public void InMemoryService(){
        try{

            // TO call Base class method.

           super.service();

        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }

    }


}
