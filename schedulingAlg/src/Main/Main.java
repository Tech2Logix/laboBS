package Main;
import java.io.*;
import model.*;

import javax.xml.bind.*;

import org.jfree.ui.RefineryUtilities;

import grafiek.Grafiek;
import scheduling.ProcessList;

public class Main {

	public static void main(String[] args) {
		ProcessList processenLijst=new ProcessList();
		//lees xml bestand in
		try {
			File file = new File("processen10000.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(ProcessList.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			processenLijst = (ProcessList) jaxbUnmarshaller.unmarshal(file);
			//System.out.println(processenLijst);
			//System.out.println(processenLijst.getSize());
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		
		//genereer grafiek
		GUI gui = new GUI(processenLijst);
		
	}
}