import javax.xml.bind.annotation.*;

@XmlRootElement( name = "process")
public class Process implements Comparable<Process>{
	private int pid, arrivaltime, servicetime, endtime, runtime, norRuntime, waittime;
	
	
	public int getEndtime() {
		return endtime;
	}

	public void setEndtime(int endtime) {
		this.endtime = endtime;
	}

	public int getRuntime() {
		runtime = servicetime + waittime;
		return runtime;
	}
	
	public void setRuntime(int t) {
		this.runtime=t;
	}

	public int getNorRuntime() {
		norRuntime = (servicetime+waittime)/servicetime;
		return norRuntime;
	}
	
	public void setNorRuntime(int t) {
		this.norRuntime=t;
	}

	public int getWaittime() {
		return waittime;
	}

	public void setWaittime(int waittime) {
		this.waittime = waittime;
	}

	public int getPid() {
		return pid;
	}
	
	@XmlElement(name = "pid")
	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getArrivaltime() {
		return arrivaltime;
	}
	
	@XmlElement(name = "arrivaltime")
	public void setArrivaltime(int arrivaltime) {
		this.arrivaltime = arrivaltime;
	}

	public int getServicetime() {
		return servicetime;
	}
	
	@XmlElement(name = "servicetime")
	public void setServicetime(int servicetime) {
		this.servicetime = servicetime;
	}
	
	public int compareTo(Process compareProcess){
		return this.servicetime - compareProcess.servicetime;
	}
	
}