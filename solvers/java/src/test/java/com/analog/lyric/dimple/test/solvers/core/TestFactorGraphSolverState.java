/*******************************************************************************
*   Copyright 2015 Analog Devices, Inc.
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

package com.analog.lyric.dimple.test.solvers.core;

import static com.analog.lyric.util.test.ExceptionTester.*;
import static java.util.Objects.*;
import static org.junit.Assert.*;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;

import com.analog.lyric.dimple.model.core.FactorGraph;
import com.analog.lyric.dimple.model.core.FactorGraphIterables;
import com.analog.lyric.dimple.model.factors.Factor;
import com.analog.lyric.dimple.model.variables.Bit;
import com.analog.lyric.dimple.model.variables.Discrete;
import com.analog.lyric.dimple.model.variables.Variable;
import com.analog.lyric.dimple.solvers.core.FactorGraphSolverState;
import com.analog.lyric.dimple.solvers.interfaces.ISolverFactor;
import com.analog.lyric.dimple.solvers.interfaces.ISolverFactorGraph;
import com.analog.lyric.dimple.solvers.interfaces.ISolverVariable;
import com.analog.lyric.dimple.test.DimpleTestBase;
import com.analog.lyric.dimple.test.dummySolver.DummyFactorGraph;
import com.analog.lyric.dimple.test.dummySolver.DummySolver;
import com.analog.lyric.util.test.Helpers;
import com.google.common.collect.Iterables;

/**
 * 
 * @since 0.08
 * @author Christopher Barber
 */
public class TestFactorGraphSolverState extends DimpleTestBase
{
	@Test
	public void test()
	{
		test(new FactorGraph());
		test(Helpers.MakeSimpleGraph("simple"));
		test(Helpers.MakeSimpleThreeLevelGraph());
	}
	
	private void test(FactorGraph fg)
	{
		DummyFactorGraph sfg = requireNonNull(fg.setSolverFactory(new DummySolver()));
		assertInvariants(sfg.solverState(), sfg);
	}
	
	
	private void assertInvariants(FactorGraphSolverState<?,?> state, ISolverFactorGraph sroot)
	{
		final FactorGraph root = sroot.getModelObject();
		assertSame(sroot, state.getSolverGraph());
		assertSame(root, state.getModelGraph());
		
		for (Variable variable :FactorGraphIterables.variables(root))
		{
			ISolverVariable svariable = state.getSolverVariableRecursive(variable, false);
			if (svariable != null)
			{
				assertSame(variable, svariable.getModelObject());
			}
		}
		
		for (Factor factor : FactorGraphIterables.factors(root))
		{
			ISolverFactor sfactor = state.getSolverFactorRecursive(factor, false);
			if (sfactor != null)
			{
				assertSame(factor, sfactor.getModelObject());
			}
		}
		
		
		Set<FactorGraph> graphs = new LinkedHashSet<>(FactorGraphIterables.subgraphs(root));
		assertSame(root, Iterables.getFirst(graphs, null));
		
		assertEquals(graphs.size(), state.getSolverSubgraphsRecursive().size());
		Set<ISolverFactorGraph> sgraphs = new LinkedHashSet<>(state.getSolverSubgraphsRecursive());
		assertEquals(graphs.size(), sgraphs.size());
		assertSame(sroot, Iterables.getFirst(sgraphs, null));
		
		for (ISolverFactorGraph sgraph : sgraphs)
		{
			FactorGraph graph = requireNonNull(sgraph.getModelObject());
			assert(graphs.contains(graph));
			
			if (graph != root)
			{
				assertSame(sgraph, state.getSolverSubgraphRecursive(graph, false));
				
				if (sgraph instanceof DummyFactorGraph)
				{
					DummyFactorGraph dsgraph = (DummyFactorGraph)sgraph;
					assertInvariants(dsgraph.solverState(), dsgraph);
				}
			}
			
			for (Variable var : graph.getOwnedVariables())
			{
				ISolverVariable svar = requireNonNull(state.getSolverVariableRecursive(var, false));
				assertSame(var, svar.getModelObject());
				if (graph == root)
				{
					assertSame(svar, state.getSolverVariable(var,false));
				}
			}

			Set<Variable> ownedVars = new LinkedHashSet<>(graph.getOwnedVariables());
			Set<ISolverVariable> ownedSVars = new LinkedHashSet<>(sgraph.getSolverVariables());
			assertEquals(ownedVars.size(), ownedSVars.size());
			for (ISolverVariable svar : ownedSVars)
			{
				assertTrue(ownedVars.contains(svar.getModelObject()));
			}
			
			for (Factor factor : graph.getOwnedFactors())
			{
				ISolverFactor sfactor = requireNonNull(state.getSolverFactorRecursive(factor, false));
				assertSame(factor, sfactor.getModelObject());
				if (graph == root)
				{
					assertSame(sfactor, state.getSolverFactor(factor, false));
				}
			}

			Set<Factor> ownedFactors = new LinkedHashSet<>(graph.getOwnedFactors());
			Set<ISolverFactor> ownedSFactors = new LinkedHashSet<>(sgraph.getSolverFactors());
			assertEquals(ownedFactors.size(), ownedSFactors.size());
			for (ISolverFactor sfactor : ownedSFactors)
			{
				assertTrue(ownedFactors.contains(sfactor.getModelObject()));
			}
		}
		
		Set<Variable> allVars = new LinkedHashSet<>(FactorGraphIterables.variables(root));
		Set<ISolverVariable> allSVars = new LinkedHashSet<>(state.getSolverVariablesRecursive());
		assertEquals(allVars.size(), allSVars.size());
		for (ISolverVariable svar : allSVars)
		{
			assertTrue(allVars.contains(svar.getModelObject()));
		}
		
		Set<Factor> allFactors = new LinkedHashSet<>(FactorGraphIterables.factors(root));
		Set<ISolverFactor> allSFactors = new LinkedHashSet<>(state.getSolverFactorsRecursive());
		assertEquals(allFactors.size(), allSFactors.size());
		for (ISolverFactor sfactor : allSFactors)
		{
			assertTrue(allFactors.contains(sfactor.getModelObject()));
		}
		
		Discrete looseVar = new Bit();
		looseVar.setName("looseVar");
		
		expectThrow(IllegalArgumentException.class, "The variable 'looseVar' does not belong to graph.",
			state, "getSolverVariable", looseVar, true);
		expectThrow(IllegalStateException.class, "Node 'looseVar' does not belong to a graph.",
			state, "getSolverVariableRecursive", looseVar, true);
		
		FactorGraph otherGraph = new FactorGraph();
		otherGraph.setName("otherGraph");
		Discrete a = new Bit();	a.setName("a");
		otherGraph.addVariables(a);
		
		expectThrow(IllegalArgumentException.class, "Cannot get solver graph for .*otherGraph.*",
			state, "getSolverVariableRecursive", a, true);
	}
}
