package com.example.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;

public class MainActivity extends AppCompatActivity {

    EditText name;
    EditText ip;
    EditText port;
    Button conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.name);
        ip = findViewById(R.id.ip);
        port = findViewById(R.id.port);
        conn = findViewById(R.id.conn);

        conn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("name", name.getText().toString());
                bundle.putString("ip", ip.getText().toString());
                bundle.putString("port", port.getText().toString());

                Intent it = new Intent();
                it.putExtras(bundle);
                it.setClass(MainActivity.this, room.class);
                startActivity(it);
            }
        });

    }

}

