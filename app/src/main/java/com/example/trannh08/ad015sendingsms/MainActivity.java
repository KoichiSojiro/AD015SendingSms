package com.example.trannh08.ad015sendingsms;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final String DEBUG_TAG = "DEBUG TAG";
    Button button_sendSms;
    EditText editText_phoneNumber;
    EditText editText_messageContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_sendSms = (Button) findViewById(R.id.button_sendSms);
        editText_phoneNumber = (EditText) findViewById(R.id.editText_phoneNumber);
        editText_messageContent = (EditText) findViewById(R.id.editText_messageContent);

        boolean isGranted = checkPermission();
        if(!isGranted) {
            setPermission();
        }

        button_sendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MainActivity.this, "Access SEND_SMS granted.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Access SEND_SMS denied!", Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void sendMessage() {
        Log.d(DEBUG_TAG, "Start sendMessage action.");

        if(checkPermission()) {
            try {
                String phoneNumber = editText_phoneNumber.getText().toString();
                String messageContent = editText_messageContent.getText().toString();
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, messageContent, null, null);
                editText_phoneNumber.setText(null);
                editText_messageContent.setText(null);
                Toast.makeText(MainActivity.this, "Your message was sent.", Toast.LENGTH_SHORT).show();
                Log.d(DEBUG_TAG, "sendMessage action run successfully");
            } catch (Exception ex) {
                Toast.makeText(MainActivity.this, "Cannot perform action sending message right now", Toast.LENGTH_SHORT).show();
                Log.d(DEBUG_TAG, "sendMessage action got errors.");
                Log.d(DEBUG_TAG, ex.getMessage());
                Log.d(DEBUG_TAG, ex.getStackTrace().toString());
            }
        } else {
            Toast.makeText(MainActivity.this, "Lack of permission to perform action sending message.", Toast.LENGTH_SHORT).show();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean checkPermission() {
        if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void setPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)) {
            Toast.makeText(this, "SEND_SMS permission is needed", Toast.LENGTH_SHORT).show();
        }
        requestPermissions(new String[]{Manifest.permission.SEND_SMS}, PackageManager.PERMISSION_GRANTED);
    }
}
