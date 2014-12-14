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

package org.ffff.wifi.ra;

import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.spi.*;
import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by flicus on 14.12.2014.
 */
public class RadiusManagedConnection implements ManagedConnection {


    private static Logger log = Logger.getLogger(RadiusManagedConnection.class.getName());


    private PrintWriter logwriter;
    private RadiusManagedConnectionFactory mcf;
    private RadiusConnectionImpl connection;
    private List<ConnectionEventListener> listeners;

    public RadiusManagedConnection(RadiusManagedConnectionFactory mcf) {
        this.mcf = mcf;
        this.listeners = Collections.synchronizedList(new ArrayList<ConnectionEventListener>(1));
        this.connection = null;
        this.logwriter = null;
    }

    public RadiusManagedConnectionFactory getMcf() {
        return mcf;
    }

    @Override
    public Object getConnection(Subject subject, ConnectionRequestInfo connectionRequestInfo) throws ResourceException {
        this.connection = new RadiusConnectionImpl(this);
        return connection;
    }

    @Override
    public void destroy() throws ResourceException {

    }

    @Override
    public void cleanup() throws ResourceException {

    }

    @Override
    public void associateConnection(Object o) throws ResourceException {
        if (connection == null)
            throw new ResourceException("Null connection handle");
        if (!(connection instanceof RadiusConnectionImpl))
            throw new ResourceException("Wrong connection handle");
        this.connection = (RadiusConnectionImpl) o;

    }

    @Override
    public void addConnectionEventListener(ConnectionEventListener connectionEventListener) {
        if (connectionEventListener == null)
            throw new IllegalArgumentException("Listener is null");
        listeners.add(connectionEventListener);
    }

    @Override
    public void removeConnectionEventListener(ConnectionEventListener connectionEventListener) {
        if (connectionEventListener == null)
            throw new IllegalArgumentException("Listener is null");
        listeners.remove(connectionEventListener);
    }

    @Override
    public XAResource getXAResource() throws ResourceException {
        throw new NotSupportedException("getXAResource() not supported");
    }

    @Override
    public LocalTransaction getLocalTransaction() throws ResourceException {
        throw new NotSupportedException("getLocalTransaction() not supported");
    }

    @Override
    public ManagedConnectionMetaData getMetaData() throws ResourceException {
        return new RadiusManagedConnectionMetaData();
    }

    @Override
    public void setLogWriter(PrintWriter printWriter) throws ResourceException {
        this.logwriter = printWriter;
    }

    @Override
    public PrintWriter getLogWriter() throws ResourceException {
        return this.logwriter;
    }
}
