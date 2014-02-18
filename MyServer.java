/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author montynewman
 */
public class MyServer{ 
    static final int PORT = 2689;
    static int clientNumber = 0;
    Socket echoServer;
    
    MyServer(){
    	echoServer = null;
    }
    
    public void listen(){
    	try {
			openSocket();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    private void openSocket() throws IOException{            
 
       
    	Main.workers.add(new ClientWorker(new Socket("lambda", PORT), clientNumber++));
    	Main.workers.add(new ClientWorker(new Socket("pi", PORT), clientNumber++));
    	Main.workers.add(new ClientWorker(new Socket("wolf", PORT), clientNumber++));
    	Main.workers.add(new ClientWorker(new Socket("rho", PORT), clientNumber++));
    	
        
    	if(Main.debug)System.out.println("Connected to clients.");
        
        
    }   
}
