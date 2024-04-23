package com.example.complaintclose.javafiles;

import java.io.Serializable;
import java.util.ArrayList;

public class datapostmodule implements Serializable {
   private String complainnumber,
            compliantid,
            party_id,
            brand_name,
            party_code,
            address,
            cityid,
            state,
            email,
            phone,
            tdsin,
            tdsout,
            description;

    public datapostmodule(String complainnumber, String compliantid, String party_id, String brand_name, String party_code, String address, String cityid, String state, String email, String phone, String tdsin, String tdsout, String description) {
        this.complainnumber = complainnumber;
        this.compliantid = compliantid;
        this.party_id = party_id;
        this.brand_name = brand_name;
        this.party_code = party_code;
        this.address = address;
        this.cityid = cityid;
        this.state = state;
        this.email = email;
        this.phone = phone;
        this.tdsin = tdsin;
        this.tdsout = tdsout;
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTdsin() {
        return tdsin;
    }

    public void setTdsin(String tdsin) {
        this.tdsin = tdsin;
    }

    public String getTdsout() {
        return tdsout;
    }

    public void setTdsout(String tdsout) {
        this.tdsout = tdsout;
    }

    public String getComplainnumber() {
        return complainnumber;
    }

    public void setComplainnumber(String complainnumber) {
        this.complainnumber = complainnumber;
    }

    public String getCompliantid() {
        return compliantid;
    }

    public void setCompliantid(String compliantid) {
        this.compliantid = compliantid;
    }

    public String getParty_id() {
        return party_id;
    }

    public void setParty_id(String party_id) {
        this.party_id = party_id;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getParty_code() {
        return party_code;
    }

    public void setParty_code(String party_code) {
        this.party_code = party_code;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

