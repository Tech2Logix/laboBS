import java.io.*;
import java.util.*;

import javax.xml.bind.*;

//testpush? aangekomen!


public class Main {

	public static void main(String[] args) {
		ProcessList processenLijst=new ProcessList();
		try {
			File file = new File("processen20000.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(ProcessList.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			processenLijst = (ProcessList) jaxbUnmarshaller.unmarshal(file);
			System.out.println(processenLijst);
			System.out.println(processenLijst.getSize());
		} catch (JAXBException e) {
			e.printStackTrace();
		}
				
		//uitvoeren FCFS
		Algoritmen al=new Algoritmen();
		al.berekenFCFS(processenLijst);
		
	}
}