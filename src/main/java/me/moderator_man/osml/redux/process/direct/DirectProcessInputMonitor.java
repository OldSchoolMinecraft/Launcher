package me.moderator_man.osml.redux.process.direct;

import me.moderator_man.osml.redux.process.GameProcessRunnable;
import me.moderator_man.osml.redux.process.OutputLogProcessor;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

/**
 * Thanks, Mojang. Since you're such a kind and forward-thinking company, I'm sure you'll be okay with me using this.
 */
public class DirectProcessInputMonitor extends Thread
{
    private static final Logger LOGGER;
    private final DirectGameProcess process;
    private final OutputLogProcessor logProcessor;

    public DirectProcessInputMonitor(final DirectGameProcess process, final OutputLogProcessor logProcessor)
    {
        this.process = process;
        this.logProcessor = logProcessor;
    }

    @Override
    public void run()
    {
        final InputStreamReader reader = new InputStreamReader(this.process.getRawProcess().getInputStream());
        final BufferedReader buf = new BufferedReader(reader);
        String line;
        while (this.process.isRunning())
        {
            try
            {
                while ((line = buf.readLine()) != null) this.logProcessor.onGameOutput(this.process, line);
            } catch (IOException ex) {
                //DirectProcessInputMonitor.LOGGER.severe(ex.getMessage());
            } finally {
                IOUtils.closeQuietly(reader);
            }
        }
        final GameProcessRunnable onExit = this.process.getExitRunnable();
        if (onExit != null)
            onExit.onGameProcessEnded(this.process);
    }

    static
    {
        LOGGER = Logger.getLogger("HydraRedux");
    }
}
