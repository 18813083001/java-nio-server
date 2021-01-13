package com.jenkov.nioserver.example;

import java.io.IOException;
import java.net.Socket;

/**
 * 模拟客户端发送数据
 * */
public class ClientSocket {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost",9090);

        String request = "GET /tomcat/debug HTTP/1.1\r\n" +
//                "Content-Length: "+size+"\r\n" +
//                "Content-Type: text/html\r\n" +
                "header1: 123\r\n"+
                "\r\n";

    }
}
