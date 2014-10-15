package net.ffff.wauth.protocol;

/**
 * Copyright (c) 2013 Amdocs jNetX.
 * http://www.amdocs.com
 * All rights reserved.
 * <p/>
 * This software is the confidential and proprietary information of
 * Amdocs jNetX. You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license
 * agreement you entered into with Amdocs jNetX.
 * <p/>
 * User: Sergey Skoptsov (sskoptsov@amdocs.com)
 * Date: 14.10.2014
 * Time: 20:12
 * <p/>
 * $Id:
 */

public class RadiusRequest extends RadiusPacket {

    private String data;

    public RadiusRequest(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RadiusRequest{" +
                "data='" + data + '\'' +
                '}';
    }
}
