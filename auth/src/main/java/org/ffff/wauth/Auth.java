package org.ffff.wauth;

import org.infinispan.manager.CacheContainer;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;

@Stateless
public class Auth {

    @Resource(lookup = "java:jboss/infinispan/container/sessions")
    private CacheContainer container;

    private org.infinispan.Cache<String, String> cache;

    @PostConstruct
    public void initCache() {
        this.cache = container.getCache();
    }
}
