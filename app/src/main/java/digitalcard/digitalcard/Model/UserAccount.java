package digitalcard.digitalcard.Model;

public class UserAccount {
    private int id;
    private String name, email, dob, identityNumber, address, phoneNumber;

    public UserAccount() {}

    public UserAccount(int id, String name, String email, String dob, String identityNumber, String address, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.identityNumber = identityNumber;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public UserAccount(String name, String email, String dob, String identityNumber, String address, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.identityNumber = identityNumber;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
