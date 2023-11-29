import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class Client {

    private String host;
    private int port;
    private Receiver receiver;
    private Sender sender;

    private String username;

    public Client(String host, int port){
        this.host = host;
        this.port = port;
    }

    public boolean connect(String username) throws IOException{
        this.username = username;

        Socket s;
        PrintWriter pw;
        BufferedReader br;
        String auth_response = "";

        s = new Socket(host, port);

        pw = new PrintWriter(s.getOutputStream(), true);
        br = new BufferedReader( new InputStreamReader(s.getInputStream()) );

        pw.println("USERNAME|"+this.username);
        //pw.println("PASSWORD|");

        auth_response = br.readLine();

        if (auth_response.equals("AUTH|ok")){
            this.receiver = new Receiver(s);
            this.receiver.start();
            this.sender = new Sender(s);
            this.sender.start();

            return true;

        } else if (auth_response.equals("AUTH|exist")){
            System.out.println("Username già collegato.");
        } else {
            System.out.println("Errore di autenticazione.");
        }

        s.close();
        return false;
    }
}