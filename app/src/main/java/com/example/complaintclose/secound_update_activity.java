package com.example.complaintclose;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
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
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.complaintclose.Adapters.itemviewAdapter;
import com.example.complaintclose.Roomdatabase.AppDatabase;
import com.example.complaintclose.Roomdatabase.ItemDao;
import com.example.complaintclose.Roomdatabase.notes;
import com.example.complaintclose.javafiles.ApiService;
import com.example.complaintclose.javafiles.ArrayData;
import com.example.complaintclose.javafiles.NetworkUtils;
import com.example.complaintclose.javafiles.RetrofitClient;
import com.example.complaintclose.javafiles.config_file;
import com.example.complaintclose.javafiles.datapostmodule;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class secound_update_activity extends AppCompatActivity {

    CardView itemcard;
    Button prebutton, submitbutton;
    AutoCompleteTextView groupname, itemName;
    TextInputEditText serialNo, qntyno;
    ImageView backarrow;
    RecyclerView recyclerview;

    List<String> grouplist, itemlist;
    Bitmap bitmap;
    TextView viewitem;
    String encodeImageString;
    ProgressBar mProgressDialog;
    ArrayList<ArrayData> datalist;
    TextView selectimage, imagepath;
    String imagePathstring;

    List<datapostmodule> postdatalist;
    TextInputLayout seriallayout, itemqntylayout, itemlayout, grouplayout;

    String index, complainno, createdate, createtime, emailid, mobileno, partyid, tdsin, tdsout, partycode, descripation, brand, address, cityid, state, country;

    int CAMERA_PIC_REQUEST = 200;
    private static final int REQUEST_GALLERY = 1;

    private static final String TAG = "ImageUploadProgress";
    private static final String CHANNEL_ID = "MyNotificationChannel";
    private static final int NOTIFICATION_ID = 1;

    static Context context;
    private AppDatabase database;
    private ItemDao noteDao;

    NotificationHelper notificationHelper;
    SwipeRefreshLayout swipeRefreshLayout;
    private static final int REQUEST_CODE = 101;

    ConstraintLayout nointernet;
    LinearLayout linearLayout1, linearLayout2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secount_activity_layout);

        TextView addbutton = findViewById(R.id.additembutton);
        nointernet = findViewById(R.id.nointernet);
        linearLayout2 = findViewById(R.id.linearLayout2);
        linearLayout1 = findViewById(R.id.linearlayout1);
        viewitem = findViewById(R.id.viewbutton);
        prebutton = findViewById(R.id.prebutton);
        submitbutton = findViewById(R.id.saveButton);
        backarrow = findViewById(R.id.backarrow);
        selectimage = findViewById(R.id.select_image);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        imagepath = findViewById(R.id.imageulr_path);
        recyclerview = findViewById(R.id.recyclerview);
        serialNo = findViewById(R.id.serialNo);
        qntyno = findViewById(R.id.qntyno);
        grouplayout = findViewById(R.id.grouplayout);
        itemcard = findViewById(R.id.itemcard);
        itemlayout = findViewById(R.id.itemlayout);
        mProgressDialog = findViewById(R.id.progressBar);
        itemqntylayout = findViewById(R.id.itemqntylayout);
        seriallayout = findViewById(R.id.seriallayout);
        LinearLayout imageuploadlinear = findViewById(R.id.imageuploadlinear);
        RelativeLayout deletelayout = findViewById(R.id.deletelayout);
        deletelayout.setVisibility(View.GONE);

        notificationHelper = new NotificationHelper(this);

        database = AppDatabase.getInstance(this);
        noteDao = database.notesDao();
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getdataofitems(complainno);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        noteDao.getAlldata().observe(secound_update_activity.this, new Observer<List<notes>>() {
            @Override
            public void onChanged(List<notes> notes) {
                Collections.reverse(notes);

                itemviewAdapter adapter = new itemviewAdapter(notes);
                recyclerview.setAdapter(adapter);

            }
        });

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
        index = intent.getStringExtra("complainIndex");

        SharedPreferences preferences = getSharedPreferences("postdata", MODE_PRIVATE);
        emailid = preferences.getString("email", null);
        mobileno = preferences.getString("number", null);



        // inflate the dynamic layout of card
