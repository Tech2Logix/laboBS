import java.util.*;
import java.io.*;
import javax.xml.bind.*;
//testpush? aangekomen!

public class Main {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		//processList originalPool = new processList();
		
		try {
			File file = new File( "C:\\Users\\Rhino\\Documents\\processen10000.xml" );
			JAXBContext jaxbContext = JAXBContext.newInstance( processList.class );
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			processList originalPool = (processList)jaxbUnmarshaller.unmarshal( file );
			
			System.out.println( originalPool );
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
