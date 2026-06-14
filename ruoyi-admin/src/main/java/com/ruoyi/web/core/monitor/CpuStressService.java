package com.ruoyi.web.core.monitor;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.PreDestroy;
import org.springframework.stereotype.Component;

/**
 * CPU压测执行器
 *
 * @author ruoyi
 */
@Component
public class CpuStressService
{
    private static final int MAX_SECONDS = 120;

    private final int maxThreads = Math.max(1, Runtime.getRuntime().availableProcessors());

    private final AtomicBoolean running = new AtomicBoolean(false);

    private final AtomicInteger activeThreads = new AtomicInteger(0);

    private volatile int threads;

    private volatile int seconds;

    private volatile long startTime;

    private volatile long endTime;

    private volatile double sink;

    public CpuStressStatus start(CpuStressRequest request)
    {
        if (running.get())
        {
            throw new IllegalStateException("CPU压测正在运行，请稍后再试");
        }

        int safeThreads = limit(request == null ? null : request.getThreads(), maxThreads, maxThreads);
        int safeSeconds = limit(request == null ? null : request.getSeconds(), 30, MAX_SECONDS);
        long now = System.currentTimeMillis();
        long end = now + TimeUnit.SECONDS.toMillis(safeSeconds);

        if (!running.compareAndSet(false, true))
        {
            throw new IllegalStateException("CPU压测正在运行，请稍后再试");
        }

        activeThreads.set(0);
        threads = safeThreads;
        seconds = safeSeconds;
        startTime = now;
        endTime = end;

        for (int i = 0; i < safeThreads; i++)
        {
            activeThreads.incrementAndGet();
            Thread worker = new Thread(() -> burnCpu(end), "cpu-stress-" + (i + 1));
            worker.setDaemon(true);
            worker.start();
        }
        return getStatus();
    }

    public CpuStressStatus stop()
    {
        running.set(false);
        return getStatus();
    }

    public CpuStressStatus getStatus()
    {
        long now = System.currentTimeMillis();
        boolean currentRunning = running.get();
        long remainingSeconds = currentRunning ? Math.max(0, (endTime - now + 999) / 1000) : 0;

        CpuStressStatus status = new CpuStressStatus();
        status.setRunning(currentRunning);
        status.setThreads(threads);
        status.setActiveThreads(activeThreads.get());
        status.setSeconds(seconds);
        status.setStartTime(startTime);
        status.setEndTime(endTime);
        status.setRemainingSeconds(remainingSeconds);
        status.setMaxThreads(maxThreads);
        status.setMaxSeconds(MAX_SECONDS);
        return status;
    }

    @PreDestroy
    public void destroy()
    {
        running.set(false);
    }

    private void burnCpu(long end)
    {
        double value = 0D;
        long count = 0L;
        try
        {
            while (running.get() && System.currentTimeMillis() < end)
            {
                value += Math.sqrt((System.nanoTime() & 1023L) + (count++ & 1023L));
                if ((count & 0x3fffL) == 0L)
                {
                    sink = value;
                }
            }
            sink = value;
        }
        finally
        {
            if (activeThreads.decrementAndGet() <= 0)
            {
                activeThreads.set(0);
                running.set(false);
            }
        }
    }

    private int limit(Integer value, int defaultValue, int maxValue)
    {
        int safeValue = value == null ? defaultValue : value;
        if (safeValue < 1)
        {
            return 1;
        }
        return Math.min(safeValue, maxValue);
    }

    public static class CpuStressRequest
    {
        private Integer threads;

        private Integer seconds;

        public Integer getThreads()
        {
            return threads;
        }

        public void setThreads(Integer threads)
        {
            this.threads = threads;
        }

        public Integer getSeconds()
        {
            return seconds;
        }

        public void setSeconds(Integer seconds)
        {
            this.seconds = seconds;
        }
    }

    public static class CpuStressStatus
    {
        private boolean running;

        private int threads;

        private int activeThreads;

        private int seconds;

        private long startTime;

        private long endTime;

        private long remainingSeconds;

        private int maxThreads;

        private int maxSeconds;

        public boolean isRunning()
        {
            return running;
        }

        public void setRunning(boolean running)
        {
            this.running = running;
        }

        public int getThreads()
        {
            return threads;
        }

        public void setThreads(int threads)
        {
            this.threads = threads;
        }

        public int getActiveThreads()
        {
            return activeThreads;
        }

        public void setActiveThreads(int activeThreads)
        {
            this.activeThreads = activeThreads;
        }

        public int getSeconds()
        {
            return seconds;
        }

        public void setSeconds(int seconds)
        {
            this.seconds = seconds;
        }

        public long getStartTime()
        {
            return startTime;
        }

        public void setStartTime(long startTime)
        {
            this.startTime = startTime;
        }

        public long getEndTime()
        {
            return endTime;
        }

        public void setEndTime(long endTime)
        {
            this.endTime = endTime;
        }

        public long getRemainingSeconds()
        {
            return remainingSeconds;
        }

        public void setRemainingSeconds(long remainingSeconds)
        {
            this.remainingSeconds = remainingSeconds;
        }

        public int getMaxThreads()
        {
            return maxThreads;
        }

        public void setMaxThreads(int maxThreads)
        {
            this.maxThreads = maxThreads;
        }

        public int getMaxSeconds()
        {
            return maxSeconds;
        }

        public void setMaxSeconds(int maxSeconds)
        {
            this.maxSeconds = maxSeconds;
        }
    }
}
