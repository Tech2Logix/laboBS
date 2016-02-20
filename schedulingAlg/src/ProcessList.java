import java.util.*;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "processlist")
public class ProcessList {
	private List<Process> processenLijst;
	
	public ProcessList(){	
	}
	
	public List<Process> getProcessenLijst() {
		return processenLijst;
	}
	
	public Process getProces(int i){
		return processenLijst.get(i);
	}
	
	@XmlElement(name = "process")
	public void setProcessenLijst(List<Process> processenLijst) {
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
	
	//voor testen
	public void printList(){
		int max=processenLijst.size();
		for(int i=0;i<max;i++){
			System.out.println(processenLijst.get(i).getServicetime());
		}
	}
}