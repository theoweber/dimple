/*******************************************************************************
*   Copyright 2013-2015 Analog Devices, Inc.
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

package com.analog.lyric.dimple.solvers.core;

import org.eclipse.jdt.annotation.Nullable;

import com.analog.lyric.dimple.events.SolverEventSource;
import com.analog.lyric.dimple.exceptions.DimpleException;
import com.analog.lyric.dimple.model.core.EdgeState;
import com.analog.lyric.dimple.model.core.Port;
import com.analog.lyric.dimple.model.factors.Factor;
import com.analog.lyric.dimple.model.repeated.BlastFromThePastFactor;
import com.analog.lyric.dimple.model.variables.Variable;
import com.analog.lyric.dimple.solvers.interfaces.ISolverBlastFromThePastFactor;
import com.analog.lyric.dimple.solvers.interfaces.ISolverEdgeState;
import com.analog.lyric.dimple.solvers.interfaces.ISolverFactorGraph;
import com.analog.lyric.dimple.solvers.interfaces.ISolverVariable;
import com.analog.lyric.dimple.solvers.interfaces.SolverNodeMapping;

public class SBlastFromThePast extends SolverEventSource implements ISolverBlastFromThePastFactor
{
	private BlastFromThePastFactor _factor;
	protected final Port _portForOtherVar;
	protected final Port _portForBlastVar;
	protected final ISolverFactorGraph _parent;
	
	public SBlastFromThePast(BlastFromThePastFactor f, ISolverFactorGraph parent)
	{
		_factor = f;
		_portForOtherVar = f.getPortForOtherVariable();
		Variable varConnectedToBlast = f.getVariableConnectedToBlast();
	    _portForBlastVar = varConnectedToBlast.getPort(varConnectedToBlast.findSibling(getModelObject()));
	    _parent = parent;
	}
	
	/*----------------
	 * Object methods
	 */
	
	@Override
	public String toString()
	{
		return String.format("[%s %s]", getClass().getSimpleName(), _factor.getQualifiedName());
	}
	
	/*----------------------
	 * ISolverNode methods
	 */

	@Override
	public ISolverFactorGraph getContainingSolverGraph()
	{
		return _parent;
	}
	
	@Override
	public @Nullable ISolverEdgeState getSiblingEdgeState(int siblingIndex)
	{
		return requireParentGraph().getSolverEdge(_factor.getSiblingEdgeIndex(siblingIndex));
	}
	
	@Override
	public ISolverVariable getSibling(int edge)
	{
		final Variable sibling = getModelObject().getSibling(edge);
		return getSolverMapping().getSolverVariable(sibling);
	}
	
	@Override
	public int getSiblingCount()
	{
		return getModelObject().getSiblingCount();
	}
	
	@Override
	public SolverNodeMapping getSolverMapping()
	{
		return _parent.getSolverMapping();
	}
	
	/*-----------------------
	 * ISolverFactor methods
	 */
	
	@Override
	public @Nullable ISolverEdgeState createEdge(EdgeState edge)
	{
		return null;
	}

	/*---------------------------
	 * SBlastFromThePast methods
	 */
	
	public BlastFromThePastFactor getFactor()
	{
		return _factor;
	}
	
	public @Nullable Port getOtherVariablePort()
	{
		return _portForOtherVar;
	}
	
	@Override
	public Object getBelief()
	{
		throw new DimpleException("Not implemented");
	}

	@Override
	public double getInternalEnergy()
	{
		throw new DimpleException("Not implemented");
	}

	@Override
	public double getBetheEntropy()
	{
		throw new DimpleException("Not implemented");
	}

	@Override
	public int[][] getPossibleBeliefIndices()
	{
		throw new DimpleException("Not implemented");
	}

	@Override
	public void update()
	{
	}

	@Override
	public void updateEdge(int outPortNum)
	{
	}

	@Override
	public void initialize()
	{
		clearFlags();
	}

	@Override
	public ISolverFactorGraph getParentGraph()
	{
		return _parent;
	}

	@Override
	public ISolverFactorGraph getRootSolverGraph()
	{
		return _parent.getRootSolverGraph();
	}

	@Override
	public double getScore()
	{
		throw new DimpleException("Not implemented");
	}

	@Override
	public Factor getModelObject()
	{
		return _factor;
	}

	@Deprecated
	@Override
	public Object getInputMsg(int portIndex)
	{
		throw new DimpleException("Not implemented");
	}

	@Deprecated
	@Override
	public Object getOutputMsg(int portIndex)
	{
		throw new DimpleException("Not implemented");
	}

	@Deprecated
	@Override
	public void setInputMsg(int portIndex, Object obj)
	{
		throw new DimpleException("Not implemented");
	}

	@Deprecated
	@Override
	public void setOutputMsg(int portIndex, Object obj)
	{
		Variable var = _factor.getSibling(portIndex);
		int index = _factor.getReverseSiblingNumber(portIndex);
		// FIXME - lookup through solver graph
		var.requireSolver("setOutputMsg").setInputMsg(index,obj);
	}

	@Deprecated
	@Override
	public void setInputMsgValues(int portIndex, Object obj)
	{
		throw new DimpleException("Not implemented");
	}

	@Override
	public void setOutputMsgValues(int portIndex, Object obj)
	{
		throw new DimpleException("Not implemented");
	}

	@SuppressWarnings("null")
	@Override
	public void advance()
	{
		// FIXME lookup through solver graph
		EdgeState edgeState = _portForBlastVar.toEdgeState();
		ISolverFactorGraph sfg = _portForBlastVar.getNode().getParentGraph().getSolver();
		ISolverEdgeState sedge = sfg.getSolverEdge(edgeState);
		
		EdgeState edgeState2 = _portForOtherVar.toEdgeState();
		ISolverFactorGraph sfg2 = _portForOtherVar.getNode().getParentGraph().getSolver();
		ISolverEdgeState sedge2 = sfg2.getSolverEdge(edgeState2);
		
		sedge.setFrom(sedge2);
		sedge2.reset();
	}

	@Override
	public void setDirectedTo(int[] indices)
	{
		//NOP
	}

	

}
