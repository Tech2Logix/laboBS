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
			processenLijst.add(p);
			//System.out.println("...");
		}
	}
	
	public LinkedList<Process> getProcessenLijst() {
		return processenLijst;
	}
	
	public Process getProces(int i){
		return processenLijst.get(i);
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
	
	//voor testen
	public void printList(){
		int max=processenLijst.size();
		for(int i=0;i<max;i++){
			System.out.println(processenLijst.get(i).getServicetime());
		}
	}
}