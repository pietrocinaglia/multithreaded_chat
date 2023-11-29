import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

class Receiver extends Thread{
    private Socket s;
    private BufferedReader br;

    public Receiver(Socket s){
        this.s = s;
        try{
            this.br = new BufferedReader(
                new InputStreamReader(
                    this.s.getInputStream()
                )
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        String message;
        try{
            while ( (message = this.br.readLine()) != null)
                System.out.println(message);
        } catch(IOException e){
            e.printStackTrace();
        } finally{
            try{
                this.br.close();
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}