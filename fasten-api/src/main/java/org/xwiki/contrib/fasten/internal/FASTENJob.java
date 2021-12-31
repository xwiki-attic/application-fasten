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
package org.xwiki.contrib.fasten.internal;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.xwiki.component.annotation.Component;
import org.xwiki.component.annotation.InstantiationStrategy;
import org.xwiki.component.descriptor.ComponentInstantiationStrategy;
import org.xwiki.extension.InstalledExtension;
import org.xwiki.extension.repository.InstalledExtensionRepository;
import org.xwiki.job.AbstractJob;
import org.xwiki.job.DefaultJobStatus;
import org.xwiki.job.Request;

/**
 * Run a FASTEN analysis on the current instance's extensions.
 * 
 * @version $Id$
 */
@Component
@InstantiationStrategy(ComponentInstantiationStrategy.PER_LOOKUP)
@Named(FASTENJob.JOBTYPE)
public class FASTENJob extends AbstractJob<FASTENRequest, DefaultJobStatus<FASTENRequest>>
{
    /**
     * The id of the job.
     */
    public static final String JOBTYPE = "fasten";

    @Inject
    private FASTENVulnerabilityScanner scanner;

    @Inject
    private InstalledExtensionRepository installedExtensions;

    @Inject
    private FASTENVulnerabilityStore vulnerabilityStore;

    @Override
    public String getType()
    {
        return JOBTYPE;
    }

    @Override
    protected FASTENRequest castRequest(Request request)
    {
        FASTENRequest indexerRequest;
        if (request instanceof FASTENRequest) {
            indexerRequest = (FASTENRequest) request;
        } else {
            indexerRequest = new FASTENRequest(request);
        }

        return indexerRequest;
    }

    @Override
    protected void runInternal() throws Exception
    {
        // Index installed extensions
        for (InstalledExtension extension : this.installedExtensions.getInstalledExtensions()) {
            try {
                // Analyze the extension
                List<FASTENVulnerability> vulnerabilities = this.scanner.scan(extension);

                // Store the result of the analysis
                this.vulnerabilityStore.update(extension.getId(), vulnerabilities);
            } catch (IOException e) {
                this.logger.warn("Failed to search vulnerabilities for extension [{}]", extension.getId(), e);
            }
        }

        // TODO: Index available compatible extensions
    }
}
