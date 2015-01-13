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
    private volatile boolean isStarted = false;
    private Sender sender;
    private DatagramSocket serverSocket;

    public Balancer() {
        sender = new Sender();
        try {
            serverSocket = new DatagramSocket(1812, InetAddress.getByName("localhost"));
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
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
            //todo call remote ejb
            byte[] data = new byte[]{};
            DatagramPacket packetOut = new DatagramPacket(data, 0, data.length, packetIn.getAddress(), packetIn.getPort());
            sender.send(packetOut);
        }
    }
}
