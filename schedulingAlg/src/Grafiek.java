import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.chart.axis.*;
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
	        
	        ProcessList werk = new ProcessList(pl);
			alg.berekenFCFS(werk);
	        Percentiel p;
			p=new Percentiel(werk); //opgelet, proceslijst is nu gesorteerd volgens servicetijd ipv. volgens aankomsttijd
	        final XYSeries series1 = new XYSeries("FCFS");
	        for(int i=0; i<100; i++) {
	        	//System.out.println(i+"   NorRuntime: " + p.getProces(i).getNorRuntime() + "      ServTime:" + p.getProces(i).getServicetime());
	        	series1.addOrUpdate(p.getProces(i).getServicetime(), p.getProces(i).getNorRuntime());
	        }
	        System.out.println("FSFC done");
	               
	        
	        
	        ProcessList werkRR = new ProcessList(pl);
			werkRR=alg.berekenRR(werkRR,8);
			p=new Percentiel(werkRR); //opgelet, proceslijst is nu gesorteerd volgens servicetijd ipv. volgens aankomsttijd
	        final XYSeries series2 = new XYSeries("RR");
	        for(int i=0; i<100; i++) {
	        	//System.out.println(i+"   NorRuntime: " + p.getProces(i).echteGetNorRuntime() + "      ServTime:" + p.getProces(i).getServicetime());
	        	series2.add(p.getProces(i).getServicetime(), p.getProces(i).echteGetNorRuntime());
	        }
	        System.out.println("RR done");
	        
	        ProcessList werkHRRN = new ProcessList(pl);
			ProcessList solution = alg.berekenHRRN(werkHRRN);
			Percentiel pHRRN = new Percentiel(solution); //opgelet, proceslijst is nu gesorteerd volgens servicetijd ipv. volgens aankomsttijd
	        final XYSeries series3 = new XYSeries("HRRN");
	        for(int i=0; i<100; i++) {
	        	//System.out.println(i+"   NorRuntime: " + p.getProces(i).getNorRuntime() + "      ServTime:" + p.getProces(i).getServicetime());
	        	series3.add(pHRRN.getProces(i).getServicetime(), pHRRN.getProces(i).getNorRuntime());
	        }
	        System.out.println("HRRN done");
	        
	        werk = new ProcessList(pl);
			//alg.berekenMLFB(werk,4);
			p=new Percentiel(werk); //opgelet, proceslijst is nu gesorteerd volgens servicetijd ipv. volgens aankomsttijd
	        final XYSeries series4 = new XYSeries("MFM");
	        for(int i=0; i<100; i++) {
	        	//System.out.println(i+"   NorRuntime: " + p.getProces(i).getNorRuntime() + "      ServTime:" + p.getProces(i).getServicetime());
	        	series4.add(p.getProces(i).getServicetime(), p.getProces(i).getNorRuntime());
	        }
	        System.out.println("MLFB done");

	        final XYSeriesCollection dataset = new XYSeriesCollection();
	        dataset.addSeries(series1);
	        dataset.addSeries(series2);
	        dataset.addSeries(series3);
	        //dataset.addSeries(series4);
	                
	        return dataset;
	    }
	    
	    private JFreeChart createChart(final XYDataset dataset) {
	        final JFreeChart chart = ChartFactory.createXYLineChart(
	            "Scheduling algoritmes",      						// chart title
	            "Bedieningstijd",                 	    // x axis label
	            "Genormaliseerde omlooptijd",                      				// y axis label
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
	        
	        /*LogAxis logAxis = new LogAxis("Genormaliseerde omlooptijd"); //test...
	        logAxis.setMinorTickMarksVisible(true);
	        
	        logAxis.setAutoRange(true);
	        
	        plot.setRangeAxis(logAxis);
	        */
	        
	        //ValueAxis vaxis = new LogAxis("Genormaliseerde omlooptijd");
	        NumberAxis domein = (NumberAxis) plot.getDomainAxis();
	        domein.setRange(0.00, 100.00);
	        
	        
	        
	        //final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
	        
	        
	        final XYSplineRenderer renderer = new XYSplineRenderer(); //smoother?
	        renderer.setSeriesShapesVisible(0, false);
	        renderer.setSeriesShapesVisible(1, true);
	        renderer.setSeriesShapesVisible(2, false);
	        renderer.setSeriesShapesVisible(3, false);
	        plot.setRenderer(renderer);
	        

	        // change the auto tick unit selection to integer units only...
	        //final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
	        //rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	        // OPTIONAL CUSTOMISATION COMPLETED.
	                
	        return chart;
	    }
}
