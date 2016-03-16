package grafiek;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import scheduling.Algoritmen;
import scheduling.Percentiel;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.awt.Color;

public class GrafiekWait extends ApplicationFrame {
	private static final long serialVersionUID = -5826676505576415011L;
	String xAs, yAs, grafiekTitle, appTitle;
	ChartPanel chartPanel1;
	Algoritmen alg;
	Percentiel p;
	String applicationTitle;

	public GrafiekWait(String applicationTitle, Algoritmen alg) {
		super(applicationTitle);
		this.applicationTitle = applicationTitle;
		this.alg = alg;
		
		final XYDataset dataset = createDataset();
		final JFreeChart chart = createChart(dataset);
		chartPanel1 = new ChartPanel(chart);
		chartPanel1.setPreferredSize(new java.awt.Dimension(800, 450));
		setContentPane(chartPanel1);
	}
	
	public ChartPanel getChartPanel() {
		return this.chartPanel1;
	}

	private XYDataset createDataset() {
		
		p = new Percentiel(alg.getFCFS());
		final XYSeries series1 = new XYSeries("FCFS");
		for (int i = 0; i < 100; i++) {
			series1.addOrUpdate(p.getProces(i).getServicetime(), p.getProces(i).getWaittime());
		}
		System.out.print("FSFC done, ");
		
		

		p = new Percentiel(alg.getRR());
		final XYSeries series2 = new XYSeries("RR");
		for (int i = 0; i < 100; i++) {
			series2.add(p.getProces(i).getServicetime(), p.getProces(i).getWaittime());
		}
		System.out.print("RR done, ");

		
		
		Percentiel pHRRN = new Percentiel(alg.getHRRN());
		final XYSeries series3 = new XYSeries("HRRN");
		for (int i = 0; i < 100; i++) {
			series3.add(pHRRN.getProces(i).getServicetime(), pHRRN.getProces(i).getWaittime());
		}
		System.out.print("HRRN done, ");

		
		
		p = new Percentiel(alg.getMLFB());
		final XYSeries series4 = new XYSeries("MLFB");
		for (int i = 0; i < 100; i++) {
			series4.add(p.getProces(i).getServicetime(), p.getProces(i).getWaittime());
		}
		System.out.println("MLFB done... ");

		
		
		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);
		dataset.addSeries(series2);
		dataset.addSeries(series3);
		dataset.addSeries(series4);

		return dataset;
	}

	private JFreeChart createChart(final XYDataset dataset) {
		final JFreeChart chart = ChartFactory.createXYLineChart(applicationTitle,
				"Bedieninstijd", // x axis label
				"WachtT  tijd", // y axis label
				dataset, // data
				PlotOrientation.VERTICAL, true, // include legend
				true, // tooltips
				false // urls
		);

		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
		chart.setBackgroundPaint(Color.white);

		// get a reference to the plot for further customisation...
		final XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);


		final XYSplineRenderer renderer = new XYSplineRenderer(); // smoother?
		renderer.setSeriesShapesVisible(0, false);
		renderer.setSeriesShapesVisible(1, false);
		renderer.setSeriesShapesVisible(2, false);
		renderer.setSeriesShapesVisible(3, false);
		plot.setRenderer(renderer);

		// change the auto tick unit selection to integer units only...
		// final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		// rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		// OPTIONAL CUSTOMISATION COMPLETED.

		return chart;
	}
}
