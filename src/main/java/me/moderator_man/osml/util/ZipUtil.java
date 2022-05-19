package me.moderator_man.osml.util;

import java.io.*;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtil
{
    private static final int BUFFER_SIZE = 4096;

    public static void unzip(String zipFilePath, String destDirectory) throws IOException
    {
        File destDir = new File(destDirectory);
        if (!destDir.exists())
            destDir.mkdir();
        java.util.zip.ZipInputStream zipIn = new java.util.zip.ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        while (entry != null)
        {
            String filePath = destDirectory + File.separator + entry.getName();
            if (!entry.isDirectory())
                extractFile(zipIn, filePath);
            else {
                File dir = new File(filePath);
                dir.mkdirs();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }

    private static void extractFile(java.util.zip.ZipInputStream zipIn, String filePath) throws IOException
    {
        File dst = new File(filePath);
        if (dst.exists()) dst.delete();
        dst.getParentFile().mkdirs();
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1)
            bos.write(bytesIn, 0, read);
        bos.close();
    }

    public static void extractAllTo(String src, String dst)
    {
        try
        {
            ZipFile zip = new ZipFile(src);
            byte[] buffer = new byte[4096];
            int readLen;

            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements())
            {
                ZipEntry entry = entries.nextElement();
                File file = new File(new File(dst), entry.getName());
                if (file.exists())
                    file.delete();
                if (entry.isDirectory())
                    file.mkdirs();
                else {
                    file.getParentFile().mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(file))
                    {
                        try (InputStream is = zip.getInputStream(entry))
                        {
                            while ((readLen = is.read(buffer)) != -1)
                                fos.write(buffer, 0, readLen);
                        }
                    }
                }
            }

            zip.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void packZip(File output, List<File> sources) throws IOException
    {
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(output));
        for (File source : sources)
        {
            if (source.isDirectory())
                zipDir(zipOut, "", source);
            else
                zipFile(zipOut, "", source);
        }
        zipOut.flush();
        zipOut.close();
    }

    private static void zipDir(ZipOutputStream zos, String path, File dir) throws IOException
    {
        if (!dir.canRead())
        {
            System.out.println("Cannot read " + dir.getCanonicalPath() + " (maybe because of permissions)");
            return;
        }

        File[] files = dir.listFiles();
        path = buildPath(path, dir.getName());

        for (File source : files)
        {
            if (source.isDirectory())
                zipDir(zos, path, source);
            else
                zipFile(zos, path, source);
        }
    }

    private static void zipFile(ZipOutputStream zos, String path, File file) throws IOException
    {
        if (!file.canRead())
        {
            System.out.println("Cannot read " + file.getCanonicalPath() + " (maybe because of permissions)");
            return;
        }
        zos.putNextEntry(new ZipEntry(buildPath(path, file.getName())));
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[4092];
        int byteCount = 0;
        while ((byteCount = fis.read(buffer)) != -1)
            zos.write(buffer, 0, byteCount);
        fis.close();
        zos.closeEntry();
    }

    public static void zipAllTo(String src, String dst)
    {
        try
        {
            try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(new File(dst))))
            {
                for (File file : new File(src).listFiles())
                {
                    if (file.isDirectory())
                        zipDir(zos, "", file);
                    else
                        zipFile(zos, "", file);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void recursiveZip(String src, String path, ZipOutputStream zos)
    {
        try
        {
            File srcFile = new File(src);
            if (srcFile.isDirectory())
            {
                for (File file : srcFile.listFiles())
                    recursiveZip(file.getAbsolutePath(), path, zos);
            } else {
                zipFile(zos, path, srcFile);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void copyContents(String src, String dst, String tmp)
    {
        try
        {
            ZipFile from = new ZipFile(src);
            ZipFile to = new ZipFile(dst);
            byte[] buffer = new byte[4096];
            int readLen;

            // extract dst first
            Enumeration<? extends ZipEntry> entries = to.entries();
            while(entries.hasMoreElements())
            {
                ZipEntry entry = entries.nextElement();
                File file = new File(new File(tmp), entry.getName());
                if (file.exists())
                    file.delete();
                file.getParentFile().mkdirs();
                try (FileOutputStream fos = new FileOutputStream(file))
                {
                    try (InputStream is = to.getInputStream(entry))
                    {
                        while ((readLen = is.read(buffer)) != -1)
                            fos.write(buffer, 0, readLen);
                    }
                }
            }

            // extract src
            entries = from.entries();
            while(entries.hasMoreElements())
            {
                ZipEntry entry = entries.nextElement();
                File file = new File(new File(tmp), entry.getName());
                if (file.exists())
                    file.delete();
                file.getParentFile().mkdirs();
                try (FileOutputStream fos = new FileOutputStream(file))
                {
                    try (InputStream is = to.getInputStream(entry))
                    {
                        while ((readLen = is.read(buffer)) != -1)
                            fos.write(buffer, 0, readLen);
                    }
                }
            }

            // write all extracted & modified files to dst
            try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(dst)))
            {
                writeDirectory(new File(tmp), zos);
            }

            from.close();
            to.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void writeDirectory(File src, ZipOutputStream zos)
    {
        try
        {
            for (File file : src.listFiles())
            {
                if (file.isDirectory())
                    writeDirectory(file, zos);
                else {
                    byte[] buffer = new byte[4096];
                    int readLen;
                    try (FileInputStream is = new FileInputStream(src))
                    {
                        while ((readLen = is.read(buffer)) != -1)
                            zos.write(buffer, 0, readLen);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static String buildPath(String path, String file)
    {
        if (path == null || path.isEmpty())
        {
            return file;
        } else {
            return path + "/" + file;
        }
    }
}