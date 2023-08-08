package me.moderator_man.osml.instance;

import com.google.gson.Gson;
import me.moderator_man.osml.util.InstanceException;
import me.moderator_man.osml.util.Util;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class InstanceManager
{
    private static final Gson gson = new Gson();
    private final File instanceDir = new File(Util.getInstallDirectory(), "instances");
    private final ArrayList<Instance> instances;


    public InstanceManager()
    {
        instances = new ArrayList<>();
    }

    public void init()
    {
        instanceDir.mkdirs();

        for (File file : Objects.requireNonNull(instanceDir.listFiles()))
        {
            try
            {
                if (!file.isDirectory()) continue;
                Instance inst = loadInstance(file);
                if (inst != null) instances.add(inst);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        try
        {
            if (instances.size() < 1) // empty :(
            {
                // create default instance
                instances.add(createInstance("(Default)", "b1.7.3"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Instance loadInstance(File dir) throws IOException
    {
        File meta = new File(dir, "instance.json");

        try (FileReader reader = new FileReader(meta))
        {
            return gson.fromJson(reader, Instance.class);
        }
    }

    public Instance createInstance(String name, String gameVersion) throws InstanceException, IOException
    {
        if (name.equalsIgnoreCase("(Default)") && new File(instanceDir, "(Default)/instance.json").exists()) throw new InstanceException("You can't use the default instance name. Please pick another one.");
        Instance inst = new Instance(name, gameVersion, new ArrayList<>());
        File meta = new File(instanceDir, Util.cleanFileName(name) + "/instance.json");
        meta.getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(meta))
        {
            gson.toJson(inst, Instance.class, writer);
        }
        return inst;
    }

    public ArrayList<Instance> getInstances()
    {
        return instances;
    }
}
