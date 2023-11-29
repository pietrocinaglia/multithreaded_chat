import java.io.IOException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println("Inserisci username:");
        String username = s.nextLine();

        Client c = new Client("localhost", 2912);
        
        boolean connected = false;
        try {
            connected = c.connect(username);
        } catch (IOException e) {}

        if(connected){
            System.out.println("--------------- Chat attiva ---------------");
            System.out.println("-------------------------------------------");
            System.out.println();
        } else{
            System.out.println("----------- Programma Terminato -----------");
            System.out.println("-------------------------------------------");
            System.out.println();
        }
    }
}