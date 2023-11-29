import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.io.IOException;

class Server {

    private ServerSocket ss;

    private Set<ServerThread> clients;
    private Set<String> allowed_users;

    Server(int port, Set<String> allowed_users){
        
        try {
            this.ss = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.allowed_users = allowed_users;
        this.clients = new HashSet<ServerThread>();

        System.out.println("Server online.");

        init();
    }

    public void init(){
        while(true){
            Socket s = null;

            try {
                s = this.ss.accept();
            } catch (IOException e) {
                continue;
            }

            ServerThread st = new ServerThread(s, this.clients, this.allowed_users);
            st.start();
        }
    }
}