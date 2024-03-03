package com.example.complaintclose.Adapters;

public class datashowmodule {
    String  groupname,itemName,qntyno, serialNo;
    int index;

    public datashowmodule(int index,String groupname, String itemName, String qntyno, String serialNo) {
        this.index = index;
        this.groupname = groupname;
        this.itemName = itemName;
        this.qntyno = qntyno;
        this.serialNo = serialNo;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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
