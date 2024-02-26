package com.example.complaintclose;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.complaintclose.javafiles.InternetConnection;
import com.example.complaintclose.javafiles.config_file;
import com.example.complaintclose.javafiles.fetchdata_from_sqlite;
import com.example.complaintclose.javafiles.fetchdata_from_sqlite_return_array;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class close_complaint_Form_Activity extends AppCompatActivity {


    List<String> itemlist,statelist ,brandlist,partynamelist,citylist ,complainlist;

    InternetConnection internetConnection;

    AutoCompleteTextView partyname, state, city, brand, complain;
    MaterialAutoCompleteTextView  createdate;
    TextInputEditText address, TDS_IN, TDS_out, partycode, complainNo;
    TextInputLayout addresslayout, statelayout, tdsoutlayout, citylayout, descripationlayout, tdsinlayout, partynamelayout, brandlayout, partycodelayout, complainNolayout;
    Button submitbutton;
    ProgressDialog mProgressDialog;

    String partynameindex, brandindex, countyindex, stateindex,createtime, cityindex,encodeImageString;

    fetchdata_from_sqlite_return_array fetchdata_arrayform;
    fetchdata_from_sqlite fetchdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_form);
        internetConnection = new InternetConnection(this);

        partycodelayout = findViewById(R.id.partycodelayout);
        partynamelayout = findViewById(R.id.partynamelayout);
        tdsinlayout = findViewById(R.id.tdsinlayout);
        addresslayout = findViewById(R.id.addresslayout);
        tdsoutlayout = findViewById(R.id.tdsoutlayout);
        complainNolayout = findViewById(R.id.complainNolayout);
        citylayout = findViewById(R.id.citylayout);
        statelayout = findViewById(R.id.statelayout);
        descripationlayout = findViewById(R.id.descripationlayout);
        brandlayout = findViewById(R.id.brandlayout);
        complainNo = findViewById(R.id.complaintNo);
        createdate = findViewById(R.id.cretedate);
        partycode = findViewById(R.id.partyCode);
        partyname = findViewById(R.id.partyName);
        brand = findViewById(R.id.brand);
        complain = findViewById(R.id.complaint);

        city = findViewById(R.id.city);
        state = findViewById(R.id.state);
        address = findViewById(R.id.address);

        TDS_IN = findViewById(R.id.tdsIn);
        TDS_out = findViewById(R.id.tdsOut);

        submitbutton = findViewById(R.id.saveButton);
