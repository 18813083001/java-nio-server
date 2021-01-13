package com.lin.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 测试nio-channel写事件发生的时机
 * */
public class NioClientClient {

    public static void main(String[] args) throws IOException, InterruptedException {
        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 9900);
        SocketChannel socketChannel = SocketChannel.open(hostAddress);
        socketChannel.configureBlocking(false);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(socketChannel.socket().getReceiveBufferSize());


        Selector selector =  Selector.open();
        socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);

        while (true){
            selector.select();
            Set selected = selector.selectedKeys();
            Iterator it = selected.iterator();
            while (it.hasNext()){
                Thread.sleep(2);
//                System.out.println("睡一秒");
                SelectionKey selectionKey = (SelectionKey)(it.next());
                // 测试客户端是否会发生OP_ACCEPT和OP_CONNECT事件，OP_CONNECT事件可能在SocketChannel.open(hostAddress)方法中就已经发生了，
                // 所以这里没测试到，OP_ACCEPT在服务端才会发生
//                if (selectionKey.isAcceptable()){
//                    System.out.println("OP_ACCEPT事件");
//                }
//                if (selectionKey.isConnectable()){
//                    System.out.println("OP_CONNECT事件");
//                }
                if (selectionKey.isReadable()){
//                    System.out.println("OP_READ事件");
                    ByteBuffer byteBuffer = ByteBuffer.allocate(20);
                    socketChannel.read(byteBuffer);
                    byteBuffer.flip();
                    System.out.println(new String(byteBuffer.array()));
                }
                if (selectionKey.isWritable()){
                    //测试客户端是否可以在读数据时，同时想服务端写数据，测试表明，可以同时进行读写
//                    SocketChannel channel = (SocketChannel) selectionKey.channel();
//                    ByteBuffer byteBuffer = ByteBuffer.allocate(12);
//                    String hello = "hello server";
//                    byteBuffer.put(hello.getBytes());
//                    byteBuffer.flip();
//                    channel.write(byteBuffer);
//                    System.out.println("OP_WRITE事件");
//                    System.out.println(hello);
                }


//                System.out.println(selectionKey.channel().toString());
            }
            selected.clear();
        }

    }

}
