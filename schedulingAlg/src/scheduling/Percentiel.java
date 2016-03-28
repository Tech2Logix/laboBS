package scheduling;

import java.util.*;

public class Percentiel {
	private LinkedList<Process> percentielen;

	public Percentiel() {
	}

	public Percentiel(ProcessList processenLijst) {

		processenLijst.sorteerOpServiceTijd();
		// processenLijst.priSntList(); //test

		percentielen = new LinkedList<Process>();
		int nProcessenPerPercentiel = processenLijst.getSize() / 100;
		Process p;
		int bedieningstijd = 0, genorOmlooptijd = 0, wachttijd = 0;
		for (int i = 0; i < 100; i++) { // per percentiel
			bedieningstijd = 0; genorOmlooptijd = 0; wachttijd = 0;
			for (int j = 0; j < nProcessenPerPercentiel; j++) {
				bedieningstijd += processenLijst.getProces(i * nProcessenPerPercentiel + j).getServicetime();
				genorOmlooptijd += processenLijst.getProces(i * nProcessenPerPercentiel + j).getNorRuntime();
				wachttijd += processenLijst.getProces(i * nProcessenPerPercentiel + j).getWaittime();
			}
			p = new Process();
			p.setServicetime((double) bedieningstijd / nProcessenPerPercentiel);
			p.setNorRuntime((double) genorOmlooptijd / nProcessenPerPercentiel);
			p.setWaittime((double) wachttijd / nProcessenPerPercentiel);
			percentielen.add(p);
		}

	}

	public LinkedList<Process> getPercentielList() {
		return percentielen;
	}

	public Process getProces(int i) {
		return percentielen.get(i);
	}

	public void printList() { // voor testen
		int max = percentielen.size();
		for (int i = 0; i < max; i++) {
			System.out.println(percentielen.get(i).getServicetime());
		}
	}
}
