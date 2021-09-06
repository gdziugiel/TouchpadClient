package com.example.student.touchpadclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText wprowadzIpVal, portVal;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wprowadzIpVal = findViewById(R.id.wprowadzIpVal);
        portVal = findViewById(R.id.portVal);
    }

    /**
     *
     * @param view
     */
    public void polacz(View view) {
        String host = wprowadzIpVal.getText().toString();
        String port = portVal.getText().toString();
        if (!TextUtils.isEmpty(host) && !TextUtils.isEmpty(port)) {
            if (Integer.parseInt(port) >= 49152 && Integer.parseInt(port) <= 65535) {
                Intent intencja = new Intent(this, TouchpadActivity.class);
                intencja.putExtra("host", host);
                intencja.putExtra("port", port);
                Toast toast = Toast.makeText(this, "Łączenie", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 700);
                toast.show();
                startActivity(intencja);
            } else {
                Toast.makeText(this, "Nieprawidłowy numer portu", Toast.LENGTH_LONG).show();
            }
        } else if (TextUtils.isEmpty(host)) {
            Toast.makeText(this, "Wprowadź nazwę lub adres IP", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Wprowadź numer portu", Toast.LENGTH_LONG).show();
        }
    }

    /**
     *
     * @param view
     */
    public void info(View view) {
        Intent intencja = new Intent(this, AboutActivity.class);
        startActivity(intencja);
    }
}
