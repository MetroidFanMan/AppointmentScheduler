/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;


/**
 *
 * @author Ethan
 */
public class Customer {
    
    private int cId, aId;
    private String name, address, address2, city, country, zipcode, phone;
    
    public Customer(int cId, int aId, String name, String address, String address2, String city, String country, String zipcode, String phone){
        this.cId = cId;
        this.aId = aId;
        this.name = name;
        this.address = address;
        this.address2 = address2;
        this.city = city;
        this.country = country;
        this.zipcode = zipcode;
        this.phone = phone;
    }

    public Customer() {
        
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }
    
    public int getcId() {
        return cId;
    }

    public void setcId(int id) {
        this.cId = id;
    }
    
    public int getaId() {
        return aId;
    }

    public void setaId(int id) {
        this.aId = id;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}