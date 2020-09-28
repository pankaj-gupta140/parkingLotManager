package core;

/*
* It contains all the fields for generating core.Parking ticket.
 */
public class Parking {
    private String uniqueId;
    private int floor;
    private int row;
    private Car car;
    private String entryDate;
    private String entryTime;
    private String exitDate;
    private String exitTime;

    public Parking() {
        this.uniqueId = "";
        this.floor = 0;
        this.row = 0;
        this.car = null;
        this.entryDate = "";
        this.entryTime = "";
        this.exitDate = "";
        this.exitTime = "";
    }


    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public String getExitDate() {
        return exitDate;
    }

    public void setExitDate(String exitDate) {
        this.exitDate = exitDate;
    }

    public String getExitTime() {
        return exitTime;
    }

    public void setExitTime(String exitTime) {
        this.exitTime = exitTime;
    }
}
