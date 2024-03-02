package com.example.complaintclose;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Base64;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.complaintclose.Adapters.data_insert_module;
import com.example.complaintclose.javafiles.ApiService;
import com.example.complaintclose.javafiles.ArrayData;
import com.example.complaintclose.javafiles.RetrofitClient;
import com.example.complaintclose.javafiles.config_file;
import com.example.complaintclose.javafiles.datapostmodule;
import com.google.android.material.textfield.TextInputEditText;
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
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class secound_update_activity extends AppCompatActivity {

    CardView itemcard;
    Button prebutton, submitbutton;
    AutoCompleteTextView groupname, itemName;
    TextInputEditText serialNo, qntyno;
    ImageView backarrow;
    LinearLayout addlinearlayout;

    List<String> grouplist, itemlist;
    Bitmap bitmap;
    TextView viewitem;
    String encodeImageString;
    ProgressDialog mProgressDialog;
    ArrayList<ArrayData> datalist;
    TextView selectimage, imagepath;

    List<datapostmodule> postdatalist;

    String index, complainno, createdate, createtime, emailid, mobileno, partyid, tdsin, tdsout, partycode, descripation, brand, address, cityid, state, country;

    int CAMERA_PIC_REQUEST = 200;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secount_activity_layout);

        TextView addbutton = findViewById(R.id.additembutton);
        viewitem = findViewById(R.id.viewbutton);
        prebutton = findViewById(R.id.prebutton);
        submitbutton = findViewById(R.id.saveButton);
        backarrow = findViewById(R.id.backarrow);
        selectimage = findViewById(R.id.select_image);
        imagepath = findViewById(R.id.imageulr_path);
        addlinearlayout = findViewById(R.id.addlinearlayout);
        serialNo = findViewById(R.id.serialNo);
        qntyno = findViewById(R.id.qntyno);
       RelativeLayout deletelayout = findViewById(R.id.deletelayout);
       deletelayout.setVisibility(View.GONE);



        grouplist = new ArrayList<>();
        itemlist = new ArrayList<>();
        datalist = new ArrayList<>();
        postdatalist = new ArrayList<>();

        Intent intent = getIntent();
        complainno = intent.getStringExtra("complainno");
        createdate = intent.getStringExtra("createdate");
        createtime = intent.getStringExtra("createtime");
        partycode = intent.getStringExtra("partycode");
        descripation = intent.getStringExtra("descripation");
        brand = intent.getStringExtra("brand");
        address = intent.getStringExtra("address");
        cityid = intent.getStringExtra("cityid");
        state = intent.getStringExtra("state");
        partyid = intent.getStringExtra("partyid");
        country = intent.getStringExtra("country");
        state = intent.getStringExtra("state");
        country = intent.getStringExtra("country");
        tdsin = intent.getStringExtra("TDS_IN");
        tdsout = intent.getStringExtra("TDS_out");

        SharedPreferences preferences = getSharedPreferences("postdata", MODE_PRIVATE);
        emailid = preferences.getString("email", null);
        mobileno = preferences.getString("number", null);
        index = preferences.getString("index", null);





        // inflate the dynamic layout of card
