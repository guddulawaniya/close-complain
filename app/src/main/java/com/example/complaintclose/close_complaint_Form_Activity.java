package com.example.complaintclose;

import android.app.Activity;
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
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
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


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class close_complaint_Form_Activity extends AppCompatActivity {


    List<String> countrylist;
    List<String> statelist;
    List<String> brandlist;
    List<String> partynamelist;
    List<String> citylist;
    List<String> complainlist;
    List<String> itemlist;
    fetchdata_from_sqlite fetchdata;

    int CAMERA_PIC_REQUEST = 200;
    InternetConnection internetConnection;

    AutoCompleteTextView partyname, state, city, brand, itemName, country, complain;
    MaterialAutoCompleteTextView closedate,createdate;
    TextInputEditText address,TDS_IN,TDS_out, partycode,complainNo,groupname,itemquantity,serialno;
    TextInputLayout addresslayout, emailidlayout, phonenumberlayout, partycodelayout,complainNolayout;
    Button submitbutton;
    ProgressDialog mProgressDialog;
    AlertDialog alertDialog;
    LinearLayout linearlayoutlist;
    TextView selectimage,imagepath;

    String partynameindex, brandindex, locationindex, countyindex, stateindex, cityindex;
    private Calendar calendar;
    Bitmap bitmap;
    String encodeImageString,createtime;
    fetchdata_from_sqlite_return_array fetchdata_arrayform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_form);
        calendar = Calendar.getInstance();
        internetConnection = new InternetConnection(this);


        complainNo = findViewById(R.id.complaintNo);
        createdate = findViewById(R.id.cretedate);
        partycode = findViewById(R.id.partyCode);
        partyname = findViewById(R.id.partyName);
        brand = findViewById(R.id.brand);
        complain = findViewById(R.id.complaint);

        city = findViewById(R.id.city);
        closedate = findViewById(R.id.closedate);
        state = findViewById(R.id.state);
        country = findViewById(R.id.country);
        address = findViewById(R.id.address);

        TDS_IN = findViewById(R.id.tdsIn);
        TDS_out = findViewById(R.id.tdsOut);
        selectimage = findViewById(R.id.select_image);
        imagepath = findViewById(R.id.imageulr_path);
        submitbutton = findViewById(R.id.saveButton);
