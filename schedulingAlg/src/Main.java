import java.io.*;
import java.util.*;

import javax.xml.bind.*;


public class Main {

	public static void main(String[] args) {
		try {
			File file = new File("processen20000.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(ProcessList.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			ProcessList processenLijst = (ProcessList) jaxbUnmarshaller.unmarshal(file);
			System.out.println(processenLijst);
			System.out.println(processenLijst.getSize());
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}