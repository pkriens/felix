/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.felix.http.base.internal.runtime;

import java.util.Map;

import org.osgi.framework.ServiceReference;
import org.osgi.service.http.context.ServletContextHelper;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

/**
 * Provides registration information for a {@link ServletContextHelper}
 */
public final class ServletContextHelperInfo extends AbstractInfo<ServletContextHelper>
{

    /**
     * Properties starting with this prefix are passed as context init parameters.
     */
    private static final String CONTEXT_INIT_PREFIX = "context.init.";

    private final String name;

    private final String path;

    private final String prefix;

    /**
     * The filter initialization parameters as provided during registration of the filter.
     */
    private final Map<String, String> initParams;

    public ServletContextHelperInfo(final ServiceReference<ServletContextHelper> ref) {
        super(ref);
        this.name = this.getStringProperty(ref, HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME);
        this.path = this.getStringProperty(ref, HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_PATH);
        String prefix = null;
        if ( !isEmpty(this.path) )
        {
            if ( !this.path.equals("/") && this.path.length() > 1 )
            {
                prefix = this.path.substring(0, this.path.length() - 1);
            }
        }
        this.prefix = prefix;
        this.initParams = getInitParams(ref, CONTEXT_INIT_PREFIX);
    }

    private boolean isValidPath()
    {
        if ( !this.isEmpty(path) )
        {
            // TODO we need more validation
            if ( path.startsWith("/") && path.endsWith("/") )
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isValid()
    {
        // TODO - check if name has valid value
        return super.isValid() && !this.isEmpty(this.name) && isValidPath();
    }

    public String getName()
    {
        return this.name;
    }

    public String getPath()
    {
        return this.path;
    }

    public String getPrefix()
    {
        return this.prefix;
    }

    public String getFullPath(final String path)
    {
        if ( this.prefix == null )
        {
            return path;
        }
        return this.prefix.concat(path);
    }

    public Map<String, String> getInitParams()
    {
        return initParams;
    }
}
