/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.contrib.fasten.script;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.contrib.fasten.FASTENExtension;
import org.xwiki.contrib.fasten.FASTENExtensionQuery;
import org.xwiki.contrib.fasten.internal.FASTENScheduler;
import org.xwiki.contrib.fasten.internal.FASTENVulnerabilityStore;
import org.xwiki.extension.index.IndexedExtensionQuery;
import org.xwiki.extension.repository.result.IterableResult;
import org.xwiki.extension.repository.search.ExtensionQuery;
import org.xwiki.extension.repository.search.SearchException;
import org.xwiki.extension.script.AbstractExtensionScriptService;
import org.xwiki.extension.script.ExtensionManagerScriptService;
import org.xwiki.job.JobException;
import org.xwiki.job.event.status.JobStatus;

/**
 * Various script APIs related to FASTEN metadata associated with extensions.
 * 
 * @version $Id: bd0ba3b5013930428bf1b9ceffbfbc8fed21a090 $
 */
@Component
@Named(ExtensionManagerScriptService.ROLEHINT + '.' + FASTENExtensionIndexScriptService.ID)
@Singleton
public class FASTENExtensionIndexScriptService extends AbstractExtensionScriptService
{
    /**
     * The identifier of the sub extension {@link org.xwiki.script.service.ScriptService}.
     */
    public static final String ID = "fasten";

    @Inject
    private FASTENVulnerabilityStore store;

    @Inject
    private FASTENScheduler scheduler;

    /**
     * Create a new instance of a {@link IndexedExtensionQuery} to be used in other APIs.
     * 
     * @param query the query to execute
     * @return a {@link ExtensionQuery} instance
     */
    public FASTENExtensionQuery newQuery(String query)
    {
        return new FASTENExtensionQuery(query);
    }

    /**
     * Search indexed extensions based of the provided query.
     * 
     * @param query the query
     * @return the found extensions descriptors, empty list if nothing could be found
     * @throws SearchException error when trying to search provided query
     */
    public IterableResult<FASTENExtension> search(FASTENExtensionQuery query) throws SearchException
    {
        return safe(this.store.search(query));
    }

    /**
     * @return the status of the currently running or last indexing process
     */
    public JobStatus getStatus()
    {
        return this.scheduler.getStatus();
    }

    /**
     * Start a new indexing process or return the status of the currently running one.
     * 
     * @return the status of the running indexing process
     * @throws JobException when failing to start indexing
     */
    public JobStatus index() throws JobException
    {
        return this.scheduler.index();
    }
}
