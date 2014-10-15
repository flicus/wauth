package net.ffff.wauth;

import net.ffff.wauth.inflow.RadiusMessageListener;
import net.ffff.wauth.protocol.RadiusRequest;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSDestinationDefinition;

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

//todo
@JMSDestinationDefinition(
        name = "",
        interfaceName = "",
        destinationName = ""
)
@MessageDriven(
        activationConfig = {
                @ActivationConfigProperty(
                        propertyName = "port",
                        propertyValue = "4444"
                )
        }
)
public class RadiusMDB implements RadiusMessageListener {

    @Override
    public void onMessage(RadiusRequest request) {

    }
}
