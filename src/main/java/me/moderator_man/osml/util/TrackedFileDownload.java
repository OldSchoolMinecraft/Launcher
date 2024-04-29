package me.moderator_man.osml.util;

import javax.swing.*;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class TrackedFileDownload extends Thread
{
    private String downloadURL;
    private File destination;
    private ThrowablePipe throwablePipe;
    private ProgressPipe progressPipe;
    private VoidPipe completedPipe;

    public TrackedFileDownload(String downloadURL, File destination)
    {
        this.downloadURL = downloadURL;
        this.destination = destination;
    }

    public TrackedFileDownload setProgressPipe(ProgressPipe pipe)
    {
        this.progressPipe = pipe;
        return this;
    }

    public TrackedFileDownload setThrowablePipe(ThrowablePipe pipe)
    {
        this.throwablePipe = pipe;
        return this;
    }

    public TrackedFileDownload setCompletedPipe(VoidPipe pipe)
    {
        this.completedPipe = pipe;
        return this;
    }

    public void run()
    {
        try
        {
            URL url = new URL(downloadURL);
            HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
            long completeFileSize = httpConnection.getContentLength();

            java.io.BufferedInputStream in = new java.io.BufferedInputStream(httpConnection.getInputStream());
            java.io.FileOutputStream fos = new java.io.FileOutputStream(destination);
            java.io.BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
            byte[] data = new byte[1024];
            long downloadedFileSize = 0;
            int x = 0;
            while ((x = in.read(data, 0, 1024)) >= 0)
            {
                downloadedFileSize += x;

                // calculate progress
                final int currentProgress = (int) ((((double)downloadedFileSize) / ((double)completeFileSize)) * 100000d);

                // update progress bar
                progressPipe.flush(currentProgress);
                bout.write(data, 0, x);
            }
            bout.close();
            in.close();
            completedPipe.flush();
        } catch (IOException ex) {
            throwablePipe.flush(ex);
        }
    }
}
