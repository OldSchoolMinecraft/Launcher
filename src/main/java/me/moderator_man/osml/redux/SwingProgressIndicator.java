package me.moderator_man.osml.redux;

import javax.swing.*;

public class SwingProgressIndicator
{
    private JFrame frame;
    private JProgressBar progressBar;
    private JLabel message;

    public SwingProgressIndicator(String title, String message)
    {
        this.frame = new JFrame();
//        this.frame.setSize(400, 600);
        this.frame.setTitle(title);

        this.progressBar = new JProgressBar();
        this.message = new JLabel(message);

        this.progressBar.setMinimum(0);
        this.progressBar.setMaximum(100);

        this.frame.add(this.progressBar);
        this.frame.add(this.message);
        this.frame.pack();
    }

    public void show()
    {
        this.frame.setVisible(true);
    }

    public void hide()
    {
        this.frame.setVisible(false);
    }

    public void dispose()
    {
        this.frame.dispose();
    }

    public void updateProgress(int percentage)
    {
        this.progressBar.setValue(percentage);
    }
}
