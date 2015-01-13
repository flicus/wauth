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

package org.ffff.wauth.ra;

import org.ffff.wauth.ra.inflow.RadiusActivation;
import org.ffff.wauth.ra.inflow.RadiusActivationSpec;

import javax.resource.ResourceException;
import javax.resource.spi.*;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.resource.spi.work.WorkManager;
import javax.transaction.xa.XAResource;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Created by flicus on 14.12.2014.
 */
@Connector(
        reauthenticationSupport = false,
        displayName = {"Radius Resource Adapter"},
        description = {"Radius Resource Adapter"},
        eisType = "radius",
        transactionSupport = TransactionSupport.TransactionSupportLevel.NoTransaction
)
public class RadiusResourceAdapter implements ResourceAdapter, Serializable {


    private static Logger log = Logger.getLogger(RadiusResourceAdapter.class.getName());
    private ConcurrentHashMap<RadiusActivationSpec, RadiusActivation> activations;
    private WorkManager workManager;

    public RadiusResourceAdapter() {
        this.activations = new ConcurrentHashMap<RadiusActivationSpec, RadiusActivation>();
    }

    @Override
    public void start(BootstrapContext bootstrapContext) throws ResourceAdapterInternalException {
        log.info("starting");
        this.workManager = bootstrapContext.getWorkManager();
    }

    @Override
    public void stop() {
        log.info("stoping");
        this.workManager = null;
        this.activations.clear();

    }

    @Override
    public void endpointActivation(MessageEndpointFactory messageEndpointFactory, ActivationSpec activationSpec) throws ResourceException {
        log.info("endpoint activation: " + activationSpec);
        RadiusActivation activation = new RadiusActivation(this, messageEndpointFactory, (RadiusActivationSpec) activationSpec);
        activations.put((RadiusActivationSpec) activationSpec, activation);
        activation.start();
    }

    @Override
    public void endpointDeactivation(MessageEndpointFactory messageEndpointFactory, ActivationSpec activationSpec) {
        log.info("endpoint deactivation: " + activationSpec);
        RadiusActivation activation = activations.remove(activationSpec);
        if (activation != null) {
            activation.stop();
        }
    }

    @Override
    public XAResource[] getXAResources(ActivationSpec[] activationSpecs) throws ResourceException {
        return new XAResource[0];
    }

    public WorkManager getWorkManager() {
        return workManager;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RadiusResourceAdapter that = (RadiusResourceAdapter) o;

        if (activations != null ? !activations.equals(that.activations) : that.activations != null) return false;
        if (workManager != null ? !workManager.equals(that.workManager) : that.workManager != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = activations != null ? activations.hashCode() : 0;
        result = 31 * result + (workManager != null ? workManager.hashCode() : 0);
        return result;
    }
}
