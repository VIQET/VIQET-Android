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

import java.io.Serializable;


@SuppressWarnings("serial")
public class SingleResult implements Serializable {

    // Test Name
    private String name;

    public SinglePhoto getPhoto() {
        return photo;
    }

    public void setPhoto(SinglePhoto photo) {
        this.photo = photo;
    }

    private SinglePhoto photo;

    public String getName() {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    public SingleResult(String name, SinglePhoto photo)
    {
        //Set name
        this.setName(name);
        this.photo=photo;
    }
}
