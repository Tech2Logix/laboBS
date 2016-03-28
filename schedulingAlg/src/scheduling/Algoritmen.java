package scheduling;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class Algoritmen {
	private double tijd, loper, grootteList, bedieningsTijd, aankomstTijd;
	Process huidigProces;
	double checksum;
	private ProcessList processen, FCFS, RR, HRRN, MLFB;

	public Algoritmen(String processListID) {
		processen = new ProcessList();
		try {
			File file = new File(processListID);
			JAXBContext jaxbContext = JAXBContext.newInstance(ProcessList.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			processen = (ProcessList) jaxbUnmarshaller.unmarshal(file);
			// System.out.println(processenLijst);
			// System.out.println(processenLijst.getSize());
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public void berekenFCFS() {
		System.out.print("FCFS ");
		ProcessList processLFCFS = new ProcessList(processen);
		checksum = 0;

		for (Process p : processLFCFS.getProcessenLijst()) {
			checksum += p.getArrivaltime();
		}
		// System.out.println(checksum);
		tijd = 0;
		loper = 0;
		bedieningsTijd = 0;
		aankomstTijd = 0;
		grootteList = processLFCFS.getSize();
		while (loper < grootteList) {
			huidigProces = processLFCFS.getProces(loper);
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
			huidigProces.setNorRuntime((double) (tijd - aankomstTijd) / bedieningsTijd);
			huidigProces.setWaittime(tijd - aankomstTijd - bedieningsTijd);
			// System.out.println(loper);
			loper++;
		}
		System.out.print("done, ");
		this.FCFS = processLFCFS;
	}

	public void berekenRR(int timeSlices) {
		System.out.print("RR ");

		ArrayList<Process> werk = new ArrayList<Process>();
		ArrayList<Process> removeLijst = new ArrayList<Process>();
		ProcessList temp = new ProcessList(processen);
		LinkedList<Process> tempLijst = temp.getProcessenLijst();
		double tijdsbeurt;
		int loperInt = 0;
		grootteList = tempLijst.size();

		while (loperInt < grootteList || !werk.isEmpty()) {
			if (werk.isEmpty()) {
				tijd = tempLijst.get(loperInt).getArrivaltime();
				werk.add(tempLijst.get(loperInt));
				loperInt++;
			}
			while ((loperInt != grootteList) && (tijd >= tempLijst.get(loperInt).getArrivaltime())) {
				werk.add(tempLijst.get(loperInt));
				loperInt++;
			}

			for (Process p : werk) {
				tijdsbeurt = Math.min(p.getRemainingServicetime(), timeSlices);
				// if (p.getStarttime() == 0) {
				// p.setStarttime(tijd);
				// p.setRemainingServicetime(p.getServicetime());
				// }
				tijd += tijdsbeurt;
				p.setRemainingServicetime(p.getRemainingServicetime() - tijdsbeurt);
				if (p.getRemainingServicetime() == 0) {
					p.setEndtime(tijd);
					removeLijst.add(p);
				}
			}
			for (Process p : removeLijst)
				werk.remove(p);
		}
		for (Process p : tempLijst) {
			p.setRuntime(p.getEndtime() - p.getArrivaltime());
			p.setWaittime(p.echteGetRuntime() - p.getServicetime());
			if (p.getWaittime() != 0)
				p.setNorRuntime(p.echteGetRuntime() / p.getServicetime());
			else
				p.setNorRuntime(1);
		}

		System.out.print("done, ");
		this.RR = temp;
	}

	public void berekenHRRN() {
		System.out.print("HRRN ");
		ProcessList processLHRRN = new ProcessList(processen);
		List<Process> werk = new ArrayList<Process>();
		Process temp;
		double tijd, maxExpectedTAT;
		int max = processLHRRN.getSize();
		loper = 0;

		// EERSTE PROCES TOEVOEGEN:
		temp = processLHRRN.getProces(0);
		werk.add(temp);
		tijd = temp.getArrivaltime();
		temp.setWaittime(0);
		temp.setNorRuntime(1); // dit is nog niet de definitieve NorRuntime,
								// maar de exptected NorRuntime

		while ((loper < max) || (!werk.isEmpty())) {
			if ((loper < max) && (processLHRRN.getProces(loper).getArrivaltime() <= tijd)) {
				temp = processLHRRN.getProces(loper);
				werk.add(temp);
				temp.setWaittime(tijd - temp.getArrivaltime());
				temp.setNorRuntime((temp.getWaittime() + temp.getServicetime()) / temp.getServicetime());
				loper++;
			} else {
				maxExpectedTAT = 0;
				for (Process p : werk) {
					if (p.getNorRuntime() > maxExpectedTAT) {
						maxExpectedTAT = p.getNorRuntime();
						temp = p;
					}
				}
				tijd += temp.getServicetime();
				temp.setEndtime(tijd);
				temp.setRuntime(tijd - temp.getArrivaltime());
				temp.setNorRuntime(temp.echteGetRuntime() / temp.getServicetime());
				temp.setWaittime(temp.echteGetRuntime() - temp.getServicetime());
				werk.remove(temp);
			}
		}

		System.out.print("done, ");
		this.HRRN = processLHRRN;
	}

	public void berekenMLFB(int mode) { // mode 0: q=2^i, mode 1: q=i
		System.out.print("MLFB ");
		ProcessList temp = new ProcessList(processen);
		int max = temp.getSize();
		double tijdsbeurt;
		tijd = 0;
		loper = 0;

		List<List<Process>> queues = new ArrayList<List<Process>>();
		ArrayList<Process> removeLijst = new ArrayList<Process>();
		List<List<Process>> removeLijstQueues = new ArrayList<List<Process>>();

		for (int i = 0; i < 4; i++) {
			queues.add(new LinkedList<Process>());
		}
		
		for (int i = 0; i < 4; i++) {
			removeLijstQueues.add(new LinkedList<Process>());
		}


		while ((loper < max) || (!queues.get(0).isEmpty() || !queues.get(1).isEmpty() || !queues.get(2).isEmpty()
				|| !queues.get(3).isEmpty())) {
			// for(Process p : temp.getProcessenLijst()) System.out.print("[ " + p.getEndtime() + "], ");
			if (queues.get(0).isEmpty() && queues.get(1).isEmpty() && queues.get(2).isEmpty()
					&& queues.get(3).isEmpty()) {
				tijd = temp.getProces(loper).getArrivaltime();
				//System.out.println(tijd);
				queues.get(0).add(temp.getProces(loper));
				loper++;
			}
			//System.out.print(" " + loper + ", ");
			while (loper != max && tijd >= temp.getProcessenLijst().get((int) loper).getArrivaltime()) {
				if (tijd >= temp.getProcessenLijst().get((int) loper).getArrivaltime()) {
					queues.get(0).add(temp.getProces(loper));
					loper++;
				}
			}
			if(mode==1) { //mode 1: q=i
				for(int i=0; i<4; i++) {
					for(Process p : queues.get(i)) {
						//System.out.print(" {"+p.getPid()+"}, ");
						tijdsbeurt = Math.min(p.getRemainingServicetime(), i);
						if (p.getStarttime() == 0) {
							p.setStarttime(tijd);
							p.setRemainingServicetime(p.getServicetime());
						}
						tijd += tijdsbeurt;
						//System.out.println(tijd);
						p.setRemainingServicetime(p.getRemainingServicetime() - tijdsbeurt);
						if (p.getRemainingServicetime() == 0) {
							p.setEndtime(tijd);
							removeLijst.add(p);
						}
						
						if(i<3) { //na 1 keer het te berekenen naar volgende queue?? (denk ik?)
							queues.get(i+1).add(p);
							removeLijstQueues.get(i).add(p);
						}
					}
					
					for(Process p : removeLijstQueues.get(i)) {
						queues.get(i).remove(p);
					}
					
					for (Process p : removeLijst) {
						//werk.remove(p);
						if(queues.get(3).contains(p)) queues.get(3).remove(p); //omgekeerde volgorde -> snelst?
						else if(queues.get(2).contains(p)) queues.get(2).remove(p);
						else if(queues.get(1).contains(p)) queues.get(1).remove(p);
						else if(queues.get(0).contains(p)) queues.get(0).remove(p);
					}
				}
			} else if(mode==0) { // mode 0: q=2^i
				for(int i=0; i<4; i++) {
					for(Process p : queues.get(i)) {
						tijdsbeurt = Math.min(p.getRemainingServicetime(), Math.pow(2, i));
						if (p.getStarttime() == 0) {
							p.setStarttime(tijd);
							p.setRemainingServicetime(p.getServicetime());
						}
						tijd += tijdsbeurt;
						p.setRemainingServicetime(p.getRemainingServicetime() - tijdsbeurt);
						if (p.getRemainingServicetime() == 0) {
							p.setEndtime(tijd);
							removeLijst.add(p);
						}
						
						if(i<3) { //na 1 keer het te berekenen naar volgende queue?? (denk ik?)
							queues.get(i+1).add(p);
							queues.get(i).remove(p);
						}
					}
					
					for (Process p : removeLijst) {
						//werk.remove(p);
						if(queues.get(3).contains(p)) queues.get(3).remove(p); //omgekeerde volgorde -> snelst?
						else if(queues.get(2).contains(p)) queues.get(2).remove(p);
						else if(queues.get(1).contains(p)) queues.get(1).remove(p);
						else if(queues.get(0).contains(p)) queues.get(0).remove(p);
					}
				}
			}

		}
		// checksum = 0;
		//
		// for (Process p : processLMLFB.getProcessenLijst()) {
		// checksum += p.getArrivaltime();
		// }
		//
		// processLMLFB.zetRemainingTerug(); // remainingServicetime stond nog
		// op 0
		// // door berekenRR()
		// int huidigePrioriteit = 1;
		// double tijdsBeurt;
		// tijd = processLMLFB.getProces(0).getArrivaltime();
		// loper = 1;
		// grootteList = processLMLFB.getSize();
		// boolean mogelijksNieuwProces;
		//
		// LinkedList<Process> prioriteit1 = new LinkedList<Process>();
		// LinkedList<Process> prioriteit2 = new LinkedList<Process>();
		// LinkedList<Process> prioriteit3 = new LinkedList<Process>();
		// LinkedList<Process> prioriteit4 = new LinkedList<Process>();
		// ArrayList<LinkedList<Process>> queues = new
		// ArrayList<LinkedList<Process>>();
		// queues.add(prioriteit1);
		// queues.add(prioriteit2);
		// queues.add(prioriteit3);
		// queues.add(prioriteit4);
		//
		// int[] tijdsBeurten = { 1, 2, 3, 4 };
		// if (mode == 0) {
		// tijdsBeurten[2] = 4;
		// tijdsBeurten[3] = 8;
		// }
		// prioriteit1.add(processLMLFB.getProces(0));
		//
		// while ((loper < grootteList) || !prioriteit1.isEmpty() ||
		// !prioriteit2.isEmpty() || !prioriteit3.isEmpty()
		// || !prioriteit4.isEmpty()) {
		// if (queues.get(huidigePrioriteit - 1).isEmpty()) {
		// if (huidigePrioriteit != 4) {
		// huidigePrioriteit++;
		// } else if (loper < grootteList) {// als er in geen enkele queue
		// // nog een taak zit =>
		// // tijdssprong
		// tijd = processLMLFB.getProces(loper).getArrivaltime();
		// prioriteit1.add(processLMLFB.getProces(loper));
		// huidigePrioriteit = 1;
		// loper++;
		// }
		// }
		//
		// else { // als er een proces gevonden is
		// huidigProces = queues.get(huidigePrioriteit - 1).get(0);
		// tijdsBeurt = Math.min(huidigProces.getRemainingServicetime(),
		// tijdsBeurten[huidigePrioriteit - 1]);
		// tijd += tijdsBeurt;
		// huidigProces.pasRemainingServicetimeAan(tijdsBeurt);
		// if (huidigProces.getRemainingServicetime() == 0) {// als een
		// // proces
		// // klaar is
		// huidigProces.setEndtime(tijd);
		// huidigProces.setRuntime(tijd - huidigProces.getArrivaltime());
		// huidigProces.setNorRuntime((double) huidigProces.echteGetRuntime() /
		// huidigProces.getServicetime());
		// huidigProces.setWaittime(huidigProces.echteGetRuntime() -
		// huidigProces.getServicetime());
		// queues.get(huidigePrioriteit - 1).remove(huidigProces);
		// } else if (huidigePrioriteit != 4) {// als het laatste proces
		// // nog niet klaar is
		// queues.get(huidigePrioriteit).add(huidigProces);
		// queues.get(huidigePrioriteit - 1).remove(huidigProces);
		// } else if (huidigePrioriteit == 4) { // proces weer achteraan in
		// // de rij plaatsen
		// prioriteit4.remove(huidigProces);
		// prioriteit4.add(huidigProces);
		// }
		//
		// // na iedere taak kijken of in prioriteit 1 nog een taak
		// // bijkomt:
		// mogelijksNieuwProces = true;
		// while ((mogelijksNieuwProces) && (loper < grootteList)) {
		// if (processLMLFB.getProces(loper).getArrivaltime() <= tijd) {
		// prioriteit1.add(processLMLFB.getProces(loper));
		// loper++;
		// huidigePrioriteit = 1; // indien geen nieuw process mag
		// // de huidige prioriteit blijven
		// // waar hij was
		// } else {
		// mogelijksNieuwProces = false;
		// }
		// }
		// }
		// }
		for (Process p : temp.getProcessenLijst()) {
			p.setRuntime(p.getEndtime() - p.getArrivaltime());
			p.setWaittime(p.echteGetRuntime() - p.getServicetime());
			if(p.getWaittime() != 0) p.setNorRuntime(p.echteGetRuntime() / p.getServicetime());
			else p.setNorRuntime(1);
		}
		System.out.println("done...");
		this.MLFB = temp;
	}

	public ProcessList getFCFS() {
		return FCFS;
	}

	public void setFCFS(ProcessList fCFS) {
		FCFS = fCFS;
	}

	public ProcessList getRR() {
		return RR;
	}

	public void setRR(ProcessList rR) {
		RR = rR;
	}

	public ProcessList getHRRN() {
		return HRRN;
	}

	public void setHRRN(ProcessList hRRN) {
		HRRN = hRRN;
	}

	public ProcessList getMLFB() {
		return MLFB;
	}

	public void setMLFB(ProcessList mLFB) {
		MLFB = mLFB;
	}

	public ProcessList getProcessen() {
		return processen;
	}

	public void setProcessen(ProcessList processen) {
		this.processen = processen;
	}

}
