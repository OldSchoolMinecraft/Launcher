package me.moderator_man.osml.redux.process;

/**
 * Thanks, Mojang. Since you're such a kind and forward-thinking company, I'm sure you'll be okay with me using this.
 */
public interface GameProcess
{
    boolean isRunning();
    void setExitRunnable(final GameProcessRunnable runnable);
    GameProcessRunnable getExitRunnable();
    int getExitCode();
    void stop();
}
