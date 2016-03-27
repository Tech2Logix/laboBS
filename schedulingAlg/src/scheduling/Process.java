package scheduling;
import javax.xml.bind.annotation.*;

@XmlRootElement( name = "process")
public class Process implements Comparable<Process>{
	private double pid, arrivaltime, servicetime, remainingServicetime, starttime, endtime, runtime, waittime;
	double norRuntime;
	boolean klaar;
	//private double HRRNPriority;
	
	public Process(Process p) {
		this.pid = p.pid;
		this.arrivaltime = p.arrivaltime;
		this.servicetime = p.servicetime;
		this.remainingServicetime = p.remainingServicetime;
		this.starttime = p.starttime;
		this.endtime = p.endtime;
		this.runtime = p.runtime;
		this.waittime = p.waittime;
		this.norRuntime = p.norRuntime;
		this.klaar = p.klaar;
	}
	
	public double getStarttime() {
		return starttime;
	}

	public void setStarttime(double starttime) {
		this.starttime = starttime;
	}

	public boolean isKlaar() {
		return klaar;
	}

	public void setKlaar(boolean klaar) {
		this.klaar = klaar;
	}

	public void setRemainingServicetime(double remainingServicetime) {
		this.remainingServicetime = remainingServicetime;
	}

	public Process() {
	}
	
	public double getEndtime() {
		return endtime;
	}

	public void setEndtime(double tijd) {
		this.endtime = tijd;
	}
	
	public double echteGetRuntime() {//
		return runtime;
	}
	
	public double getRuntime() {
		runtime = servicetime + waittime;
		return runtime;
	}
	
	public void setRuntime(double t) {
		this.runtime=t;
	}

	public double getNorRuntime() {
		norRuntime = (double)(servicetime+waittime)/servicetime;
		//een getter dient wel niet om waarden aan te passen eh RHINO!!
		//ik vroeg getNorRuntime op terwijl mijn wachttijd op 0 stond waardoor ik altijd 1 kreeg...
		return norRuntime;
	}
	public double echteGetNorRuntime() {//
		return norRuntime;
	}
	
	public void setNorRuntime(double t) {
		this.norRuntime=t;
	}

	public double getWaittime() {
		return waittime;
	}

	public void setWaittime(double waittime) {
		this.waittime = waittime;
	}

	public double getPid() {
		return pid;
	}
	
	@XmlElement(name = "pid")
	public void setPid(double pid) {
		this.pid = pid;
	}

	public double getArrivaltime() {
		return arrivaltime;
	}
	
	@XmlElement(name = "arrivaltime")
	public void setArrivaltime(double arrivaltime) {
		this.arrivaltime = arrivaltime;
	}

	public double getServicetime() {
		return servicetime;
	}
	
	@XmlElement(name = "servicetime")
	public void setServicetime(double servicetime) {
		this.servicetime = servicetime;
		remainingServicetime = servicetime;
		//System.out.println(pid + " remainingSericeTime: "+remainingServicetime);
	}
	
	public double getRemainingServicetime(){
		return remainingServicetime;
	}
	
	public void pasRemainingServicetimeAan(double tijdsbeurt){
		remainingServicetime-=tijdsbeurt;
	}
	/*
	public void setHRRNPriority(double d) {
		this.HRRNPriority = d;
	}
	
	public double getHRRNPriority() {
		return HRRNPriority;
	}*/
	
	public int compareTo(Process compareProcess){
		return (int) (this.servicetime - compareProcess.servicetime);
	}
	
	public void setRemaining(){
		remainingServicetime = servicetime;
	}

	public void addRuntime(double timeSlices) {
		runtime += timeSlices;
	}
	
	public void print() {
        System.out.println("PID: " + pid);
        System.out.println("Arrival: " + arrivaltime);
		System.out.println("Service: " + servicetime
                + "\nStart: " + starttime
                + "\nEnd: " + endtime
                + "\nTAT: " + runtime
                + "\nnormTAT: "+ norRuntime
                + "\nwait: "+ waittime +"\n");

    }
	
}