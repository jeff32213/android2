package com.example.server;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class room extends AppCompatActivity {

    Button leave;
    Button send;
    TextView hi;
    TextView box;
    EditText input;

    ServerSocket ss;

    public ArrayList<ClientHandler2> clients = new ArrayList<>();

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
        String name = bundle.getString("server_name");
        box.setText("Server started(192.168.200.2:6100)");
        hi.setText("Hi! " + name);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ss = new ServerSocket(7100);
                    while(!ss.isClosed()){
                        Socket s = ss.accept();
                        System.out.println("connect");
                        ClientHandler2 clientHandler = new ClientHandler2(s);

                        Thread thread = new Thread(clientHandler);
                        thread.start();

                    }
                }catch(IOException e){
                    //shutdown();
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

                        HashMap<String, String> hash= new HashMap<String, String>();
                        hash.put("msg", "[SERVER]: " +  input.getText().toString() + "\n");
                        Map<String, String> tmp;
                        tmp = hash;
                        JSONObject json = new JSONObject(tmp);

                        for(int i=0; i<clients.size(); i++){
                            try{
                                clients.get(i).bw.write(json.toString());
                                clients.get(i).bw.newLine();
                                clients.get(i).bw.flush();

                            }catch (IOException e){
                                System.out.println("error");
                            }
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                box.append("[SERVER]: " +  input.getText().toString() + "\n");
                            }
                        });
                    }
                }).start();
            }
        });

        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try{
                            shutdown();
                            ss.close();

                            Intent it = new Intent();
                            it.setClass(room.this, MainActivity.class);
                            startActivity(it);
                        }catch (IOException e){

                        }
                    }
                }).start();

            }
        });

    }

    public void shutdown(){

        HashMap<String, String> hash= new HashMap<String, String>();
        hash.put("msg", "Server closed, please leave\n");
        Map<String, String> tmp;
        tmp = hash;
        JSONObject json = new JSONObject(tmp);

        for(int i=0; i<clients.size(); i++){
            try{
                clients.get(i).bw.write(json.toString());
                clients.get(i).bw.newLine();
                clients.get(i).bw.flush();

            }catch (IOException e){
                System.out.println("error");
            }
        }


        for(int i=0; i<clients.size(); i++){
            try{
                clients.get(i).bw.close();
                clients.get(i).bw.close();
                clients.get(i).bw.close();

            }catch (IOException e){
                System.out.println("error");
            }
        }

    }


    class ClientHandler2 implements Runnable {

        public Socket s;
        public BufferedReader br;
        public BufferedWriter bw;
        public String cname;
        public String cip;
        public String cport;

        public ClientHandler2(Socket socket){
            try{

                this.s = socket;
                this.br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                this.bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                String line = br.readLine();
                try{
                    JSONObject jsonObj = new JSONObject(line);
                    this.cname = jsonObj.getString("name");
                    this.cip =  jsonObj.getString("ip");
                    this.cport =  jsonObj.getString("port");
                }catch (JSONException e){

                }

                clients.add(this);

                broadcast_init();

            }catch(IOException e){
                quit(s, br, bw);
            }
        }

        @Override
        public void run() {

            String line;

            while(!s.isClosed()){
                try{

                    while((line = br.readLine()) != null ) {
                        try{

                            JSONObject jsonObj = new JSONObject(line);
                            broadcast(jsonObj.getString("msg"));

                            System.out.println("read");


                        }catch(JSONException e){
                            //quit(s, br, bw);
                            System.out.println("test");
                        }

                    }
                    quit(s, br, bw);
                    System.out.println("out");

                }catch (IOException e){
                    break;
                }

            }

        }

        public void broadcast(String str){

            HashMap<String, String> hash= new HashMap<String, String>();
            hash.put("msg", cname + ": " + str + "\n");
            Map<String, String> tmp;
            tmp = hash;

            JSONObject json = new JSONObject(tmp);

            for(ClientHandler2 client: clients){
                try{

                    client.bw.write(json.toString());
                    client.bw.newLine();
                    client.bw.flush();

                }catch (IOException e){
                    quit(s, br, bw);
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    box.append(cname + ": " + str + "\n");
                }
            });
        }

        public void broadcast_init(){

            HashMap<String, String> hash= new HashMap<String, String>();
            hash.put("msg", cname + "(" + cip + ":" + cport + ")" + " join the chat\n");
            Map<String, String> tmp;
            tmp = hash;

            JSONObject json = new JSONObject(tmp);

            for(ClientHandler2 client: clients){
                try{
                    client.bw.write(json.toString());
                    client.bw.newLine();
                    client.bw.flush();

                }catch (IOException e){
                    quit(s, br, bw);
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    box.append(cname + "(" + cip + ":" + cport + ")" + " join the chat\n");
                }
            });
        }

        public void broadcast_quit(String str){

            System.out.println("broad_quit");
            HashMap<String, String> hash= new HashMap<String, String>();
            hash.put("msg", str + "\n");
            Map<String, String> tmp;
            tmp = hash;

            JSONObject json = new JSONObject(tmp);

            for(ClientHandler2 client: clients){
                try{
                    if(!client.cname.equals(cname)){

                        client.bw.write(json.toString());
                        client.bw.newLine();
                        client.bw.flush();
                    }

                }catch (IOException e){
                    quit(s, br, bw);
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    box.append(str + "\n");
                }
            });
        }

        public void quit(Socket socket, BufferedReader br, BufferedWriter bw){

            try{
                clients.remove(this);
                broadcast_quit(cname + " has left");
                br.close();
                bw.close();
                socket.close();

            }catch (IOException e){

            }
        }
    }



}





