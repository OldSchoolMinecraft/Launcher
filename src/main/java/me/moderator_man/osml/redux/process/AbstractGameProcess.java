package me.moderator_man.osml.redux.process;

/**
 * Thanks, Mojang. Since you're such a kind and forward-thinking company, I'm sure you'll be okay with me using this.
 */
public abstract class AbstractGameProcess implements GameProcess
{
    private GameProcessRunnable onExit;

    public void setExitRunnable(GameProcessRunnable onExit)
    {
        this.onExit = onExit;
    }

    public GameProcessRunnable getExitRunnable()
    {
        return onExit;
    }
}