/*******************************************************************************
*   Copyright 2013 Analog Devices, Inc.
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

package com.analog.lyric.dimple.solvers.template;

import org.eclipse.jdt.annotation.Nullable;

import com.analog.lyric.dimple.model.core.FactorGraph;
import com.analog.lyric.dimple.solvers.core.SolverBase;
import com.analog.lyric.dimple.solvers.interfaces.ISolverFactorGraph;

/**
 * The solver is essentially a factory responsible for creating FactorGraph solver objects.
 */
public class Solver extends SolverBase<SFactorGraph>
{

	/**
	 * This method creates a Solver FactorGraph object given the corresponding model object.
	 */
	@Override
	public SFactorGraph createFactorGraph(FactorGraph factorGraph, @Nullable ISolverFactorGraph parent)
	{
		return new SFactorGraph(factorGraph, parent);
	}
	
	
}
