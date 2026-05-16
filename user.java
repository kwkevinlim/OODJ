public class user {

    //attributes
    private String userID;
    private String username;
    private String password;
    private String role;
    private String legalName;
    private String email;
    private String phoneNumber;
    private String address;
    private String gender;
    private String dob;

    //constructor
    public user (String userID, String username, String password, String role, 
        String legalName, String email, String phoneNumber, String address, 
        String gender, String dob) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.role = role;
        this.legalName = legalName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.gender = gender;
        this.dob = dob;
    }

    //getters
    public String getUserID() {return userID;}
    public String getUsername() {return username;}
    public String getPassword() {return password;}
    public String getRole() {return role;}
    public String getLegalName() {return legalName;}
    public String getEmail() {return email;}
    public String getPhoneNumber() {return phoneNumber;}
    public String getAddress() {return address;}
    public String getGender() {return gender;}
    public String getDob() {return dob;}

    //setters
    public void setUserID(String userID) {this.userID = userID;}
    public void setUsername(String username) {this.username = username;}
    public void setPassword(String password) {this.password = password;}
    public void setRole(String role) {this.role = role;}
    public void setLegalName(String legalName) {this.legalName = legalName;}
    public void setEmail(String email) {this.email = email;}
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}
    public void setAddress(String address) {this.address = address;}
    public void setGender(String gender) {this.gender = gender;}
    public void setDob(String dob) {this.dob = dob;}

    @Override
    public String toString() {
        return "UserID: " + userID + " | Username: " + username + 
               " | Password: " + password + " | Role: " + role + 
               " | Legal Name: " + legalName + " | Email: " + email + 
               " | Phone Number: " + phoneNumber + " | Address: " + address + 
               " | Gender: " + gender + " | Date of Birth: " + dob;
    }
    
}
