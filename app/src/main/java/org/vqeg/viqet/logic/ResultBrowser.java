/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.logic;

import org.vqeg.viqet.data.RemoteInfoProvider;
import org.vqeg.viqet.data.Result;
import org.vqeg.viqet.data.ResultStore;

import java.util.List;

public class ResultBrowser 
{
	public List<Result> getResults()
	{
		return ResultStore.GetResultStore().getResults();
	}
	
	public void deleteResult(Result result)
	{
		ResultStore.GetResultStore().deleteResult(result);	
	}
	
	public void renameResult(Result result, String newName)
	{
		result.setName(newName);
	}
	
	public int getIndex(Result result)
	{
		return ResultStore.GetResultStore().getResults().indexOf(result);
	}
	
	public Result addResult(String name)
	{
		//TODO: Check if the result name has been used before
		//Create a new result and initialize it
		Result result = new Result(name, RemoteInfoProvider.getRemoteInfo());
		ResultStore.GetResultStore().addResult(result);
		return result;
	}
}
