package me.moderator_man.osml.debug;

import me.moderator_man.osml.redux.launch.LaunchUtility;

import java.io.File;

public class Main
{
    public static void main(String[] args)
    {
        System.out.println(new LaunchUtility().buildClasspath(new File(System.getenv("BIN_DIR"))));
    }
}
