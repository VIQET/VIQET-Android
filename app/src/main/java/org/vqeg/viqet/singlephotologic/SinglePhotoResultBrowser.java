package org.vqeg.viqet.singlephotologic;

import org.vqeg.viqet.data.ResultStore;
import org.vqeg.viqet.singlephotodata.SinglePhoto;
import org.vqeg.viqet.singlephotodata.SinglePhotoResultStore;
import org.vqeg.viqet.singlephotodata.SingleResult;

import java.util.List;

/**
 * Created by rkalidin on 3/25/2016.
 */
public class SinglePhotoResultBrowser
{
    public List<SingleResult> getResults()
    {
        return SinglePhotoResultStore.GetResultStore().getResults();
    }

    public void deleteResult(SingleResult result)
    {
        SinglePhotoResultStore.GetResultStore().deleteResult(result);
    }

    public void renameResult(SingleResult result, String newName)
    {
        result.setName(newName);
    }

    public int getIndex(SinglePhotoResultStore result)
    {
        return ResultStore.GetResultStore().getResults().indexOf(result);
    }

    public SingleResult addResult(String name, SinglePhoto photo)
    {
        //TODO: Check if the result name has been used before
        //Create a new result and initialize it
        SingleResult result = new SingleResult(name, photo);

        SinglePhotoResultStore.GetResultStore().addResult(result);

        return result;
    }
}
