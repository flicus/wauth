/*
 * The MIT License
 *
 * Copyright (c) 2014.  schors (https://github.com/flicus)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.ffff.wauth.protocol;

import org.ffff.wauth.ra.inflow.RadiusActivation;

/**
 * Created by flicus on 14.12.2014.
 */
public class RadiusResponse extends RadiusPacket {

    private RadiusRequest request;
    private StringBuilder data = new StringBuilder();
    private RadiusActivation activation;

    public RadiusResponse(RadiusRequest request, RadiusActivation activation) {
        this.request = request;
        this.activation = activation;
    }

    public RadiusResponse addField(String field) {
        data.append(field);
        return this;
    }

    public void send() {
        activation.sendResponse(this);
    }

    public String getMessage() {
        return data.toString();
    }

    public RadiusRequest getRequest() {
        return request;
    }

    @Override
    public String toString() {
        return "RadiusResponse{" +
                "request=" + request +
                ", data=" + data +
                '}';
    }
}
