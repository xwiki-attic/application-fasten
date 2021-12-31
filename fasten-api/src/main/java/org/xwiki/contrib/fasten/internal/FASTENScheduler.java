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

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.xwiki.component.annotation.Component;
import org.xwiki.component.manager.ComponentLifecycleException;
import org.xwiki.component.phase.Disposable;
import org.xwiki.job.Job;
import org.xwiki.job.JobExecutor;

/**
 * Try to find risk associated with installed extension son FASTEN Knowledge Base server.
 * 
 * @version $Id$
 */
@Component(roles = FASTENScheduler.class)
@Singleton
public class FASTENScheduler implements Runnable, Disposable
{
    @Inject
    private JobExecutor jobs;

    @Inject
    private Logger logger;

    private ScheduledExecutorService executor;

    public void initialize()
    {
        // Start the scheduling
        this.executor = Executors.newSingleThreadScheduledExecutor(new ThreadFactory()
        {
            @Override
            public Thread newThread(Runnable r)
            {
                Thread thread = new Thread(r);
                thread.setName("FASTEN scanner");
                thread.setPriority(Thread.NORM_PRIORITY - 2);
                thread.setDaemon(true);

                return thread;
            }
        });
        this.executor.scheduleWithFixedDelay(this, 0, 24, TimeUnit.HOURS);
    }

    @Override
    public void dispose() throws ComponentLifecycleException
    {
        this.executor.shutdownNow();
    }

    @Override
    public void run()
    {
        Job job;
        try {
            job = this.jobs.execute(FASTENJob.JOBTYPE, new FASTENRequest());
            job.join();
        } catch (Exception e) {
            this.logger.error("Failed to execute job", e);
        }
    }
}
