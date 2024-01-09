package me.moderator_man.osml.util;

import java.util.ArrayList;

public class Server
{
    public boolean onlineMode;
    public PlayerAverage average;
    public String serverVersion;
    public int maxPlayers;
    public ArrayList<Player> players;
    public ArrayList<Flag> flags;
    public String serverName;
    public boolean whitelist;
    public short serverPort;
    public String uuid;
    public String serverDescription;
    public int onlinePlayers;
    public String serverIP;
    public  String serverIcon;
    public String numericalIP;

    public boolean isOfficial()
    {
        //TODO: change this to use a remote api
        return serverName.contains("Old School Minecraft") || serverName.contains("AlphaPlace") || serverName.contains("RetroMC");
    }

    public class PlayerAverage
    {
        public float month;
        public float day;
    }

    public class Player
    {
        public long secondsOnline;
        public String uuid;
        public long secondsSinceLastMove;
        public String username;
    }

    public class Flag
    {
        public String name;
        public boolean enabled;
    }
}