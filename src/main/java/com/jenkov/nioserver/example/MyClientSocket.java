package com.jenkov.nioserver.example;


import org.junit.Test;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class MyClientSocket {

    public static void main(String[] args) throws IOException, InterruptedException {
        String message = "1234567890ab\r\n";

        Socket socket = new Socket("localhost",8080);
        socket.setKeepAlive(true);

        String httpResponse = "HTTP/1.1 200 OK\r\n" +
                "Content-Length: "+14*10+"\r\n" +
                "Content-Type: text/html\r\n" +
                "\r\n" +
                ""+message+"";

        System.out.println("==:"+httpResponse.getBytes().length);
        socket.getOutputStream().write(httpResponse.getBytes());
        int i= 0;
        while (i<10-1){
            i++;
            Thread.sleep(1000);
            System.out.println("发送");

            socket.getOutputStream().write(message.getBytes());
//           socket.getOutputStream().flush();
            byte[] b = new byte[102];
            Thread.sleep(1500);
            System.out.println("接收");
            socket.getInputStream().read(b);
            System.out.println(new String(b,"utf-8"));
            System.out.println("\r\n----");

//            Thread.sleep(50);
//            socket.getOutputStream().write(httpResponse.getBytes());
//             b = new byte[50];
//            socket.getInputStream().read(b);
//            System.out.println(new String(b,"utf-8"));
//        b = new byte[500];
//        socket.getInputStream().read(b);
//        socket.getInputStream().close();
//            Thread.sleep(1000);
//            socket.getOutputStream().write(httpResponse.getBytes());
//            socket.close();

        }


    }

    @Test
    public void test1() throws IOException, InterruptedException {

        String message = "1234567890ab\r\n";
        System.out.println(message.getBytes().length);
        StringBuffer buffer = new StringBuffer();
        System.out.println(message);
        for (int i=0;i < 1;i++)
            buffer.append(message);
        System.out.println(buffer.toString());
        Socket socket = new Socket("localhost",9090);
        socket.setKeepAlive(true);
        int size = buffer.toString().getBytes().length;
        System.out.println(size);
        String httpResponse = "HTTP/1.1 200 OK\r\n" +
                "Content-Length: "+size+"\r\n" +
                "Content-Type: text/html\r\n" +
                "\r\n" +
                ""+buffer.toString()+"";

        System.out.println("==:"+httpResponse.getBytes().length);
        int i= 0;
//        while (true){
        i++;
        Thread.sleep(1000);
//            System.out.println("发送");

        socket.getOutputStream().write(httpResponse.getBytes());
        socket.getOutputStream().flush();
        byte[] b = new byte[50];
        socket.close();
//            Thread.sleep(1500);
//            socket.getInputStream().read(b);
//            System.out.println(new String(b,"utf-8"));
//             System.out.println("\r\n----");

//            Thread.sleep(50);
//            socket.getOutputStream().write(httpResponse.getBytes());
//             b = new byte[50];
//            socket.getInputStream().read(b);
//            System.out.println(new String(b,"utf-8"));
//        b = new byte[500];
//        socket.getInputStream().read(b);
//        socket.getInputStream().close();
//            Thread.sleep(1000);
//            socket.getOutputStream().write(httpResponse.getBytes());
//            socket.close();

//        }

    }

    @Test
    public void httpRequest() throws IOException, InterruptedException {

        String message = "1234567=90ab\r\n";
        Socket socket = new Socket("localhost",9090);
        socket.setKeepAlive(true);

        int k = 10;
        String httpResponse = "GET /tomcat/dubug HTTP/1.1\r\n" +
                "Content-Length: "+14*k+"\r\n" +
                "Host: "+"localhost"+"\r\n"+
                "Content-Type: text/html\r\n" +
                "\r\n" +
                ""+message+"";

        System.out.println("==:"+httpResponse.getBytes().length);
        socket.getOutputStream().write(httpResponse.getBytes());
        int i= 0;
        while (i<k-1){
            i++;
            Thread.sleep(1000);
            System.out.println("发送");
            socket.getOutputStream().write(message.getBytes());
        }
        socket.getOutputStream().flush();

        byte[] b = new byte[160];
        Thread.sleep(1500);
        System.out.println("接收");
        socket.getInputStream().read(b);
        System.out.println(new String(b,"utf-8"));
        System.out.println("\r\n----");
        socket.close();

    }
}
