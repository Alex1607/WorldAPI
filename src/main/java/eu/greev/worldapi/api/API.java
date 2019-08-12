package eu.greev.worldapi.api;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class API {

    /**
     * Will return the name of a random File / Dictory in the given folder.
     *
     * @param Path of a folder
     * @return
     */
    public String getRandomFile(String path) {
        final File dir = new File(path);
        String x;
        File[] files = dir.listFiles();
        int idx = (int) (Math.random() * Objects.requireNonNull(files).length);
        x = files[idx].getName();
        return x;
    }

    /**
     * Deletes the directory with all files in it.
     *
     * @param Path of the directory to delete
     * @return
     */
    public boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            for (File file : Objects.requireNonNull(files)) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        return (path.delete());
    }

    /**
     * Unloads the World and deletes the directory of the world
     *
     * @param World
     * @return
     */
    public boolean removeWorld(String world) {
        Bukkit.unloadWorld(world, false);
        deleteDirectory(new File(world));
        return true;
    }

    /**
     * Loads a zip file as a world with a given name.
     * @param file ZIP File
     * @return true if the world loaded successful and false if not
     */
    public boolean loadMap(File file, String name) {
        int i = file.getName().lastIndexOf('.');
        if(i == 0) {
            return false;
        }
        if(!file.getName().substring(i+1).toLowerCase().equals("zip")) {
            return false;
        }
        try {
            ZipFile zipFile = new ZipFile(file);
            File destination = new File(name);
            if(destination.exists()) {
                return false;
            } else {
                destination.mkdir();
            }
            zipFile.extractAll(name);
        } catch (ZipException e) {
            e.printStackTrace();
            return false;
        }
        loadAsVoid(name);
        return true;
    }

    /**
     * Loads a zip file as a world
     *
     * @param file ZIP File
     * @return Name of the loaded world or null if an error has occurred
     */
    public String loadMap(File file) {
        if(file.isDirectory()) {
            return null;
        }
        int i = file.getName().lastIndexOf('.');
        if(i == 0) {
            return null;
        }
        if(!file.getName().substring(i+1).toLowerCase().equals("zip")) {
            return null;
        }
        String name = UUID.randomUUID().toString();
        try {
            ZipFile zipFile = new ZipFile(file);
            File destination = new File(name);
            if(destination.exists()) {
                return null;
            } else {
                destination.mkdir();
            }
            zipFile.extractAll(name);
        } catch (ZipException e) {
            e.printStackTrace();
        }
        loadAsVoid(name);
        return name;
    }

    /**
     * Saved the dictory of the world.
     * However it does not save the world ingame. So its best combined with a "Bukkit.getWorld(world).save();"
     * @param world World that will be saved
     * @param output Requires a path and filename with the .zip extention like: worlds/world.zip
     * @param compress Should the zip file be compressed or just store the world
     * @return true if the world was saved successful and false if not
     */
    public boolean saveMap(String world, File output, boolean compress) {
        ZipFile zipFile = new ZipFile(output);
        File path = new File(world);

        ZipParameters zipParameters = new ZipParameters();
        if(compress) {
            zipParameters.setCompressionMethod(CompressionMethod.DEFLATE);
        } else {
            zipParameters.setCompressionMethod(CompressionMethod.STORE);
        }

        for (final File fileEntry : path.listFiles()) {
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

    /**
     * Loads a world directory if its already in the server folder. If not it will create a new void world.
     *
     * @param worldname
     */
    public void loadAsVoid(String worldname) {
        WorldCreator creator = new WorldCreator(worldname);
        creator = creator.copy(creator).generateStructures(false);
        creator = creator.copy(creator).generator(new VoidGenerator());
        creator = creator.copy(creator).type(WorldType.FLAT);
        Bukkit.getServer().createWorld(creator);
    }
}
