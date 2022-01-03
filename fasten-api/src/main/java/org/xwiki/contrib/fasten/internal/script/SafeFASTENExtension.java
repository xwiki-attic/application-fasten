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
package org.xwiki.contrib.fasten.internal.script;

import java.util.List;

import org.xwiki.contrib.fasten.FASTENExtension;
import org.xwiki.contrib.fasten.FASTENVulnerability;
import org.xwiki.extension.script.internal.safe.SafeIndexedExtension;
import org.xwiki.script.internal.safe.ScriptSafeProvider;

/**
 * Provide a public script access to a FASTEN extension.
 * 
 * @param <T> the extension type
 * @version $Id: 6833d59c3cbe32d7039c2a4563a63d4b7fc0bb24 $
 */
public class SafeFASTENExtension<T extends FASTENExtension> extends SafeIndexedExtension<T> implements FASTENExtension
{
    /**
     * @param extension the wrapped extension
     * @param safeProvider the provider of instances safe for public scripts
     */
    public SafeFASTENExtension(T extension, ScriptSafeProvider<Object> safeProvider)
    {
        super(extension, safeProvider);
    }

    // IndexedExtension

    @Override
    public Boolean isCompatible(String namespace)
    {
        return getWrapped().isCompatible(namespace);
    }

    @Override
    public List<FASTENVulnerability> getVulnerabilities()
    {
        return getWrapped().getVulnerabilities();
    }
}
