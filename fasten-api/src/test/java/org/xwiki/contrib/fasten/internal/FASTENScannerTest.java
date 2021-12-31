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

import org.junit.jupiter.api.Test;
import org.xwiki.extension.ExtensionId;
import org.xwiki.extension.InstalledExtension;
import org.xwiki.extension.repository.InstalledExtensionRepository;
import org.xwiki.test.junit5.mockito.ComponentTest;
import org.xwiki.test.junit5.mockito.InjectMockComponents;
import org.xwiki.test.junit5.mockito.MockComponent;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link FASTENVulnerabilityScanner}.
 *
 * @version $Id: 98cddccab82040ece6151099a38d001b1a2f6225 $
 */
@ComponentTest
class FASTENScannerTest
{
    @MockComponent
    private InstalledExtensionRepository extensions;

    @InjectMockComponents
    private FASTENVulnerabilityScanner scanner;

    @Test
    void scan() throws IOException
    {
        InstalledExtension extension1 = mock(InstalledExtension.class);
        when(extension1.getId()).thenReturn(new ExtensionId("org.jboss.resteasy:resteasy-jaxrs", "3.0.23.Final"));

        this.scanner.scan(extension1);
    }
}
