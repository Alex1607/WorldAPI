package eu.greev.worldapi.api.impl;

import eu.greev.worldapi.api.Api;
import eu.greev.worldapi.utils.VoidGenerator;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionMethod;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public class ApiImpl implements Api {
    private final Random random = new Random();

    public String getRandomFile(File path) {
        File[] files = path.listFiles();
        int idx = this.random.nextInt(Objects.requireNonNull(files).length);
        return files[idx].getName();
    }

    public void deleteDirectory(File path) throws IOException {
        if (path.exists()) {
            File[] files = path.listFiles();
            for (File file : Objects.requireNonNull(files)) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    Files.delete(file.toPath());
                }
            }
        }
        Files.delete(path.toPath());
    }

    public boolean removeWorld(String world) {
        Bukkit.unloadWorld(world, false);
        try {
            deleteDirectory(new File(world));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean loadMap(File file, String name) {
        if (file.isDirectory()) {
            return false;
        }

        int i = file.getName().lastIndexOf('.');

        if (i == 0 || !file.getName().substring(i + 1).equalsIgnoreCase("zip")) {
            return false;
        }

        try {
            ZipFile zipFile = new ZipFile(file);
            File destination = new File(name);

            if (destination.exists()) {
                return false;
            }

            boolean folderCreated = destination.mkdir();

            if (!folderCreated) {
                return false;
            }

            zipFile.extractAll(name);
        } catch (ZipException e) {
            e.printStackTrace();
        }

        loadAsVoid(name);

        return true;
    }

    public Optional<String> loadMap(File file) {
        String name = UUID.randomUUID().toString();
        if (loadMap(file, name)) {
            return Optional.of(name);
        } else {
            return Optional.empty();
        }
    }

    public boolean saveMap(String world, File output, boolean compress) {
        ZipFile zipFile = new ZipFile(output);
        File path = new File(world);

        ZipParameters zipParameters = new ZipParameters();
        if (compress) {
            zipParameters.setCompressionMethod(CompressionMethod.DEFLATE);
        } else {
            zipParameters.setCompressionMethod(CompressionMethod.STORE);
        }

        for (final File fileEntry : Objects.requireNonNull(path.listFiles())) {
            if (fileEntry.isDirectory()) {
                try {
                    saveMap(String.valueOf(fileEntry.getCanonicalFile()), output, compress);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    zipFile.addFolder(fileEntry.getAbsoluteFile(), zipParameters);
                } catch (ZipException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    zipFile.addFile(fileEntry.getAbsoluteFile(), zipParameters);
                } catch (ZipException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public void loadAsVoid(String worldname) {
        WorldCreator creator = new WorldCreator(worldname);

        creator = creator.copy(creator).generateStructures(false);
        creator = creator.copy(creator).generator(new VoidGenerator());
        creator = creator.copy(creator).type(WorldType.FLAT);

        Bukkit.getServer().createWorld(creator);
    }
}
