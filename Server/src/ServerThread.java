import java.net.Socket;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

class ServerThread extends Thread {

    private Socket s;
    private BufferedReader br;
    private PrintWriter pw;

    private Set<ServerThread> clients;
    private Set<String> allowed_users;

    private String username;

    ServerThread(Socket s, Set<ServerThread> clients, Set<String> allowed_users){
        this.s = s;
        this.clients = clients;

        if (allowed_users != null)
            this.allowed_users = allowed_users;
        else
            this.allowed_users = new HashSet<String>();

        /* Temporary is Thread's name.
         * Username will be sent by client, and it will be defined in 'checkingClientAuth'.
         */
        this.username = this.getName();
    }

    public String getUsername() {
        return this.username;
    }

    @Override
    public void run(){
        init();
    }

    public void init(){
        try {
            this.br = new BufferedReader(new InputStreamReader(this.s.getInputStream()));
            this.pw = new PrintWriter(this.s.getOutputStream(), true);
        } catch (IOException e) {
        }

        int auth = checkingClientAuth(false);

        if (auth == 1){
            pw.println("AUTH|ok");
            this.clients.add(this);
            listening();
        } else if (auth == 2){
            pw.println("AUTH|exist");
            disconnect();
        } else {
            pw.println("AUTH|ko");
            disconnect();
        }
    }

    private int checkingClientAuth(boolean onlyRegisteredUser){
        String message = "";

        try {
            message = this.br.readLine();
        } catch (IOException e) {
        }

        String[] submessages = message.split("\\|");
        if ( submessages[0].equals("USERNAME") )
            this.username = submessages[1].toUpperCase();
        
        // Check if the same username is online
        if ( this.clients.contains(this) ){
            return 2; // username already used, client must change it.
        }

        /*
         * Checking authentication
         */
        // - Case 1: allowed_users is not used (it is empty), or check is disabled 'onlyRegisteredUser' is false.
        if (allowed_users.isEmpty() || onlyRegisteredUser == false)
            return 1; // OK
        // - Case 2: allowed_users is used.
        if (allowed_users.contains(this.username)){
            return 1; // OK
        }

        return 0; // authetication failed.
    }

    private void listening(){
        System.out.println(" - " + this.username + " connected.");

        String message;
        try {
            while( (message = this.br.readLine()) != null){
                broadcasting(message);
            }
        } catch (IOException e) {
        } finally{
            this.clients.remove(this);
            System.out.println(" - " + this.username + " disconnected.");
        }
    }

    public void sendmessage(String message){
        this.pw.println(message);
    }

    private void broadcasting(String message){
        Iterator<ServerThread> it = this.clients.iterator();

        while(it.hasNext()){
            ServerThread st = it.next();
            if (st.equals(this) == false)
                st.sendmessage(this.username + "| " + message);
        }
    }

    public void disconnect() {
        try {
            this.s.close();
        } catch (IOException e) {
        } finally {
            this.clients.remove(this);
            System.out.println(" - " + this.username + " refused (/disconnected).");
        }
    }

    @Override
    public boolean equals(Object obj){
        if ( !(obj instanceof ServerThread) )
            return false;
        
        ServerThread st = (ServerThread) obj;

        return this.hashCode() == st.hashCode();
    }

    @Override
    public int hashCode() {
        return this.username.hashCode();
    }
}