package com.example.complaintclose.Adapters;

public class datashowmodule {
    String groupname,itemName,qntyno, serialNo;

    public datashowmodule(String groupname, String itemName, String qntyno, String serialNo) {
        this.groupname = groupname;
        this.itemName = itemName;
        this.qntyno = qntyno;
        this.serialNo = serialNo;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getQntyno() {
        return qntyno;
    }

    public void setQntyno(String qntyno) {
        this.qntyno = qntyno;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }
}
