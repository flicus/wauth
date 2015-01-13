import org.ffff.wauth.Dialog;
import org.ffff.wauth.logic.Authenticator;
import org.ffff.wauth.logic.RemoteAuthenticator;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Test2 {

    public static void main(String[] args) throws NamingException {
        Context context = new InitialContext();
        RemoteAuthenticator auth = (RemoteAuthenticator) context.lookup("ejb:wifi/auth//" + Authenticator.class.getSimpleName() + "!" + RemoteAuthenticator.class.getName());
        byte[] a = auth.execute(new Dialog("h1", 12, new byte[]{1, 2, 3}));
        a = auth.execute(new Dialog("h1", 12, new byte[]{3, 2, 1}));
        a = auth.execute(new Dialog("h1", 12, new byte[]{3, 3, 3}));
        a = auth.execute(new Dialog("h1", 12, new byte[]{4, 4, 4}));

    }

}
