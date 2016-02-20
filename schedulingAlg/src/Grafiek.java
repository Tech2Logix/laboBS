import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class Grafiek extends ApplicationFrame{
	public Grafiek( String applicationTitle , String chartTitle, Percentiel p){
	      super(applicationTitle);
	      JFreeChart lineChart = ChartFactory.createLineChart(
	         chartTitle,
	         "genormaliseerde omlooptijd","bedieningstijd",
	         createDataset(p),
	         PlotOrientation.VERTICAL,
	         true,true,false);
	         
	      ChartPanel chartPanel = new ChartPanel( lineChart );
	      chartPanel.setPreferredSize( new java.awt.Dimension( 600 , 400 ) );
	      setContentPane( chartPanel );
	   }

	   private DefaultCategoryDataset createDataset(Percentiel p ){
	      DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
	      for (int i=0;i<100;i++){
	    	  dataset.addValue(p.getProces(i).getNorRuntime(), "nog invullen", ""+p.getProces(i).getServicetime());
	     }
	     return dataset;
	   }
	   
	   public void maakGrafiek(Percentiel p){
	      Grafiek chart = new Grafiek("genormaliseerde omlooptijd" ,"bedieningstijd", p);

	      chart.pack( );
	      RefineryUtilities.centerFrameOnScreen( chart );
	      chart.setVisible( true );
	      System.out.println("test");
	   }

}
