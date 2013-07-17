package com.analog.lyric.dimple.examples;

import com.analog.lyric.dimple.model.Bit;
import com.analog.lyric.dimple.model.FactorGraph;

public class Nested {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		
		////////////////////////////////////// 
		// Define 4 bit xor from two 3 bit xors 
		////////////////////////////////////// 
		Bit [] b = new Bit[4];
		for (int i = 0; i < b.length; i++)
			b[i] = new Bit();
		
		FactorGraph xorGraph = new FactorGraph(b);
		
		Bit c = new Bit(); 
		ThreeBitXor xd = new ThreeBitXor();
		
		xorGraph.addFactor(xd,b[0],b[1],c); 
		xorGraph.addFactor(xd,b[2],b[3],c);
		////////////////////////////////////// 
		// Create graph for 6 bit code 
		////////////////////////////////////// 
		Bit [] d = new Bit[6];
		for (int i = 0; i < d.length; i++)
			d[i] = new Bit();

		FactorGraph myGraph = new FactorGraph(d); 
		myGraph.addFactor(xorGraph,d[0],d[1],d[2],d[4]); 
		myGraph.addFactor(xorGraph,d[0],d[1],d[3],d[5]);
		////////////////////////////////////// 
		// Set input and Solve 
		////////////////////////////////////// 
		double [] inputs = new double [] {.75, .6, .9, .1, .2, .9};
		for (int i = 0; i < inputs.length; i++)
			d[i].setInput(inputs[i]);
		myGraph.solve();
		for (int i = 0; i < d.length; i++)
			System.out.println(d[i].getValue());

	}

}
