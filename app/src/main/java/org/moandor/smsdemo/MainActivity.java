package org.moandor.smsdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            requestSmsPermission();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ArrayAdapter<String> listAdapter = new ArrayAdapter<>(
                this, R.layout.item_phone_number, R.id.phone_number);
        ((ListView) findViewById(R.id.list_phone_number)).setAdapter(listAdapter);
        final EditText phoneNumber = findViewById(R.id.text_phone_number);
        findViewById(R.id.button_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listAdapter.add(phoneNumber.getText().toString());
            }
        });
        final EditText message = findViewById(R.id.text_message);
        findViewById(R.id.button_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageContent = message.getText().toString();
                for (int i = 0; i < listAdapter.getCount(); i++) {
                    String phone = listAdapter.getItem(i);
                    sendMessage(phone, messageContent);
                }
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) !=
                PackageManager.PERMISSION_GRANTED) {
            requestSmsPermission();
        }
    }

    private void requestSmsPermission() {
        ActivityCompat.requestPermissions(
                this, new String[]{Manifest.permission.SEND_SMS}, 0);
    }

    private void sendMessage(String phone, String text) {
        SmsManager manager = SmsManager.getDefault();
        manager.sendTextMessage(phone, null, text, null, null);
    }
}
