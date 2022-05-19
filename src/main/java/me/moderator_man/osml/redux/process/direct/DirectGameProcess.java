package me.moderator_man.osml.redux.process.direct;

import me.moderator_man.osml.redux.process.AbstractGameProcess;
import me.moderator_man.osml.redux.process.OutputLogProcessor;

/**
 * Thanks, Mojang. Since you're such a kind and forward-thinking company, I'm sure you'll be okay with me using this.
 */
public class DirectGameProcess extends AbstractGameProcess
{
    private final Process process;
    protected final DirectProcessInputMonitor monitor;

    public DirectGameProcess(final Process process, final OutputLogProcessor outputLogProcessor)
    {
        this.process = process;
        (this.monitor = new DirectProcessInputMonitor(this, outputLogProcessor)).start();
    }

    public Process getRawProcess()
    {
        return process;
    }

    @Override
    public int getExitCode()
    {
        try
        {
            return this.process.exitValue();
        } catch (IllegalThreadStateException ignored) {}
        return -1;
    }

    @Override
    public boolean isRunning()
    {
        try
        {
            this.process.exitValue();
        } catch (IllegalThreadStateException ex) {
            return true;
        }
        return false;
    }

    @Override
    public void stop()
    {
        this.process.destroy();
    }
}
