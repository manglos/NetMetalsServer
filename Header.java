import java.io.Serializable;

public class Header implements Serializable{
	
	private static final long serialVersionUID = 1429731867059635800L;
	public Long S, T;
	public static double C[];
	public int M_HEIGHT = 4;
	public int M_WIDTH = 16;
	public volatile TempPoint rMap[][], wMap[][], rQuad[][], wQuad[][];
	public Long MAX_TEMP=(long)(10000 * 100);
	
	public Header(Long sVal, Long tVal, double cVal[], int hVal, int wVal, Long mVal, TempPoint[][] rM, TempPoint[][] wM, TempPoint[][] rQ, TempPoint[][] wQ){
		S=sVal;T=tVal;C=cVal;M_HEIGHT=hVal;M_WIDTH=wVal;MAX_TEMP=mVal;rMap=rM;wMap=wM;rQuad=rQ;wQuad=wQ;
	} 
	

}
