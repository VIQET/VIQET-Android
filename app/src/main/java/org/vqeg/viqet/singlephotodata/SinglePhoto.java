package org.vqeg.viqet.singlephotodata;

/**
 * Created by rkalidin on 3/25/2016.
 */
/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

import org.vqeg.viqet.data.PhotoDetail;
import org.vqeg.viqet.data.Version;
import org.vqeg.viqet.data.Visualization;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class SinglePhoto implements Serializable
{
    // Filename
    private String filename;
    public String getFilename()
    {
        return this.filename;
    }
    public void setFilename(String filename)
    {
        this.filename = filename;
    }

    // File Path
    private String filePath;
    public String getFilePath()
    {
        return this.filePath;
    }
    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }

    // Input Category
    private String inputCategory;
    public String getInputCategory()
    {
        return this.inputCategory;
    }
    public void setInputCategory(String inputCategory)
    {
        this.inputCategory = inputCategory;
    }

    // List of photo details
    private Map<String, PhotoDetail> photoDetailMap;
    public Map<String, PhotoDetail> getPhotoDetailMap()
    {
        return this.photoDetailMap;
    }

    public void addPhotoDetail(String parameterName, PhotoDetail photoDetail)
    {
        if (this.photoDetailMap == null)
        {
            this.photoDetailMap = new HashMap<String, PhotoDetail>();
        }
        this.photoDetailMap.put(parameterName, photoDetail);
    }

    public PhotoDetail getPhotoDetail(String parameterName)
    {
        if (this.photoDetailMap != null)
        {
            return this.photoDetailMap.get(parameterName);
        }
        return null;
    }

    // List of visualizations (Heat maps)
    private List<Visualization> visualizationList;
    public List<Visualization> getVisualizationList()
    {
        return this.visualizationList;
    }
    public void addVisualization(Visualization visualization)
    {
        if (this.visualizationList == null)
        {
            this.visualizationList = new ArrayList<Visualization>();
        }
        this.visualizationList.add(visualization);
    }

    //Blob Name
    private String blobName;
    public String getBlobName()
    {
        return blobName;
    }
    public void setBlobName(String blobName)
    {
        this.blobName = blobName;
    }

    //Container Name
    private String containerName;
    public String getContainerName()
    {
        return containerName;
    }
    public void setContainerName(String containerName)
    {
        this.containerName = containerName;
    }

    //Cloud version
    private Version cloudVersion;
    public Version getCloudVersion()
    {
        return cloudVersion;
    }
    public void setCloudVersion(Version cloudVersion)
    {
        this.cloudVersion = cloudVersion;
    }

    //STATUS
    public enum State { NO_PHOTO, UNANALYZED,
        UPLOAD_STARTED, UPLOAD_FAILED, UPLOAD_COMPLETE,
        ANALYSIS_STARTED, ANALYSIS_FAILED, ANALYSIS_INPROGRESS, ANALYSIS_COMPLETE };

    private State state;

    public State getState()
    {
        return state;
    }

    public void setState(State state)
    {
        this.state = state;
    }

    public void clear()
    {
        //Delete the existing photo
        if(this.filePath != null)
        {
            File file = new File(this.filePath);
            if(file !=null)
            {
                file.delete();
            }
        }

        //Delete photos in the visualization list
        if(visualizationList != null)
        {
            for(Visualization visualization:this.visualizationList)
            {
                String visualizationFilePath = visualization.getFilePath();
                if(visualizationFilePath != null)
                {
                    File visualizationFile = new File(visualizationFilePath);
                    if(visualizationFile != null)
                    {
                        visualizationFile.delete();
                    }
                }
            }
        }
        this.filename = "Add Photo";
        this.filePath = null;
        this.photoDetailMap = null;
        this.visualizationList = null;
        this.state = SinglePhoto.State.NO_PHOTO;
    }

    public SinglePhoto()
    {
        this.filename = "Add Photo";
        this.state = SinglePhoto.State.NO_PHOTO;
        this.inputCategory= "";
    }
}

