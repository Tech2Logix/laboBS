import java.io.*;

import javax.xml.bind.*;

import org.jfree.ui.RefineryUtilities;

public class Main {

	public static void main(String[] args) {
		ProcessList processenLijst=new ProcessList();
		//lees xml bestand in
		try {
			File file = new File("processen10000.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(ProcessList.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			processenLijst = (ProcessList) jaxbUnmarshaller.unmarshal(file);
			System.out.println(processenLijst);
			System.out.println(processenLijst.getSize());
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		
		//genereer grafiek
		Grafiek g = new Grafiek("Test", processenLijst);
		g.pack();
        RefineryUtilities.centerFrameOnScreen(g);
        g.setVisible(true);
		
	}
}