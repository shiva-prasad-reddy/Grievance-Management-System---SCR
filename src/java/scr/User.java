
package scr;

public class User {

    private String id;
    private String name;
    private String designation;
    private String station;
    private String address;
    private String phone;
    private String mail;
    private TYPE type;
    public String higherofficial;
    public User() {
        this.id = null;
        this.name = null;
        this.designation = null;
        this.station = null;
        this.address = null;
        this.phone = null;
        this.mail = null;
        this.type = null;
    }
    public User(String id, String name, String designation, String station, String address, String phone, String mail, TYPE type) {
        this.id = id;
        this.name = name;
        this.designation = designation;
        this.station = station;
        this.address = address;
        this.phone = phone;
        this.mail = mail;
        this.type = type;
    }

    public void setHigherofficial(String higherofficial) {
        this.higherofficial = higherofficial;
    }

    public String getHigherofficial() {
        return higherofficial;
    }

   

    //setters
    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDesignation(String designation) {
        this.designation = designation;
    }
    public void setStation(String station) {
        this.station = station;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }
    public void setType(TYPE type) {
        this.type = type;
    }
    
    //Getters
    public String getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    public String getDesignation() {
        return this.designation;
    }
    public String getStation() {
        return this.station;
    }
    public String getAddress() {
        return this.address;
    }
    public String getPhone() {
        return this.phone;
    }
    public String getMail() {
        return this.mail;
    }
    public TYPE getType() {
        return this.type;
    }
}