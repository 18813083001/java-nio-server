package com.jenkov.nioserver.example;

import com.jenkov.nioserver.*;
import com.jenkov.nioserver.http.HttpMessageReaderFactory;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by jjenkov on 19-10-2015.
 */
public class Main {

    /*
    * 该程序的主要思想：接受socket,获取channel,channel注册到readSelector,当可读时，读取信息，
    * 将返回的消息放到消息队列，循环读取消息队列，有消息时，将socket注册到writeSelector,当可写时，
    * 将消息写给客户端
    *
    * */
    public static void main(String[] args) throws IOException {



        IMessageProcessor messageProcessor = (request, writeProxy) -> {
            String httpResponse = "HTTP/1.1 200 OK\r\n" +
                    "Content-Length: 38\r\n" +
                    "Content-Type: text/html\r\n" +
                    "\r\n" +
                    "<html><body>Hello World!"+Math.random()+"</body></html>";

            byte[] httpResponseBytes = httpResponse.getBytes("UTF-8");
            System.out.println("Message Received from socketId: " + request.socketId);

            Message response = writeProxy.getMessage();
            response.socketId = request.socketId;
            response.writeToMessage(httpResponseBytes);

            writeProxy.enqueue(response);
        };

        Server server = new Server(9090, new HttpMessageReaderFactory(), messageProcessor);

        server.start();

    }


}
