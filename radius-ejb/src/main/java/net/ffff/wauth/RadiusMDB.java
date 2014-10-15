package net.ffff.wauth;

import net.ffff.wauth.inflow.RadiusMessageListener;
import net.ffff.wauth.protocol.RadiusRequest;
import org.jboss.ejb3.annotation.ResourceAdapter;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSDestinationDefinition;
import javax.jms.Topic;


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
 * Time: 19:46
 * <p/>
 * $Id:
 */

@JMSDestinationDefinition(
        name = "java:app/wauth/radius",
        interfaceName = "javax.jms.Topic",
        destinationName = "radius"
)
@MessageDriven(
        activationConfig = {
                @ActivationConfigProperty(
                        propertyName = "port",
                        propertyValue = "4444"
                )
        },
        messageListenerInterface = net.ffff.wauth.inflow.RadiusMessageListener.class
)
@ResourceAdapter(value = "radius-ra-rar.rar")
public class RadiusMDB implements RadiusMessageListener {

    @Resource(lookup = "java:app/wauth/radius")
    private Topic topic;

    @Inject
    private JMSContext jmsContext;

    public RadiusMDB() {
    }

    @Override
    public void onMessage(RadiusRequest request) {

    }
}
