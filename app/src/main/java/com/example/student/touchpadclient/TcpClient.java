package com.example.student.touchpadclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

class TcpClient {

    private OnMessageReceived nasluch;

    TcpClient(OnMessageReceived listener) {
        nasluch = listener;
    }

    void run(String wiadomosc) {
        try {
            String port = TouchpadActivity.getPort();
            Socket gniazdo = new Socket();
            InetSocketAddress adres = new InetSocketAddress(InetAddress.getByName(TouchpadActivity.getHost()), Integer.parseInt(port));
            gniazdo.connect(adres, 1000);
            PrintWriter pisanie = new PrintWriter(new BufferedWriter(new OutputStreamWriter(gniazdo.getOutputStream())), true);
            BufferedReader czytanie = new BufferedReader(new InputStreamReader(gniazdo.getInputStream()));
            String wiadomoscSerwera = czytanie.readLine();
            String[] pozycja = wiadomoscSerwera.split(" x ");
            if (nasluch != null) {
                nasluch.messageReceived(wiadomoscSerwera);
                switch (wiadomosc) {
                    case "start":
                        TouchpadActivity.setPolaczenie(true);
                        TouchpadActivity.setKsx(Integer.parseInt(pozycja[0]));
                        TouchpadActivity.setKsy(Integer.parseInt(pozycja[1]));
                        pisanie.println(wiadomosc);
                        wiadomoscSerwera = czytanie.readLine();
                        String[] rozdzielczosc = wiadomoscSerwera.split(" x ");
                        TouchpadActivity.setEx(Integer.parseInt(rozdzielczosc[0]));
                        TouchpadActivity.setEy(Integer.parseInt(rozdzielczosc[1]));
                        break;
                    case "lewy":
                    case "lewy2":
                    case "srodkowy":
                    case "prawy":
                    case "przytrzymanieWlacz":
                    case "przytrzymanieWylacz":
                    case "stop":
                        TouchpadActivity.setKsx(Integer.parseInt(pozycja[0]));
                        TouchpadActivity.setKsy(Integer.parseInt(pozycja[1]));
                        pisanie.println(wiadomosc);
                        break;
                    default:
                        if (wiadomosc.charAt(0) == 'k') {
                            TouchpadActivity.setKsx(Integer.parseInt(pozycja[0]));
                            TouchpadActivity.setKsy(Integer.parseInt(pozycja[1]));
                        } else {
                            String[] roznica = wiadomosc.split("x");
                            int x = Integer.parseInt(pozycja[0]) + Integer.parseInt(roznica[0]);
                            if (x < 0) {
                                x = 0;
                            } else if (x > TouchpadActivity.getEx()) {
                                x = TouchpadActivity.getEx();
                            }
                            int y = Integer.parseInt(pozycja[1]) + Integer.parseInt(roznica[1]);
                            if (y < 0) {
                                y = 0;
                            } else if (y > TouchpadActivity.getEy()) {
                                y = TouchpadActivity.getEy();
                            }
                            wiadomosc = x + "x" + y;
                            TouchpadActivity.setKsx(x);
                            TouchpadActivity.setKsy(y);
                        }
                        pisanie.println(wiadomosc);
                        break;
                }
            }
            gniazdo.close();
        } catch (Exception e) {
            TouchpadActivity.setPolaczenie(false);
        }
    }

    public interface OnMessageReceived {
        void messageReceived(String message);
    }
}
