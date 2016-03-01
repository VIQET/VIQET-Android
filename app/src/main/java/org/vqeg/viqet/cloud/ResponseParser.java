/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.cloud;

import com.google.gson.Gson;
import org.vqeg.viqet.cloud.Json.MethodologyResponse;
import org.vqeg.viqet.cloud.Json.PhotoResponse;
import org.vqeg.viqet.cloud.Json.SASResponse;
import org.vqeg.viqet.cloud.Json.VersionResponse;

public class ResponseParser
{
    public static PhotoResponse ParsePhotoResponse(String response)
    {
        return new Gson().fromJson(response, PhotoResponse.class);
    }

    public static VersionResponse ParseVersionResponse(String response)
    {
        return new Gson().fromJson(response, VersionResponse.class);
    }

    public static MethodologyResponse ParseMethodologyResponse(String response)
    {
        return new Gson().fromJson(response, MethodologyResponse.class);
    }

    public static SASResponse ParseSASResponse(String response)
    {
        return new Gson().fromJson(response, SASResponse.class);
    }

}
