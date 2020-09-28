package Admin;

import Admin.APIs.adminServices;
import dbClients.RedisDB;
import mainClassFile.ParkingLotManager;

import java.io.BufferedReader;

import java.util.Map;
import java.util.Scanner;

public class RedisAdmin implements adminServices {

    private static String password = "password";

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        RedisAdmin.password = password;
    }


    @Override
    public String carsFromColor(String color) {
        try{

            System.out.println("\t* Registration Number of all " +
                    color + " Cars Parked till today");

            int i=1;

            for (String key : RedisDB.jedis.keys("WLPK*")) {
                Map<String, String> mp = RedisDB.jedis.hgetAll(key);
                if(mp.get("color").equalsIgnoreCase(color)){
                    System.out.println("\t\t "+ i++ +"-> " + mp.get("regNum"));
                }
            }
            if(i==1)
                return "NO RESULT FOUND";

            return "Success";
        }
        catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
    public String slotOfCar(String regNumber) {
        try{

            System.out.println("\t* Car Registration Number: "+ regNumber);
            System.out.println("\t\t Floor|Row\t EntryDate|EntryTime\t ExitDate|ExitTime");
            boolean flag = true;
            for (String key : RedisDB.jedis.keys("WLPK*")) {
                Map<String, String> mp = RedisDB.jedis.hgetAll(key);
                if(mp.get("regNum").equalsIgnoreCase(regNumber)){
                    flag = false;
                    System.out.println("\t\t "+ Integer.parseInt(mp.get("floor"))+" | "+Integer.parseInt(mp.get("row"))+"\t "+
                            mp.get("entryDate")+" | "+mp.get("entryTime")+"\t "+
                            mp.get("exitDate")+" | "+mp.get("exitTime"));
                }
            }

            if(flag)
                return "NO RESULT FOUND";

            return "Success";
        }
        catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
    public String slotsFromColor(String color) {
        try{
            System.out.println("\t* Car Color: "+ color);
            System.out.println("\t\t Floor|Row\t RegNum\t EntryDate|EntryTime\t ExitDate|ExitTime");

            boolean flag = true;
            for (String key : RedisDB.jedis.keys("WLPK*")) {
                Map<String, String> mp = RedisDB.jedis.hgetAll(key);
                if(mp.get("color").equalsIgnoreCase(color)){
                    flag = false;
                    System.out.println("\t\t "+ Integer.parseInt(mp.get("floor"))+" | "+Integer.parseInt(mp.get("row"))+"\t "+
                            mp.get("regNum")+"\t "+
                            mp.get("entryDate")+" | "+mp.get("entryTime")+"\t "+
                            mp.get("exitDate")+" | "+mp.get("exitTime"));
                }
            }
            if(flag)
                return "NO RESULT FOUND";

            return "Success";

        }
        catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
    }


    public void service(){
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
                System.out.println("\t\t\t****** REDIS MySqlAdmin Service Portal ******");
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
                        System.out.println(carsFromColor(color));
                        break;
                    case 2:
                        System.out.println("\t\t Enter The Registration Number");
                        regNumber = br.readLine().trim().toUpperCase();
                        System.out.println(slotOfCar(regNumber));
                        break;
                    case 3:
                        System.out.println("\t\t Enter The Color Of the Car");
                        color = br.readLine().trim().toUpperCase();
                        System.out.println(slotsFromColor(color));
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
            e.printStackTrace();

        }

    }

}
