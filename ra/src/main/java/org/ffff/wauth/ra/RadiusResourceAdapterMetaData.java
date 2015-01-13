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

package org.ffff.wauth.ra;

import javax.resource.cci.ResourceAdapterMetaData;

/**
 * Created by flicus on 14.12.2014.
 */
public class RadiusResourceAdapterMetaData implements ResourceAdapterMetaData {
    @Override
    public String getAdapterVersion() {
        return "1.0";
    }

    @Override
    public String getAdapterVendorName() {
        return "0xffff.net";
    }

    @Override
    public String getAdapterName() {
        return "Radius RA";
    }

    @Override
    public String getAdapterShortDescription() {
        return "Radius Resource Adapter";
    }

    @Override
    public String getSpecVersion() {
        return "1.0";
    }

    @Override
    public String[] getInteractionSpecsSupported() {
        return new String[0];
    }

    @Override
    public boolean supportsExecuteWithInputAndOutputRecord() {
        return false;
    }

    @Override
    public boolean supportsExecuteWithInputRecordOnly() {
        return false;
    }

    @Override
    public boolean supportsLocalTransactionDemarcation() {
        return false;
    }
}
