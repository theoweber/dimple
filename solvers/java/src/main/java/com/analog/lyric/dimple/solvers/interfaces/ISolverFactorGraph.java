/*******************************************************************************
*   Copyright 2012 Analog Devices, Inc.
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

package com.analog.lyric.dimple.solvers.interfaces;

import java.util.Collection;

import org.eclipse.jdt.annotation.Nullable;

import com.analog.lyric.dimple.factorfunctions.core.IFactorTable;
import com.analog.lyric.dimple.model.core.EdgeState;
import com.analog.lyric.dimple.model.core.FactorGraph;
import com.analog.lyric.dimple.model.factors.Factor;
import com.analog.lyric.dimple.model.repeated.BlastFromThePastFactor;
import com.analog.lyric.dimple.model.variables.Variable;
import com.analog.lyric.util.misc.Internal;
import com.analog.lyric.util.misc.Matlab;

public interface ISolverFactorGraph	extends ISolverNode
{
	// FIXME review new names!
	
	/*---------------------
	 * ISolverNode methods
	 */
	
	/**
	 * Returns the factor graph represented by this solver graph.
	 */
	@Override
	public FactorGraph getModelObject();

	/*----------------------------
	 * ISolverFactorGraph methods
	 */
	
	public @Nullable ISolverEdgeState getSolverEdge(EdgeState edge);
	
	public @Nullable ISolverEdgeState getSolverEdge(int edgeIndex);
	
	/**
	 * @return solver-specific factor representing the given model factor or else null.
	 */
	public @Nullable ISolverFactor getSolverFactor(Factor factor);

	public Collection<? extends ISolverFactor> getSolverFactors();
	
	/**
	 * Get solver factor for given factor.
	 * 
	 * @param factor must a factor whose parent is the same as the model object ({@link #getModelObject()}).
	 * @param create if true, the solver will attempt to create the solver factor if it has not already done so
	 * @since 0.08
	 */
	public @Nullable ISolverFactor getSolverFactor(Factor factor, boolean create);
	
	/**
	 * Get solver factor with given local index in this solver graph, if it exists.
	 * <p>
	 * This method does not force instantiation of the solver factor.
	 * <p>
	 * @param index is the local index of the factor within its graph, i.e. the index portion of the factor's
	 * local id.
	 * @since 0.08
	 */
	public @Nullable ISolverFactor getSolverFactorByIndex(int index);
	
	public ISolverFactor getSolverFactorForEdge(EdgeState edge);
	
	public Collection<? extends ISolverFactor> getSolverFactorsRecursive();
	
	/**
	 * Returns solver node with given local id in the graph, if it exists.
	 * <p>
	 * This method does not force instantiation of solver node.
	 * @since 0.08
	 */
	public @Nullable ISolverNode getSolverNodeByLocalId(int localId);
	
	public ISolverFactorGraph getSolverSubgraph(FactorGraph subgraph);
	
	public @Nullable ISolverFactorGraph getSolverSubgraph(FactorGraph subgraph, boolean create);
	
	/**
	 * Get solver subgraph with given local index in this solver graph, if it exists.
	 * <p>
	 * This method does not force instantiation of the subgraph.
	 * <p>
	 * @param index is the local index of the subgraph within its parent graph, i.e. the index portion of the subgraph's
	 * local id.
	 * @since 0.08
	 */
	public @Nullable ISolverFactorGraph getSolverSubgraphByIndex(int index);
	
	public Collection<? extends ISolverFactorGraph> getSolverSubgraphs();
	
	public Collection<? extends ISolverFactorGraph> getSolverSubgraphsRecursive();

	/**
	 * @return solver-specific variable representing the given model variable or else null.
	 */
	public ISolverVariable getSolverVariable(Variable var);
	
	/**
	 * Get solver variable for given variable.
	 * 
	 * @param variable must a variable whose parent is the same as the model object ({@link #getModelObject()}).
	 * @param create if true, the solver will attempt to create the solver variable if it has not already done so
	 * @since 0.08
	 */
	public @Nullable ISolverVariable getSolverVariable(Variable variable, boolean create);
	
	/**
	 * Get solver variable with given local index in this solver graph, if it exists.
	 * <p>
	 * This method does not force instantiation of the solver variable.
	 * <p>
	 * @param index is the local index of the variable within its graph, i.e. the index portion of the variable's
	 * local id.
	 * @since 0.08
	 */
	public @Nullable ISolverVariable getSolverVariableByIndex(int index);
	
	public Collection<? extends ISolverVariable> getSolverVariables();
	
	public Collection<? extends ISolverVariable> getSolverVariablesRecursive();
	
	public ISolverBlastFromThePastFactor createBlastFromThePast(BlastFromThePastFactor factor);

	/**
	 * Removes solver state object with given edge index from this graph.
	 * <p>
	 * This only removes the state entry for this graph; i.e., if this is a boundary edge the corresponding
	 * entry in the other solver graph will not be removed. This is only intended to be used in implementing
	 * {@link #removeSolverEdge(EdgeState)}.
	 * <p>
	 * @param edgeIndex
	 * @since 0.08
	 * @category internal
	 */
	@Internal
	public void removeSolverEdge(int edgeIndex);
	
	/**
	 * Removes solver state object for given edge in its solver graph(s).
	 * <p>
	 * @since 0.08
	 * @category internal
	 */
	@Internal
	public void removeSolverEdge(EdgeState edge);
	
	/**
	 * Removes solver factor from its parent graph.
	 * <p>
	 * @param factor must have this object as its {@linkplain ISolverNode#getParentGraph() parent}.
	 * @since 0.08
	 * @category internal
	 */
	@Internal
	public void removeSolverFactor(ISolverFactor factor);
	
	/**
	 * Removes solver graph from its parent graph.
	 * <p>
	 * @param subgraph must have this object as its {@linkplain ISolverNode#getParentGraph() parent}.
	 * @since 0.08
	 * @category internal
	 */
	@Internal
	public void removeSolverGraph(ISolverFactorGraph subgraph);
	
	/**
	 * Removes solver variable from its parent graph.
	 * <p>
	 * @param variable must have this object as its {@linkplain ISolverNode#getParentGraph() parent}.
	 * @since 0.08
	 * @category internal
	 */
	@Internal
	public void removeSolverVariable(ISolverVariable variable);
	
	/**
	 * Indicates whether or not the specified factor is available only as a custom factor.
	 * @param factorName
	 * @return True only for custom factors that do not have a corresponding FactorFunction of the same name
	 */
	@Matlab
	public boolean customFactorExists(String factorName);
	public void solve();
	public void solveOneStep();
	public void continueSolve();
	public void startSolveOneStep();
	public void startContinueSolve();
	public void iterate();
	public void iterate(int numIters);
	public boolean isSolverRunning();
	public void interruptSolver();
	public void startSolver();
	public void setNumIterations(int numIterations);
	public int getNumIterations();
	public double getBetheFreeEnergy();
	public void estimateParameters(IFactorTable [] tables,int numRestarts,int numSteps, double stepScaleFactor);
	public void baumWelch(IFactorTable[] tables,int numRestarts,int numSteps);
	public void moveMessages(ISolverNode other);
	public void postAdvance();
	public void postAddFactor(Factor f);
	public void postSetSolverFactory();

	/**
	 * Returns the name of a solver-specific MATLAB wrapper function that should be invoked from
	 * MATLAB to do the solve. The value or existence of the function is allowed to change depending
	 * on the parameters to the solver. The MATLAB function should take two positional arguments:
	 * the MATLAB FactorGraph object and the instance of this interface.
	 * 
	 * @return name of MATLAB function used to do actual solve in MATLAB interface or null if there is none.
	 */
	@Matlab
	public @Nullable String getMatlabSolveWrapper();
	
	/**
	 * @category internal
	 */
	@Internal
	public void recordDefaultSubgraphSolver(FactorGraph subgraph);
	
	public void useMultithreading(boolean use);
	public boolean useMultithreading();
	
	
	// For solver to indicate whether or not schedules must include all edges in the graph
	// TODO: This could be replaced by function that actually does the checking, which could be solver specific
	public boolean checkAllEdgesAreIncludedInSchedule();
	
	/**
	 * @category internal
	 * @param parent
	 */
	@Internal
	public void setParent(ISolverFactorGraph parent);
}