//        additemfor_series_number();
        groupname = findViewById(R.id.groupname);
        itemName =  findViewById(R.id.itemName);

        ArrayAdapter<String> groupadapter = new ArrayAdapter<>(this, R.layout.list_layout, grouplist);
        groupname.setDropDownBackgroundResource(R.color.dialog_bg);

        groupname.setAdapter(groupadapter);

        ArrayAdapter<String> itemadapter = new ArrayAdapter<>(this, R.layout.list_layout, itemlist);
        itemName.setDropDownBackgroundResource(R.color.dialog_bg);
        itemName.setAdapter(itemadapter);


        // loader dialog box
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Please Wait");
        mProgressDialog.setMessage("Loading..");

        getdropdowndata(config_file.Base_url + "getgroupname.php", grouplist,false);
        getdropdowndata(config_file.Base_url + "getitemname.php", itemlist,true);



        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        viewitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(secound_update_activity.this,data_show.class);
                intent1.putExtra("id",complainno);
                startActivity(intent1);

            }
        });

        selectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camerapermission();

            }
        });
        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (imagepath.getText().toString().isEmpty()) {
                    Toast.makeText(secound_update_activity.this, "Please select upload image", Toast.LENGTH_SHORT).show();
                } else {
                    getdatafromdynamic_layout();
                    uploaddatatodb();
                }

            }
        });

        prebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (groupname.getText().toString().isEmpty())
                {
                    Toast.makeText(secound_update_activity.this, "Select Group name", Toast.LENGTH_SHORT).show();
                } else if (itemName.getText().toString().isEmpty()) {
                    Toast.makeText(secound_update_activity.this, "Select Group name", Toast.LENGTH_SHORT).show();

                } else if (qntyno.getText().toString().isEmpty()) {
                    Toast.makeText(secound_update_activity.this, "Select Group name", Toast.LENGTH_SHORT).show();

                } else if (serialNo.getText().toString().isEmpty()) {
                    Toast.makeText(secound_update_activity.this, "Select Group name", Toast.LENGTH_SHORT).show();
                }
                else {

                    postdataonlygroupapi();
                }

            }
        });

    }

    private void postdataonlygroupapi() {
        mProgressDialog.show();

        String registrationURL = config_file.Base_url+"item_details_close.php?complainnumber="+complainno+"&group="+groupname.getText().toString()+"&itemname="+itemName.getText().toString()+"&qty="+qntyno.getText().toString()+"&srno="+serialNo.getText().toString();
        class registration extends AsyncTask<String, String, String> {
            @Override
            protected void onPostExecute(String s) {
                groupname.setText("");
                itemName.setText("");
                serialNo.setText("");
                qntyno.setText("");
                mProgressDialog.dismiss();

                Toast.makeText(secound_update_activity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();


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

    void closedialog(String response)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(secound_update_activity.this);

        builder.setMessage(response);

        builder.setTitle("Complaint");

        builder.setCancelable(false);


        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            Intent intent1 = new Intent(secound_update_activity.this, MainActivity.class);
            (secound_update_activity.this).finish();
            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent1);

        });
        AlertDialog  alertDialog = builder.create();
        alertDialog.show();
    }

    void getdatafromdynamic_layout() {
        int count = addlinearlayout.getChildCount();
        for (int i = 0; i < count; i++) {
            View itemlayout = addlinearlayout.getChildAt(i);
            TextView groupfield = itemlayout.findViewById(R.id.groupname);
            TextView itemname = itemlayout.findViewById(R.id.itemName);
            TextView qntyno = itemlayout.findViewById(R.id.qntyno);
            TextView serialno = itemlayout.findViewById(R.id.serialNo);


            String gpdata = groupfield.getText().toString();
            String itdata = itemname.getText().toString();
            String qntydata = qntyno.getText().toString();
            String serialdata = serialno.getText().toString();
            datalist.add(new ArrayData(gpdata, itdata, qntydata, serialdata));

        }
    }

    private void animateDuplicateView(View view) {
        // ViewPropertyAnimator example (translation animation)
        Animation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f
        );

        animation.setDuration(500);
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }



    private void camerapermission() {
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

                            Intent intent = new Intent(Intent.ACTION_PICK);

                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent, "Browse Image"), CAMERA_PIC_REQUEST);


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

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(secound_update_activity.this);

        builder.setTitle("Need Permissions");

        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {

            dialog.cancel();

            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, 101);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {

            dialog.cancel();
        });

        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {
            Uri filepath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                String imagePath = saveImageToExternalStorage(bitmap);
                imagepath.setText(imagePath);
                encodeBitmapImage(bitmap);
            } catch (Exception ex) {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void encodeBitmapImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytesofimage = byteArrayOutputStream.toByteArray();
        encodeImageString = android.util.Base64.encodeToString(bytesofimage, Base64.DEFAULT);
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


    private void getdropdowndata(String registrationURL, List<String> list,boolean check) {
        mProgressDialog.show();

        class registration extends AsyncTask<String, String, String> {
            @Override
            protected void onPostExecute(String s) {

                if (check)
                {
                    mProgressDialog.dismiss();
                }

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

    void senddataonserver()
    {
        mProgressDialog.show();
        datapostmodule datapostmodule = new datapostmodule(complainno,index,partyid,brand,partycode,cityid,state,emailid,mobileno,descripation,datalist);

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        Call<Void> call = apiService.sendDataArray(datapostmodule);
        call.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {

                if (response.isSuccessful()) {

                    mProgressDialog.dismiss();
                    Toast.makeText(secound_update_activity.this, "Successfully Close Complaint", Toast.LENGTH_SHORT).show();
                    closedialog(response.message().toString());
                } else {
                    // Handle error
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
            }
        });
    }


    private void postDataUsingVolley() {
        String url = config_file.Base_url + "closecomplaint.php";

        long currentTimeMillis = System.currentTimeMillis();
        Date currentDate = new Date(currentTimeMillis);
        LocalDate dates = LocalDate.now();

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String currentTime = dateFormat.format(currentDate);


        RequestQueue queue = Volley.newRequestQueue(secound_update_activity.this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                mProgressDialog.dismiss();
                Toast.makeText(secound_update_activity.this, "Successfully Close Complaint", Toast.LENGTH_SHORT).show();
//                closedialog();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
//                Toast.makeText(secound_update_activity.this, "Please try again something went wrong", Toast.LENGTH_SHORT).show();
                Toast.makeText(secound_update_activity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("compliantnumber", mobileno);
                params.put("compliantid", index);
                params.put("party_id", partyid);
                params.put("brand_name", brand);
                params.put("party_code", partycode);
                params.put("cityid", cityid);
                params.put("state", state);
                params.put("email", emailid);
                params.put("phone", mobileno);
                params.put("description", descripation);
                params.put("under", datalist.toString());
//                params.put("party_address", address);
//                params.put("date_s", createdate);
//                params.put("time_s", createtime);
//                params.put("under", descripation);
//                params.put("TDS_IN", tdsin);
//                params.put("TDS_OUT", tdsout);
//                params.put("DATE", String.valueOf(dates));
//                params.put("TIME", currentTime);
                return params;
            }
        };
        queue.add(request);
    }

    private void uploaddatatodb() {

        mProgressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, config_file.Base_url + "imageupload.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                senddataonserver();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("upload", encodeImageString);
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
}
