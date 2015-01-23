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

package com.analog.lyric.dimple.solvers.core.proxy;

import java.util.Collection;

import org.eclipse.jdt.annotation.Nullable;

import com.analog.lyric.dimple.exceptions.DimpleException;
import com.analog.lyric.dimple.factorfunctions.core.IFactorTable;
import com.analog.lyric.dimple.model.core.FactorGraph;
import com.analog.lyric.dimple.model.core.FactorGraphEdgeState;
import com.analog.lyric.dimple.model.factors.Factor;
import com.analog.lyric.dimple.model.variables.Variable;
import com.analog.lyric.dimple.solvers.core.SFactorGraphBase;
import com.analog.lyric.dimple.solvers.interfaces.ISolverFactor;
import com.analog.lyric.dimple.solvers.interfaces.ISolverFactorGraph;
import com.analog.lyric.dimple.solvers.interfaces.ISolverNode;
import com.analog.lyric.dimple.solvers.interfaces.ISolverVariable;

/**
 * @since 0.05
 */
public abstract class ProxySolverFactorGraph<SFactor extends ISolverFactor, SVariable extends ISolverVariable,
	SEdge, Delegate extends ISolverFactorGraph>
	extends SFactorGraphBase<SFactor, SVariable, SEdge>
	implements IProxySolverNode<ISolverFactorGraph>
{
	/*--------------
	 * Construction
	 */
	
	/**
	 * @param modelFactorGraph
	 */
	protected ProxySolverFactorGraph(FactorGraph modelFactorGraph)
	{
		super(modelFactorGraph);
	}

	/*---------------------
	 * ISolverNode methods
	 */
	
	@Override
	public double getBetheEntropy()
	{
		return requireDelegate("getBetheEntropy").getBetheEntropy();
	}

	@Override
	public double getInternalEnergy()
	{
		return requireDelegate("getInternalEnergy").getInternalEnergy();
	}

	@Override
	public Object getInputMsg(int portIndex)
	{
		throw unsupported("getInputMsg");
	}

	@Override
	public Object getOutputMsg(int portIndex)
	{
		throw unsupported("getOutputMsg");
	}

	@Override
	public double getScore()
	{
		return requireDelegate("getScore").getScore();
	}

	@Override
	public void initialize()
	{
		clearFlags();
		requireDelegate("initialize").initialize();
	}

	@Override
	public void moveMessages(ISolverNode other, int thisPortNum, int otherPortNum)
	{
		throw unsupported("moveMessages");
	}

	@Override
	public void resetEdgeMessages(int portNum)
	{
		throw unsupported("resetEdgeMessages");
	}

	@Override
	public void setInputMsg(int portIndex, Object obj)
	{
		throw unsupported("setInputMsg");
	}

	@Override
	public void setOutputMsg(int portIndex, Object obj)
	{
		throw unsupported("setOutputMsg");
	}

	@Override
	public void setInputMsgValues(int portIndex, Object obj)
	{
		throw unsupported("setInputMsgValues");
	}

	@Override
	public void setOutputMsgValues(int portIndex, Object obj)
	{
		throw unsupported("setOutputMsgValues");
	}

	@Override
	public void update()
	{
		requireDelegate("update").update();
	}

	@Override
	public void updateEdge(int outPortNum)
	{
		throw unsupported("updateEdge");
	}

	/*----------------------------
	 * ISolverFactorGraph methods
	 */

	@Override
	public void baumWelch(IFactorTable[] tables, int numRestarts, int numSteps)
	{
		throw unsupported("baumWelch");
	}

	@Override
	public void continueSolve()
	{
		requireDelegate("continueSolve").continueSolve();
	}

	@SuppressWarnings("null")
	@Override
	public SEdge createEdgeState(FactorGraphEdgeState edge)
	{
		return null;
	}
	
	@Override
	public boolean customFactorExists(String funcName)
	{
		ISolverFactorGraph sfg = getDelegate();
		return sfg != null && sfg.customFactorExists(funcName);
	}

	@Override
	public void estimateParameters(IFactorTable[] tables, int numRestarts, int numSteps, double stepScaleFactor)
	{
		throw unsupported("estimateParameters");
	}

	@Override
	public double getBetheFreeEnergy()
	{
		return requireDelegate("getBetheFreeEnergy").getBetheFreeEnergy();
	}

	@Override
	public @Nullable String getMatlabSolveWrapper()
	{
		ISolverFactorGraph sfg = getDelegate();
		return sfg != null ? sfg.getMatlabSolveWrapper() : null;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Default implementation returns null.
	 */
	@Override
	public @Nullable SEdge getSolverEdge(FactorGraphEdgeState edge)
	{
		return _state.getSolverEdge(edge, true);
	}
	
	@Override
	public @Nullable ISolverFactor getSolverFactor(Factor factor)
	{
		return _state.getSolverFactorRecursive(factor, true);
	}
	
	@Override
	public @Nullable SFactor getSolverFactor(Factor factor, boolean create)
	{
		return _state.getSolverFactor(factor, create);
	}
	
	@Override
	public Collection<SFactor> getSolverFactors()
	{
		return _state.getSolverFactors();
	}
	
	@Override
	public Collection<ISolverFactor> getSolverFactorsRecursive()
	{
		return _state.getSolverFactorsRecursive();
	}
	
	@Override
	public @Nullable ISolverFactorGraph getSolverSubgraph(FactorGraph subgraph)
	{
		return _state.getSolverSubgraphRecursive(subgraph, true);
	}
	
	@Override
	public Collection<? extends ISolverFactorGraph> getSolverSubgraphs()
	{
		return _state.getSolverSubgraphs();
	}

	@Override
	public Collection<? extends ISolverFactorGraph> getSolverSubgraphsRecursive()
	{
		return _state.getSolverSubgraphsRecursive();
	}

	@Override
	public @Nullable ISolverVariable getSolverVariable(Variable var)
	{
		return _state.getSolverVariableRecursive(var, true);
	}
	
	@Override
	public @Nullable SVariable getSolverVariable(Variable variable, boolean create)
	{
		return _state.getSolverVariable(variable, create);
	}
	
	@Override
	public Collection<SVariable> getSolverVariables()
	{
		return _state.getSolverVariables();
	}
	
	@Override
	public Collection<ISolverVariable> getSolverVariablesRecursive()
	{
		return _state.getSolverVariablesRecursive();
	}
	
	@Override
	public void setNumIterations(int numIterations)
	{
		if (numIterations != _numIterations)
		{
			_numIterations = numIterations;
			ISolverFactorGraph delegate = getDelegate();
			if (delegate != null)
			{
				delegate.setNumIterations(numIterations);
			}
		}
	}

	@Override
	public int getNumIterations()
	{
		return _numIterations;
	}

	@Override
	public boolean hasEdgeState()
	{
		return false;
	}
	
	@Override
	public void interruptSolver()
	{
		ISolverFactorGraph sfg = getDelegate();
		if (sfg != null)
		{
			sfg.interruptSolver();
		}
	}

	@Override
	public boolean isSolverRunning()
	{
		ISolverFactorGraph sfg = getDelegate();
		return sfg != null && sfg.isSolverRunning();
	}

	@Override
	public void iterate()
	{
		requireDelegate("iterate").iterate();
	}

	@Override
	public void iterate(int numIters)
	{
		requireDelegate("iterate").iterate(numIters);
	}

	@Override
	public void moveMessages(ISolverNode other)
	{
		requireDelegate("moveMessages").moveMessages(other);
	}

	@Override
	public void solve()
	{
		requireDelegate("solve").solve();
	}

	@Override
	public void solveOneStep()
	{
		requireDelegate("solveOneSte").solveOneStep();
	}

	@Override
	public void startContinueSolve()
	{
		requireDelegate("startContinueSolve").startContinueSolve();
	}

	@Override
	public void startSolveOneStep()
	{
		requireDelegate("startSolveOneStep").startSolveOneStep();
	}

	@Override
	public void startSolver()
	{
		requireDelegate("startSolver").startSolver();
	}

	@Override
	public void postAdvance()
	{
		requireDelegate("postAdvance").postAdvance();
	}

	@Override
	public void postAddFactor(Factor f)
	{
		ISolverFactorGraph sfg = getDelegate();
		if (sfg != null)
		{
			sfg.postAddFactor(f);
		}
	}

	@Override
	public void postSetSolverFactory()
	{
		ISolverFactorGraph sfg = getDelegate();
		if (sfg != null)
		{
			sfg.postSetSolverFactory();
		}
	}

	@Override
	public void recordDefaultSubgraphSolver(FactorGraph subgraph)
	{
		_state.setSubgraphSolver(subgraph,  subgraph.getSolver());
	}
	
	@Override
	public boolean useMultithreading()
	{
		return _useMultithreading;
	}

	@Override
	public void useMultithreading(boolean use)
	{
		if (use != _useMultithreading)
		{
			_useMultithreading = use;
			ISolverFactorGraph delegate = getDelegate();
			if (delegate != null)
			{
				delegate.useMultithreading(use);
			}
		}
	}

	/*---------------
	 * SNode methods
	 */
	
	@Override
	protected void doUpdateEdge(int edge)
	{
	}
	
	/*-------------------------
	 * ProxySolverNode methods
	 */
	
	@Override
	public abstract @Nullable Delegate getDelegate();

	/**
	 * Returns non-null delegate or throws an error indicating method requires that
	 * delegate solver has been set.
	 * @since 0.06
	 */
	protected Delegate requireDelegate(String method)
	{
		Delegate delegate = getDelegate();
		if (delegate == null)
		{
			throw new DimpleException("Delegate solver required by '%s' has not been set.", method);
		}
		return delegate;
	}
	
	protected RuntimeException unsupported(String method)
	{
		return DimpleException.unsupportedMethod(getClass(), method,
			"Not supported for proxy solver because graph topology may be different.");
	}

	/**
	 * Should be invoked by implementing subclass when delegate changes.
	 * 
	 * @param delegate is the new value that will be returned by {@link #getDelegate()}. May be null.
	 * @return delegate
	 * @since 0.05
	 */
	protected @Nullable Delegate notifyNewDelegate(@Nullable Delegate delegate)
	{
		if (delegate != null)
		{
			// Copy locally saved parameters.
			delegate.useMultithreading(_useMultithreading);
			delegate.setNumIterations(_numIterations);
		}
		return delegate;
	}
}
