package org.ffff.wauth;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by flicus on 13.01.15.
 */
public class Dialog implements Serializable {
    private String originHost;
    private int originPort;
    private byte[] message;

    public Dialog() {
    }

    public Dialog(String originHost, int originPort, byte[] message) {
        this.originHost = originHost;
        this.originPort = originPort;
        this.message = message;
    }

    public String getOriginHost() {
        return originHost;
    }

    public void setOriginHost(String originHost) {
        this.originHost = originHost;
    }

    public int getOriginPort() {
        return originPort;
    }

    public void setOriginPort(int originPort) {
        this.originPort = originPort;
    }

    public byte[] getMessage() {
        return message;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "BalancedMessage{" +
                "originHost='" + originHost + '\'' +
                ", originPort=" + originPort +
                ", message=" + Arrays.toString(message) +
                '}';
    }
}
