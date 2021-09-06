package com.example.student.touchpadclient;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class TouchpadActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener, View.OnTouchListener {
    private LinearLayout touchpadLayout, scrollLayout;
    private TextView rozdzielczoscVal, pozycjaVal;
    private boolean dwuklik = false, przytrzymywanie = false;
    private static boolean polaczenie = false;
    private static String host, port;
    private static int ex = 0, ey = 0, ksx = 0, ksy = 0, kolko = 0;
    private static double kkx = 0, kky = 0, kkx2 = 0, kky2 = 0, wspolczynnikX = 0, wspolczynnikY = 0;

    public static String getHost() {
        return host;
    }

    public static String getPort() {
        return port;
    }

    public static void setPolaczenie(boolean polaczenie) {
        TouchpadActivity.polaczenie = polaczenie;
    }

    public static int getEx() {
        return ex;
    }

    public static void setEx(int ex) {
        TouchpadActivity.ex = ex;
    }

    public static int getEy() {
        return ey;
    }

    public static void setEy(int ey) {
        TouchpadActivity.ey = ey;
    }

    public static void setKsx(int ksx) {
        TouchpadActivity.ksx = ksx;
    }

    public static void setKsy(int ksy) {
        TouchpadActivity.ksy = ksy;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touchpad);
        touchpadLayout = findViewById(R.id.tuchpadLayout);
        scrollLayout = findViewById(R.id.scrollLayout);
        Button lewy = findViewById(R.id.lewy);
        Button srodkowy = findViewById(R.id.srodkowy);
        Button prawy = findViewById(R.id.prawy);
        TextView ipVal = findViewById(R.id.ipVal);
        rozdzielczoscVal = findViewById(R.id.rozdzielczoscVal);
        pozycjaVal = findViewById(R.id.pozycjaVal);
        Intent intencja = getIntent();
        host = intencja.getStringExtra("host");
        port = intencja.getStringExtra("port");
        lewy.setOnClickListener(this);
        srodkowy.setOnClickListener(this);
        prawy.setOnClickListener(this);
        lewy.setOnLongClickListener(this);
        touchpadLayout.setOnTouchListener(this);
        scrollLayout.setOnTouchListener(this);
        ipVal.setText(host);
        wyslij("start");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lewy:
                Toast.makeText(this, "Lewy", Toast.LENGTH_LONG).show();
                if (dwuklik) {
                    wyslij("lewy2");
                    dwuklik = false;
                } else {
                    wyslij("lewy");
                    dwuklik = true;
                }
                przytrzymywanie = false;
                break;
            case R.id.srodkowy:
                Toast.makeText(this, "Środkowy", Toast.LENGTH_LONG).show();
                wyslij("srodkowy");
                dwuklik = false;
                przytrzymywanie = false;
                break;
            case R.id.prawy:
                Toast.makeText(this, "Prawy", Toast.LENGTH_LONG).show();
                wyslij("prawy");
                dwuklik = false;
                przytrzymywanie = false;
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        Toast.makeText(this, "Przytrzymywanie", Toast.LENGTH_LONG).show();
        if (przytrzymywanie) {
            wyslij("przytrzymanieWylacz");
            przytrzymywanie = false;
        } else {
            wyslij("przytrzymanieWlacz");
            przytrzymywanie = true;
        }
        dwuklik = false;
        return true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (view.getId()) {
            case R.id.tuchpadLayout:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (event.getX() < 0) {
                            kkx = 0;
                        } else if (event.getX() > touchpadLayout.getWidth()) {
                            kkx = touchpadLayout.getWidth();
                        } else {
                            kkx = event.getX();
                        }
                        if (event.getY() < 0) {
                            kky = 0;
                        } else if (event.getY() > touchpadLayout.getHeight()) {
                            kky = touchpadLayout.getHeight();
                        } else {
                            kky = event.getY();
                        }
                        kkx2 = kkx;
                        kky2 = kky;
                        return true;
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_UP:
                        view.performClick();
                        double x;
                        if (event.getX() < 0) {
                            x = 0;
                        } else if (event.getX() > touchpadLayout.getWidth()) {
                            x = touchpadLayout.getWidth();
                        } else {
                            x = event.getX();
                        }
                        double y;
                        if (event.getY() < 0) {
                            y = 0;
                        } else if (event.getY() > touchpadLayout.getHeight()) {
                            y = touchpadLayout.getHeight();
                        } else {
                            y = event.getY();
                        }
                        skalowanie();
                        if (event.getAction() == MotionEvent.ACTION_UP && kkx2 == x && kky2 == y) {
                            Toast.makeText(this, "Lewy", Toast.LENGTH_LONG).show();
                            if (dwuklik) {
                                wyslij("lewy2");
                                dwuklik = false;
                            } else {
                                wyslij("lewy");
                                dwuklik = true;
                            }
                            przytrzymywanie = false;
                        } else {
                            wyslij(Math.round(wspolczynnikX * (x - kkx)) + "x" + Math.round(wspolczynnikY * (y - kky)));
                        }
                        kkx = x;
                        kky = y;
                        break;
                }
                break;
            case R.id.scrollLayout:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (event.getY() < 0) {
                            kolko = 0;
                        } else if (event.getY() > scrollLayout.getHeight()) {
                            kolko = scrollLayout.getHeight();
                        } else {
                            kolko = (int) event.getY();
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_UP:
                        view.performClick();
                        int kolkoNowe;
                        if (event.getY() < 0) {
                            kolkoNowe = 0;
                        } else if (event.getY() > scrollLayout.getHeight()) {
                            kolkoNowe = scrollLayout.getHeight();
                        } else {
                            kolkoNowe = (int) event.getY();
                        }
                        wyslij("k" + (kolko - kolkoNowe));
                        kolko = kolkoNowe;
                        break;
                }
                break;
        }
        return true;
    }

    public void skalowanie() {
        wspolczynnikX = ((double) ex / touchpadLayout.getWidth());
        wspolczynnikY = ((double) ey / touchpadLayout.getHeight());
    }

    private void wyslij(String akcja) {
        try {
            new ConnectTask().execute(akcja).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!polaczenie) {
            Toast toast = Toast.makeText(this, "Błąd połączenia", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 700);
            toast.show();
            finish();
        } else if (akcja.equals("start")) {
            Toast.makeText(this, "Połączono", Toast.LENGTH_LONG).show();
        }
        rozdzielczoscVal.setText(getString(R.string.rozdzielczosc, ex, ey));
        pozycjaVal.setText(getString(R.string.pozycja, ksx, ksy));
    }

    public void wstecz(View view) {
        Toast.makeText(this, "Rozłączono", Toast.LENGTH_LONG).show();
        wyslij("stop");
        polaczenie = false;
        finish();
    }
}
