package Admin;

import Admin.APIs.adminServices;
import dbClients.MySqlDB;
import mainClassFile.ParkingLotManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Scanner;

public class MySqlAdmin implements adminServices {
    private static String password = "password";
    ResultSet resultset = null;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        MySqlAdmin.password = password;
    }

    @Override
    public String carsFromColor(String color){
        try{

            String qry = "SELECT RegNum from parking where color = '"+color+"'";
            resultset = MySqlDB.st.executeQuery(qry);

            int i=1;
            if(!resultset.next()){
                return "NO RESULT FOUND";
            }
            System.out.println("\t* Registration Number of all " +
                    color + " Cars Parked till today");
            do {
                System.out.println("\t\t "+ i++ +"-> " + resultset.getString(1));
            }while(resultset.next());

            return "Success";
        }
        catch (SQLException e){
            e.printStackTrace();
            return "exception";
        }


    }

    @Override
    public String slotOfCar(String regNumber){
        try{

            String qry = "SELECT pfloor,prow,entryDate,entryTime,exitDate,exitTime from parking where RegNum = '"+regNumber+"'";
            resultset = MySqlDB.st.executeQuery(qry);


            if(!resultset.next()){
                return "NO RECORD FOUND";
            }
            System.out.println("\t* Car Registration Number: "+ regNumber);
            System.out.println("\t\t Floor|Row\t EntryDate|EntryTime\t ExitDate|ExitTime");
            do {
                System.out.println("\t\t "+ resultset.getInt(1)+" | "+resultset.getInt(2)+"\t "+
                        resultset.getString(3)+" | "+resultset.getString(4)+"\t "+
                        resultset.getString(5)+" | "+resultset.getString(6));
            }while(resultset.next());

            return "Success";
        }
        catch (SQLException e){
            e.printStackTrace();
            return "exception";
        }

    }

    @Override
    public String slotsFromColor(String color){

        try{

            String qry = "SELECT pfloor,prow,RegNum,entryDate,entryTime,exitDate,exitTime from parking where color = '"+color+"'";
            resultset = MySqlDB.st.executeQuery(qry);


            if(!resultset.next()){
                return "NO RECORD FOUND";
            }
            System.out.println("\t* core.Car Color: "+ color);
            System.out.println("\t\t Floor|Row\t RegNum\t EntryDate|EntryTime\t ExitDate|ExitTime");
            do {
                System.out.println("\t\t "+ resultset.getInt(1)+" | "+resultset.getInt(2)+"\t "+
                        resultset.getString(3)+"\t "+
                        resultset.getString(4)+" | "+resultset.getString(5)+"\t "+
                        resultset.getString(6)+" | "+resultset.getString(7));
            }while(resultset.next());

            return "Success";
        }
        catch (SQLException e){
            e.printStackTrace();
            return "exception";
        }

    }
    /*
    All Admin.MySqlAdmin services options are here
     */
    public void service()  {
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
                System.out.println("\t\t\t****** MySQLAdmin Service Portal ******");
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
