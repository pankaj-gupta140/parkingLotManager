package mainClassFile;

//import static org.junit.jupiter.api.Assertions.*;
//class ParkingLotManagerTest {
//
//}

import Admin.MySqlAdmin;
import dbClients.MySqlDB;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.FileReader;
import java.sql.ResultSet;
import java.util.Properties;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ParkingLotManagerTest {
    static MySqlDB park = null;
    static MySqlAdmin admin = null;
    ResultSet resultset = null;
    static String id = "";

    @BeforeClass
    public static void init(){
        try {
            park = new MySqlDB();
            admin = new MySqlAdmin();
            FileReader reader = new FileReader("src/main/resources/database.conf");
            Properties properties = new Properties();
            properties.load(reader);

            MySqlDB.driver = properties.getProperty("driver");
            MySqlDB.url = properties.getProperty("url");
            MySqlDB.username = properties.getProperty("username");
            MySqlDB.password = properties.getProperty("password");
            MySqlDB.createDatabase();
            park.updateParkingLot();

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }



    @Test
    public void t1_generateEntryTicketTest() {
        assertEquals("Success",park.generateEntryTicket("aaaa","red"));

    }



    @Test
    public void t3_exitTheTicketTest() throws Exception{
        resultset=MySqlDB.st.executeQuery("select uniqueId from parking where RegNum= 'aaaa' and exitDate = ''");
        if(resultset.next())
            id = resultset.getString(1);
        assertEquals("Success", park.exitTheTicket(id));

    }

//    @Test
//    public void t4_nearestParkingTest() throws Exception{
//        assertEquals("Success",park.generateEntryTicket("eeee","blue"));
//
//    }

    @Test
    public void t5_TicketExpiredTest() {
        assertEquals("Ticket Is Already Expired",park.exitTheTicket(id));

    }

    @Test
    public void t6_InValidTicketTest() {

        assertEquals("Ticket ID is Not Valid",park.exitTheTicket("123werrtt"));

    }

    @Test
    public void t7_carsFromColorTest(){

        assertEquals("Success",admin.carsFromColor("red"));

    }

    @Test
    public void t8_slotOfCarTest(){

        assertEquals("Success",admin.slotOfCar("aaaa"));


    }

    @Test
    public void t9_slotsFromColorTest(){

        assertEquals("Success",admin.slotsFromColor("red"));

    }






}