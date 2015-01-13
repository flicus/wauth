package org.ffff.wauth.logic;

import java.util.logging.Logger;

/**
 * Created by flicus on 13.01.15.
 */

@Stateless
@Remote({RemoteAuthenticator.class})
public class Authenticator implements RemoteAuthenticator {
    Logger logger = Logger.getLogger(Authenticator.class.getName());

    public Authenticator() {
    }

    public byte[] execute(BalancedMessage incoming) {
        this.logger.info("onAuth");
        String nodeName = System.getProperty("jboss.node.name");
        return nodeName.getBytes();
    }
}
