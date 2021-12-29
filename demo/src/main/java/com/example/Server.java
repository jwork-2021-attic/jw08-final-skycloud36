package com.example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

import com.example.screen.ServerWorldScreen;


public class Server {
    static int localHost = 8888;
    SocketChannel client1;
    SocketChannel client2;
    ByteBuffer byteBuffer;
    Selector selector;
    ServerWorldScreen serverWorldScreen;

    public Server(){
        client1 = null;
        client2 = null;
        try{
            ServerSocketChannel serverSocket = ServerSocketChannel.open();
            serverSocket.configureBlocking(false);
            serverSocket.socket().bind(new InetSocketAddress(localHost));
            byteBuffer = ByteBuffer.allocate(128);
            selector = Selector.open();
            serverWorldScreen = new ServerWorldScreen(this);
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void handleSocketByThread(SocketChannel socketChannel) throws Exception{
        class myRunnable implements Runnable{
            SocketChannel client;

            myRunnable(SocketChannel socketChannel) throws Exception{
                this.client = socketChannel;
                client.configureBlocking(false);
                client.register(selector, SelectionKey.OP_READ);
            }
            @Override
            public void run(){
                try{
                    client.close();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }

        myRunnable mRunnable = new myRunnable(socketChannel);
        Thread t = new Thread(mRunnable);
        t.start();
    }

    public void listen(){
        try{
            boolean listen = true;
            while(listen){
                if(selector.select() == 0){
                    continue;
                }
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while(it.hasNext()){
                    SelectionKey key = it.next();
                    if(key.isAcceptable()){
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        System.out.println("client " + socketChannel.getRemoteAddress() + " come");
                        if(client1 == null || client1.isConnected() == false){
                            client1 = socketChannel;
                            // handleSocketByThread(socketChannel);
                            client1.configureBlocking(false);
                            client1.register(selector, SelectionKey.OP_READ);
                        }
                        else if(client2 == null || client2.isConnected() == false){
                            client2 = socketChannel;
                            // handleSocketByThread(socketChannel);
                            client2.configureBlocking(false);
                            client2.register(selector, SelectionKey.OP_READ);
                        }
                    }
                    if(key.isReadable()){
                        SocketChannel socketChannel = (SocketChannel)key.channel();
                        if(socketChannel != null && socketChannel.isOpen()){
                            readData(socketChannel);
                        }
                        writeToClient("Hello client");
                    }
                    it.remove();
                }

            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public boolean readData(SocketChannel socketChannel) throws IOException{
        int res = socketChannel.read(byteBuffer);
        if(res > 0){
            String process = bufToString(byteBuffer);
            System.out.println(socketChannel.getRemoteAddress()+ ":" + process);
        }
        else if(res == -1){
            socketChannel.close();
            return false;
        }
        return true;
    }

    public String bufToString(ByteBuffer buf){
        buf.flip();
        Charset charset = Charset.forName("utf-8");
        CharBuffer charBuffer = charset.decode(buf);
        String temp = charBuffer.toString();
        String[] Process = temp.split("\n");
        if(Process.length > 0){
            String process = Process[0];
            buf.position(process.length());
            // System.out.println("length: " + process.length());
            // buf.compact();
            buf.clear();
            // charBuffer = charset.decode(buf);
            // temp = charBuffer.toString();
            // System.out.print("temp: " + temp);
            return process;
        }
        return "";
    } 

    public void writeToClient(String t){
        try{
            t = t + "\n";
            if(client1 != null && client1.isOpen()){
                ByteBuffer buffer = ByteBuffer.wrap(t.getBytes("utf-8"));
                // Charset charset = Charset.forName("utf-8");
                // CharBuffer charBuffer = charset.decode(buffer);
                // String temp = charBuffer.toString();
                // System.out.print("writeToClient1: " + temp);
                client1.write(buffer);
            }
            // ByteBuffer buffer = ByteBuffer.wrap(t.getBytes("utf-8"));
            if(client2 != null && client2.isOpen()){
                ByteBuffer buffer = ByteBuffer.wrap(t.getBytes("utf-8"));
                // Charset charset = Charset.forName("utf-8");
                // CharBuffer charBuffer = charset.decode(buffer);
                // String temp = charBuffer.toString();
                // System.out.print("writeToClient2: " + temp);
                client2.write(buffer);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String args[]){
        Server server = new Server();
        server.listen();
    }
}
