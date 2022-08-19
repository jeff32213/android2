package com.example.client;

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

public class client{

    private Socket s;
    private BufferedReader br;
    private BufferedWriter bw;
    private String cname;

    public client(Socket socket, String name){
        try{
            this.s = socket;
            this.br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            this.bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            this.cname = name;
        }catch(IOException e){
            //shutdown(s, br, bw);
        }

    }

    public void sendmsg(){
        try{
            bw.write(cname);
            bw.newLine();
            bw.flush();
        }catch(IOException e){

        }
    }



    /*
    @Override
    public void run() {
        try{
            //System.out.println(getLocalIpAddress());
            Socket s = new Socket(getLocalIpAddress(), 7777);
            System.out.println(getLocalIpAddress());

        }catch (IOException e){
            System.out.println("Server Socket ERROR");
        }


    }*/

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
