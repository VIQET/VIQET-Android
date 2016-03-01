/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.data;

public class SharedAccessSignature
{
    private String containerName;
    public String getContainerName() {return containerName;}
    public void setContainerName(String containerName) {this.containerName = containerName;}

    private String blobName;
    public String getBlobName() {return blobName;}
    public void setBlobName(String blobName) {this.blobName = blobName;}

    private String sasURL;
    public String getSasURL() {return sasURL;}
    public void setSasURL(String sasURL) {this.sasURL = sasURL;}
}
