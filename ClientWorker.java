import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.RecursiveAction;

@SuppressWarnings("serial")
public class ClientWorker extends RecursiveAction{
    private Socket server;
    int myClientNumber;
    private Header myHeader;
    private Packet myPacket;
    private TempPoint[][] newMap;
    
    ObjectOutputStream oos;
    ObjectInputStream ois;

    public ClientWorker(Socket server, int n) {
      this.server=server;
      
      newMap=null;
      myClientNumber=n;
      myHeader= null;
      myPacket=null;
    }
    
    public void setHeader(Header h, Packet p){
    	myHeader = h;
    	myPacket = p;
    }
    
    public Header getHeader(){
    	return myHeader;
    }

    public void compute() {

    	TempPoint result[][]=null;
    	if(Main.debug){
    		if(Main.headerSent)
    			System.out.println("Trying to re-connect...");
    		else
    			System.out.println("Trying to connect...");
    	}
        	
        try {
        	oos = new ObjectOutputStream(server.getOutputStream());
    		ois = new ObjectInputStream(server.getInputStream());
        	
    		if(Main.debug)System.out.println("Connected to client.");
            
    		
    		if(!Main.headerSent){
    			if(Main.debug)System.out.println("Writing Header...");
    			oos.writeObject(myHeader);
    			Main.headerSent=true; 
    		}
    		
    		if(Main.debug)System.out.println("Writing packet...");
    		oos.writeObject(myPacket);
    		
            if(Main.debug)System.out.println("Waiting for response...");
            
            Object obj = ois.readObject();
            
            result = (TempPoint[][])obj;
            
            if(Main.debug)System.out.println("Got a response.");
            
            oos.close();
            ois.close();
            server.close();


        } catch (IOException ioe) {
          System.err.println("IOException on socket listen: ");
          ioe.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
        	System.err.println("ClassNotFoundException on socket listen: ");
			cnfe.printStackTrace();
		}
        
        setNewMap(result);
    }
    
    void setNewMap(TempPoint[][] m){
    	newMap=m;
    }
    
    public TempPoint[][] getNewMap(){
    	return newMap;
    }            
}
