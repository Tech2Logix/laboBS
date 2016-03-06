import java.util.*;

public class Percentiel {
	private LinkedList<Process> percentielen;
	
	Percentiel(){
	}
	
	Percentiel(ProcessList processenLijst){
		
		processenLijst.sorteerOpServiceTijd();
		//processenLijst.printList(); //test
		
		percentielen=new LinkedList<Process>();
		int nProcessenPerPercentiel=processenLijst.getSize()/100;
		int bedieningstijd=0, genorOmlooptijd=0, wachttijd=0;
		Process p;
		for (int i=0;i<100;i++){  //per percentiel
			for(int j=0;j<nProcessenPerPercentiel;j++){
				bedieningstijd+=processenLijst.getProces(i*nProcessenPerPercentiel+j).getServicetime();
				genorOmlooptijd+=processenLijst.getProces(i*nProcessenPerPercentiel+j).getNorRuntime();
				wachttijd+=processenLijst.getProces(i*nProcessenPerPercentiel+j).getWaittime();
			}
			p=new Process();
			p.setServicetime(bedieningstijd/nProcessenPerPercentiel);	//dit is dus de GEMIDDELDE bedieningstijd
			p.setNorRuntime(genorOmlooptijd/nProcessenPerPercentiel);
			p.setWaittime(wachttijd/nProcessenPerPercentiel);
			percentielen.add(p);
		}
		
	}
	public LinkedList<Process> getPercentielList(){
		return percentielen;
	}
	public Process getProces(int i){
		return percentielen.get(i);
	}
	public void printList(){ //voor testen
		int max=percentielen.size();
		for(int i=0;i<max;i++){
			System.out.println(percentielen.get(i).getServicetime());
		}
	}
}
