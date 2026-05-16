public class service {
    //attributes
    private String serviceID;
    private String serviceName;
    private String price;
    private String duration;

    public service (String serviceID, String serviceName, String price, String duration) {
        this.serviceID = serviceID;
        this.serviceName = serviceName;
        this.price = price;
        this.duration = duration;
    }

    //getters
    public String getServiceID() { return this.serviceID; }
    public String getServiceName() { return this.serviceName; }
    public String getPrice() { return this.price; }
    public String getDuration() { return this.duration; }

    //set values
    public void setPrice(String price) { this.price = price; }
    public void setDuration(String duration) { this.duration = duration; }

    @Override 
    public String toString() {
        return "Service ID: " + serviceID + " | Service Name: " + serviceName + " | Price: " + price + " | Duration: " + duration;
    }
}
