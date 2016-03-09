package model;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.jfree.chart.ChartPanel;

import grafiek.Grafiek;
//import listeners.*;
//import model.*;
import scheduling.ProcessList;

public class GUI extends JFrame {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7424162668964223288L;
	private JPanel p;
	private JPanel ptop;
	private JPanel pbottem;

	private JMenuBar menuBar;
	private JMenu menu_dataSet;
	private JMenuItem menuItem_XML1;
	private JMenuItem menuItem_XML2;
	private JMenuItem menuItem_XML5;

	private JMenu menu_instellingen;
	private JMenuItem menuItem_RR;
	private JMenuItem menuItem_MFM;
	
	private JLabel lblNewLabel;
	private JLabel lblNewLabel2;
	private JPanel chartPanel;
	private ChartPanel chart;
	public GUI(ProcessList processenLijst) {
		p = new JPanel();
		ptop = new JPanel();
		pbottem = new JPanel();

		getContentPane().add(ptop, BorderLayout.NORTH);
		getContentPane().add(pbottem, BorderLayout.SOUTH);

		lblNewLabel = new JLabel("3ELICTI - 2015/2016 - Michiel Dhont & Rhino Van Boxelaere");
		pbottem.add(lblNewLabel);
		
		lblNewLabel2 = new JLabel();
		lblNewLabel2.setText("Ingeladen data set: (todo...)");
		ptop.add(lblNewLabel2);

		Grafiek g = new Grafiek("titelGrafiek", processenLijst);
		chart = g.getChartPanel();
		chartPanel = new JPanel();
		chartPanel.setLayout(new BoxLayout(chartPanel, 1));
		chartPanel.add(chart);
		
		/**
		 * Om die wait time grafiek te importeren ->
		 * gewoon nieuwe Grafiek g ...
		 * nieuwe chart
		 * en dan chartPanel.add(chart);
		 */

		p.add(chartPanel);

		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		menu_dataSet = new JMenu("Data set");
		menuBar.add(menu_dataSet);

		menuItem_XML1 = new JMenuItem("xml10000 dataset");
		// menuItem_XML1.addActionListener(new readGeg(al, jl));
		menu_dataSet.add(menuItem_XML1);

		menuItem_XML2 = new JMenuItem("xml20000 dataset");
		// menuItem_XML2.addActionListener();
		menu_dataSet.add(menuItem_XML2);

		menuItem_XML5 = new JMenuItem("xml50000 dataset");
		// menuItem_XML5.addActionListener();
		menu_dataSet.add(menuItem_XML5);

		menu_instellingen = new JMenu("Instellingen");
		menu_instellingen.setMnemonic(KeyEvent.VK_V);
		menuBar.add(menu_instellingen);

		menuItem_RR = new JMenuItem("q instellen voor RR");
		// menuItem_RR.addActionListener();
		menu_instellingen.add(menuItem_RR);

		menuItem_MFM = new JMenuItem("q instellen voor MFM");
		// menuItem_MFM.addActionListener();
		menu_instellingen.add(menuItem_MFM);

		this.setTitle("Labo besturingssystemen: Uniprocessor scheduling algoritmhs");
		this.getContentPane().add(p);

		this.setBounds(200, 0, 1400, 600);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
	}

}
