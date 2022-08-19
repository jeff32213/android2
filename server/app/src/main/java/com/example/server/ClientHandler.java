package com.example.server;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import org.json.*;
import android.os.*;

class ClientHandler implements Runnable {
    @Override
    public void run() {

    }
    /*
    public ArrayList<ClientHandler> clients = new ArrayList<>();
    private Socket s;
    private BufferedReader br;
    private BufferedWriter bw;
    private String cname;

    public ClientHandler(Socket socket){
        try{
            System.out.println("1");
            this.s = socket;
            this.br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            this.bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            //this.cname = br.readLine();
            this.cname = "yyy";
            clients.add(this);
            broadcast("Welcome " + cname + " ! \n");
            System.out.println("2");

        }catch(IOException e){
            shutdown(s, br, bw);
        }
    }

    @Override
    public void run() {
        System.out.println("4");
        String line;

        while(s.isConnected()){
            try{
                System.out.println("5");
                while((line = br.readLine()) != null) {
                    try{
                        JSONObject jsonObj = new JSONObject(line);

                        //message += line;
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            public void run() {

                                try{
                                    broadcast(jsonObj.getString("msg"));
                                }catch (JSONException e){

                                }

                            }
                        });

                    }catch(JSONException e){

                    }

                }


                //msg = br.readLine();
                System.out.println("6");
                //broadcast(jsonObj.get);
            }catch (IOException e){
                shutdown(s, br, bw);
                break;
            }
        }

    }

    public void broadcast(String str){
        for(ClientHandler client: clients){
            try{
                System.out.println("3");
                client.bw.write(str);
                client.bw.newLine();
                client.bw.flush();

            }catch (IOException e){
                shutdown(s, br, bw);
            }
        }
    }

    public void shutdown(Socket socket, BufferedReader br, BufferedWriter bw){

        try{
            clients.remove(this);
            broadcast(cname + " has left\n");
            br.close();
            bw.close();
            socket.close();
        }catch (IOException e){

        }
    }

 */
}
