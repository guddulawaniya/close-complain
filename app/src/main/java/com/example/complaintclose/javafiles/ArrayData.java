package com.example.complaintclose.javafiles;

public class ArrayData {

    String group, itemname, itemqnty, serialno;

    public ArrayData(String group, String itemname, String itemqnty, String serialno) {
        this.group = group;
        this.itemname = itemname;
        this.itemqnty = itemqnty;
        this.serialno = serialno;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItemqnty() {
        return itemqnty;
    }

    public void setItemqnty(String itemqnty) {
        this.itemqnty = itemqnty;
    }

    public String getSerialno() {
        return serialno;
    }

    public void setSerialno(String serialno) {
        this.serialno = serialno;
    }
}