//        linearlayoutlist = findViewById(R.id.linearlayoutlist);
        ImageView backarrow = findViewById(R.id.backarrow);

        brandlist = new ArrayList<>();
        statelist = new ArrayList<>();
        citylist = new ArrayList<>();
        partynamelist = new ArrayList<>();
        itemlist = new ArrayList<>();
        complainlist = new ArrayList<>();
        fetchdata_arrayform = new fetchdata_from_sqlite_return_array(this);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        String complainno = intent.getStringExtra("complainno");
        complainNo.setText(complainno);
        partyname.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                partynameindex = String.valueOf(arg2 + 1);

            }
        });
        brand.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                brandindex = String.valueOf(arg2 + 1);

            }
        });
        state.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                stateindex = String.valueOf(arg2 + 1);

            }
        });
        city.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                cityindex = String.valueOf(arg2 + 1);

            }
        });

        fetchdata = new fetchdata_from_sqlite(this);
        if (internetConnection.isConnected() && id == 2) {

            getSingledata_From_complain_number(complainNo.getText().toString());

        }


        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getdatafromtextfields();
            }
        });


        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });




        TextWatchercalls();




        fetchdata_arrayform.partysetdynamicdata(partynamelist);
        fetchdata_arrayform.brandsetdynamicdata(brandlist);
        fetchdata_arrayform.citysetdynamicdata(citylist);
        fetchdata_arrayform.statesetdynamicdata(statelist);

        getdropdowndata(config_file.Base_url + "getcomplaintdescription.php", complainlist);

        ArrayAdapter<String> partnameAdapter = new ArrayAdapter<>(this, R.layout.list_layout, partynamelist);
        partyname.setDropDownBackgroundResource(R.color.dialog_bg);
        partyname.setAdapter(partnameAdapter);

        ArrayAdapter<String> brandadapter = new ArrayAdapter<>(this, R.layout.list_layout, brandlist);

        brand.setDropDownBackgroundResource(R.color.dialog_bg);
        brand.setAdapter(brandadapter);


        ArrayAdapter<String> stateadapter = new ArrayAdapter<>(this, R.layout.list_layout, statelist);
        state.setDropDownBackgroundResource(R.color.dialog_bg);
        state.setAdapter(stateadapter);

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, R.layout.list_layout, citylist);
        city.setDropDownBackgroundResource(R.color.dialog_bg);
        city.setAdapter(cityAdapter);

        ArrayAdapter<String> complainlistAdapter = new ArrayAdapter<>(close_complaint_Form_Activity.this, R.layout.list_layout, complainlist);
        complain.setDropDownBackgroundResource(R.color.dialog_bg);
        complain.setAdapter(complainlistAdapter);

        complainNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 10) {
                    getSingledata_From_complain_number(complainNo.getText().toString());
                } else if (s.length()==2) {
                    complainNo.setInputType(InputType.TYPE_CLASS_NUMBER);

                } else if (s.length()<2) {
                    complainNo.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);

                } else {
                    createdate.setText("");
                    partycode.setText("");
                    partyname.setText("");
                    brand.setText("");
                    complain.setText("");
                    city.setText("");
                    state.setText("");
                    address.setText("");
                    TDS_IN.setText("");
                    TDS_out.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    void TextWatchercalls()
    {
        textwachterinput(complainNo,complainNolayout);
        textwachterinput(partycode,partycodelayout);
        textwachterinput(address,addresslayout);
        textwachterinput(TDS_IN,tdsinlayout);
        textwachterinput(TDS_out,tdsoutlayout);

        textwachter(partyname,partynamelayout);
        textwachter(brand,brandlayout);
        textwachter(city,citylayout);
        textwachter(state,statelayout);
        textwachter(complain,descripationlayout);

    }

    void textwachter(AutoCompleteTextView autoCompleteTextView,TextInputLayout layout)
    {
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layout.setErrorEnabled(false);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    void textwachterinput(TextInputEditText inputEditText,TextInputLayout layout)
    {
        inputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layout.setErrorEnabled(false);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    void getdatafromtextfields()
    {
        if (complainNo.getText().toString().isEmpty()) {
            errorShowFunction(complainNolayout, complainNo);
        } else if (partycode.getText().toString().isEmpty()) {
            errorShowFunction(partycodelayout, partycode);
        } else if (partyname.getText().toString().isEmpty()) {
            autoerrorShowFunction(partynamelayout, partyname);
        } else if (brand.getText().toString().isEmpty()) {
            autoerrorShowFunction(brandlayout, brand);
        } else if (city.getText().toString().isEmpty()) {
            autoerrorShowFunction(citylayout, city);
        } else if (state.getText().toString().isEmpty()) {
            autoerrorShowFunction(statelayout, state);
        } else if (address.getText().toString().isEmpty()) {
            errorShowFunction(addresslayout, address);
        } else if (TDS_IN.getText().toString().isEmpty()) {
            errorShowFunction(tdsinlayout, TDS_IN);
        } else if (TDS_out.getText().toString().isEmpty()) {
            errorShowFunction(tdsoutlayout, TDS_out);
        } else if (complain.getText().toString().isEmpty()) {
            autoerrorShowFunction(descripationlayout, complain);
        }else {

                   nextactivity();
        }
    }

    void nextactivity()
    {
        Intent intent1 = new Intent(close_complaint_Form_Activity.this, secound_update_activity.class);
        intent1.putExtra("complainno", complainNo.getText().toString());
        intent1.putExtra("createdate", complainNo.getText().toString());
        intent1.putExtra("partycode", partycode.getText().toString());
        intent1.putExtra("descripation", complain.getText().toString());
        intent1.putExtra("address", address.getText().toString());
        intent1.putExtra("TDS_IN", TDS_IN.getText().toString());
        intent1.putExtra("TDS_out", TDS_out.getText().toString());
        intent1.putExtra("encodeImageString", encodeImageString);
        intent1.putExtra("createtime", createtime);
        intent1.putExtra("partyid", partynameindex);
        intent1.putExtra("brand", brandindex);
        intent1.putExtra("cityid", cityindex);
        intent1.putExtra("state", stateindex);
        startActivity(intent1);

    }
    void errorShowFunction(TextInputLayout layout, TextInputEditText text) {
        layout.startAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.shake_text));
        layout.setBoxStrokeErrorColor(ColorStateList.valueOf(Color.RED));
        layout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
        layout.setError("Required*");
        text.requestFocus();
    }

    void autoerrorShowFunction(TextInputLayout layout, AutoCompleteTextView text) {
        layout.startAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.shake_text));
        layout.setBoxStrokeErrorColor(ColorStateList.valueOf(Color.RED));
        layout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
        layout.setError("Required*");
        text.requestFocus();
    }


    private void getdropdowndata(String registrationURL, List<String> list) {

        class registration extends AsyncTask<String, String, String> {
            @Override
            protected void onPostExecute(String s) {

                try {
                    JSONArray object = new JSONArray(s);


                    for (int i = 0; i < object.length(); ++i) {
                        JSONObject object1 = object.getJSONObject(i);
                        String brandname = object1.getString("name");
                        list.add(brandname);
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);

                }
                super.onPostExecute(s);
            }

            @Override
            protected String doInBackground(String... param) {

                try {
                    URL url = new URL(param[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    return br.readLine();
                } catch (Exception ex) {
                    return ex.getMessage();
                }

            }


        }

        registration obj = new registration();
        obj.execute(registrationURL);

    }

    private void getSingledata_From_complain_number(String complainno) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Please Wait");
        mProgressDialog.setMessage("Loading..");
        mProgressDialog.show();

        String registrationURL = config_file.Base_url + "get_complaint_data_by_id.php" + "?complaint_id=" + complainno;


        class registration extends AsyncTask<String, String, String> {

            @Override
            protected void onPostExecute(String s) {

                try {
                    JSONArray object = new JSONArray(s);
                    JSONObject object1 = object.getJSONObject(0);
                    int status = object1.getInt("status");
                    createtime = object1.getString("create_time");
                    createdate.setText(object1.getString("create_date") + " " + createtime);
                    partycode.setText(object1.getString("party_code"));
                    complain.setText(object1.getString("complaint"));
                    address.setText(object1.getString("address"));
                    partynameindex = object1.getString("party_id");
                    brandindex = object1.getString("brand_id");
                    cityindex = object1.getString("city_id");
                    stateindex = object1.getString("state");
                    countyindex = object1.getString("country");

                    String partygetname = fetchdata.partysetdynamicdata(partynameindex);
                    String brandgetname = fetchdata.brandsetdynamicdata(brandindex);
                    String citygetname = fetchdata.citysetdynamicdata(cityindex);
                    String stategetname = fetchdata.statesetdynamicdata(stateindex);

                    partyname.setText(partygetname);
                    brand.setText(brandgetname);
                    city.setText(citygetname);
                    state.setText(stategetname);
                    mProgressDialog.dismiss();

                } catch (JSONException e) {
                    mProgressDialog.dismiss();
                    Toast.makeText(close_complaint_Form_Activity.this, "something went Wrong Try Again ", Toast.LENGTH_SHORT).show();
                }
                super.onPostExecute(s);
            }

            @Override
            protected String doInBackground(String... param) {

                try {
                    URL url = new URL(param[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    return br.readLine();
                } catch (Exception ex) {
                    return ex.getMessage();
                }

            }

        }
        registration obj = new registration();
        obj.execute(registrationURL);


    }


}