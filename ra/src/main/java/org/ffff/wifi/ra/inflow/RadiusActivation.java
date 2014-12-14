/*
 * IronJacamar, a Java EE Connector Architecture implementation
 * Copyright 2013, Red Hat Inc, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.ffff.wifi.ra.inflow;

import org.ffff.wifi.protocol.RadiusParser;
import org.ffff.wifi.protocol.RadiusRequest;
import org.ffff.wifi.protocol.RadiusResponse;
import org.ffff.wifi.ra.RadiusResourceAdapter;

import javax.resource.ResourceException;
import javax.resource.spi.endpoint.MessageEndpoint;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkException;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

/**
 * RadiusActivation
 *
 * @version $Revision: $
 */
public class RadiusActivation {

    private static Logger log = Logger.getLogger(RadiusActivation.class.getName());

    private SocketHandler socketHandler;

    /**
     * The resource adapter
     */
    private RadiusResourceAdapter ra;

    /**
     * Activation spec
     */
    private RadiusActivationSpec spec;

    /**
     * The message endpoint factory
     */
    private MessageEndpointFactory endpointFactory;

    /**
     * Default constructor
     *
     * @throws ResourceException Thrown if an error occurs
     */
    public RadiusActivation() throws ResourceException {
        this(null, null, null);
    }

    /**
     * Constructor
     *
     * @param ra              RadiusResourceAdapter
     * @param endpointFactory MessageEndpointFactory
     * @param spec            RadiusActivationSpec
     * @throws ResourceException Thrown if an error occurs
     */
    public RadiusActivation(RadiusResourceAdapter ra,
                            MessageEndpointFactory endpointFactory,
                            RadiusActivationSpec spec) throws ResourceException

    {
        this.ra = ra;
        this.endpointFactory = endpointFactory;
        this.spec = spec;
    }

    /**
     * Get activation spec class
     *
     * @return Activation spec
     */
    public RadiusActivationSpec getActivationSpec() {
        return spec;
    }

    /**
     * Get message endpoint factory
     *
     * @return Message endpoint factory
     */
    public MessageEndpointFactory getMessageEndpointFactory() {
        return endpointFactory;
    }

    /**
     * Start the activation
     *
     * @throws ResourceException Thrown if an error occurs
     */
    public void start() throws ResourceException {
        log.info("starting ");
        int port = Integer.parseInt(spec.getPort());
        MessageEndpoint endpoint = endpointFactory.createEndpoint(null);
        socketHandler = new SocketHandler(port, endpoint);
        ra.getWorkManager().scheduleWork(socketHandler);
    }

    /**
     * Stop the activation
     */
    public void stop() {
        log.info("stoping ");
        socketHandler.release();
        socketHandler = null;
    }

    public void sendResponse(RadiusResponse response) {
        socketHandler.send(response);
    }

    private class SocketHandler implements Work {
        private final ThreadLocal<RadiusParser> parser = new ThreadLocal<RadiusParser>() {
            @Override
            protected RadiusParser initialValue() {
                return new RadiusParser(RadiusActivation.this);
            }
        };
        private DatagramSocket socket;
        private MessageEndpoint endpoint;
        private ReentrantLock socketLock = new ReentrantLock();
        private volatile boolean listen;

        public SocketHandler(int port, MessageEndpoint endpoint) {
            log.info("binding socket: " + port);
            this.endpoint = endpoint;
            try {
                socket = new DatagramSocket(port, InetAddress.getLocalHost());
                socket.setSoTimeout(5000);
                socket.setBroadcast(false);
                listen = true;
            } catch (SocketException e) {
                log.severe("Unable to bind to port " + e.getMessage());
            } catch (UnknownHostException e) {
                log.severe("Unknown host " + e.getMessage());
            }
        }

        public void send(final RadiusResponse response) {
            log.info("Response to send: " + response);
            Work writer = new Work() {
                @Override
                public void release() {

                }

                @Override
                public void run() {
                    byte[] data = parser.get().encode(response);
                    DatagramPacket packet = new DatagramPacket(
                            data,
                            data.length,
                            response.getRequest().getAddress(),
                            response.getRequest().getPort());
                    try {
                        socketLock.lock();
                        socket.send(packet);
                    } catch (IOException e) {
                        log.severe("Cannot send radius response: " + e.getMessage());
                    } finally {
                        socketLock.unlock();
                    }
                }
            };
            try {
                ra.getWorkManager().scheduleWork(writer);
            } catch (WorkException e) {
                log.warning("Unable to start socket writer: " + e.getMessage());
            }
        }

        @Override
        public void release() {
            log.info("releasing socket listner");
            listen = false;
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }

        @Override
        public void run() {
            while (listen) {
                try {
                    DatagramPacket packet = new DatagramPacket(new byte[4096], 4096);
                    socket.receive(packet);
                    ra.getWorkManager().scheduleWork(new SocketReader(this.endpoint, packet, parser));
                } catch (IOException e) {
                    log.warning(e.getMessage());
                } catch (WorkException e) {
                    log.warning(e.getMessage());
                }
            }
        }
    }

    private class SocketReader implements Work {

        private final MessageEndpoint endpoint;
        private final DatagramPacket packet;
        private final ThreadLocal<RadiusParser> parser;

        public SocketReader(MessageEndpoint endpoint, DatagramPacket packet, ThreadLocal<RadiusParser> parser) {
            this.endpoint = endpoint;
            this.packet = packet;
            this.parser = parser;
        }

        @Override
        public void release() {

        }

        @Override
        public void run() {
            log.info("New data readed");
            RadiusRequest request = parser.get().decode(packet.getData());
            request.setAddress(packet.getAddress());
            request.setPort(packet.getPort());
            log.info("### request: " + request);
            ((RadiusMessageListener) endpoint).onMessage(request);
        }
    }

}
