package com.example.client;
import java.io.*;
import java.net.*;
import java.util.*;

public class test1 {
    public static void main(String[] args){
        try{
            System.out.println(getLocalIpAddress());
            Socket s = new Socket(getLocalIpAddress(), 7777);
            System.out.println(getLocalIpAddress());

        }catch (IOException e){
            System.out.println("Server Socket ERROR");
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
