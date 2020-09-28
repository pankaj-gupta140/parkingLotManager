package Admin.APIs;

public interface adminServices {

    // Registration numbers of all cars of a Particular color
    String carsFromColor(String color);

    // Slot number in which a car with a given registration number is parked
    String slotOfCar(String regNumber);

    // Slot numbers of all slots where a car of a particular color is parked
    String slotsFromColor(String color);

    // Get the Details of Cars of a Particular Date
//    void carsFromDate();


}
