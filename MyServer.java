/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author montynewman
 */
public class MyServer{ 
    static final int PORT = 2689;
    FileInputStream fis = null;
    ObjectInputStream ois = null;
    static ArrayList<String> output;
    static PrintStream os;
    static int clientNumber = 0;
    ServerSocket echoServer;
    
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
        
        //String output="";
        String line;
        DataInputStream is;
        Socket clientSocket = null;
        BufferedReader b=null;
 
        // Try to open a server socket on port 9999
        try {
           echoServer = new ServerSocket(PORT);
           System.out.println("I am listening...");
        }
        catch (IOException e) {
           System.out.println(e);
        }   
        // Create a socket object from the ServerSocket to listen and accept 
        // connections.
        // Open input and output streams

            
        while(true){
            ClientWorker w;
            try{
              w = new ClientWorker(echoServer.accept(), clientNumber++);
              Thread t = new Thread(w);
              t.start();
            } catch (IOException e) {
              System.out.println("Accept failed: " + PORT);
              echoServer.close();
              System.exit(-1);
            }
        }
        
        
    }

   

    static void sendMessage(ArrayList<String> message){

		os.println(message.size()+"");
		for(String s : message)
			os.println(s);

    }    
}
