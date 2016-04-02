package org.vqeg.viqet.singlephotodata;

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

/**
 * Created by rkalidin on 3/25/2016.
 */

public class SinglePhotoResultStore
{

    private List<SingleResult> results;
    private static final String filename = "Results_Single_Photo.vqt";
    private static final String TAG = "ResultStore_Single_Photo";
    private static File resultsFile;

    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public List<SingleResult> getResults()
    {
        return results;
    }

    public void addResult(SingleResult result)
    {
        this.readWriteLock.writeLock().lock();
        this.results.add(result);
        this.readWriteLock.writeLock().unlock();
        this.persist();
    }

    public void deleteResult(SingleResult result)
    {
        this.readWriteLock.writeLock().lock();
        this.results.remove(result);
        this.readWriteLock.writeLock().unlock();
        this.persist();
    }

    public SinglePhotoResultStore()
    {
        this.results = new ArrayList<SingleResult>();
        this.resultsFile = FileHelper.getFile(filename, FileHelper.photoDirectoryName);
    }

    private static SinglePhotoResultStore resultStore;

    public static SinglePhotoResultStore GetResultStore()
    {
        if(resultStore == null)
        {
            resultStore = new SinglePhotoResultStore();
            try
            {
                if(resultsFile.exists())
                {
                    FileInputStream fileInputStream = new FileInputStream(resultsFile);
                    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                    try
                    {
                        resultStore.results = (List<SingleResult>) objectInputStream.readObject();
                    }
                    catch (ClassNotFoundException e)
                    {
                        resultStore = new SinglePhotoResultStore();
                    }
                    objectInputStream.close();
                }
                else
                {
                    resultStore = new SinglePhotoResultStore();
                }
            }
            catch (FileNotFoundException e)
            {
                Log.e("", "File Not Found Exception thrown by GetResultStore() ");
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
            Log.e("", "File Not Found Exception thrown by Persist() ");
        }
        catch (IOException e)
        {
            Log.e("", "IO Exception thrown by Persist() ");
        }
    }
}

