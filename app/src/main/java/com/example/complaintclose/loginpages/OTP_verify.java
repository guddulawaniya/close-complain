package com.example.complaintclose.loginpages;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.example.complaintclose.MainActivity;
import com.example.complaintclose.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class OTP_verify extends AppCompatActivity {


    TextView resendotp;
    boolean rdcheck = false;

    String numberotp="1234";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verify);

        PinView pinView = findViewById(R.id.pinview);
         resendotp = findViewById(R.id.resendotp);
        Button verifybuttonotp = findViewById(R.id.verifybuttonotp);

        Intent intent = getIntent();
        String verifypin = intent.getStringExtra("otp");
        String mobilenumber = intent.getStringExtra("number");


        verifybuttonotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pinView.getText().toString().equals(verifypin))
                {
                    SharedPreferences.Editor editor = getSharedPreferences("postdata",MODE_PRIVATE).edit();
                    editor.putString("number",mobilenumber);
                    editor.commit();

                    Toast.makeText(OTP_verify.this, "Successfully Verified", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(OTP_verify.this, MainActivity.class));
                    finish();
                }
                else
                {
                    Toast.makeText(OTP_verify.this, "Invalid OTP Try Again ", Toast.LENGTH_SHORT).show();
                }

            }
        });

        timecounter();
        resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (rdcheck)
                {
                    rdcheck=false;
                    //numberotp= new DecimalFormat("0000").format(new Random().nextInt(9999));

                    timecounter();
                    Toast.makeText(OTP_verify.this, "OTP Resend Successfully ", Toast.LENGTH_SHORT).show();

//                    sendotpnumbers sm = new sendotpnumbers(getApplication());
//                    sm.execute(s);

                }
            }
        });


    }

    void timecounter()
    {

        new CountDownTimer(30000,1000)
        {

            @Override
            public void onTick(long l) {


                NumberFormat format = new DecimalFormat("00");
                long sec = (l/1000) % 60;
                resendotp.setText("00 : "+format.format(sec));

            }

            @Override
            public void onFinish() {
                resendotp.setText("Resend");
                rdcheck = true;


            }
        }.start();
    }
}