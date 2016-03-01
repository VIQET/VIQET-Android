/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.cloud.Json;

import java.util.List;

public class PhotoResponse
{
    public enum ErrorCodes
    {
        Success, PhotoQueued, MissingBlob, MissingParameters, OtherError
    }

    private String ErrorString;
    public ErrorCodes getErrorCode()
    {
        switch(ErrorString)
        {
            case "Success_FetchResultsIfAlreadyPresent": return ErrorCodes.Success;
            case "ErrorCode_FetchResultsIfAlreadyPresent_failed!ErrorCode_Sent_to_Queue_for_Processing!": return ErrorCodes.PhotoQueued;
            case "ErrorCode_Blob_not_exist!": return ErrorCodes.MissingBlob;
            case "ErrorCode_blobFileName_or_Other_required_parameters_are_empty": return ErrorCodes.MissingParameters;
        }
        return ErrorCodes.OtherError;
    }

    private List<OtherInformationResponse> OtherInformation;
    private boolean ValidResult;

    private String VersionString;
    public String getVersionString() {return VersionString;}

    private List<PhotoDetailResponse> PhotoDetailsString;
    public List<PhotoDetailResponse> getPhotoDetails() {return PhotoDetailsString;}

    private List<VisualizationImagesResponse> VisualizationImages;
    public List<VisualizationImagesResponse> getVisualizationImages() {return VisualizationImages;}


}
