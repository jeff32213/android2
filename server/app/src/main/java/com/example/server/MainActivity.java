package com.example.server;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    EditText server_name;
    Button server_conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        server_name = findViewById(R.id.server_name);
        server_conn = findViewById(R.id.server_conn);

        server_conn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("server_name", server_name.getText().toString());

                Intent it = new Intent();
                it.putExtras(bundle);
                it.setClass(MainActivity.this, room.class);
                startActivity(it);
            }
        });



    }



/*

    class server implements Runnable{
        @Override
        public void run() {
            try{
                ServerSocket ss = new ServerSocket(7777);
                //tv.setText(getLocalIpAddress());
                //System.out.println(getLocalIpAddress());
                Socket s = ss.accept();
                //tv.setText("connected");
                System.out.println("connected");
            }catch (IOException e){
                //tv.setText("123");
                System.out.println("error");
            }

        }
    }

    /*
    private Runnable serverThread = new Runnable() {
        @Override
        public void run() {
            int count=0;
            try{
                ServerSocket server = new ServerSocket(12345);

                System.out.println("Server開始執行");


                while (!server.isClosed()) {
                    // 呼叫等待接受客戶端連接
                    Socket socket = server.accept();
                    ++count;
                    System.out.println("現在使用者個數："+count);

                    add(socket);
                }




            }catch(IOException e){
                System.out.println("Server Socket ERROR");
            }


        }
    };

    public void add(final Socket socket) throws IOException{
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try{

                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    while (socket.isConnected()) {
                        // 取得網路串流的訊息
                        String msg= br.readLine();

                        if(msg==null){
                            System.out.println("Client Disconnected!");
                            break;
                        }
                        //輸出訊息
                        System.out.println(msg);

                    }


                }catch (IOException e){
                    e.getStackTrace();
                }

            }
        });

        t.start();
    }
    */



}


