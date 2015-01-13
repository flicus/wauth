package org.ffff.wauth.logic;

import javax.ejb.Remote;

/**
 * Created by flicus on 13.01.15.
 */
@Remote
public interface RemoteAuthenticator {
    byte[] execute(byte[] request);
}
