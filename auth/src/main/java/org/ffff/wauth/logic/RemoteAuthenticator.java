package org.ffff.wauth.logic;

import org.ffff.wauth.Dialog;

import javax.ejb.Remote;

/**
 * Created by flicus on 13.01.15.
 */
@Remote
public interface RemoteAuthenticator {
    byte[] execute(Dialog dialog);
}
