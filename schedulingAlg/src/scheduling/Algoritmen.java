package scheduling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class Algoritmen {
	private double tijd, loper, grootteList, bedieningsTijd, aankomstTijd;
	Process huidigProces;
	double checksum;

	public void berekenFCFS(ProcessList processen) {
		checksum = 0;

		for (Process p : processen.getProcessenLijst()) {
			checksum += p.getArrivaltime();
		}
		System.out.println(checksum);
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
			huidigProces.setNorRuntime((double) (tijd - aankomstTijd) / bedieningsTijd);
			huidigProces.setWaittime(tijd - aankomstTijd - bedieningsTijd);
			// System.out.println(loper);
			loper++;
		}
	}

	public ProcessList berekenRR(ProcessList processen, int timeSlices) {
		ArrayList<Process> werk = new ArrayList<Process>();
		ArrayList<Process> removeLijst = new ArrayList<Process>();
		ProcessList temp = new ProcessList(processen);
		double overigeBedieningstijd;
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
		return temp;
	}

	public ProcessList berekenHRRN(ProcessList processen) {
		List<Process> werk = new ArrayList<Process>();
		double tijd = 0;
		// boolean bezet = false;
		for (int i = 0; i < processen.getSize(); i++) {
			Process temp = processen.getProces(i);
			if (werk.size() == 0 && tijd <= temp.getArrivaltime()) {
				tijd = temp.getArrivaltime();
				temp.setWaittime(tijd - temp.getArrivaltime());
				tijd += temp.getServicetime();
				temp.setEndtime(tijd);
			} else {
				int j = i; // iets fucked up hier... Maar werkt wel blijkbaar,
							// probeersel van op trein.
				// Mischien toch iets duidelijker te proberen herschrijven..
				while ((j < processen.getSize()) && (tijd > processen.getProces(j).getArrivaltime())) {
					werk.add(processen.getProces(j));
					j++;
				}
				i = j - 1;

				for (Process p : werk) {  //update waitTime
					p.setWaittime(tijd - p.getArrivaltime());
				}

				//blijkbaar is zo'n lambda uitdrukking nog iet handig :p
				Collections.sort(werk,
						(Process p1, Process p2) -> Double.compare(p2.getNorRuntime(), p1.getNorRuntime()));
				
				while (werk.size() != 0) {
					Process uitvoeren = werk.get(0);
					werk.remove(0);
					tijd += uitvoeren.getServicetime();
					uitvoeren.setEndtime(tijd);
					/*for (Process p : werk) { //update waitTime
						p.setWaittime(tijd - p.getArrivaltime());
					}
					
					Collections.sort(werk,
							(Process p1, Process p2) -> Double.compare(p2.getNorRuntime(), p1.getNorRuntime()));
					*/
				}
			}
		}
		return processen;
	}

	public void berekenMLFB(ProcessList processen, int mode) { // mode 0: q=2^i
																// , mode 1: q=i
		checksum = 0;

		for (Process p : processen.getProcessenLijst()) {
			checksum += p.getArrivaltime();
		}
		System.out.println(checksum);
		processen.zetRemainingTerug(); // remainingServicetime stond nog op 0
										// door berekenRR()
		int huidigePrioriteit = 1;
		double tijdsBeurt;
		tijd = processen.getProces(0).getArrivaltime();
		loper = 1;
		grootteList = processen.getSize();
		boolean mogelijksNieuwProces;

		System.out.println(processen.getProces(0).getRemainingServicetime());

		List<Process> prioriteit1 = new LinkedList<Process>();
		List<Process> prioriteit2 = new LinkedList<Process>();
		List<Process> prioriteit3 = new LinkedList<Process>();
		List<Process> prioriteit4 = new LinkedList<Process>();
		List<List<Process>> queues = new ArrayList<List<Process>>();
		queues.add(prioriteit1);
		queues.add(prioriteit2);
		queues.add(prioriteit3);
		queues.add(prioriteit4);

		int[] tijdsBeurten = { 1, 2, 3, 4 };
		if (mode == 0) {
			tijdsBeurten[2] = 4;
			tijdsBeurten[3] = 8;
		}
		prioriteit1.add(processen.getProces(0));

		while (loper < grootteList) {
			System.out.println("huidige prioriteit: " + huidigePrioriteit);
			if (queues.get(huidigePrioriteit - 1).isEmpty()) {
				System.out.println("huidigePrioriteit " + huidigePrioriteit + " is leeg");
				if (huidigePrioriteit != 4) {
					huidigePrioriteit++;
				} else { // als er in geen enkele queue nog een taak zit =>
							// tijdssprong
					tijd = queues.get(huidigePrioriteit - 1).get((int) loper).getArrivaltime();
					huidigePrioriteit = 1;
					loper++;
				}
			}

			else {
				huidigProces = queues.get(huidigePrioriteit - 1).get(0);
				System.out.println("overblijvende service tijd " + huidigProces.getRemainingServicetime());
				tijdsBeurt = Math.min(huidigProces.getRemainingServicetime(), tijdsBeurten[huidigePrioriteit - 1]);
				tijd += tijdsBeurt;
				huidigProces.pasRemainingServicetimeAan(tijdsBeurt);
				System.out.println("overblijvende service tijd " + huidigProces.getRemainingServicetime());
				if (huidigProces.getRemainingServicetime() == 0) {
					System.out.println("test2");
					huidigProces.setEndtime(tijd);
					huidigProces.setRuntime(tijd = huidigProces.getArrivaltime());
					huidigProces.setNorRuntime(huidigProces.getRuntime() / huidigProces.getServicetime());
					huidigProces.setWaittime(huidigProces.getRuntime() - huidigProces.getServicetime());
					queues.get(huidigePrioriteit - 1).remove(huidigProces);
				} else if (huidigePrioriteit != 4) {
					System.out.println("test");
					queues.get(huidigePrioriteit).add(huidigProces);
					queues.get(huidigePrioriteit - 1).remove(huidigProces);
				}

				// na iedere taak kijken of in prioriteit 1 nog een taak
				// bijkomt:
				mogelijksNieuwProces = true;
				while (mogelijksNieuwProces) {
					if (processen.getProces(loper).getArrivaltime() <= tijd) {
						prioriteit1.add(processen.getProces(loper));
						loper++;
						huidigePrioriteit = 1; // indien geen nieuw process mag
												// de huidige prioriteit blijven
												// waar hij was
					}
				}
			}
		}
	}
}
