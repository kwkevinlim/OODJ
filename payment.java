public class payment {
    //attributes
    private String paymentID;
    private String appointmentID;
    private String customerID;
    private String csID;
    private String paymentDate;
    private String paymentTime;

    public payment (String paymentID, String appointmentID, String customerID, String csID, String paymentDate, String paymentTime) {
        this.paymentID = paymentID;
        this.appointmentID = appointmentID;
        this.customerID = customerID;
        this.csID = csID;
        this.paymentDate = paymentDate;
        this.paymentTime = paymentTime;
    }

    //getters
    public String getPaymentID() { return this.paymentID; }
    public String getAppointmentID() { return this.appointmentID; }
    public String getCustomerID() { return this.customerID; }
    public String getCsID() { return this.csID; }
    public String getPaymentDate() { return this.paymentDate; }
    public String getPaymentTime() { return this.paymentTime; }

    @Override
    public String toString() {
        return "Payment ID: " + paymentID + " | Appointment ID: " + appointmentID + " | customerID: " + customerID +
                " | csID: " + csID + " | Payment Date: " + paymentDate + " | Payment Time: " + paymentTime;
    }
}
