import java.util.Set;
import java.util.HashSet;

public class App {
    public static void main(String[] args) {
        Set<String> allowed_users = new HashSet<String>();
        //allowed_users.add("User1");
        //allowed_users.add("User2");

        /*
         * Una possibile estensione può riguardare la creazione di un oggetto User
         * avente username e password, in modo da simulare una autenticazione rudimentale.
         */

        new Server(2912, allowed_users);
    }
}
