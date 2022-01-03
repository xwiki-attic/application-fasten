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
import org.xwiki.job.JobException;
import org.xwiki.job.JobExecutor;
import org.xwiki.job.JobStatusStore;
import org.xwiki.job.event.status.JobStatus;
import org.xwiki.job.event.status.JobStatus.State;

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
    private JobStatusStore jobStore;

    @Inject
    private Logger logger;

    private ScheduledExecutorService executor;

    /**
     * Initialize and start the scheduler.
     */
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
        try {
            // Execute job
            Job job = this.jobs.execute(FASTENJob.JOBTYPE, new FASTENRequest());

            // Wait for the job to finish
            job.join();
        } catch (InterruptedException e) {
            // Restore interrupted state...
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            this.logger.error("Failed to execute job", e);
        }
    }

    /**
     * @return the status of the currently running or last indexing process
     */
    public JobStatus getStatus()
    {
        // Try running jobs
        Job job = this.jobs.getJob(FASTENRequest.ID);

        if (job != null) {
            return job.getStatus();
        }

        // Try serialized jobs
        return this.jobStore.getJobStatus(FASTENRequest.ID);
    }

    /**
     * Start a new indexing process or return the status of the currently running one.
     * 
     * @return the status of the running indexing process
     * @throws JobException when failing to start indexing
     */
    public JobStatus index() throws JobException
    {
        Job job = this.jobs.getJob(FASTENRequest.ID);

        if (job == null || job.getStatus().getState() == State.FINISHED) {
            job = this.jobs.execute(FASTENJob.JOBTYPE, new FASTENRequest());
        }

        return job.getStatus();
    }
}
