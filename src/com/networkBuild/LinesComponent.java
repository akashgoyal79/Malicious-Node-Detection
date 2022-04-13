package com.networkBuild;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.NodeProperty.Node;

@SuppressWarnings("serial")
public class LinesComponent extends JComponent {

	private int padding = 25;
	private Color red = new Color(255, 0, 0, 255);
	private Color green = new Color(100, 250, 100, 180);
	private Color pointColor = new Color(100, 100, 100, 180);
	private Color gridColor = new Color(200, 200, 200, 200);
	private Color fontColor = new Color(0, 0, 0, 255);
	private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
	private int pointWidth = 4;
	private int numberDivisions = 12;
	private List<Point> graphPoints = new ArrayList<>();
	private List<Point> route = new ArrayList<>();
	private List<Point> oldRoute = new ArrayList<>();
	List<String> tags = new ArrayList<>();
	
	static LinesComponent comp;
	
	public LinesComponent() {}
	
	public LinesComponent(ArrayList<Node> list){
		for(Node n : list){
			graphPoints.add(new Point((int)n.x + 20, (int)n.y + 20));
		}
	}
	
	public LinesComponent getInstance(){
		if(comp == null)
			comp = new LinesComponent();
		return comp;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);		

		// draw white background
		g2.setColor(Color.WHITE);
		g2.fillRect(padding, padding, getWidth() - (2 * padding),
				getHeight() - 2 * padding);
		
		// draw scores
		drawScores(g2);
		
		g2.setColor(pointColor);
		g2.setStroke(GRAPH_STROKE);
		for (int i = 0; i < graphPoints.size(); i++) {
			int x1 = graphPoints.get(i).x + padding;
			int y1 = graphPoints.get(i).y + padding;
			g2.fillOval(x1, y1, 8, 8);
		}
		FontMetrics metrics = g2.getFontMetrics();
		if(oldRoute.size() > 1){
			g2.setColor(red);
			g2.setStroke(GRAPH_STROKE);
			int x1, y1, x2, y2;
			for(int i=1;i<oldRoute.size();i++){
				x1 = oldRoute.get(i-1).x + padding + 3;
				y1 = oldRoute.get(i-1).y + padding + 3;
				x2 = oldRoute.get(i).x + padding + 3;
				y2 = oldRoute.get(i).y + padding + 3;
				g2.setColor(red);
				g2.drawLine(x1, y1, x2, y2);
				g2.setColor(fontColor);
				g2.setFont(new Font("Default", Font.PLAIN, 24));
				String xLabel = "";
				for(Point p : graphPoints)
					if(p.x == oldRoute.get(i-1).x && p.y == oldRoute.get(i-1).y){
						xLabel = "" + graphPoints.indexOf(p);
					}
				int labelWidth = metrics.stringWidth(xLabel);
				g2.drawString(xLabel, x1 - labelWidth / 2 - 2, y1 + metrics.getHeight() + 8);
				
				if(i == oldRoute.size()-1){
					g2.setColor(fontColor);
					g2.setFont(new Font("Default", Font.PLAIN, 24));
					for(Point p : graphPoints)
						if(p.x == oldRoute.get(i).x && p.y == oldRoute.get(i).y){
							xLabel = "" + graphPoints.indexOf(p);
						}
					labelWidth = metrics.stringWidth(xLabel);
					g2.drawString(xLabel, x2 - labelWidth / 2 - 2, y2 + metrics.getHeight() + 8);
				}
			}
			g2.setColor(Color.BLACK);
		}
		
		if(route.size() > 1){
			Color newc=(oldRoute.size()>1)?green:red;
			g2.setColor(newc);
			g2.setStroke(GRAPH_STROKE);
			int x1, y1, x2, y2;
			for(int i=1;i<route.size();i++){
				x1 = route.get(i-1).x + padding + 3;
				y1 = route.get(i-1).y + padding + 3;
				x2 = route.get(i).x + padding + 3;
				y2 = route.get(i).y + padding + 3;
				g2.setColor(newc);
				g2.drawLine(x1, y1, x2, y2);
				g2.setColor(fontColor);
				g2.setFont(new Font("Default", Font.PLAIN, 24));
				String xLabel = "";
				for(Point p : graphPoints)
					if(p.x == route.get(i-1).x && p.y == route.get(i-1).y){
						xLabel = "" + graphPoints.indexOf(p);
					}
				int labelWidth = metrics.stringWidth(xLabel);
				g2.drawString(xLabel, x1 - labelWidth / 2 - 2, y1 + metrics.getHeight() + 8);
				
				if(i == route.size()-1){
					g2.setColor(fontColor);
					g2.setFont(new Font("Default", Font.PLAIN, 24));
					for(Point p : graphPoints)
						if(p.x == route.get(i).x && p.y == route.get(i).y){
							xLabel = "" + graphPoints.indexOf(p);
						}
					labelWidth = metrics.stringWidth(xLabel);
					g2.drawString(xLabel, x2 - labelWidth / 2 - 2, y2 + metrics.getHeight() + 8);
				}
			}
			g2.setColor(Color.BLACK);
		}
		
		
	}
	
	private void drawScores(Graphics2D g2){
		g2.setColor(Color.BLACK);

		// create hatch marks and grid lines for x and y axis.
		for (int i = 0; i < numberDivisions + 1; i++) {
			int x0 = padding;
			int x1 = pointWidth + padding;
			int y0 = getHeight()
					- ((i * (getHeight() - padding * 2)) / numberDivisions + padding);
			int y1 = y0;
			g2.drawLine(x0, y0, x1, y1);
			g2.setColor(gridColor);
			g2.drawLine(padding + 1 + pointWidth, y0, getWidth() - padding, y1);
			g2.setColor(Color.BLACK);
			
			x0 = i*(getWidth() - padding * 2) / numberDivisions + padding;
			x1 = x0;
			y0 = getHeight() - pointWidth - padding - 1;
			y1 = getHeight() - padding - 1;
			g2.drawLine(x0, y0, x1, y1);
			g2.setColor(gridColor);
			g2.drawLine(x0, padding, x0, getHeight() - padding);
			g2.setColor(Color.BLACK);
		}
	}
	
	public void drawPath(ArrayList<Integer> path){

		if(route.size() > 0){
			for(int i=0;i<route.size();i++){
				oldRoute.add(new Point(route.get(i).x, route.get(i).y));
			}
		}
		route.clear();
		int x, y;
		for(int i=0;i<path.size();i++){
			x = graphPoints.get(path.get(i)).x;
			y = graphPoints.get(path.get(i)).y;			
			route.add(new Point(x, y));
		}
		this.repaint();
	}
	
	public static void perform(ArrayList<Node> nodes){
		int length=600,width=600;
		JFrame testFrame = new JFrame();
		testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		testFrame.setLocation((screen.width-width)/2, (screen.height-length)/2);
		comp = new LinesComponent(nodes);
		
		comp.setPreferredSize(new Dimension(length, width));
		testFrame.getContentPane().add(comp, BorderLayout.CENTER);
		JPanel buttonsPanel = new JPanel();
		testFrame.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
		testFrame.pack();
		testFrame.setVisible(true);
	}
}