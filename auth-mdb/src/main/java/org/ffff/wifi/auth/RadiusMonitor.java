/*
 * The MIT License
 *
 * Copyright (c) 2014.  schors (https://github.com/flicus)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.ffff.wifi.auth;

import org.ffff.wifi.protocol.RadiusRequest;
import org.ffff.wifi.ra.inflow.RadiusMessageListener;
import org.jboss.ejb3.annotation.ResourceAdapter;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import java.util.logging.Logger;

/**
 * Created by flicus on 14.12.2014.
 */
@MessageDriven(
        name = "RadiusMonitor",
        messageListenerInterface = RadiusMessageListener.class,
        activationConfig = {
            @ActivationConfigProperty(propertyName = "port", propertyValue = "4444")
        }
)
@ResourceAdapter("@RADIUSRA@")
public class RadiusMonitor implements RadiusMessageListener {
    private static Logger log = Logger.getLogger(RadiusMonitor.class.getName());

    @Override
    public void onMessage(RadiusRequest request) {
        log.info("Incoming radius request:: "+request);
        System.out.println("Incoming radius request:: "+request);
        request.makeResponse().addField("response").send();
    }
}
