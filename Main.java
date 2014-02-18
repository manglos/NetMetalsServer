
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.ArrayList;

import javax.swing.*;

public class Main extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final String ANSI_RED = "\u001B[31m";
	static final String ANSI_WHITE = "\u001B[0m";
	static final String ANSI_GREEN = "\u001B[32m";
	static final String ANSI_BLUE = "\u001B[34m";
	static final String ANSI_YELLOW = "\u001B[33m";
	static final String ANSI_CYAN = "\u001B[36m";
	static final String ANSI_PURPLE = "\u001B[35m";
	public static boolean debug=false;
	static Long S, T;
	public static double C[];
	static int M_HEIGHT = 4;
	static int M_WIDTH = 16;
	public static volatile TempPoint rMap[][], wMap[][];// = new AtomicReferenceArray<TempPoint>();//new TempPoint[M_WIDTH][M_HEIGHT]; 
	public static AtomicInteger botCount = new AtomicInteger(0);
	GraphPanel display;
	JPanel centerPanel;
	public static Long MAX_TEMP=(long)(10000 * 100);
	static Color GRID_COLOR;
	public static ForkJoinPool myPool = new ForkJoinPool();
	
	public Main(){
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null); 
		setLayout(new BorderLayout());
		
		centerPanel = new JPanel();
		display = new GraphPanel();
		centerPanel.add(display);
		
		
		System.out.println("Test");
		add(centerPanel, BorderLayout.CENTER);

		pack();
		setResizable(true); 
		setVisible(true);   
		
    } //end constructor
	
	
	public static void main(String[] args) throws InterruptedException{
		
		
		
		int argNum=0;
		C = new double[3];
		C[0]=.75;
		C[1]=1.0;
		C[2]=1.25;
		S=(long)32 * 100;
		T=(long)32 * 100;
		
		
		if(args.length>0){
			if(args[argNum].equals("-d")){
				debug = true;
				argNum++;
			}
			else if(args[argNum].equals("-h")){
				System.out.println(ANSI_YELLOW+"HELP?? Ok...\n"+ANSI_GREEN+"Debug -d "+ANSI_PURPLE+"(prints extra info)\n"+ANSI_GREEN+"Help -h"+ANSI_PURPLE+" (prints help info(you're looking at it))"+ANSI_WHITE);
				System.exit(0);
			}
		}

		try{
			M_WIDTH = Integer.parseInt(args[argNum++]);
			M_HEIGHT = Integer.parseInt(args[argNum++]);
			S = Long.parseLong(args[argNum++]) * 100;
			T = Long.parseLong(args[argNum++]) * 100;
			if(T>S)
				MAX_TEMP = 4*T;
			else
				MAX_TEMP = 4*S;
			
			C[0] = Double.parseDouble(args[argNum++]);
			C[0] = Double.parseDouble(args[argNum++]);
			C[0] = Double.parseDouble(args[argNum++]);
		}catch(Exception e){
			System.out.println("Bad input, using defaults: (Width, Height, S, T, C1, C2, C6403) -> (" + M_WIDTH + ", " + M_HEIGHT + ", " + S + ", " + T + ", " + C[0] + ", " + C[1] + ", " + C[2] + ")");
		}
		
		rMap = new TempPoint[M_WIDTH][M_HEIGHT];
		wMap = new TempPoint[M_WIDTH][M_HEIGHT];
		
		//addPoint(1,0,new TempPoint());
		//addPoint(9,0,new TempPoint());
		for(int i=0;i<M_WIDTH;i++){
			for(int j=0;j<M_HEIGHT;j++){
				Random r = new Random();
				double p[] = new double[3];
				p[0]= 1 * r.nextDouble();
				p[1]= (1.0-p[0]) * r.nextDouble();
				p[2]= 1.0 - p[0] - p[2];
				
				if(i==0 && j==0){					
					rMap[i][j] = new TempPoint(i,j,S,p);
					wMap[i][j] = new TempPoint(i,j,S,p);
				}
					
				else if(i==M_WIDTH-1 && j==M_HEIGHT-1){
					rMap[i][j] = new TempPoint(i,j,T,p);
					wMap[i][j] = new TempPoint(i,j,T,p);
				}
				else{
					rMap[i][j] = new TempPoint(i,j,(long)0,p);
					wMap[i][j] = new TempPoint(i,j,(long)0,p);
				}
		
			}
		}
		
		
		/*//GRID_COLOR = getColor(1301);
		
		SwingUtilities.invokeLater(
				   new Runnable() {
				       public void run() {
					   createAndShowGUI();
				       }
				   });*/
		
		MyServer server = new MyServer();
		server.listen();
		/*for(;;){
			QuadrantWorker headQuad = new QuadrantWorker(rMap, wMap);
			myPool.invoke(headQuad);
			
			switchMaps();
			
			if(debug)System.out.println(printMap(rMap));
			if(debug)System.out.println(printMap(wMap));
			
		}*/
		
		
		
		

	}
	
	private static void switchMaps() {
		TempPoint temp[][] = rMap;
		rMap = wMap;
		wMap = temp;
	}

	static String printMap(TempPoint m[][]){
		String result = "";
		String max = "             ";
		String min = "   ";
		
		result+= min;
		for(int i=0;i<M_WIDTH;i++){
			String num = i+"";
			result += num + max.substring(num.length());
		}
		result +="\n";
		result+= min.replaceAll(" ", "-");
		for(int i=0;i<M_WIDTH;i++){
			result += max.replaceAll(" ", "-");
		}
		result +="\n";
		
		for(int i=0;i<M_HEIGHT;i++){
			String index = i+"";
			result += index + min.substring(index.length());
			for(int j=0;j<M_WIDTH;j++){
				String num = m[j][i].getTemp()+"";
				result += num + max.substring(num.length());
			}
			result += "\n";
		}
		
		return result;
	}
	
	public static void createAndShowGUI() {
		
		JFrame.setDefaultLookAndFeelDecorated(true);
		try {
	            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		}
		catch (Exception e) {}
		
		@SuppressWarnings("unused")
		Main main = new Main();
	}
	    
	@SuppressWarnings("serial")
	class GraphPanel extends JPanel {
		
		int cellWidth, cellHeight;

		public GraphPanel() {
	            setBorder(BorderFactory.createLineBorder(Color.blue));
	            
		}
		public Dimension getPreferredSize() {
	            return new Dimension(1024,256);
		}
		
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			cellWidth = this.getWidth() / M_WIDTH;
            cellHeight = this.getHeight() / M_HEIGHT;
            
            if(cellWidth<=0 || cellHeight<=0){
            	cellWidth=1;
            	cellHeight=1;
            }

    		setBackground(Color.black);
    		g.setColor(GRID_COLOR);
    		
    		for(int i=0;i<M_WIDTH;i++){
    			for(int j=0;j<M_HEIGHT;j++){
    				if((i==0 && j==0) || (i==M_WIDTH-1 && j==M_HEIGHT-1)){
    					g.setColor(Color.orange);
	    				g.fillRect(
	    						cellWidth * i, cellHeight * j, cellWidth, cellHeight);
	    				g.setColor(GRID_COLOR);
    				}
    				else{
	    				g.drawRect(cellWidth * i, cellHeight * j, cellWidth, cellHeight);
	    				g.setColor(getColor(rMap[i][j].getTemp()));
	    				g.fillRect(cellWidth * i, cellHeight * j, cellWidth, cellHeight);
	    				g.setColor(GRID_COLOR);
    				}
    			}
    		}
    		
    		this.repaint();
		}

	}
	
	public static Color getColor(long temp){
		Color result;
		double ratio = (temp/(double)MAX_TEMP);
		int redCode = (int) (255 * ratio);
		result = new Color(redCode, 0, 0);
		return result;
	}
}
