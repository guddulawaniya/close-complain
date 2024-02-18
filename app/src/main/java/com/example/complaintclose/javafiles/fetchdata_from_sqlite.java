package com.example.complaintclose.javafiles;

import android.content.Context;
import android.database.Cursor;

import com.example.complaintclose.Sqlite_Files.Citynamedb;
import com.example.complaintclose.Sqlite_Files.brandnamedb;
import com.example.complaintclose.Sqlite_Files.countrynamedb;
import com.example.complaintclose.Sqlite_Files.partynamedb;
import com.example.complaintclose.Sqlite_Files.statenamedb;

public class fetchdata_from_sqlite {

    Citynamedb citynamedb;
    statenamedb statedb;
    countrynamedb countrydb;
    partynamedb partydb;
    brandnamedb branddb;
    Cursor cursor;
    Context context;
    String data = null;

    public fetchdata_from_sqlite(Context context) {
        this.context = context;
    }

    public String partysetdynamicdata(String id) {
        partydb = new partynamedb(context);
        cursor = partydb.getdata();

        if (this.cursor != null && this.cursor.moveToNext()) {
            int compid = Integer.parseInt(id);
            do {
                int userId = this.cursor.getInt(0);
                if (userId == compid) {
                    data = this.cursor.getString(1);
                }
            } while (this.cursor.moveToNext());
            this.cursor.close();
        }

        return data;
    }

    public String citysetdynamicdata(String id) {
        citynamedb = new Citynamedb(context);
        cursor = citynamedb.getdata();

        if (this.cursor != null && this.cursor.moveToNext()) {
            int compid = Integer.parseInt(id);
            do {
                int userId = this.cursor.getInt(0);
                if (userId == compid) {
                    data = this.cursor.getString(1);
                }
            } while (this.cursor.moveToNext());
            this.cursor.close();
        }
        return data;

    }

    public String brandsetdynamicdata(String id) {
        branddb = new brandnamedb(context);
        cursor = branddb.getdata();

        if (this.cursor != null && this.cursor.moveToNext()) {
            int compid = Integer.parseInt(id);
            do {
                int userId = this.cursor.getInt(0);
                if (userId == compid) {
                    data = this.cursor.getString(1);
                }
            } while (this.cursor.moveToNext());
            this.cursor.close();
        }
        return data;

    }

    public String statesetdynamicdata(String id) {
        statedb = new statenamedb(context);
        cursor = statedb.getdata();
        String data = null;

        if (this.cursor != null && this.cursor.moveToNext()) {
            int scompid = Integer.parseInt(id);

            do {
                int userId = this.cursor.getInt(0);
                if (userId == scompid) {
                    data = this.cursor.getString(1);
//                    textView.setText(data);
                }
            } while (this.cursor.moveToNext());
            this.cursor.close();
        }
        return data;

    }

    public String countrysetdynamicdata(String id) {
        countrydb = new countrynamedb(context);
        cursor = countrydb.getdata();

        if (this.cursor != null && this.cursor.moveToNext()) {
            int compid = Integer.parseInt(id);
            do {
                int userId = this.cursor.getInt(0);
                if (userId == compid) {
                    data = this.cursor.getString(1);
                }
            } while (this.cursor.moveToNext());
            this.cursor.close();
        }
        return data;
    }

}
