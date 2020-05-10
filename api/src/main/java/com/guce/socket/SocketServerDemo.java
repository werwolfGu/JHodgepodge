package com.guce.socket;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author chengen.gce
 * @DATE 2020/5/2 9:28 上午
 */
public class SocketServerDemo {

    public static void main(String[] args) throws IOException {

        ServerSocket socketServer = new ServerSocket(8080);
        while(true){
            Socket socket = socketServer.accept();
            new Thread ( () -> {
                try {
                    InputStream in = socket.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ;
            }).start();

        }

    }
}
