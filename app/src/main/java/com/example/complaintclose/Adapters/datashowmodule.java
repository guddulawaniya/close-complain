package com.example.complaintclose.Adapters;

public class datashowmodule {
    String  groupname,itemName,qntyno, serialNo;
    int id;

    public datashowmodule(int id,String groupname, String itemName, String qntyno, String serialNo) {
        this.id = id;
        this.groupname = groupname;
        this.itemName = itemName;
        this.qntyno = qntyno;
        this.serialNo = serialNo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
