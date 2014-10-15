package net.ffff.wauth;

import net.ffff.wauth.inflow.RadiusActivationSpec;
import net.ffff.wauth.protocol.RadiusParser;

import javax.resource.spi.endpoint.MessageEndpoint;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkException;
import javax.resource.spi.work.WorkManager;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Copyright (c) 2013 Amdocs jNetX.
 * http://www.amdocs.com
 * All rights reserved.
 * <p/>
 * This software is the confidential and proprietary information of
 * Amdocs jNetX. You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license
 * agreement you entered into with Amdocs jNetX.
 * <p/>
 * User: Sergey Skoptsov (sskoptsov@amdocs.com)
 * Date: 15.10.2014
 * Time: 12:23
 * <p/>
 * $Id:
 */

public class Worker implements Work {

    private DatagramSocket socket;
    private RadiusActivationSpec spec;
    private MessageEndpoint mep;
    private WorkManager workManager;
    private volatile boolean listen;

    public Worker(RadiusActivationSpec spec, MessageEndpoint mep, WorkManager workManager) {
        this.spec = spec;
        this.mep = mep;
        this.workManager = workManager;
        listen = true;
    }

    @Override
    public void release() {
        listen = false;
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }

    @Override
    public void run() {

        int port = Integer.parseInt(spec.getPort());
        try {
            socket = new DatagramSocket(port);
            socket.setSoTimeout(5000);
            socket.setBroadcast(false);
            while (listen) {
                DatagramPacket packet = new DatagramPacket(new byte[4096], 4096);
                socket.receive(packet);
                workManager.scheduleWork(new RadiusParser(packet, mep));
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WorkException e) {
            e.printStackTrace();
        }

    }
}
