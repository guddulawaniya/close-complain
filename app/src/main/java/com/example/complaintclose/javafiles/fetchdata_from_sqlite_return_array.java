package com.example.complaintclose.javafiles;

import android.content.Context;
import android.database.Cursor;

import com.example.complaintclose.Sqlite_Files.Citynamedb;
import com.example.complaintclose.Sqlite_Files.brandnamedb;
import com.example.complaintclose.Sqlite_Files.countrynamedb;
import com.example.complaintclose.Sqlite_Files.partynamedb;
import com.example.complaintclose.Sqlite_Files.statenamedb;

import java.util.ArrayList;
import java.util.List;

public class fetchdata_from_sqlite_return_array {

    Citynamedb citynamedb;
    statenamedb statedb;
    countrynamedb countrydb;
    partynamedb partydb;
    brandnamedb branddb;
    Cursor cursor;
    Context context;

    public fetchdata_from_sqlite_return_array(Context context) {
        this.context = context;
    }

    public void partysetdynamicdata(List<String> list) {
        partydb = new partynamedb(context);
        cursor = partydb.getdata();

        if (this.cursor != null && this.cursor.moveToNext()) {
            do {
                String data = this.cursor.getString(1);
                list.add(data);

            } while (this.cursor.moveToNext());
            this.cursor.close();
        }

    }

    public void citysetdynamicdata(List<String> list) {
        citynamedb = new Citynamedb(context);
        cursor = citynamedb.getdata();

        if (this.cursor != null && this.cursor.moveToNext()) {

            do {

                String data = this.cursor.getString(1);
                list.add(data);
            } while (this.cursor.moveToNext());
            this.cursor.close();
        }
    }

    public void brandsetdynamicdata(List<String> list) {
        branddb = new brandnamedb(context);
        cursor = branddb.getdata();

        if (this.cursor != null && this.cursor.moveToNext()) {

            do {
                String data = this.cursor.getString(1);
                list.add(data);
            } while (this.cursor.moveToNext());
            this.cursor.close();
        }

    }

    public void statesetdynamicdata(List<String> list) {
        statedb = new statenamedb(context);
        cursor = statedb.getdata();

        if (this.cursor != null && this.cursor.moveToNext()) {

            do {
                String data = this.cursor.getString(1);
                list.add(data);
            } while (this.cursor.moveToNext());
            this.cursor.close();
        }

    }

    public void countrysetdynamicdata(List<String> list) {
        countrydb = new countrynamedb(context);
        cursor = countrydb.getdata();

        if (this.cursor != null && this.cursor.moveToNext()) {
            do {
                String data = this.cursor.getString(1);
                list.add(data);
            } while (this.cursor.moveToNext());
            this.cursor.close();
        }
    }

}
