package com.lin.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 测试nio-channel写事件发生的时机
 * */
public class NioServer {

    public static void main(String[] args) throws IOException {
        NioServer nioTest = new NioServer();
        Reactor reactor =  nioTest.new Reactor(9900);
        reactor.run();

    }

    class Reactor implements Runnable{
        final Selector selector;
        final ServerSocketChannel serverSocket;
        Reactor(int port) throws IOException {
            selector = Selector.open();
            serverSocket = ServerSocketChannel.open();
            serverSocket.socket().bind(new InetSocketAddress(port));
            serverSocket.configureBlocking(false);
            SelectionKey sk =
                    serverSocket.register(selector,
                            SelectionKey.OP_ACCEPT); sk.attach(new Acceptor());
        }

        public void run() {  // normally in a new Thread
            try {
                while (!Thread.interrupted()) {
                    selector.select();
                    Set selected = selector.selectedKeys();
                    Iterator it = selected.iterator();
                    while (it.hasNext()){
                        SelectionKey selectionKey = (SelectionKey)(it.next());
                        //以下用于测试服务端的socket会发生什么事件，测试发现只有当一个新连接建立时会发生OP_ACCEPT，其他事件不会发生
//                        if (selectionKey.isAcceptable()){
//                            System.out.println("OP_ACCEPT事件");
//                        }else if (selectionKey.isConnectable()){
//                            System.out.println("OP_CONNECT事件");
//                        }else if (selectionKey.isReadable()){
//                            System.out.println("OP_READ事件");
//                        }else if (selectionKey.isWritable()){
//                            System.out.println("OP_WRITE事件");
//                        }
                        dispatch(selectionKey);
                    }
                    selected.clear();
                }
            } catch (IOException ex) { ex.printStackTrace(); }
        }
        void dispatch(SelectionKey k) {
            Runnable r = (Runnable)(k.attachment());
            if (r != null){
                r.run();
            }
        }

        class Acceptor implements Runnable { // inner
            public void run() {
                try {
                    SocketChannel c = serverSocket.accept();
                    System.out.println("send buffer size:"+c.socket().getSendBufferSize());
                    System.out.println("receive buffer size:"+c.socket().getReceiveBufferSize());
                    System.out.println("接受到socket:"+c.toString());
                    c.configureBlocking(false);
                    Selector selector = Selector.open();
                    c.register(selector,SelectionKey.OP_WRITE | SelectionKey.OP_READ);
                    if (c != null){
                        while (true){
                            selector.select();
                            Set selected = selector.selectedKeys();
                            Iterator it = selected.iterator();
                            while (it.hasNext()){
                                SelectionKey selectionKey = (SelectionKey)(it.next());
                                if (selectionKey.isReadable()){
                                    //用于检验 SelectionKey.OP_WRITE | SelectionKey.OP_READ组合是否可以响应读和写事件，实践证明可以
//                                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
//                                    System.out.println("OP_READ事件");
//                                    ByteBuffer byteBuffer = ByteBuffer.allocate(12);
//                                    socketChannel.read(byteBuffer);
//                                    byteBuffer.flip();
//                                    System.out.println(new String(byteBuffer.array()));
                                }

                                if (selectionKey.isWritable()){
                                    // 用于测试可写事件发生的时机，测试表明：当send buffer size=146988，receive buffer size=408300时，两个皆为系统默认
                                    // 值，总大小555288，当接收方的receive buffer和发送方的send buffer都写满时（测试时写了553200字节数据），不会再发生写
                                    // 事件，只有当接受方消费了部分数据后(一半数据左右)，才会出发写事件
                                    System.out.println("OP_WRITE事件");
                                    ByteBuffer byteBuffer = ByteBuffer.allocate(20);
                                    byteBuffer.put("11111111111111111111".getBytes());
                                    byteBuffer.flip();
                                    c.write(byteBuffer);
                                }
                            }
                            selected.clear();
                        }
//                       new Handler(selector, c);
                    }
                }
                catch(IOException ex) { ex.printStackTrace(); }
            } }
    }
}
