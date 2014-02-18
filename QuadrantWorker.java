
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;


@SuppressWarnings("serial")
class QuadrantWorker extends RecursiveAction{

	TempPoint rQuad[][], wQuad[][];
	final int THRESHOLD = 8;
	ForkJoinPool myPool;
	
	
	QuadrantWorker(TempPoint[][] r, TempPoint[][] w){
		rQuad = r;
		wQuad = w;	
	}
	
	TempPoint[][] split(TempPoint m[][], int q){
		TempPoint result[][];
		int width, height, startX, startY;
		
		if(m.length == m[0].length){
			width = m.length/2;
			height = m[0].length/2;
			startX = 0;
			startY = 0;
			
			if(q==2){
				startX = width;
				startY = 0;
			}
			else if(q==3){
				startX = 0;
				startY = height;
			}
			else if(q==4){
				startX = width;
				startY = height;
			}
			
		}
		else{
			width = m.length/4;
			height = m[0].length;
			startX = width * (q - 1);
			startY = 0;	
		}
		
		result = new TempPoint[width][height];
		
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				result[i][j] = m[startX+i][startY+j];
			}
		}
		
		return result;
	}

	
	
	@Override
	protected void compute() {
		myPool = new ForkJoinPool();
			TempPoint rSplit[][][] = new TempPoint[4][][];
			
			for(int i=0;i<rSplit.length;i++){
				rSplit[i] = split(rQuad, i+1);
			}
			for(int i=0;i<rSplit.length;i++){
				Main.workers.get(i).setHeader(new Header(Main.S, Main.T, Main.C, Main.M_HEIGHT, Main.M_WIDTH, Main.MAX_TEMP), new Packet(getLeftNeighbors(rSplit[i], i+1), rSplit[i], getRightNeighbors(rSplit[i], i+1)));
			}
			
			if(Main.debug)System.out.println("Map split, starting workers...");
			
			invokeAll(Main.workers);
			
			for(ClientWorker c : Main.workers){
				merge(c.getNewMap());
			}
	}

	private TempPoint[][] getLeftNeighbors(TempPoint[][] rq, int index){

		if(index==1)
			return null;
		else{
			TempPoint result[][] = new TempPoint[1][Main.M_HEIGHT];

			for(int i=0;i<Main.M_HEIGHT;i++){
				result[0][i] = Main.rMap[rq[0][i].xPos - 1][i];
			}
			return result;
		}
	}
	
	private TempPoint[][] getRightNeighbors(TempPoint[][] rq, int index){

		if(index==4)
			return null;
		else{
			TempPoint result[][] = new TempPoint[1][Main.M_HEIGHT];

			for(int i=0;i<Main.M_HEIGHT;i++){
				result[0][i] = Main.rMap[(rq[rq.length-1][i].xPos + 1)][i];
			}
			return result;
		}
	}
	
	private void merge(TempPoint[][] quad){
		if(Main.debug)System.out.println("Merging...");
		
		for(int i=0;i<quad.length;i++){
			for(int j=0;j<quad[0].length;j++){
				
				wQuad[quad[i][j].xPos][quad[i][j].yPos].setTemp(quad[i][j].getTemp());
				
			}
		}
		
	}
}
