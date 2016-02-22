import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.Color;

public class Grafiek extends ApplicationFrame{
	String xAs, yAs, grafiekTitle, appTitle;
	public Grafiek(String applicationTitle, ProcessList pl){
		  super(applicationTitle);
		  
	       final XYDataset dataset = createDataset(pl);
	       final JFreeChart chart = createChart(dataset);
	       final ChartPanel chartPanel = new ChartPanel(chart);
	       chartPanel.setPreferredSize(new java.awt.Dimension(1000, 700));
	       setContentPane(chartPanel);
	   }
	
	 private XYDataset createDataset(ProcessList pl) {
	        Algoritmen alg = new Algoritmen();
	        
	        ProcessList werk = pl;
			alg.berekenFCFS(pl);
			Percentiel p=new Percentiel(pl); //opgelet, proceslijst is nu gesorteerd volgens servicetijd ipv. volgens aankomsttijd
	        final XYSeries series1 = new XYSeries("FCFS");
	        for(int i=0; i<100; i++) {
	        	System.out.println(i+"   NorRuntime: " + p.getProces(i).getNorRuntime() + "      ServTime:" + p.getProces(i).getServicetime());
	        	series1.addOrUpdate(p.getProces(i).getNorRuntime(), p.getProces(i).getServicetime());
	        }
	        System.out.println("dOneeeeeeeeeeeee");
	        
	        werk = pl;
			alg.berekenRR(pl);
			p=new Percentiel(pl); //opgelet, proceslijst is nu gesorteerd volgens servicetijd ipv. volgens aankomsttijd
	        final XYSeries series2 = new XYSeries("RR");
	        for(int i=0; i<100; i++) {
	        	series2.add(p.getProces(i).getNorRuntime(), p.getProces(i).getServicetime());
	        }
	        
	        werk = pl;
			alg.berekenHRRN(pl);
			p=new Percentiel(pl); //opgelet, proceslijst is nu gesorteerd volgens servicetijd ipv. volgens aankomsttijd
	        final XYSeries series3 = new XYSeries("HRRN");
	        for(int i=0; i<100; i++) {
	        	series3.add(p.getProces(i).getNorRuntime(), p.getProces(i).getServicetime());
	        }
	        
	        werk = pl;
			alg.berekenMFM(pl);
			p=new Percentiel(pl); //opgelet, proceslijst is nu gesorteerd volgens servicetijd ipv. volgens aankomsttijd
	        final XYSeries series4 = new XYSeries("MFM");
	        for(int i=0; i<100; i++) {
	        	series4.add(p.getProces(i).getNorRuntime(), p.getProces(i).getServicetime());
	        }

	        final XYSeriesCollection dataset = new XYSeriesCollection();
	        dataset.addSeries(series1);
	        System.out.println("doneeeee v2");
	        dataset.addSeries(series2);
	        dataset.addSeries(series3);
	        dataset.addSeries(series4);
	                
	        return dataset;
	    }
	    
	    private JFreeChart createChart(final XYDataset dataset) {
	        final JFreeChart chart = ChartFactory.createXYLineChart(
	            "Scheduling algoritmes",      						// chart title
	            "Genormaliseerde omlooptijd",                 	    // x axis label
	            "Bedieningstijd",                      				// y axis label
	            dataset,                  							// data
	            PlotOrientation.VERTICAL,
	            true,                     							// include legend
	            true,                     							// tooltips
	            false                     							// urls
	        );

	        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
	        chart.setBackgroundPaint(Color.white);
	        
	        // get a reference to the plot for further customisation...
	        final XYPlot plot = chart.getXYPlot();
	        plot.setBackgroundPaint(Color.lightGray);
	        plot.setDomainGridlinePaint(Color.white);
	        plot.setRangeGridlinePaint(Color.white);
	        
	        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
	        renderer.setSeriesShapesVisible(0, false);
	        renderer.setSeriesShapesVisible(1, false);
	        renderer.setSeriesShapesVisible(2, false);
	        renderer.setSeriesShapesVisible(3, false);
	        plot.setRenderer(renderer);

	        // change the auto tick unit selection to integer units only...
	        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
	        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	        // OPTIONAL CUSTOMISATION COMPLETED.
	                
	        return chart;
	    }
}
