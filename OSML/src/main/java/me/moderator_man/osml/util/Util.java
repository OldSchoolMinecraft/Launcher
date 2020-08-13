package me.moderator_man.osml.util;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

public class Util
{
    public static String linuxHomeDir;

    public static void findLinuxHomeDirectory()
    {
        // $HOME environment variable should exist, but handle the situation when
        // it doesn't exist and/or the user executes program as root.
        String linux_home = System.getenv("HOME");
        if (linux_home == null)
        {
            String linux_user = System.getenv("USER");
            if (linux_user == "root")
                linuxHomeDir = "/root";
            else
                linuxHomeDir = "/home/" + linux_user;
        } else
            linuxHomeDir = linux_home;

    }
    
    public static boolean isEmail(String email)
    {
        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher mat = pattern.matcher(email);
        return mat.matches();
    }
    
    public static JSONObject postJSON(String url, JSONObject payload)
    {
        try
        {
            URL object = new URL(url);

            HttpURLConnection con = (HttpURLConnection) object.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("POST");

            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            wr.write(payload.toString());
            wr.flush();

            StringBuilder sb = new StringBuilder();
            int HttpResult = con.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK)
            {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null)
                    sb.append(line + "\n");
                br.close();
                return new JSONObject(sb.toString());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JSONObject obj = new JSONObject();
            obj.put("error", "Something went wrong: " + ex.getMessage());
            return obj;
        }
        JSONObject obj = new JSONObject();
        obj.put("error", "Can this even happen?");
        return obj;
    }

    /*
     * Thank you JuliusVan for the most retarded term known to man (netpage).
     */
    public static void openNetpage(String url)
    {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE))
        {
            try
            {
                Desktop.getDesktop().browse(new URI(url));
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public static String getNativesPath()
    {
        switch (OS.getOS())
        {
            default:
                System.out.println("Unknown operating system (assuming Windows).");
                return backslashes(System.getProperty("user.home") + "/AppData/Roaming/.osm/bin/natives/");
            case Windows:
                return backslashes(System.getProperty("user.home") + "/AppData/Roaming/.osm/bin/natives/");
            case Mac:
                return String.format("~/Library/Application Support/osm/bin/natives/");
            case Linux:
                return linuxHomeDir + "/.osm/bin/natives/";
            case Unsupported:
                System.out.println("Unsupported operating system (assuming Linux).");
                return linuxHomeDir + "/.osm/bin/natives/";
        }
    }

    private static String backslashes(String input)
    {
        return input.replaceAll("/", "\\\\");
    }

    public static String getBinPath()
    {
        switch (OS.getOS())
        {
            default:
                System.out.println("Unknown operating system (assuming Windows).");
                return backslashes(System.getProperty("user.home") + "/AppData/Roaming/.osm/bin/");
            case Windows:
                return backslashes(System.getProperty("user.home") + "/AppData/Roaming/.osm/bin/");
            case Mac:
                return String.format("~/Library/Application Support/osm/bin/");
            case Linux:
                return linuxHomeDir + "/.osm/bin/";
            case Unsupported:
                System.out.println("Unsupported operating system (assuming Linux).");
                return linuxHomeDir + "/.osm/bin/";
        }
    }

    public static String getInstallDirectory()
    {
        switch (OS.getOS())
        {
            default:
                System.out.println("Unknown operating system (assuming Windows).");
                return backslashes(System.getProperty("user.home") + "/AppData/Roaming/.osm/");
            case Windows:
                return backslashes(System.getProperty("user.home") + "/AppData/Roaming/.osm/");
            case Mac:
                return String.format("~/Library/Application Support/osm/");
            case Linux:
                return linuxHomeDir + "/.osm/";
            case Unsupported:
                System.out.println("Unsupported operating system (assuming Linux).");
                return linuxHomeDir + "/.osm/";
        }
    }

    public static String getCurrentLogFile()
    {
        switch (OS.getOS())
        {
            default:
                System.out.println("Unknown operating system (assuming Windows).");
                return backslashes(getInstallDirectory() + "/logs/" + getCurrentTimestamp()) + ".log";
            case Windows:
                return backslashes(getInstallDirectory() + "/logs/" + getCurrentTimestamp()) + ".log";
            case Mac:
                return getInstallDirectory() + "/logs/" + getCurrentTimestamp() + ".log";
            case Linux:
                return getInstallDirectory() + "/logs/" + getCurrentTimestamp() + ".log";
            case Unsupported:
                System.out.println("Unsupported operating system (assuming Linux).");
                return getInstallDirectory() + "/logs/" + getCurrentTimestamp() + ".log";
        }
    }

    public static String getCurrentTimestamp()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss z");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }

    public static byte[] createChecksum(String filename) throws Exception
    {
        InputStream fis = new FileInputStream(filename);
        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        int numRead;
        do
        {
            numRead = fis.read(buffer);
            if (numRead > 0)
                complete.update(buffer, 0, numRead);
        } while (numRead != -1);
        fis.close();
        return complete.digest();
    }

    public static String getMD5Checksum(String filename) throws Exception
    {
        byte[] b = createChecksum(filename);
        String result = "";
        for (int i = 0; i < b.length; i++)
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        return result;
    }
}
