package com.client.myapplication.client;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import static com.client.myapplication.client.socket.dis;
import static com.client.myapplication.client.socket.s;

public class Client extends AppCompatActivity {
    static TextView output;
    static EditText input;
    static Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        output=findViewById(R.id.output);
        input=findViewById(R.id.input);
        send=findViewById(R.id.send);

        try {
            final Socket s=new socket().s;
            final DataInputStream dis=new socket().dis;
            final DataOutputStream dos=new socket().dos;

            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        dos.writeUTF(input.getText().toString());dos.flush();
                        input.setText("");
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();


                    }
                }
            });


            Handler h=new Handler(Looper.getMainLooper()){
                @Override
                public void handleMessage(Message msg) {
                    String m=(String) msg.obj;
                    output.setText(m);
                }
            };

        Thread t=new read(s,dis,dos,h);
        t.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

class read extends Thread{

    private final DataInputStream dis;
    DataOutputStream dos;
    Socket s;
    Handler h;

    read(Socket s, DataInputStream dis, DataOutputStream dos, Handler h)
    {
        this.dis=dis;
        this.dos=dos;
        this.s=s;
        this.h=h;
    }
    public void run() {
                while (true) {
                    String reply = null;

                    try {
                        reply = dis.readUTF();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (reply.equals("Block")) {

                        Message m = h.obtainMessage(1, "Connection Lost...You are removed by admin...");
                        h.sendMessage(m);

                        try {
                            s.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    if (reply.equals("Write")) {
                        try {
                            write();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                    Message m = h.obtainMessage(1, reply);
                    h.sendMessage(m);
                }

                }


                private void write() throws IOException {
                    String compname=dis.readUTF();

                    Message m=h.obtainMessage(1,dis.readUTF());
                    h.sendMessage(m);
                    compname=compname+".txt";

                    String rep=dis.readUTF();



                   m=h.obtainMessage(1,"Currently :\n"+rep);
                    h.sendMessage(m);

                    m=h.obtainMessage(1,"Start Editting :\n(Type over to stop)\n");
                    h.sendMessage(m);
                }
}