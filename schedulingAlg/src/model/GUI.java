package model;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.jfree.chart.ChartPanel;

import grafiek.Grafiek;
import grafiek.GrafiekWait;
import listeners.listener_XML1;
import listeners.listener_XML2;
import listeners.listener_XML3;
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
	private JScrollPane scrPane;

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
	private ChartPanel chart, chart2;
	public GUI(ProcessList processenLijst) {
		p = new JPanel();
		ptop = new JPanel();
		pbottem = new JPanel();

		getContentPane().add(ptop, BorderLayout.NORTH);
		getContentPane().add(pbottem, BorderLayout.SOUTH);
		setSize(850, 1030);
		
		lblNewLabel = new JLabel("3ELICTI - 2015/2016 - Michiel Dhont & Rhino Van Boxelaere");
		pbottem.add(lblNewLabel);
		
		lblNewLabel2 = new JLabel();
		lblNewLabel2.setText("scheduling strategieën");
		ptop.add(lblNewLabel2);

		Grafiek g = new Grafiek("titelGrafiek", processenLijst);
		chart = g.getChartPanel();
		chartPanel = new JPanel();
		chartPanel.setLayout(new BoxLayout(chartPanel, 1));
		chartPanel.add(chart, BorderLayout.NORTH);
		
		GrafiekWait g2 =new GrafiekWait("titelGrafiek", processenLijst);
		chart2 = g2.getChartPanel();
		chartPanel.add(chart2, BorderLayout.SOUTH);
		

		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		menu_dataSet = new JMenu("Data set");
		menuBar.add(menu_dataSet);

		menuItem_XML1 = new JMenuItem("xml10000 dataset");
		menuItem_XML1.addActionListener(new listener_XML1(p));
		menu_dataSet.add(menuItem_XML1);

		menuItem_XML2 = new JMenuItem("xml20000 dataset");
		menuItem_XML2.addActionListener(new listener_XML2(p));
		menu_dataSet.add(menuItem_XML2);

		menuItem_XML5 = new JMenuItem("xml50000 dataset");
		menuItem_XML5.addActionListener(new listener_XML3(p));
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

		p.add(chartPanel, BorderLayout.SOUTH);
		
		//this.getContentPane().add(p);
		scrPane = new JScrollPane(p);
		scrPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.getContentPane().add(scrPane);
		
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
	}

}
