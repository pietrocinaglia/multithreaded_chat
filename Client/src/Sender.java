import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

class Sender extends Thread{
    private Socket s;
    private PrintWriter pw;
    private Scanner scanner;

    public Sender(Socket s){
        this.s = s;
        this.scanner = new Scanner(System.in);
        try{
            this.pw = new PrintWriter(this.s.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        String message;
        while (true){
            message = scanner.nextLine();
            this.pw.println(message);
        }
    }
}