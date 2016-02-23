
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

	
	public void berekenRR(ProcessList processen) {
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
			huidigProces.setNorRuntime(((double)(tijd - aankomstTijd)) / bedieningsTijd);
			huidigProces.setWaittime(tijd - aankomstTijd - bedieningsTijd);
			loper++;
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
			do {
				for (Process p : temp.getProcessenLijst()) {
					if (p.getArrivaltime() <= tijd) {
						werk.addProcess(p);
					} else
						break; // is de lijst gesorteerd op aankomsttijd...?
				}
				if (werk.getProcessenLijst().size() == 0)
					tijd++;
			} while (werk.getProcessenLijst().size() == 0);

			for (Process p : werk.getProcessenLijst()) temp.deleteProcess(p);
			
			if (werk.getSize() == 1) {
				// System.out.println("Werk size = 1?");
				huidigProces = werk.getProcessenLijst().getFirst();
			} else {
				// System.out.println("Werk size > 1?");
				double maxHRRNPriority = 0;
				for (Process p : werk.getProcessenLijst()) {
					double currentHRRN = (p.getRuntime() + (tijd - p.getArrivaltime())) / (p.getRuntime());
					if (currentHRRN > maxHRRNPriority) {
						next = p;
					}
				}
				huidigProces = next;
			}
			werk.deleteProcess(huidigProces);
			temp.deleteProcess(huidigProces);

			System.out.println(loper + " - " + huidigProces.getArrivaltime() + " - " + huidigProces.getServicetime() + " - "+temp.getSize() + " - "+tijd);
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
