import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Algoritmen {
	private int tijd, loper, grootteList, bedieningsTijd, aankomstTijd;
	Process huidigProces;

	public void berekenFCFS(ProcessList processen) {
		tijd = 0;
		loper = 0;
		bedieningsTijd = 0;
		aankomstTijd = 0;
		grootteList = processen.getSize();
		while (loper < grootteList) {
			huidigProces = processen.getProces(loper);
			// System.out.println(huidigProces.getArrivaltime());
			aankomstTijd = huidigProces.getArrivaltime();
			bedieningsTijd = huidigProces.getServicetime();
			if (aankomstTijd > tijd) {
				tijd = aankomstTijd; // er wordt een tijdje geen proces
										// uitgevoerd omdat er nog geen klaar
										// staat
			}
			tijd += bedieningsTijd;
			huidigProces.setEndtime(tijd);
			huidigProces.setRuntime(tijd - aankomstTijd);
			huidigProces.setNorRuntime((tijd - aankomstTijd) / bedieningsTijd);
			huidigProces.setWaittime(tijd - aankomstTijd - bedieningsTijd);
			// System.out.println(loper);
			loper++;
		}
	}

	
	
	
	
	public void berekenRR(ProcessList processen, int timeSlices){
		ArrayList <Process>werk = new ArrayList<Process>();
		ArrayList <Process>removeLijst = new ArrayList<Process>();
		ProcessList temp = new ProcessList(processen);
		int overigeBedieningstijd, tijdsbeurt;
		
		//begin:
		tijd=temp.getProces(0).getArrivaltime();
		loper=1;
		
		grootteList = processen.getSize();
		werk.add(temp.getProces(0));
		int loperWerk=0;
		int maxWerk=1;
		while (loper < grootteList) {
			
			while(loperWerk<maxWerk){

				huidigProces=werk.get(loperWerk);
				loperWerk++;
				overigeBedieningstijd=huidigProces.getRemainingServicetime();
				tijdsbeurt=Math.min(overigeBedieningstijd, timeSlices);
				tijd+=tijdsbeurt;
				huidigProces.pasRemainingServicetimeAan(tijdsbeurt);
				System.out.println("proces: "+huidigProces.getPid()+" overigeService tijd: "+huidigProces.getRemainingServicetime()+" tijd: "+tijd);
				
				if(huidigProces.getRemainingServicetime()==0){  //als proces voltooid is
					huidigProces.setEndtime(tijd);
					huidigProces.setRuntime(tijd - huidigProces.getArrivaltime());
					huidigProces.setNorRuntime(huidigProces.getRuntime() / huidigProces.getServicetime());
					huidigProces.setWaittime(huidigProces.getRuntime() - huidigProces.getServicetime());
					removeLijst.add(huidigProces);
				}
				if((loperWerk == maxWerk) && (loper < grootteList)){ //als we aan het einde van ons toertje zitten moeten we kijken of de volgende ondertussen ook al mogelijk is
					if(temp.getProces(loper).getArrivaltime()<=tijd){
						werk.add(temp.getProces(loper));
						loper++;
						maxWerk++;
					}
					else{
						for(Process p: removeLijst){ 
							werk.remove(p);
							maxWerk--;
						}
						removeLijst.clear();
						loperWerk=0; //we starten weer met het eerste proces in werk
					}
				}
				
				if(werk.isEmpty()){
					tijd=temp.getProces(loper).getArrivaltime();
					werk.add(temp.getProces(loper));
					loper++;
					maxWerk++;
				}
			}
		}
	}


	
	
	
	public void berekenHRRN(ProcessList processen) {
		ProcessList werk = new ProcessList();
		ProcessList temp = new ProcessList(processen);
		Process next = new Process();
		tijd = 0;
		loper = 0;
		bedieningsTijd = 0;
		aankomstTijd = 0;
		grootteList = processen.getSize();
		
		while (loper < grootteList) {
			do {                                               //voeg processen toe aan proceslijst werk
				for (Process p : temp.getProcessenLijst()) {
					if (p.getArrivaltime() <= tijd) {
						werk.addProcess(p);
					} else
						break; // is de lijst gesorteerd op aankomsttijd...? -->ik denk van wel (Michiel)
				}
				if (werk.getProcessenLijst().size() == 0)
					tijd++;
			} while (werk.getProcessenLijst().size() == 0);

			for (Process p : werk.getProcessenLijst()) temp.deleteProcess(p);   //verwijder toegevoegde processen uit proceslijst werk
			
			if (werk.getSize() == 1) {                                          //als er slechts 1 in proceslijst werk zit
				// System.out.println("Werk size = 1?");
				huidigProces = werk.getProcessenLijst().getFirst();
			} else {															//als er meerdere in proceslijst werk zitten
				// System.out.println("Werk size > 1?");
				double maxHRRNPriority = 0;
				for (Process p : werk.getProcessenLijst()) {
					double currentHRRN = (p.getRuntime() + (tijd - p.getArrivaltime())) / (p.getRuntime());
					if (currentHRRN > maxHRRNPriority) {
						next = p;						//onthoudt enkel tot nu toe hoogste prioriteit 
					}
				}
				huidigProces = next;
			}
			werk.deleteProcess(huidigProces);
			temp.deleteProcess(huidigProces); //is dit niet al 16 regels geleden verwijderd?

			//System.out.println(loper + " - " + huidigProces.getArrivaltime() + " - " + huidigProces.getServicetime() + " - "+temp.getSize() + " - "+tijd);
			aankomstTijd = huidigProces.getArrivaltime();
			bedieningsTijd = huidigProces.getServicetime();

			tijd += bedieningsTijd;
			huidigProces.setEndtime(tijd);
			huidigProces.setRuntime(tijd - aankomstTijd);
			huidigProces.setNorRuntime(((double)(tijd - aankomstTijd)) / bedieningsTijd);
			huidigProces.setWaittime(tijd - aankomstTijd - bedieningsTijd);
			loper++;
		}
	}

	public void berekenMFM(ProcessList processen) {
	}
}
