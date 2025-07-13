

package com.example.firstapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView homeText = findViewById(R.id.hello_first);
        homeText.setText("Welcome, from code.");

        Button homeButton = findViewById(R.id.home_button);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL("http://www.android.com/");
                            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                            StringBuilder sb = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                sb.append(line).append('\n');
                            }
                            String result = sb.toString();
                            runOnUiThread(() -> homeText.setText(result));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        Button tcpButton = findViewById(R.id.tcp_button);
        tcpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Socket socket = new Socket("www.android.com", 80);
                            socket.setSoTimeout(3000); // 3 second timeout
                            runOnUiThread(() -> homeText.setText("Connected...")); // Debug line
                            OutputStream outputStream = socket.getOutputStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            // Raw HTTP Request
                            String request = "GET / HTTP/1.1\r\nHost: www.android.com\r\nConnection: close\r\nUser-Agent: app\r\nAccept: */*\r\n\r\n";
                            outputStream.write(request.getBytes());
                            outputStream.flush();
                            StringBuilder sb = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                sb.append(line).append('\n');
                            }
                            String result = sb.toString();
                            runOnUiThread(() -> homeText.setText(result));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
}