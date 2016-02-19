import javax.xml.bind.annotation.*;

//@XmlType( propOrder = { "pid", "arrivaltime", "servicetime"} )

@XmlRootElement( name = "process" )

public class process {
	int pid, arrivaltime, servicetime;
	
	public process() {
		int pid=-1;
		arrivaltime=0;
		servicetime=0;
	}
	
	public process(int pid, int arrivaltime, int servicetime) {
		this.pid = pid;
		this.arrivaltime = arrivaltime;
		this.servicetime = servicetime;
	}
	

    @XmlElement (name = "pid")
	public void setPid(int pid) {
		this.pid = pid;
	}
	
    @XmlElement (name = "arrivaltime")
	public void setArrivaltime(int arrivaltime) {
		this.arrivaltime = arrivaltime;
	}
    
    @XmlElement (name = "servicetime")
	public void setServicetime(int servicetime) {
		this.servicetime = servicetime;
	}
}
