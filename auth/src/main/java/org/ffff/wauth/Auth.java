package org.ffff.wauth;

import org.infinispan.manager.CacheContainer;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;

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
 * Date: 12.01.2015
 * Time: 17:48
 * <p/>
 * $Id:
 */

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
