package listeners;

import java.awt.event.*;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import org.jfree.chart.ChartPanel;

import grafiek.Grafiek;
import grafiek.GrafiekWait;
import scheduling.Algoritmen;
import java.awt.*;

public class listener_XML1 implements ActionListener {
	private Algoritmen alg = new Algoritmen("processen10000.xml");;
	private CardLayout CL;
	private JPanel chartPanel, waitEnNorTime;
	
	public listener_XML1(JPanel XML1graph, CardLayout CL, JPanel chartPanel) {
		System.out.println("XML 10k set generated");
		this.CL = CL;
		this.chartPanel = chartPanel;
		waitEnNorTime = new JPanel();
		waitEnNorTime.setLayout(new BoxLayout(waitEnNorTime, 1));
		
		
		Grafiek g = new Grafiek("norRunTime", alg);
		ChartPanel chart = g.getChartPanel();
		waitEnNorTime.add(chart);
		
		System.out.println("Eerste grafiek done");
		GrafiekWait g2 = new GrafiekWait("waitTime", alg);
		ChartPanel chart2 = g2.getChartPanel();
		waitEnNorTime.add(chart2);
		
		XML1graph.add(waitEnNorTime);
		chartPanel.add("XML10000 dataset", XML1graph);
		
		System.out.println("Tweede grafiek done");
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		System.out.println("XML 10k set inserted");
		CL.show(chartPanel, "XML10000 dataset");
	}

}
