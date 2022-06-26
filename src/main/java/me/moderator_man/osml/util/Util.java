package me.moderator_man.osml.util;

import java.awt.Desktop;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.moderator_man.osml.Configuration;
import me.moderator_man.osml.Main;
import org.json.JSONObject;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class Util
{
    public static String getLinuxHomeDirectory()
    {
        String linuxHomeDir = System.getenv("user.home");
        // $HOME environment variable should exist, but handle the situation when
        // it doesn't exist and/or the user executes program as root.
        String linux_home = System.getenv("HOME");
        if (linux_home == null)
        {
            String linux_user = System.getenv("USER");
            if (Objects.equals(linux_user, "root"))
                linuxHomeDir = "/root";
            else
                linuxHomeDir = "/home/" + linux_user;
        } else
            linuxHomeDir = linux_home;
        return linuxHomeDir;
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
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
                String line = null;
                while ((line = br.readLine()) != null)
                    sb.append(line).append("\n");
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

    public static Configuration getDefaultConfig()
    {
        Configuration config = new Configuration();
        config.disableUpdate = false;
        config.rememberPassword = false;
        config.legacyUI = false;
        config.ramMb = 1024;
        return config;
    }

    public static void setTrustStore(final String[] trustStoreString, final String password) throws Exception
    {
        final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("X509");
        final KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        final Path ksPath = Paths.get(System.getProperty("java.home"), "lib", "security", "cacerts");
        keystore.load(Files.newInputStream(ksPath), password.toCharArray());
        for (final String trustStore : trustStoreString)
        {
            final InputStream keystoreStream = Main.class.getResourceAsStream(trustStore);
            final CertificateFactory cf = CertificateFactory.getInstance("X.509");
            final Certificate crt = cf.generateCertificate(keystoreStream);
            Logger.log("Added certificate for " + ((X509Certificate)crt).getSubjectDN());
            keystore.setCertificateEntry(trustStore.replace(".der", ""), crt);
        }
        trustManagerFactory.init(keystore);
        final TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        final SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustManagers, null);
        SSLContext.setDefault(sc);
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
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static File getRelativeHome()
    {
        String home = System.getProperty("user.home");

        switch (OS.getOS())
        {
            case Windows:
            default:
                return new File(home + "/AppData/Roaming");
            case Mac:
                return new File("~/Library/Application Support");
            case Linux:
                return new File(getLinuxHomeDirectory());
        }
    }

    public static String getNativesPath()
    {
        return new File(getInstallDirectory(), "bin/natives/").getAbsolutePath();
    }

    private static String backslashes(String input)
    {
        return input.replaceAll("/", "\\\\");
    }

    public static String getBinPath()
    {
        return new File(getInstallDirectory(), "bin/").getAbsolutePath();
    }

    public static String getJavaName()
    {
        return String.format("jdk-8-%s-x64", OS.getOS()).toLowerCase();
    }

    public static File getJavaDir()
    {
        return new File(getInstallDirectory(), "java/" + getJavaName());
    }

    public static File getJavaExecutable()
    {
        return new File(getJavaDir(), "bin/java" + (OS.getOS() == OS.Windows ? ".exe" : ""));
    }

    public static String generateToken()
    {
        return UUID.randomUUID().toString().replaceAll("-", "");
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
                return "~/Library/Application Support/osm/";
            case Linux:
                return getLinuxHomeDirectory() + "/.osm/";
            case Unsupported:
                System.out.println("Unsupported operating system (assuming Linux).");
                return getLinuxHomeDirectory() + "/.osm/";
        }
    }

    public static String getCurrentLogFile()
    {
        return new File(getInstallDirectory(), "logs/" + getCurrentTimestamp() + ".log").getAbsolutePath();
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

    /**
     * Compare a String md5 hash to a File md5 hash.
     * @param text
     * @param file
     * @return true if the same, false if they're different. It is assumed to be the same file in case of errors.
     */
    public static boolean textToFileComparison(String text, String file)
    {
        try
        {
            File f = getInstalledFile(file);
            String textHash = stringToMD5(text);
            String fileHash = getMD5Checksum(file);
            System.out.println(String.format("Comparing %s to %s:\nText hash: %s\nFile hash: %s", text, file, textHash, fileHash));
            return textHash.equalsIgnoreCase(fileHash);
        } catch (Exception ex) {
            ex.printStackTrace();
            return true;
        }
    }

    public static File getInstalledFile(String file)
    {
        return new File(getInstallDirectory(), file);
    }

    public static String getMD5Checksum(String filename) throws Exception
    {
        return checksumToString(createChecksum(filename));
    }

    public static String checksumToString(byte[] checksum)
    {
        String result = "";
        for (int i = 0; i < checksum.length; i++)
            result += Integer.toString((checksum[i] & 0xff) + 0x100, 16).substring(1);
        return result;
    }

    public static String stringToMD5(String input)
    {
        try
        {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            return checksumToString(digest.digest(input.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }
}
