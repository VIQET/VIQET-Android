/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.cloud.Json;

public class SASResponse
{
    private String blobName;
    public String getBlobName() {return blobName;}

    private String containerName;
    public String getContainerName() {return containerName;}

    private String sasUrl;
    public String getSASURL() {return sasUrl;}
}
