package digitalcard.digitalcard.Model;

public class Location {
    public String name, address;
    public  Double latitude, longitude;

    public Location(String name, String address, Double latitude, Double longitude) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double  getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude){this.latitude = latitude;}

    public Double  getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude){this.longitude = longitude;}

}