//        additemfor_series_number();
        groupname = findViewById(R.id.groupname);
        itemName = findViewById(R.id.itemName);

        ArrayAdapter<String> groupadapter = new ArrayAdapter<>(this, R.layout.list_layout, grouplist);
        groupname.setDropDownBackgroundResource(R.color.dialog_bg);

        groupname.setAdapter(groupadapter);

        ArrayAdapter<String> itemadapter = new ArrayAdapter<>(this, R.layout.list_layout, itemlist);
        itemName.setDropDownBackgroundResource(R.color.dialog_bg);
        itemName.setAdapter(itemadapter);


        getdataofitems(complainno);

        getdropdowndata(config_file.Base_url + "getgroupname.php", grouplist, false);
        getdropdowndata(config_file.Base_url + "getitemname.php", itemlist, true);


        autotextwatcher(groupname, grouplayout);
        autotextwatcher(itemName, itemlayout);
        textwatcher(qntyno, itemqntylayout);
        textwatcher(serialNo, seriallayout);

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        viewitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(secound_update_activity.this, data_show.class);
                intent1.putExtra("id", complainno);
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
                if (viewitem.getVisibility() == View.GONE) {

                    itemcard.startAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.shake_text));
                    Toast.makeText(secound_update_activity.this, "Please the Prodcut Items", Toast.LENGTH_SHORT).show();
                } else if (imagepath.getText().toString().isEmpty()) {
                    imageuploadlinear.startAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.shake_text));

                    Toast.makeText(secound_update_activity.this, "Please select upload image", Toast.LENGTH_SHORT).show();
                } else {
//                    getdatafromdynamic_layout();
//                    uploaddatatodb();
//                    senddataonserver();
//                    uploaddatatodb();
                    datapostmodule datapostmodule = new datapostmodule(complainno, index, partyid, brand, partycode, address, cityid, state, emailid, mobileno, tdsin, tdsout, descripation);
                    Intent intent = new Intent(secound_update_activity.this, DataUploadIntentService.class);
                    intent.putExtra("image", encodeImageString);
                    intent.putExtra("datamodule", datapostmodule);
                    startService(intent);
                    closedialog();
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
                if (groupname.getText().toString().isEmpty()) {
                    autoerrorShowFunction(grouplayout, groupname);

                } else if (itemName.getText().toString().isEmpty()) {
                    autoerrorShowFunction(itemlayout, itemName);

                } else if (qntyno.getText().toString().isEmpty()) {
                    errorShowFunction(itemqntylayout, qntyno);


                } else if (serialNo.getText().toString().isEmpty()) {
                    errorShowFunction(seriallayout, serialNo);

                } else {

                    uploaditem_details();
                }

            }
        });

    }


    void chooseimageselect() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image Source");
        builder.setItems(new CharSequence[]{"Gallery", "Camera"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        openGallery();
                        break;
                    case 1:
                        openCamera();
                        break;
                }
            }
        });
        builder.show();
    }


    void errorShowFunction(TextInputLayout layout, TextInputEditText text) {
        layout.startAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.shake_text));
        layout.setBoxStrokeErrorColor(ColorStateList.valueOf(Color.RED));
        layout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
        layout.setError("Required*");
        text.requestFocus();
    }

    void textwatcher(TextInputEditText text, TextInputLayout textInputLayout) {
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textInputLayout.setErrorEnabled(false);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    void autotextwatcher(AutoCompleteTextView text, TextInputLayout textInputLayout) {
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textInputLayout.setErrorEnabled(false);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    void autoerrorShowFunction(TextInputLayout layout, AutoCompleteTextView text) {
        layout.startAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.shake_text));
        layout.setBoxStrokeErrorColor(ColorStateList.valueOf(Color.RED));
        layout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
        layout.setError("Required*");
        text.requestFocus();
    }

    private void uploaditem_details() {
        notificationHelper.showProgressNotification(secound_update_activity.this, "Item Uploading");

        String registrationURL = config_file.Base_url + "item_details_close.php?complainnumber=" + complainno + "&group=" + groupname.getText().toString() + "&itemname=" + itemName.getText().toString() + "&qty=" + qntyno.getText().toString() + "&srno=" + serialNo.getText().toString();
        String itemdata = itemName.getText().toString();
        notes note = new notes(itemdata);
        noteDao.insert(note);
        groupname.setText("");
        itemName.setText("");
        serialNo.setText("");
        qntyno.setText("");
        class registration extends AsyncTask<String, String, String> {
            @Override
            protected void onPostExecute(String s) {

                notificationHelper.updateprogressbar(secound_update_activity.this, "item Uploaded");

                viewitem.setVisibility(View.VISIBLE);

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

    void closedialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(secound_update_activity.this);

        builder.setMessage("Your Complain In Progress Please Check Your Notification..");

        builder.setTitle("Complain Close");


        builder.setCancelable(false);


        builder.setPositiveButton("Okay", (DialogInterface.OnClickListener) (dialog, which) -> {
            Intent intent1 = new Intent(secound_update_activity.this, MainActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent1);
            finish();
            overridePendingTransition(R.anim.right_out, R.anim.left_in);


        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

//    void getdatafromdynamic_layout() {
//        int count = addlinearlayout.getChildCount();
//        for (int i = 0; i < count; i++) {
//            View itemlayout = addlinearlayout.getChildAt(i);
//            TextView groupfield = itemlayout.findViewById(R.id.groupname);
//            TextView itemname = itemlayout.findViewById(R.id.itemName);
//            TextView qntyno = itemlayout.findViewById(R.id.qntyno);
//            TextView serialno = itemlayout.findViewById(R.id.serialNo);
//
//
//            String gpdata = groupfield.getText().toString();
//            String itdata = itemname.getText().toString();
//            String qntydata = qntyno.getText().toString();
//            String serialdata = serialno.getText().toString();
//            datalist.add(new ArrayData(gpdata, itdata, qntydata, serialdata));
//
//        }
//    }

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

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_PIC_REQUEST);
    }

    private void camerapermission() {
        Dexter.withActivity(this)
                .withPermissions(android.Manifest.permission.CAMERA, Manifest.permission.POST_NOTIFICATIONS)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {

                            chooseimageselect();

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
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GALLERY && data != null) {
                Uri selectedImageUri = data.getData();
                encoder(selectedImageUri);


            } else if (requestCode == CAMERA_PIC_REQUEST && data != null) {

                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Uri cameraImageUri = getImageUri(secound_update_activity.this, photo);
                encoder(cameraImageUri);

            }

        }


    }

    private Uri getImageUri(Activity inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    void encoder(Uri filepath) {
        try {

            // Display the selected image using an ImageView
            InputStream inputStream = getContentResolver().openInputStream(filepath);
            bitmap = BitmapFactory.decodeStream(inputStream);
            imagePathstring = saveImageToExternalStorage(bitmap);
            imagepath.setText(imagePathstring);
            encodeBitmapImage(bitmap);
        } catch (Exception ex) {

        }

    }

    private String getRealPathFromURI(Uri uri) {
        String filePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};

        try (Cursor cursor = getContentResolver().query(uri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                filePath = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filePath;
    }

    private void encodeBitmapImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
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


    private void getdataofitems(String complainnumber) {
        mProgressDialog.setVisibility(View.VISIBLE);
        nointernet.setVisibility(View.GONE);

        if (!NetworkUtils.isNetworkAvailable(this)) {
            mProgressDialog.setVisibility(View.GONE);
            nointernet.setVisibility(View.VISIBLE);
            linearLayout1.setVisibility(View.GONE);
            swipeRefreshLayout.setEnabled(true);
            linearLayout2.setVisibility(View.GONE);
//                Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        String registrationURL = config_file.Base_url + "get_item_details_close.php?complaint_id=" + complainnumber;

        class registration extends AsyncTask<String, String, String> {
            @Override
            protected void onPostExecute(String s) {
                mProgressDialog.setVisibility(View.GONE);
                noteDao.deleteAllUsers();
                try {
                    JSONObject jsonObject = new JSONObject(s);


                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        JSONArray dataarray = jsonObject.getJSONArray("data");
                        int arrLength = dataarray.length();
                        if (arrLength > 0) {
                            viewitem.setVisibility(View.VISIBLE);
                        }
                        for (int i = 0; i < arrLength; i++) {
                            JSONObject object = dataarray.getJSONObject(i);
                            String item_name = object.getString("item_name");
                            notes note = new notes(item_name);
                            noteDao.insert(note);
                        }
                    } else {
                        // Handle the case when status is false
                        Toast.makeText(secound_update_activity.this, "Do Not Have any data", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    mProgressDialog.setVisibility(View.GONE);
                    Log.e("error", e.getMessage());
                    Toast.makeText(secound_update_activity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
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


    private void getdropdowndata(String registrationURL, List<String> list, boolean check) {
        mProgressDialog.setVisibility(View.VISIBLE);
        nointernet.setVisibility(View.GONE);

        if (!NetworkUtils.isNetworkAvailable(this)) {
            mProgressDialog.setVisibility(View.GONE);
            swipeRefreshLayout.setEnabled(true);
            nointernet.setVisibility(View.VISIBLE);
            linearLayout1.setVisibility(View.GONE);
            linearLayout2.setVisibility(View.GONE);
//                Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

        class registration extends AsyncTask<String, String, String> {
            @Override
            protected void onPostExecute(String s) {

                if (check) {
                    mProgressDialog.setVisibility(View.GONE);
                }

                try {
                    JSONArray object = new JSONArray(s);

                    for (int i = 0; i < object.length(); ++i) {
                        JSONObject object1 = object.getJSONObject(i);
                        String brandname = object1.getString("name");
                        list.add(brandname);
                    }

                } catch (JSONException e) {
                    Toast.makeText(secound_update_activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

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

    void senddataonserver() {
        datapostmodule datapostmodule = new datapostmodule(complainno, index, partyid, brand, partycode, address, cityid, state, emailid, mobileno, tdsin, tdsout, descripation);

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        Call<Void> call = apiService.sendDataArray(datapostmodule);
        call.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {

                if (response.isSuccessful()) {

                    notificationHelper.updateprogressbar(secound_update_activity.this, "Upload Complain");
                    Toast.makeText(secound_update_activity.this, "Successfully Close Complaint", Toast.LENGTH_SHORT).show();
//                    closedialog(response.message().toString());
//                    onBackPressed();
                } else {
                    // Handle error
                    Toast.makeText(secound_update_activity.this, ""+response.errorBody(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(secound_update_activity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                // Handle failure
            }
        });
    }

    private void uploaddatatodb() {
        notificationHelper.showProgressNotification(secound_update_activity.this, "Upload Complain");
        StringRequest request = new StringRequest(Request.Method.POST, config_file.Base_url + "closecomplaintthird.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                senddataonserver();
                Toast.makeText(secound_update_activity.this, "data updated ", Toast.LENGTH_SHORT).show();
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

                map.put("complainnumber", complainno);
                map.put("compliantid", index);
                map.put("partycode", partycode);
                map.put("party_id", partyid);
                map.put("brand_name", brand);
                map.put("cityid", cityid);
                map.put("state", state);
                map.put("email", emailid);
                map.put("description", descripation);
                map.put("phone", index);
                map.put("tdsin", tdsin);
                map.put("tdsout", tdsout);
                map.put("address", address);
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

}
