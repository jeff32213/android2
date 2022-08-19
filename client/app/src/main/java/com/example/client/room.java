package com.example.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import java.util.*;
import org.json.*;

public class room extends AppCompatActivity {

    Button leave;
    Button send;
    TextView hi;
    TextView box;
    EditText input;

    String name;
    String ip;
    String port;

    Socket s;
    BufferedReader br;
    BufferedWriter bw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        leave = findViewById(R.id.leave);
        send = findViewById(R.id.send);
        hi = findViewById(R.id.hi);
        box = findViewById(R.id.box);
        input = findViewById(R.id.input);

        Intent it = this.getIntent();
        Bundle bundle = it.getExtras();
        name = bundle.getString("name");
        ip = bundle.getString("ip");
        port = bundle.getString("port");
        hi.setText("Hi! " + name);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    s = new Socket("10.0.2.2", 6100);
                    br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

                    try{
                        HashMap<String, String> hash= new HashMap<String, String>();
                        hash.put("name",name);
                        hash.put("ip", ip);
                        hash.put("port", port);
                        Map<String, String> tmp;
                        tmp = hash;

                        JSONObject json = new JSONObject(tmp);
                        bw.write(json.toString());
                        bw.newLine();
                        bw.flush();


                    }catch (IOException e){
                        System.out.println("err");
                    }

                    listen();

                }catch(IOException e){
                    quit(s, br, bw);
                    e.printStackTrace();
                }
            }

        }).start();


        send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                new Thread(new Runnable() {

                    @Override
                    public void run() {


                        try{
                            HashMap<String, String> hash= new HashMap<String, String>();
                            hash.put("msg", input.getText().toString());
                            Map<String, String> tmp;
                            tmp = hash;
                            //String msg = input.getText().toString();


                            JSONObject json = new JSONObject(tmp);
                            bw.write(json.toString());
                            bw.newLine();
                            bw.flush();


                        }catch (IOException e){
                            quit(s, br, bw);
                            System.out.println("err");
                        }

                        /*
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                box.append("(YOU) " + input.getText().toString() + "\n");
                            }
                        });

                        */

                    }

                }).start();

            }



        });

        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                quit(s, br, bw);

                Intent it = new Intent();
                it.setClass(room.this, MainActivity.class);
                startActivity(it);
            }
        });

    }

    public void listen(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //String msg;
                while(!s.isClosed()){
                    try{
                        final String msg = br.readLine();

                        if(msg == null){
                            quit(s, br, bw);
                            break;
                        }

                        try{
                            JSONObject jsonObj = new JSONObject(msg);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    try{
                                        box.append(jsonObj.getString("msg"));
                                    }catch (JSONException e){
                                        quit(s, br, bw);
                                    }
                                }
                            });
                        }catch (JSONException e){
                            quit(s, br, bw);
                        }

                    }catch(IOException e){
                        quit(s, br, bw);
                    }
                }
                quit(s, br, bw);
            }
        }).start();
    }

    public void quit(Socket socket, BufferedReader br, BufferedWriter bw){

        try{
            if(socket != null){
                socket.shutdownInput();
                socket.shutdownOutput();
                socket.close();
            }

           // if(br != null){
           //     br.close();
           // }

            //if(bw != null){
            //    bw.close();
            //}

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        }catch (SocketException ex) {

        }
        return null;
    }

}

