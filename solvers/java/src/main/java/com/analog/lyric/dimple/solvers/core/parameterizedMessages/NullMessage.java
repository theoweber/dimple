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

package com.analog.lyric.dimple.solvers.core.parameterizedMessages;

import java.io.PrintStream;

/**
 * A singleton empty message with no parameters.
 * <p>
 * This may be used in place of null to allow non-null {@link IParameterizedMessage} fields or variables.
 * <p>
 * @since 0.08
 * @author Christopher Barber
 */
public class NullMessage implements IParameterizedMessage, Cloneable
{
	private static final long serialVersionUID = 1L;

	public static final NullMessage INSTANCE = new NullMessage();
	
	private NullMessage()
	{
	}
	
	@Override
	public NullMessage clone()
	{
		return this;
	}
	
	@Override
	public void print(PrintStream out, int verbosity)
	{
		out.print(toString(verbosity));
	}

	@Override
	public void println(PrintStream out, int verbosity)
	{
		out.println(toString(verbosity));
	}

	@Override
	public String toString(int verbosity)
	{
		return toString();
	}

	@Override
	public double computeKLDivergence(IParameterizedMessage that)
	{
		return 0;
	}
	
	@Override
	public void setFrom(IParameterizedMessage other)
	{
	}

	@Override
	public void setNull()
	{
	}

	@Override
	public void setUniform()
	{
	}

}
