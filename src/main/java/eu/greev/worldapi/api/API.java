package eu.greev.worldapi.api;

import eu.greev.worldapi.utils.VoidGenerator;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

import java.io.File;
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
     * @return true if the world loaded successfull and false if not
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
