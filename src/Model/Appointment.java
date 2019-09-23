/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;


import java.time.LocalDate;
import java.time.LocalTime;


/**
 *
 * @author Ethan
 */
public class Appointment {

    private int aId, cId;
    private String type, title, contact, desc, location, url;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalTime endTime;

    public Appointment(int aId, int cId, String type, String title, String contact, String location, String desc, String url, LocalDate startDate, LocalTime startTime, LocalTime endTime) {
        this.aId = aId;
        this.cId = cId;
        this.type = type;
        this.title = title;
        this.location = location;
        this.desc = desc;
        this.contact = contact;
        this.url = url;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Appointment(){
        
    }
    
    public int getApptId() {
        return aId;
    }

    public void setApptId(int id) {
        this.aId = id;
    }
    
    public int getCustId() {
        return cId;
    }

    public void setCustId(int id) {
        this.cId = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

}
