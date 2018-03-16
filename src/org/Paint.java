package org;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;

public class Paint {

    Font paintFont = new Font("Times New Roman", Font.PLAIN, 12);

    public void onPaint(Graphics2D g, Point mousePos) {
		g.setFont(paintFont);
		g.setColor(Color.BLACK);
		g.fillRect(290, 340, 205, 128);
		g.setColor(Color.MAGENTA);
		g.drawOval(mousePos.x, mousePos.y, 3, 3);
		g.drawString("~ Apa's GE Looter ~", 295, 360);
		g.drawString("Runtime: " + calcRuntime(), 295, 380);
		g.drawString("Loots: " + State.i.loots + " (" + calcHourly(State.i.loots) + ")", 295, 400);
		g.drawString("Profit: " + coolFormat(State.i.totalProfit, 0) + " (" + coolFormat(calcHourly(State.i.totalProfit), 0) + ")", 295, 420);
		g.drawString("Currently " + State.i.state, 295, 440);
		if (State.i.itemToGet != null)
			g.drawString("After Some " + State.i.itemToGet.getName() + " (" + State.i.itemToGet.getAmount() + ")", 295, 460);
	}

	public int calcHourly(int v) {
		if (v == 0)
			return 0;
		return (int)((float)v / ((System.currentTimeMillis() - State.START_TIME) / 3600000f));
	}
	
	private static char[] c = new char[]{'k', 'm', 'b', 't'};
	
	/**
	* Recursive implementation, invokes itself for each factor of a thousand, increasing the class on each invokation.
	* @param n the number to format
	* @param iteration in fact this is the class from the array c
	* @return a String representing the number n formatted in a cool looking way.
	*/
	private static String coolFormat(double n, int iteration) {
	double d = ((long) n / 100) / 10.0;
	boolean isRound = (d * 10) %10 == 0;//true if the decimal part is equal to 0 (then it's trimmed anyway)
	return (d < 1000? //this determines the class, i.e. 'k', 'm' etc
	    ((d > 99.9 || isRound || (!isRound && d > 9.99)? //this decides whether to trim the decimals
	     (int) d * 10 / 10 : d + "" // (int) d * 10 / 10 drops the decimal
	     ) + "" + c[iteration]) 
	    : coolFormat(d, iteration+1));
	
	}
	
	public String calcRuntime() {
		int secsPassed = (int)((System.currentTimeMillis() - State.START_TIME) / 1000);
		int hours = secsPassed / 3600;
		secsPassed -= hours * 3600;
		int minutes = secsPassed / 60;
		secsPassed -= minutes * 60;
		return (hours < 10 ? "0" + hours : hours) + ":" + (minutes < 10 ? "0" + minutes : minutes) + ":" + (secsPassed < 10 ? "0" + secsPassed : secsPassed);
	}
}
