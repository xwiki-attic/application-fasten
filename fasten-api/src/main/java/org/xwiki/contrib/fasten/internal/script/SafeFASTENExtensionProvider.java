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

import javax.inject.Inject;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.contrib.fasten.FASTENExtension;
import org.xwiki.contrib.fasten.internal.FASTENSolrExtension;
import org.xwiki.script.internal.safe.ScriptSafeProvider;

/**
 * Provide safe {@link FASTENExtension}.
 * 
 * @version $Id: d028670c06429f7b26e9faa8b7a5ef4a47c90246 $
 */
@Component
@Singleton
public class SafeFASTENExtensionProvider implements ScriptSafeProvider<FASTENSolrExtension>
{
    /**
     * The provider of instances safe for public scripts.
     */
    @Inject
    @SuppressWarnings("rawtypes")
    private ScriptSafeProvider defaultSafeProvider;

    @Override
    public <S> S get(FASTENSolrExtension unsafe)
    {
        return (S) new SafeFASTENExtension<FASTENExtension>(unsafe, this.defaultSafeProvider);
    }
}
