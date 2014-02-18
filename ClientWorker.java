/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author uman
 */
public class ClientWorker implements Runnable {
    private Socket server;
    private String line,input;
    int myClientNumber;
    ArrayList<String> output;    
    static String coopString = "";
    static int coopInt = 0;

    //DataInputStream in = null;// new DataInputStream (server.getInputStream());
    PrintWriter os = null;// new PrintStream(server.getOutputStream());
    BufferedReader b = null;// new BufferedReader(new InputStreamReader(server.getInputStream()));

    
    Socket smtpSocket = null;  
    DataOutputStream dos = null;
    DataInputStream dis = null;
    String hostname = "localhost";

    public ClientWorker(Socket server, int n) {
      this.server=server;
      myClientNumber=n;
    }

    @Override
    public void run () {

    	TempPoint result[][];
        input="";

        try {
          
        	System.out.println("Connected to client, recieving packet...");
            os = new PrintWriter(server.getOutputStream(), true);
            b = new BufferedReader(new InputStreamReader(server.getInputStream()));
            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
      
            
            Object obj = ois.readObject();  
            
            result = (TempPoint[][])obj;  
            
            System.out.println(result[0][0].getTemp());
            
		    b.close();
		    ois.close();


        } catch (IOException ioe) {
          System.err.println("IOException on socket listen: ");
          ioe.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
			// TODO Auto-generated catch block
        	System.err.println("ClassNotFoundException on socket listen: ");
			cnfe.printStackTrace();
		}
    }
    
    
    void sendToClient(String s){
        
        if(smtpSocket==null && dos==null && dis==null){
            try {
                smtpSocket = new Socket(hostname, 4891);
                dos = new DataOutputStream(smtpSocket.getOutputStream());
                dis = new DataInputStream(smtpSocket.getInputStream());
            } catch (UnknownHostException e) {
                System.err.println("Don't know about host: " + hostname);
            } catch (IOException e) {
                System.err.println("Couldn't get I/O for the connection to: " + hostname);
            } 
        }
        
        if (smtpSocket != null && dos != null && dis != null) {
            try {
                dos.writeBytes(s + "\n");
            } catch (UnknownHostException e) {
                System.err.println("Trying to connect to unknown host: " + e);
            } catch (IOException e) {
                System.err.println("IOException:  " + e);
            }
        }
        
        run();
        
    }

    String calculate(String s){
	return s;
    }

    void sendMessage(ArrayList<String> message){

	os.println(message.size()+"");
	for(String s : message)
		os.println(s);
    }              
}
