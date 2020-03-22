package com.client.myapplication.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

class socket {
    static Socket s=null;
    static DataInputStream dis=null;
    static DataOutputStream dos=null;
    socket(){}
    socket(Socket s) throws Exception{
        this.s=s;
        this.dis=new DataInputStream(s.getInputStream());
        this.dos=new DataOutputStream(s.getOutputStream());
    }
}
