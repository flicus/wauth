package net.ffff.wauth.protocol;

import net.ffff.wauth.inflow.RadiusMessageListener;

import javax.resource.spi.endpoint.MessageEndpoint;
import javax.resource.spi.work.Work;
import java.net.DatagramPacket;

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
 * Date: 14.10.2014
 * Time: 20:14
 * <p/>
 * $Id:
 */

public class RadiusParser implements Work {

    private DatagramPacket packet;
    private MessageEndpoint endpoint;

    public RadiusParser(DatagramPacket packet, MessageEndpoint endpoint) {
        this.packet = packet;
        this.endpoint = endpoint;
    }

    @Override
    public void release() {

    }

    @Override
    public void run() {

        ((RadiusMessageListener) endpoint).onMessage(new RadiusRequest(new String(packet.getData())));

    }
}
