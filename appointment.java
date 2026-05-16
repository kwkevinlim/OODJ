public class appointment {
    
    //attributes
    private String appointmentID;
    private String customerName;
    private String service;
    private String technicianName;
    private String carModel;
    private String carPlateNumber;
    private String appointmentDate;
    private String appointmentTime;
    private String appointmentStatus;

    //constructor
    public appointment(String appointmentID, String customerName, String service, String technicianName, String carModel, String carPlateNumber, String appointmentDate, String appointmentTime, String appointmentStatus) {
        this.appointmentID = appointmentID;
        this.customerName = customerName;
        this.service = service;
        this.technicianName = technicianName;
        this.carModel = carModel;
        this.carPlateNumber = carPlateNumber;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.appointmentStatus = appointmentStatus;
    }

    //getters
    public String getAppointmentID() { return this.appointmentID; }
    public String getCustomerName() { return this.customerName; }
    public String getService() { return this.service; }
    public String getTechnicianName() { return this.technicianName; }
    public String getCarModel() { return this.carModel; }
    public String getCarPlateNumber() { return this.carPlateNumber; }
    public String getAppointmentDate() { return this.appointmentDate; }
    public String getAppointmentTime() { return this.appointmentTime; }
    public String getAppointmentStatus() { return this.appointmentStatus; }

    //status setter
    public void setStatus(String appointmentStatus) { this.appointmentStatus = appointmentStatus;}

    @Override
    public String toString () {
        return "Appointment ID: " + appointmentID + " | Customer Name: " + customerName +
               " | Service: " + service + " | Technician Name: " + technicianName +
               " | Car Model: " + carModel + " | Car Plate Number: " + carPlateNumber +
               " | Appointment Date: " + appointmentDate + " | Appointment Time: " + appointmentTime +
               " | Appointment Status: " + appointmentStatus;
    }
}
