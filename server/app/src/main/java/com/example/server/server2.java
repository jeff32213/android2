package com.example.server;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class server2 extends AppCompatActivity implements Runnable{

    public void run() {

        try{

            ServerSocket ss = new ServerSocket(7777);
            //tv.setText(getLocalIpAddress());
            //System.out.println(getLocalIpAddress());
            Socket s = ss.accept();

            System.out.println("connected");
        }catch (IOException e){
            //tv.setText("123");
            System.out.println("error");
        }
    }

}