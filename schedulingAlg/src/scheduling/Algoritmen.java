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
	private ProcessList processen, FCFS, RR2, RR4, RR8, HRRN, MLFB0, MLFB1;

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
		if (timeSlices == 2)
			this.RR2 = temp;
		else if (timeSlices == 4)
			this.RR4 = temp;
		else
			this.RR8 = temp;
	}

	public void berekenHRRN() {
		System.out.print("HRRN ");
		ProcessList processLHRRN = new ProcessList(processen);
		List<Process> werk = new ArrayList<Process>();
		tijd = 0;
		loper = 0;
		grootteList = processLHRRN.getSize();

		for (int i = 0; i < grootteList; i++) {
			Process temp = processLHRRN.getProces(i);
			if (werk.isEmpty() && tijd <= temp.getArrivaltime()) {
				tijd = temp.getArrivaltime();
				temp.setWaittime(tijd - temp.getArrivaltime());
				tijd += temp.getServicetime();
				temp.setEndtime(tijd);
			} else {
				loper = i; // for i-lus updaten
				while ((loper < grootteList) && (tijd > processLHRRN.getProces((int) loper).getArrivaltime())) {
					werk.add(processLHRRN.getProces(loper));
					loper++;
				}
				i = (int) (loper - 1);

				// blijkbaar is zo'n lambda uitdrukking nog iet handig :p
				Collections.sort(werk,
						(Process p1, Process p2) -> Double.compare(p2.getNorRuntime(), p1.getNorRuntime()));
				
				for (Process p : werk) // update waitTime
					p.setWaittime(tijd - p.getArrivaltime());
				
				while (werk.size() != 0) {
					Process uitvoeren = werk.get(0);
					werk.remove(0);
					tijd += uitvoeren.getServicetime();
					uitvoeren.setEndtime(tijd);
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

		processLMLFB.zetRemainingTerug(); // remainingServicetime stond nog op 0
											// door berekenRR()
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
				} else if (loper < grootteList) {// als er in geen enkele queue
													// nog een taak zit =>
													// tijdssprong
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
				if (huidigProces.getRemainingServicetime() == 0) {// als een
																	// proces
																	// klaar is
					huidigProces.setEndtime(tijd);
					huidigProces.setRuntime(tijd - huidigProces.getArrivaltime());
					huidigProces.setNorRuntime((double) huidigProces.echteGetRuntime() / huidigProces.getServicetime());
					huidigProces.setWaittime(huidigProces.echteGetRuntime() - huidigProces.getServicetime());
					queues.get(huidigePrioriteit - 1).remove(huidigProces);
				} else if (huidigePrioriteit != 4) {// als het laatste proces
													// nog niet klaar is
					queues.get(huidigePrioriteit).add(huidigProces);
					queues.get(huidigePrioriteit - 1).remove(huidigProces);
				} else if (huidigePrioriteit == 4) { // proces weer achteraan in
														// de rij plaatsen
					prioriteit4.remove(huidigProces);
					prioriteit4.add(huidigProces);
				}

				// na iedere taak kijken of in prioriteit 1 nog een taak
				// bijkomt:
				mogelijksNieuwProces = true;
				while ((mogelijksNieuwProces) && (loper < grootteList)) {
					if (processLMLFB.getProces(loper).getArrivaltime() <= tijd) {
						prioriteit1.add(processLMLFB.getProces(loper));
						loper++;
						huidigePrioriteit = 1; // indien geen nieuw process mag
												// de huidige prioriteit blijven
												// waar hij was
					} else {
						mogelijksNieuwProces = false;
					}
				}
			}
		}
		System.out.print("done...");
		if (mode == 0)
			this.MLFB0 = processLMLFB;
		else
			this.MLFB1 = processLMLFB;
	}

	public ProcessList getFCFS() {
		return FCFS;
	}

	public void setFCFS(ProcessList fCFS) {
		FCFS = fCFS;
	}

	public ProcessList getRR2() {
		return RR2;
	}

	public void setRR2(ProcessList rR2) {
		RR2 = rR2;
	}

	public ProcessList getRR4() {
		return RR4;
	}

	public void setRR4(ProcessList rR4) {
		RR4 = rR4;
	}

	public ProcessList getRR8() {
		return RR8;
	}

	public void setRR8(ProcessList rR8) {
		RR8 = rR8;
	}

	public ProcessList getMLFB0() {
		return MLFB0;
	}

	public void setMLFB0(ProcessList mLFB0) {
		MLFB0 = mLFB0;
	}

	public ProcessList getMLFB1() {
		return MLFB1;
	}

	public void setMLFB1(ProcessList mLFB1) {
		MLFB1 = mLFB1;
	}

	public ProcessList getHRRN() {
		return HRRN;
	}

	public void setHRRN(ProcessList hRRN) {
		HRRN = hRRN;
	}

	public ProcessList getProcessen() {
		return processen;
	}

	public void setProcessen(ProcessList processen) {
		this.processen = processen;
	}

}
