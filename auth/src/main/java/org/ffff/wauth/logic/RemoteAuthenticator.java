package org.ffff.wauth.logic;

/**
 * Created by flicus on 13.01.15.
 */
@Remote
public interface RemoteAuthenticator {
    byte[] execute(BalancedMessage var1);
}
