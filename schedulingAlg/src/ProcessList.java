import java.util.*;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "processlist")
public class ProcessList {
	private LinkedList<Process> processenLijst;
	
	public ProcessList(){	
		
		this.processenLijst = new LinkedList<Process>();
	}
	
	public ProcessList(ProcessList pl) {
		this.processenLijst = new LinkedList<Process>();
		for(Process p : pl.getProcessenLijst()) {
			Process x = new Process(p);
			processenLijst.add(x);
			//System.out.println("...");
		}
	}
	
	public LinkedList<Process> getProcessenLijst() {
		return processenLijst;
	}
	
	public Process getProces(double loper){
		return processenLijst.get((int) loper);
	}
	
	@XmlElement(name = "process")
	public void setProcessenLijst(LinkedList<Process> processenLijst) {
		this.processenLijst = processenLijst;
	}

	public int getSize() {
		return processenLijst.size();
	}
	
	public void sorteerOpServiceTijd(){
		Collections.sort(processenLijst);
	}
	
	public void addProcess(Process p){
		processenLijst.add(p);
	}
	
	public void deleteProcess(Process p) {
		processenLijst.remove(p);
	}
	
	public void zetRemainingTerug(){
		for (Process p: processenLijst){
			p.setRemaining();
		}
	}
	
	//voor testen
	public void printList(){
		int max=processenLijst.size();
		for(int i=0;i<max;i++){
			System.out.println(processenLijst.get(i).getServicetime());
		}
	}
	
}