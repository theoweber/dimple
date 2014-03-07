/*******************************************************************************
*   Copyright 2014 Analog Devices, Inc.
*
*   Licensed under the Apache License, Version 2.0 (the "License");
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*       http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
********************************************************************************/

package com.analog.lyric.dimple.test.model;

import static org.junit.Assert.*;

import java.util.Random;

import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.junit.Test;

import com.analog.lyric.dimple.model.core.FactorGraph;
import com.analog.lyric.dimple.model.domains.DiscreteDomain;
import com.analog.lyric.dimple.model.factors.Factor;
import com.analog.lyric.dimple.model.transform.FactorGraphTransformMap;
import com.analog.lyric.dimple.model.transform.FactorGraphTransformMap.AddedDeterministicVariable;
import com.analog.lyric.dimple.model.transform.JunctionTreeTransform;
import com.analog.lyric.dimple.model.values.Value;
import com.analog.lyric.dimple.model.variables.Discrete;
import com.analog.lyric.dimple.model.variables.VariableBase;
import com.analog.lyric.dimple.model.variables.VariableList;
import com.analog.lyric.dimple.solvers.gibbs.GibbsSolver;
import com.analog.lyric.dimple.solvers.gibbs.ISolverVariableGibbs;
import com.analog.lyric.dimple.solvers.gibbs.SFactorGraph;
import com.analog.lyric.util.misc.Misc;

/**
 * Tests for {@link JunctionTreeTransform}
 */
public class TestJunctionTreeTransform
{

	private final long _seed = new Random().nextLong();
	private final Random rand = new Random(_seed);
	private final RandomGraphGenerator _graphGenerator = new RandomGraphGenerator(rand);
	
	private static final DiscreteDomain d2 = DiscreteDomain.range(0, 1);
	private static final DiscreteDomain d3 = DiscreteDomain.range(0, 2);
	private static final DiscreteDomain d4 = DiscreteDomain.range(0, 3);
	
	@Test
	public void testTrivialLoop()
	{
		testGraph(_graphGenerator.buildTrivialLoop(d2));
	}
	
	@Test
	public void testTriangle()
	{
		testGraph(_graphGenerator.buildTriangle(d2));
		
	}
	
	@Test
	public void testGrid2()
	{
		testGraph(_graphGenerator.buildGrid(2, d2));
	}
	
	@Test
	public void testGrid3()
	{
		testGraph(_graphGenerator.buildGrid(3, d2, d3, d4));
	}
	
	@Test
	public void testGrid4()
	{
		testGraph(_graphGenerator.buildGrid(4, d2));
	}
	
	/**
	 * @see RandomGraphGenerator#buildStudentNetwork()
	 */
	@Test
	public void testStudentNetwork()
	{
		testGraph(_graphGenerator.buildStudentNetwork());
	}
	
	/*-----------------
	 * Helper methods
	 */
	
	/**
	 * Assert that source and target graphs in {@code transformMap} represent the same
	 * joint distribution down to some level of precision.
	 * 
	 * @param transformMap
	 */
	private void assertModelsEquivalent(FactorGraphTransformMap transformMap)
	{
		final FactorGraph source = transformMap.source();
		final FactorGraph target = transformMap.target();
		
		GibbsSolver gibbs = new GibbsSolver();
		SFactorGraph sourceGibbs = source.setSolverFactory(gibbs);
		SFactorGraph targetGibbs = target.setSolverFactory(gibbs);
		
		final int nSamples = 100;
		
		final double[] differences = new double[nSamples];
		for (int n = 0; n < nSamples; ++n)
		{
			// Generate a sample on the source graph
			source.solve();
			
			// Copy sample values to new graph
			for (VariableBase sourceVar : source.getVariables())
			{
				VariableBase targetVar = (VariableBase) transformMap.sourceToTarget().get(sourceVar);
				
				ISolverVariableGibbs sourceSVar = sourceGibbs.getSolverVariable(sourceVar);
				ISolverVariableGibbs targetSVar = targetGibbs.getSolverVariable(targetVar);

				targetSVar.setCurrentSample(sourceSVar.getCurrentSampleValue());
			}
			
			// Update values of added variables
			for (AddedDeterministicVariable added : transformMap.addedDeterministicVariables())
			{
				final ISolverVariableGibbs addedSVar = targetGibbs.getSolverVariable(added.getVariable());
				final Value value = addedSVar.getCurrentSampleValue();
				final Value[] inputs = new Value[added.getInputCount()];
				for (int i = inputs.length; --i>=0;)
				{
					final VariableBase inputVar = added.getInput(i);
					final ISolverVariableGibbs inputSVar = targetGibbs.getSolverVariable(inputVar);
					inputs[i] = inputSVar.getCurrentSampleValue();
				}
				
				added.updateValue(value, inputs);
			}
			
			// Compare the joint likelihoods
			final double sourcePotential = sourceGibbs.getTotalPotential();
			final double targetPotential = targetGibbs.getTotalPotential();
			final double difference = sourcePotential - targetPotential;
			if (Math.abs(difference) > 1e-10)
			{
				Misc.breakpoint();
			}
			differences[n] = difference;
		}
		
		double variance = new Variance().evaluate(differences);
		assertEquals(0.0, variance, 1e-10);
	}
	
	private void testGraph(FactorGraph model)
	{
		RuntimeException error = null;
		
		try
		{
			testGraphImpl(model);
		}
		catch (Throwable ex)
		{
			String msg = String.format("%s. TestJunctionTreeTransform._seed==%dL", ex.toString(), _seed);
			throw new RuntimeException(msg, ex);
		}
	}
	
	private void testGraphImpl(FactorGraph model)
	{
		JunctionTreeTransform jt = new JunctionTreeTransform().random(rand);
		FactorGraphTransformMap transformMap = jt.transform(model);
		
		if (transformMap.isIdentity())
		{
			assertTrue(model.isForest());
			return;
		}
		
		for (Factor factor : transformMap.target().getFactors())
		{
			// Name target factors as a debugging aid
			RandomGraphGenerator.labelFactor(factor);
		}
		assertTrue(transformMap.target().isTree());
		assertModelsEquivalent(transformMap);
		
		// Try with conditioning
		model.setSolverFactory(null);
		model.setSchedule(null);
		VariableList variables = model.getVariables();
		for (int i = 0; i < 100000; ++i)
		{
			VariableBase variable = variables.getByIndex(rand.nextInt(variables.size()));
			if (variable instanceof Discrete)
			{
				Discrete discrete = (Discrete)variable;
				discrete.setFixedValueIndex(rand.nextInt(discrete.getDomain().size()));
				break;
			}
		}
		jt.useConditioning(true);
		transformMap = jt.transform(model);
		for (Factor factor : transformMap.target().getFactors())
		{
			// Name target factors as a debugging aid
			RandomGraphGenerator.labelFactor(factor);
		}
		assertTrue(transformMap.target().isForest());
		assertModelsEquivalent(transformMap);
	}
	
}
