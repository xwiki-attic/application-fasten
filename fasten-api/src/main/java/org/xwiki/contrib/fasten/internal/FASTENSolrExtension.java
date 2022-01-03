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

import java.util.ArrayList;
import java.util.List;

import org.xwiki.contrib.fasten.FASTENExtension;
import org.xwiki.contrib.fasten.FASTENVulnerability;
import org.xwiki.extension.ExtensionId;
import org.xwiki.extension.index.internal.SolrExtension;
import org.xwiki.extension.repository.ExtensionRepository;

/**
 * Extends {@link SolrExtension} with FASTEN specific metadata.
 * 
 * @version $Id$
 */
// TODO: expose publicly what's needed to avoid using internal components
public class FASTENSolrExtension extends SolrExtension implements FASTENExtension
{
    private List<FASTENVulnerability> vulnerabilities = new ArrayList<>();

    /**
     * @param repository the repository where this extension comes from
     * @param extensionId the extension identifier
     */
    public FASTENSolrExtension(ExtensionRepository repository, ExtensionId extensionId)
    {
        super(repository, extensionId);
    }

    @Override
    public List<FASTENVulnerability> getVulnerabilities()
    {
        return this.vulnerabilities;
    }

    /**
     * @param vulnerability the vulnerability to assciate to the extension
     */
    public void addVulnerability(FASTENVulnerability vulnerability)
    {
        this.vulnerabilities.add(vulnerability);
    }
}
