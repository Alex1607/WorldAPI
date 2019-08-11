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
     * Returns a random File / Folder from a directory
     *
     * @param path
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
     * Deletes a given directory with all files and folder in it.
     *
     * @param path
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
     * Unloads AND deletes a world
     *
     * @param world
     * @return
     */
    public boolean removeWorld(String world) {
        Bukkit.unloadWorld(world, false);
        deleteDirectory(new File(world));
        return true;
    }

    /**
     * Loads a given ZIP File as a world.
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
     * Loads a given ZIP File as a world and returns the Name of the world. (Random Name!)
     *
     * @param file ZIP File
     * @return Name of the loaded world or null if file is not a ZIP File
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
     * Saves a world as a zip file. WARNING! It does NOT save the world ingame! Only the folders so do a Bukkit.getWorld(world).save(); befor you run this.
     * @param world
     * @param output Requires a path and filename with the .zip extention like: worlds/world.zip
     * @param compress should the zip file also be compressed or just store the world
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
     * Loads a world which is already unziped
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
