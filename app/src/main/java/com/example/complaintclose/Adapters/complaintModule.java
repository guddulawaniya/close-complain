package com.example.complaintclose.Adapters;

public class complaintModule {

    String compliant_no, partyname, createDate, emailid,address,mobileno,brand,createtime,
            partycode,complainreason , city , state , country , groupname , itemname
            , tdsin ,tdsout;
    int status;

    public complaintModule(String compliant_no, String createDate, String partyname, String address, int statusText)
    {
        this.compliant_no = compliant_no;
        this.createDate = createDate;
        this.partyname = partyname;
        this.address = address;
        this.status = statusText;
    }

    public complaintModule(String compliant_no, String createDate, String createtime, String partyname, String address, String emailid, String mobileno, String brand, String partycode, String complainreason, String city, String state, String country, int status) {
        this.compliant_no = compliant_no;
        this.partyname = partyname;
        this.createDate = createDate;
        this.createtime = createtime;
        this.emailid = emailid;
        this.address = address;
        this.mobileno = mobileno;
        this.brand = brand;
        this.partycode = partycode;
        this.complainreason = complainreason;
        this.city = city;
        this.state = state;
        this.country = country;
        this.status = status;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getCompliant_no() {
        return compliant_no;
    }

    public void setCompliant_no(String compliant_no) {
        this.compliant_no = compliant_no;
    }

    public String getPartyname() {
        return partyname;
    }

    public void setPartyname(String partyname) {
        this.partyname = partyname;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPartycode() {
        return partycode;
    }

    public void setPartycode(String partycode) {
        this.partycode = partycode;
    }

    public String getComplainreason() {
        return complainreason;
    }

    public void setComplainreason(String complainreason) {
        this.complainreason = complainreason;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    String staticText,dynamicText;

    public complaintModule(String staticText, String dynamicText) {
        this.staticText = staticText;
        this.dynamicText = dynamicText;
    }

    public String getStaticText() {
        return staticText;
    }

    public void setStaticText(String staticText) {
        this.staticText = staticText;
    }

    public String getDynamicText() {
        return dynamicText;
    }

    public void setDynamicText(String dynamicText) {
        this.dynamicText = dynamicText;
    }


}
