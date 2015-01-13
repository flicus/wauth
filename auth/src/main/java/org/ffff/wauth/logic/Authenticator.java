package org.ffff.wauth.logic;

import org.ffff.wauth.Dialog;
import org.infinispan.manager.CacheContainer;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.util.logging.Logger;

/**
 * Created by flicus on 13.01.15.
 */

@Stateless
@Remote({RemoteAuthenticator.class})
public class Authenticator implements RemoteAuthenticator {
    Logger logger = Logger.getLogger(Authenticator.class.getName());

    @Resource(lookup = "java:jboss/infinispan/container/ras")
    private CacheContainer container;

    private org.infinispan.Cache<String, String> cache;

    public Authenticator() {
    }

    @PostConstruct
    public void initCache() {
        this.cache = container.getCache();
    }

    public byte[] execute(Dialog incoming) {
        this.logger.info("onAuth");
        String nodeName = System.getProperty("jboss.node.name");
        return nodeName.getBytes();
    }
}
