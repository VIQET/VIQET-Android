/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.data;

import android.util.Log;

import org.vqeg.viqet.utilities.FileHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ResultStore 
{
	
	private List<Result> results;
	private static final String filename = "Results.vqt";
	private static final String TAG = "ResultStore";
	private static File resultsFile;

    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

	public List<Result> getResults()
	{
		return results;
	}

	public void addResult(Result result) 
	{
		this.readWriteLock.writeLock().lock();
        this.results.add(result);
        this.readWriteLock.writeLock().unlock();
		this.persist();
	}
	
	public void deleteResult(Result result)
	{
        this.readWriteLock.writeLock().lock();
		this.results.remove(result);
        this.readWriteLock.writeLock().unlock();
		this.persist();
	}
	
	public ResultStore()
	{
		this.results = new ArrayList<Result>();
        this.resultsFile = FileHelper.getFile(filename, FileHelper.photoDirectoryName);
    }

	private static ResultStore resultStore;
	
	public static ResultStore GetResultStore()
	{
		if(resultStore == null)
		{	
			resultStore = new ResultStore();
			try 
			{
				if(resultsFile.exists())
				{
					FileInputStream fileInputStream = new FileInputStream(resultsFile);
					ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
					try 
					{
						resultStore.results = (List<Result>) objectInputStream.readObject();
					} 
					catch (ClassNotFoundException e)
					{
						resultStore = new ResultStore();
						Log.e(TAG, "Class Not Found Exception thrown by GetResultStore() ");
					}
					objectInputStream.close(); 
				}
				else
				{
					resultStore = new ResultStore();
				}
			}
			catch (FileNotFoundException e)
			{
				Log.e(TAG, "File Not Found Exception thrown by GetResultStore() ");
			} 
			catch (IOException e)
			{
				Log.e(TAG, "IO Exception thrown by GetResultStore() ");
			}
		}
		return resultStore;
	}
	
	public void persist()
	{
		try 
		{
            this.readWriteLock.writeLock().lock();
            FileOutputStream fileOutputStream = new FileOutputStream(resultsFile);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(this.results);
			objectOutputStream.close();
            this.readWriteLock.writeLock().unlock();
		}
		catch (FileNotFoundException e)
		{
			Log.e(TAG, "File Not Found Exception thrown by Persist() ");
		} 
		catch (IOException e)
		{
			Log.e(TAG, "IO Exception thrown by Persist() ");
		}
	}
}

