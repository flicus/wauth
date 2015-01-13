/*
 * The MIT License
 *
 * Copyright (c) 2014.  schors (https://github.com/flicus)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by flicus on 14.12.2014.
 */
public class Test1 {

    public static void main(String[] args) throws IOException, InterruptedException {

        DatagramSocket socket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName("127.0.0.1");
        int port = 1812;
        byte[] data = "hello!".getBytes();

        for (int i = 0; i <= 10; i++) {
            DatagramPacket packet = new DatagramPacket(data, data.length, IPAddress, port);
            socket.send(packet);
//        Thread.sleep(1000);
            DatagramPacket p = new DatagramPacket(new byte[4096], 4096);
            socket.receive(p);
            System.out.println(new String(p.getData()));
        }

    }
}
