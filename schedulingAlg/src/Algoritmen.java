
public class Algoritmen {
	private int tijd,loper,grootteList,bedieningsTijd,aankomstTijd;
	Process huidigProces;
	public void berekenFCFS(ProcessList processen){
		tijd =0; 
		loper=0;
		grootteList=processen.getSize();
		while (loper < grootteList) {
			huidigProces=processen.getProces(loper);
			aankomstTijd=huidigProces.getArrivaltime();
			bedieningsTijd=huidigProces.getServicetime();
			if(aankomstTijd>tijd){
				tijd=aankomstTijd;    //er wordt een tijdje geen proces uitgevoerd omdat er nog geen klaar staat
			}
			tijd+=bedieningsTijd;
			huidigProces.setEndtime(tijd);
			huidigProces.setRuntime(tijd-aankomstTijd);
			huidigProces.setNorRuntime((tijd-aankomstTijd)/bedieningsTijd);
			huidigProces.setWaittime(tijd-aankomstTijd-bedieningsTijd);
			loper++;
		}	
	}
	public void berekenRR(ProcessList processen){
		
	}
	
	public void berekenHRRN(){
		
	}
	public void berekenMFM(){
	
	}
}
