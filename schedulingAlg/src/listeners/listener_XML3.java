package listeners;

import java.awt.event.*;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.jfree.chart.ChartPanel;

import grafiek.Grafiek;
import grafiek.GrafiekWait;
import scheduling.ProcessList;

import java.awt.*;

public class listener_XML3 implements ActionListener {
	private JPanel chartPanel;
	private ChartPanel chart, chart2;
	
	public listener_XML3(JPanel p) {
		p.removeAll();
		
		ProcessList processenLijst=new ProcessList();
		try {
			File file = new File("processen50000.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(ProcessList.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			processenLijst = (ProcessList) jaxbUnmarshaller.unmarshal(file);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		Grafiek g = new Grafiek("titelGrafiek", processenLijst);
		chart = g.getChartPanel();
		chartPanel = new JPanel();
		chartPanel.setLayout(new BoxLayout(chartPanel, 1));
		chartPanel.add(chart, BorderLayout.NORTH);
		
		GrafiekWait g2 =new GrafiekWait("titelGrafiek", processenLijst);
		chart2 = g2.getChartPanel();
		chartPanel.add(chart2, BorderLayout.SOUTH);
		
		p.add(chartPanel, BorderLayout.SOUTH);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		System.out.println("XML 10k set applied");
		
	}

}