//        linearlayoutlist = findViewById(R.id.linearlayoutlist);
       ImageView backarrow = findViewById(R.id.backarrow);

        brandlist = new ArrayList<>();
        countrylist = new ArrayList<>();
        statelist = new ArrayList<>();
        citylist = new ArrayList<>();
        partynamelist = new ArrayList<>();
        itemlist = new ArrayList<>();
        complainlist = new ArrayList<>();
        fetchdata_arrayform = new fetchdata_from_sqlite_return_array(this);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id",0);
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
        if (internetConnection.isConnected() && id == 2)
        {

            getSingledata_From_complain_number(complainNo.getText().toString());

        }

        closedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(closedate);
            }
        });

        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(close_complaint_Form_Activity.this,secound_update_activity.class);
                intent1.putExtra("complainno",complainNo.getText().toString());
                intent1.putExtra("createdate",complainNo.getText().toString());
                intent1.putExtra("partycode",partycode.getText().toString());
                intent1.putExtra("descripation",complain.getText().toString());
                intent1.putExtra("address",address.getText().toString());
                intent1.putExtra("TDS_IN",TDS_IN.getText().toString());
                intent1.putExtra("TDS_out",TDS_out.getText().toString());
                intent1.putExtra("encodeImageString",encodeImageString);
                intent1.putExtra("createtime",createtime);
                intent1.putExtra("partyid",partynameindex);
                intent1.putExtra("brand",brandindex);
                intent1.putExtra("cityid",cityindex);
                intent1.putExtra("state",stateindex);
                intent1.putExtra("country",countyindex);
               startActivity(intent1);
            }
        });

        createdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(createdate);
            }
        });

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        selectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissions();

            }
        });


        mProgressDialog = new  ProgressDialog(this);
        mProgressDialog.setTitle("Please Wait");
        mProgressDialog.setMessage("Loading..");


        fetchdata_arrayform.partysetdynamicdata(partynamelist);
        fetchdata_arrayform.brandsetdynamicdata(brandlist);
        fetchdata_arrayform.citysetdynamicdata(citylist);
        fetchdata_arrayform.statesetdynamicdata(statelist);
        fetchdata_arrayform.countrysetdynamicdata(countrylist);

        getdropdowndata(config_file.Base_url + "getcomplaintdescription.php",complainlist);



        ArrayAdapter<String> partnameAdapter = new ArrayAdapter<>(this, R.layout.list_layout, partynamelist);
        partyname.setDropDownBackgroundResource(R.color.dialog_bg);
        partyname.setAdapter(partnameAdapter);

     ArrayAdapter<String> brandadapter = new ArrayAdapter<>(this, R.layout.list_layout, brandlist);

        brand.setDropDownBackgroundResource(R.color.dialog_bg);
        brand.setAdapter(partnameAdapter);


        ArrayAdapter<String> stateadapter = new ArrayAdapter<>(this, R.layout.list_layout, statelist);
        state.setDropDownBackgroundResource(R.color.dialog_bg);
        state.setAdapter(stateadapter);

        ArrayAdapter<String> citystateadapter = new ArrayAdapter<>(this, R.layout.list_layout, countrylist);
        country.setDropDownBackgroundResource(R.color.dialog_bg);
        country.setAdapter(citystateadapter);

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
                if (s.length()>10)
                {
                    getSingledata_From_complain_number(complainNo.getText().toString());
                }else
                {
                    createdate.setText("");
                    partycode.setText("");
                    partyname.setText("");
                    brand.setText("");
                    complain.setText("");
                    city.setText("");
                    state.setText("");
                    country.setText("");
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

    void errorShowFunction(TextInputLayout layout, TextInputEditText text)
    {
        layout.startAnimation(AnimationUtils.loadAnimation(getApplication(),R.anim.shake_text));
        layout.setBoxStrokeErrorColor(ColorStateList.valueOf(Color.RED));
        layout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
        layout.setError("Required*");
        text.requestFocus();
    }


    private void showDatePickerDialog(MaterialAutoCompleteTextView setdataontext) {
        long maxDateMillis = System.currentTimeMillis();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Update calendar with the selected date
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // Update the text in TextView
                        updateSelectedDateText(setdataontext);
                        showTimePickerDialog(setdataontext);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.getDatePicker().setMaxDate(maxDateMillis);
        datePickerDialog.show();

    }


    private void showTimePickerDialog(MaterialAutoCompleteTextView setdataontext) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Update calendar with the selected time
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        // Update the text in TextView
                        updateSelectedDateText(setdataontext);
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false // 24-hour format
        );

        timePickerDialog.show();
    }

    private void updateSelectedDateText(MaterialAutoCompleteTextView createdate) {
        // Format the selected date and time and display it in TextView
        String selectedDateTime = android.text.format.DateFormat.format("yyyy-MM-dd HH:mm", calendar).toString();
        createdate.setText(selectedDateTime);
    }
    private void getdropdowndata(String registrationURL,List<String> list) {

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

        String registrationURL = config_file.Base_url + "get_complaint_data_by_id.php"+"?complaint_id="+complainno;


        class registration extends AsyncTask<String, String, String> {

            @Override
            protected void onPostExecute(String s) {

                try {
                    JSONArray object = new JSONArray(s);
                    JSONObject object1 = object.getJSONObject(0);
                    int status = object1.getInt("status");
                    createtime = object1.getString("create_time");
                    createdate.setText(object1.getString("create_date")+" "+createtime);
                    partycode.setText(object1.getString("party_code"));
                    complain.setText(object1.getString("complaint"));
                    address.setText(object1.getString("address"));

                    partynameindex = object1.getString("party_id");
                    brandindex = object1.getString("brand_id");
                    cityindex = object1.getString("city_id");
                    stateindex = object1.getString("state");
                    countyindex = object1.getString("country");

                    String partygetname =  fetchdata.partysetdynamicdata(partynameindex);
                    String brandgetname =  fetchdata.brandsetdynamicdata(brandindex);
                    String citygetname =  fetchdata.citysetdynamicdata(cityindex);
                    String stategetname =  fetchdata.statesetdynamicdata(stateindex);
                    String countrygetname =  fetchdata.citysetdynamicdata(countyindex);

                    partyname.setText(partygetname);
                    brand.setText(brandgetname);
                    city.setText(citygetname);
                    state.setText(stategetname);
                    country.setText(countrygetname);



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

    private void requestPermissions() {
        // below line is use to request permission in the current activity.
        // this method is use to handle error in runtime permissions
        Dexter.withActivity(this)
                // below line is use to request the number of permissions which are required in our app.
                .withPermissions(android.Manifest.permission.CAMERA)
                // after adding permissions we are calling an with listener method.
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        // this method is called when all permissions are granted
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {

                            Intent intent=new Intent(Intent.ACTION_PICK);

                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent,"Browse Image"),CAMERA_PIC_REQUEST);


//                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                            startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                        }
                        // check for permanent denial of any permission
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permanently, we will show user a dialog message.
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        // this method is called when user grants some permission and denies some of them.
                        permissionToken.continuePermissionRequest();
                    }
                }).withErrorListener(error -> {
                    // we are displaying a toast message for error message.
                    Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                })
                // below line is use to run the permissions on same thread and to check the permissions
                .onSameThread().check();
    }

    // below is the shoe setting dialog method which is use to display a dialogue message.
    private void showSettingsDialog() {
        // we are displaying an alert dialog for permissions
        AlertDialog.Builder builder = new AlertDialog.Builder(close_complaint_Form_Activity.this);

        // below line is the title for our alert dialog.
        builder.setTitle("Need Permissions");

        // below line is our message for our dialog
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            // this method is called on click on positive button and on clicking shit button
            // we are redirecting our user from our app to the settings page of our app.
            dialog.cancel();
            // below is the intent from which we are redirecting our user.
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, 101);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            // this method is called when user click on negative button.
            dialog.cancel();
        });
        // below line is used to display our dialog
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if(requestCode==CAMERA_PIC_REQUEST && resultCode==RESULT_OK)
        {
            Uri filepath=data.getData();
            try
            {
                InputStream inputStream=getContentResolver().openInputStream(filepath);
                bitmap= BitmapFactory.decodeStream(inputStream);
                String imagePath = saveImageToExternalStorage(bitmap);
                imagepath.setText(imagePath);
                encodeBitmapImage(bitmap);
            }catch (Exception ex)
            {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void encodeBitmapImage(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] bytesofimage=byteArrayOutputStream.toByteArray();
        encodeImageString=android.util.Base64.encodeToString(bytesofimage, Base64.DEFAULT);
    }


    private String saveImageToExternalStorage(Bitmap imageBitmap) {
        // Define the directory where you want to save the image
        File directory = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "YourAppName");

        // Create the directory if it doesn't exist
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Create a unique file name for the image
        String fileName = "IMG_" + System.currentTimeMillis() + ".jpg";

//        // Create the file in the specified directory
//        File file = new File(directory, fileName);
//
//        try {
//            // Save the bitmap to the file
//            FileOutputStream fos = new FileOutputStream(file);
//            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//            fos.flush();
//            fos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // Return the absolute path of the saved file
        return fileName;
    }

}