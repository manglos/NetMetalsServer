import java.io.Serializable;


public class TempPoint implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2012546879875911708L;
	final int xPos, yPos;
	volatile Long temperature;
	final double p[];

	TempPoint(int x, int y, Long t, double ps[]){
		xPos = x;
		yPos = y;
		p=new double[3];
		p[0]=ps[0];
		p[1]=ps[1];
		p[2]=ps[2];
		temperature = t;
	}

	String getKey(){
		return xPos+"-"+yPos;
	}

	int getCol(){
		return xPos;
	}

	int getRow(){
		return yPos;
	}

	void setTemp(Long t){
		temperature = t;
	}

	double[] getPtgs(){
		return p;
	}
	
	public Long getTemp(){
		return temperature;
	}

	
	public Long calcTemp() {
		TempPoint neighbors[]=getNeighbors();
 
		//if(Main.debug)System.out.println("CALC TEMP");

		Long temps[] = new Long[4];
		double mPtgs[][] = new double[4][3];
		
		for(int i=0;i<neighbors.length;i++){
			if(neighbors[i]!=null){
				temps[i]=neighbors[i].getTemp();
				mPtgs[i]=neighbors[i].getPtgs();
			}
			else{
				temps[i]=null;
				mPtgs[i]=null;
			}
		}

		if(!(getCol()==0 && getRow()==0) && !(getCol()==Main.M_WIDTH-1 && getRow()==Main.M_HEIGHT-1))
			return calculateTemp(temps, mPtgs);
		
		return temperature;

	}

	TempPoint[] getNeighbors(){
		TempPoint result[] = new TempPoint[4];
		
		if(xPos>0)
			result[3] = Main.rMap[xPos-1][yPos];
		if(xPos<Main.M_WIDTH-1)
			result[1] = Main.rMap[xPos+1][yPos];
		if(yPos>0)
			result[0] = Main.rMap[xPos][yPos-1];
		if(yPos<Main.M_HEIGHT-1)
			result[2] = Main.rMap[xPos][yPos+1];
		
		return result;
	}
	
	long calculateTemp(Long nTemps[], double mPtgs[][]){

		double result = 0;

		for(int i=0;i<Main.C.length;i++){
			double sumNeighbors = 0;
			int numNeighbors = 0;
			
			for(int j=0;j<4;j++){
				if(nTemps[j]!=null){
					sumNeighbors+=(double)(nTemps[j] * mPtgs[j][i]);
					numNeighbors++;
				}
			}
			
			if(numNeighbors!=0)
				result += (double)(Main.C[i] * (double)(sumNeighbors/numNeighbors));
		}

		if(result>Main.MAX_TEMP){
			if(Main.debug)System.out.println("Reached Max Temp");
			return Main.MAX_TEMP;
		}
		return (long)result;
	}
	
}
