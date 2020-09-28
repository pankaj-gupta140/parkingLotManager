package Admin;

import Admin.APIs.adminServices;
import core.Parking;
import mainClassFile.ParkingLotManager;

import java.io.BufferedReader;
import java.util.Iterator;
import java.util.Scanner;

public class ElasticSearchAdmin implements adminServices {

    private static String password = "password";

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        ElasticSearchAdmin.password = password;
    }

    @Override
    public String carsFromColor(String color) {
        System.out.println("\t* Registration Number of all " +
                color + " Cars Parked till today");
        Iterator<Parking> parkIt= ParkingLotManager.parkingList.iterator();
        Parking park;
        int i=1;

        // Iterate the List
        while(parkIt.hasNext()){
            park = parkIt.next();

            if(color.equalsIgnoreCase(park.getCar().getColor())){
                System.out.println("\t\t "+ i++ +"-> " + park.getCar().getRegistrationNumber());
            }
        }

        return "Success";
    }

    @Override
    public String slotOfCar(String regNumber) {
        Iterator<Parking> parkIt= ParkingLotManager.parkingList.iterator();
        Parking park;
        System.out.println("\t* Car Registration Number: "+ regNumber);
        System.out.println("\t\t Floor|Row\t EntryDate|EntryTime\t ExitDate|ExitTime");
        // Iterate the List
        while(parkIt.hasNext()){
            park = parkIt.next();

            if(regNumber.equalsIgnoreCase(park.getCar().getRegistrationNumber())){
                System.out.println("\t\t "+ park.getFloor()+" | "+park.getRow()+"\t "+
                        park.getEntryDate()+" | "+park.getEntryTime()+"\t "+
                        park.getExitDate()+" | "+park.getExitTime());
            }
        }

        return "Success";
    }

    @Override
    public String slotsFromColor(String color) {
        Iterator<Parking> parkIt= ParkingLotManager.parkingList.iterator();
        Parking park;
        System.out.println("\t* Car Color: "+ color);
        System.out.println("\t\t Floor|Row\t RegNum\t EntryDate|EntryTime\t ExitDate|ExitTime");
        // Iterate the List
        while(parkIt.hasNext()){
            park = parkIt.next();

            if(color.equalsIgnoreCase(park.getCar().getColor())){
                System.out.println("\t\t "+ park.getFloor()+" | "+park.getRow()+"\t "+
                        park.getCar().getRegistrationNumber()+"\t "+
                        park.getEntryDate()+" | "+park.getEntryTime()+"\t "+
                        park.getExitDate()+" | "+park.getExitTime());
            }
        }

        return "Success";
    }



    public void service() {
        try{
            BufferedReader br = ParkingLotManager.br;
            Scanner sc = ParkingLotManager.sc;

            System.out.println("\t\t Enter the Password Please (password = 'password') ");
            String pass = br.readLine();
            int i=0;
            while(i<2 && (! pass.equals(password))){
                if(i==1){
                    System.out.println("\t\t You are Redirected to Main Menu");
                    return;
                }
                System.out.println("\t\t Password Is Wrong Try again");
                pass = br.readLine();
                i++;
            }
            boolean flag = true;
            while(flag){
                System.out.println("\t\t\t****** Elastic Search Admin Service Portal ******");
                System.out.println("\n\t\t Please Select an Option number ");
                System.out.println("\t 1. Registration numbers of all cars of a particular color" +
                        "\n\t 2. Slot number in which a car with a given registration number is parked" +
                        "\n\t 3. Slot numbers of all slots where a car of a particular color is parked" +
                        "\n\t 4. Change the password" +
                        "\n\t 5. Exit to Main Menu \n");

                while (!sc.hasNextInt()) {
                    System.out.println("\tError... Please Enter Valid Choice (A Number)!!");
                    sc.next();
                }
                int ch=sc.nextInt();
                System.out.flush();
                String color, regNumber;
                switch(ch){
                    case 1:
                        System.out.println("\t\t Enter The Color Of the Car");
                        color = br.readLine().trim().toUpperCase();
                        carsFromColor(color);
                        break;
                    case 2:
                        System.out.println("\t\t Enter The Registration Number");
                        regNumber = br.readLine().trim().toUpperCase();
                        slotOfCar(regNumber);
                        break;
                    case 3:
                        System.out.println("\t\t Enter The Color Of the Car");
                        color = br.readLine().trim().toUpperCase();
                        slotsFromColor(color);
                        break;
                    case 4:
                        System.out.println("\t\t Enter the New Password");
                        setPassword(br.readLine());
                        break;
                    case 5:
                        flag=false;
                        break;
                    default:
                        System.out.println("\n\t Enter the valid option");

                }

            }

        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
