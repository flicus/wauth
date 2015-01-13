package org.ffff.wauth;

import org.ffff.wauth.logic.Authenticator;
import org.ffff.wauth.logic.RemoteAuthenticator;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Balancer implements Runnable {

    private final Lock serverSocketLock = new ReentrantLock();
    final private ExecutorService taskExecutor = Executors.newCachedThreadPool();
    private final Context context;
    private final RemoteAuthenticator authenticator;
    private volatile boolean isStarted = false;
    private Sender sender;
    private DatagramSocket serverSocket;

    public Balancer() throws NamingException {
        System.out.println("balancer create");
        context = new InitialContext();
        authenticator = (RemoteAuthenticator) context.lookup("ejb:wifi/auth//" + Authenticator.class.getSimpleName() + "!" + RemoteAuthenticator.class.getName());
        sender = new Sender();
        try {
            serverSocket = new DatagramSocket(1812, InetAddress.getByName("127.0.0.1"));
            serverSocket.setSoTimeout(5000);
//            serverSocket.setReceiveBufferSize(4096);
//            serverSocket.setSendBufferSize(4096);
            serverSocket.setBroadcast(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            Balancer balancer = new Balancer();
            Thread t = new Thread(balancer, "radius-balancer");
            t.start();
            t.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("balancer run");
        isStarted = true;
        try {
            sender.start();
            while (isStarted) {
                DatagramPacket packetIn = new DatagramPacket(new byte[4096], 4096);
                try {
                    serverSocket.receive(packetIn);
                    Runnable task = new HandleIncomingPacketTask(packetIn);
                    taskExecutor.execute(task);
                } catch (SocketTimeoutException e) {
                } catch (IOException e) {
                    isStarted = false;
                    e.printStackTrace();
                }

            }
        } finally {
            clear();
        }
    }

    public void clear() {
        System.out.println("balancer stop");
        sender.stop();
        if (serverSocket != null) {
            if (!serverSocket.isClosed()) {
                serverSocket.close();
            }
            serverSocket = null;
        } else {

        }
    }

    private class Sender {
        private final BlockingQueue<DatagramPacket> dialogQueue = new LinkedBlockingQueue<DatagramPacket>();
        private Thread senderThread;

        public void start() {
            System.out.println("sender start");
            senderThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!senderThread.isInterrupted()) {
                        try {
                            DatagramPacket packet = dialogQueue.take();
                            sendInternal(packet);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            senderThread.interrupt();
                        }
                    }
                }
            }, "radius-sender");
            senderThread.start();
        }

        private void sendInternal(final DatagramPacket packet) {
            System.out.println("sender:: send");
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    try {
                        serverSocketLock.lock();
                        serverSocket.send(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        serverSocketLock.unlock();
                    }
                }
            };
            taskExecutor.execute(task);
        }

        public void send(DatagramPacket packetOut) {
            dialogQueue.offer(packetOut);
        }

        public void stop() {
            System.out.println("sender stop");
            if (senderThread != null) {
                if (senderThread.isAlive()) {
                    senderThread.interrupt();
                    try {
                        senderThread.join();
                    } catch (InterruptedException e) {
                        //todo
                    }
                }
                dialogQueue.clear();
            }
        }
    }

    private class HandleIncomingPacketTask implements Runnable {

        private DatagramPacket packetIn;

        public HandleIncomingPacketTask(DatagramPacket packetIn) {
            this.packetIn = packetIn;
        }

        @Override
        public void run() {
            System.out.println("handler::incoming packet");
            byte[] data = authenticator.execute(packetIn.getData());
            DatagramPacket packetOut = new DatagramPacket(data, 0, data.length, packetIn.getAddress(), packetIn.getPort());
            sender.send(packetOut);
        }
    }
}
