package com.example.student.touchpadclient;

import android.os.AsyncTask;

public class ConnectTask extends AsyncTask<String, String, TcpClient> {
    @Override
    protected TcpClient doInBackground(String... message) {
        String wiadomosc = message[0];
        TcpClient mTcpClient = new TcpClient(new TcpClient.OnMessageReceived() {
            @Override
            public void messageReceived(String message) {
                publishProgress(message);
            }
        });
        mTcpClient.run(wiadomosc);
        return null;
    }
}
