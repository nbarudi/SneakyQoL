package ca.bungo.sneakyqol.utility;

import ca.bungo.sneakyqol.SneakyQOL;
import ca.bungo.sneakyqol.SneakyQOLClient;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ResourceExtractor {

    public static File extractResourceToConfig(String resourcePath, String configDir) throws IOException {
        File modelDir = new File(configDir, resourcePath);
        if (modelDir.exists() && modelDir.isDirectory()) {
            return modelDir;
        }

        InputStream resourceStream = ResourceExtractor.class.getClassLoader().getResourceAsStream(resourcePath);
        if (resourceStream == null) {
            throw new FileNotFoundException("Resource not found: " + resourcePath);
        }

        copyInputStreamToFile(resourceStream, modelDir);

        return modelDir;
    }

    private static void copyInputStreamToFile(InputStream source, File destination) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        try (OutputStream out = new FileOutputStream(destination)) {
            while ((length = source.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        }
    }

    public static void copyDirectoryFromResources(String resourcePath, File targetDir) throws IOException {
        Path targetPath = targetDir.toPath();
        try (InputStream is = SneakyQOL.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            try (ZipInputStream zis = new ZipInputStream(is)) {
                ZipEntry zipEntry;
                while ((zipEntry = zis.getNextEntry()) != null) {
                    Path newPath = zipSlipProtect(zipEntry, targetPath);
                    if (zipEntry.isDirectory()) {
                        Files.createDirectories(newPath);
                    } else {
                        if (newPath.getParent() != null) {
                            if (Files.notExists(newPath.getParent())) {
                                Files.createDirectories(newPath.getParent());
                            }
                        }
                        try (OutputStream fos = Files.newOutputStream(newPath)) {
                            byte[] buffer = new byte[1024];
                            int len;
                            while ((len = zis.read(buffer)) > 0) {
                                fos.write(buffer, 0, len);
                            }
                        }
                    }
                }
            }
        }
    }

    private static Path zipSlipProtect(ZipEntry zipEntry, Path targetDir) throws IOException {
        Path targetDirResolved = targetDir.resolve(zipEntry.getName());
        Path normalizePath = targetDirResolved.normalize();
        if (!normalizePath.startsWith(targetDir)) {
            throw new IOException("Bad zip entry: " + zipEntry.getName());
        }
        return normalizePath;
    }
}
