import java.util.List;

import javax.xml.bind.annotation.*;

@XmlRootElement( name = "processlist" )
public class processList {
	List<process> processlist;
	
    @XmlElement( name = "process" )
    public void setCountries( List<process> processlist ){
        this.processlist = processlist;
    }
}
