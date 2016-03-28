package listeners;

import java.awt.event.*;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import org.jfree.chart.ChartPanel;

import grafiek.Grafiek;
import grafiek.GrafiekWait;
import scheduling.Algoritmen;
import java.awt.*;

public class listener_XML2 implements ActionListener {
	private Algoritmen alg = new Algoritmen("processen20000.xml");;
	private CardLayout CL;
	private JPanel chartPanel, waitEnNorTime;
	
	public listener_XML2(JPanel XML1graph, CardLayout CL, JPanel chartPanel) {
		System.out.println();
		System.out.print("XML 20k set generating... ");
		this.CL = CL;
		this.chartPanel = chartPanel;
		waitEnNorTime = new JPanel();
		waitEnNorTime.setLayout(new BoxLayout(waitEnNorTime, 1));
		
		alg.berekenFCFS();
		alg.berekenRR(2);
		alg.berekenRR(4);
		alg.berekenRR(8);
		alg.berekenHRRN();
		alg.berekenMLFB(0);
		alg.berekenMLFB(1);
		System.out.println();

		System.out.print("Eerste grafiek genereren... ");
		Grafiek g = new Grafiek("norRunTime", alg);
		ChartPanel chart = g.getChartPanel();
		waitEnNorTime.add(chart);
		

		System.out.print("Tweede grafiek genereren... ");
		GrafiekWait g2 = new GrafiekWait("waitTime", alg);
		ChartPanel chart2 = g2.getChartPanel();
		waitEnNorTime.add(chart2);
		
		XML1graph.add(waitEnNorTime);
		chartPanel.add("XML20000 dataset", XML1graph);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		System.out.println("XML 20k set activated");
		CL.show(chartPanel, "XML20000 dataset");
	}

}
