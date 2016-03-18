package scheduling;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
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
		// double overigeBedieningstijd;
		double tijdsbeurt;

		// begin:
		tijd = temp.getProces(0).getArrivaltime();
		loper = 1;

		grootteList = processen.getSize();
		werk.add(temp.getProces(0));
		int loperWerk = 0;
		int maxWerk = 1;
		while (loper < grootteList) {

			huidigProces = werk.get(loperWerk);
			loperWerk++;
			tijdsbeurt = Math.min(huidigProces.getRemainingServicetime(), timeSlices);
			tijd += tijdsbeurt;
			huidigProces.pasRemainingServicetimeAan(tijdsbeurt);

			if (huidigProces.getRemainingServicetime() == 0) { // als proces
																// voltooid is
				huidigProces.setEndtime(tijd);
				huidigProces.setRuntime(tijd - huidigProces.getArrivaltime());
				huidigProces.setWaittime(tijd - huidigProces.getArrivaltime() - huidigProces.getServicetime());
				huidigProces
						.setNorRuntime((double) (huidigProces.echteGetNorRuntime()) / huidigProces.getServicetime());

				removeLijst.add(huidigProces);
			}
			if ((loperWerk == maxWerk)) { // als we aan het einde van ons
											// toertje zitten moeten we kijken
											// of de volgende ondertussen ook al
											// mogelijk is
				if ((loper < grootteList) && (temp.getProces(loper).getArrivaltime() <= tijd)) {
					werk.add(temp.getProces(loper));
					loper++;
					maxWerk++;
				} else {
					for (Process p : removeLijst) {
						werk.remove(p);
						maxWerk--;
					}
					removeLijst.clear();
					loperWerk = 0; // we starten weer met het eerste proces in
									// werk
				}
			}

			if (werk.isEmpty()) {
				tijd = temp.getProces(loper).getArrivaltime();
				werk.add(temp.getProces(loper));
				loper++;
				maxWerk++;
			}
		}
		System.out.print("done, ");
		this.RR = temp;
	}

	public void berekenHRRN() {
		System.out.print("HRRN ");
		ProcessList processLHRRN = new ProcessList(processen);
		List<Process> werk = new ArrayList<Process>();
		double tijd = 0;
		// boolean bezet = false;
		for (int i = 0; i < processLHRRN.getSize(); i++) {
			Process temp = processLHRRN.getProces(i);
			if (werk.size() == 0 && tijd <= temp.getArrivaltime()) {
				tijd = temp.getArrivaltime();
				temp.setWaittime(tijd - temp.getArrivaltime());
				tijd += temp.getServicetime();
				temp.setEndtime(tijd);
			} else {
				int j = i; // iets fucked up hier... Maar werkt wel blijkbaar,
							// probeersel van op trein.
				// Mischien toch iets duidelijker te proberen herschrijven..
				while ((j < processLHRRN.getSize()) && (tijd > processLHRRN.getProces(j).getArrivaltime())) {
					werk.add(processLHRRN.getProces(j));
					j++;
				}
				i = j - 1;

				for (Process p : werk) { // update waitTime
					p.setWaittime(tijd - p.getArrivaltime());
				}

				// blijkbaar is zo'n lambda uitdrukking nog iet handig :p
				Collections.sort(werk,
						(Process p1, Process p2) -> Double.compare(p2.getNorRuntime(), p1.getNorRuntime()));

				while (werk.size() != 0) {
					Process uitvoeren = werk.get(0);
					werk.remove(0);
					tijd += uitvoeren.getServicetime();
					uitvoeren.setEndtime(tijd);
					/*
					 * for (Process p : werk) { //update waitTime
					 * p.setWaittime(tijd - p.getArrivaltime()); }
					 * 
					 * Collections.sort(werk, (Process p1, Process p2) ->
					 * Double.compare(p2.getNorRuntime(), p1.getNorRuntime()));
					 */
				}
			}
		}
		System.out.print("done, ");
		this.HRRN = processLHRRN;
	}

	public void berekenMLFB(int mode) { // mode 0: q=2^i, mode 1: q=i
		System.out.print("MLFB ");
		ProcessList processLMLFB = new ProcessList(processen);
		checksum = 0;

		for (Process p : processLMLFB.getProcessenLijst()) {
			checksum += p.getArrivaltime();
		}

		processLMLFB.zetRemainingTerug(); // remainingServicetime stond nog op 0 door berekenRR()
		int huidigePrioriteit = 1;
		double tijdsBeurt;
		tijd = processLMLFB.getProces(0).getArrivaltime();
		loper = 1;
		grootteList = processLMLFB.getSize();
		boolean mogelijksNieuwProces;

		LinkedList<Process> prioriteit1 = new LinkedList<Process>();
		LinkedList<Process> prioriteit2 = new LinkedList<Process>();
		LinkedList<Process> prioriteit3 = new LinkedList<Process>();
		LinkedList<Process> prioriteit4 = new LinkedList<Process>();
		ArrayList<LinkedList<Process>> queues = new ArrayList<LinkedList<Process>>();
		queues.add(prioriteit1);
		queues.add(prioriteit2);
		queues.add(prioriteit3);
		queues.add(prioriteit4);

		int[] tijdsBeurten = { 1, 2, 3, 4 };
		if (mode == 0) {
			tijdsBeurten[2] = 4;
			tijdsBeurten[3] = 8;
		}
		prioriteit1.add(processLMLFB.getProces(0));

		while ((loper < grootteList) || !prioriteit1.isEmpty() || !prioriteit2.isEmpty() || !prioriteit3.isEmpty()
				|| !prioriteit4.isEmpty()) {
			if (queues.get(huidigePrioriteit - 1).isEmpty()) {
				if (huidigePrioriteit != 4) {
					huidigePrioriteit++;
				} else if (loper < grootteList) {// als er in geen enkele queue nog een taak zit => tijdssprong
					tijd = processLMLFB.getProces(loper).getArrivaltime();
					prioriteit1.add(processLMLFB.getProces(loper));
					huidigePrioriteit = 1;
					loper++;
				}
			}

			else { // als er een proces gevonden is
				huidigProces = queues.get(huidigePrioriteit - 1).get(0);
				tijdsBeurt = Math.min(huidigProces.getRemainingServicetime(), tijdsBeurten[huidigePrioriteit - 1]);
				tijd += tijdsBeurt;
				huidigProces.pasRemainingServicetimeAan(tijdsBeurt);
				if (huidigProces.getRemainingServicetime() == 0) {// als een proces klaar is
					huidigProces.setEndtime(tijd);
					huidigProces.setRuntime(tijd - huidigProces.getArrivaltime());
					huidigProces.setNorRuntime((double) huidigProces.echteGetRuntime() / huidigProces.getServicetime());
					huidigProces.setWaittime(huidigProces.echteGetRuntime() - huidigProces.getServicetime());
					queues.get(huidigePrioriteit - 1).remove(huidigProces);
				} else if (huidigePrioriteit != 4) {// als het laatste proces nog niet klaar is
					queues.get(huidigePrioriteit).add(huidigProces);
					queues.get(huidigePrioriteit - 1).remove(huidigProces);
				} else if (huidigePrioriteit == 4) { // proces weer achteraan in de rij plaatsen
					prioriteit4.remove(huidigProces);
					prioriteit4.add(huidigProces);
				}

				// na iedere taak kijken of in prioriteit 1 nog een taak bijkomt:
				mogelijksNieuwProces = true;
				while ((mogelijksNieuwProces) && (loper < grootteList)) {
					if (processLMLFB.getProces(loper).getArrivaltime() <= tijd) {
						prioriteit1.add(processLMLFB.getProces(loper));
						loper++;
						huidigePrioriteit = 1; // indien geen nieuw process mag de huidige prioriteit blijven waar hij was
					} else {
						mogelijksNieuwProces = false;
					}
				}
			}
		}
		System.out.println("done...");
		this.MLFB = processLMLFB;
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
