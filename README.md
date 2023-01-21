# WorldAPI
This WorldAPI was initially made for my server, however I have switched to SWM.
I still think that this API can come in handy for some people, so here is it.

## Information
What I should mention: This is by no means a replacement for a good worldmanager like multiverse, I made this API exclusively for minigames. That's also the reason you are not able to set a worldname and instead will receive an uuid as a string.
(In case its not clear, my intention was to load a temporary world — hence the uuid — for a minigames and then delete it when its no longer needed)

## Usage
First you have to get the WorldAPI instance. To do so include this code in your @onEnable methode.  
`worldapi = new WorldAPI().getAPI();`
after that you have 7 methodes to work with:
```java
    /**
     * Will return the name of a random File / Directory in the given folder.
     *
     * @param path of a directory
     * @return String
     */
    String getRandomFile(File path);

    /**
     * Deletes the directory with all files in it.
     *
     * @param path of the directory to delete
     * @throws IOException If the file deletion wasn't successful
     */
    void deleteDirectory(File path) throws IOException;

    /**
     * Unloads the World and deletes the directory of the world
     *
     * @param world Name of the World which should be removed from the server
     * @return a boolean that indicates if the removing was successful
     */
    boolean removeWorld(String world);

    /**
     * Loads a zip file as a world with a given name.
     *
     * @param file ZIP File
     * @return true if the world loaded successful and false if not
     */
    boolean loadMap(File file, String name);

    /**
     * Loads a zip file as a world
     *
     * @param file ZIP File
     * @return Name of the loaded world or an empty Optional if an error has occurred
     */
    Optional<String> loadMap(File file);

    /**
     * Saved the directory of the world.
     * However, it does not save the world ingame. So it's best combined with a `Bukkit.getWorld(world).save();`, else it will not represent the latest changes that happened ingame.
     *
     * @param world    World that will be saved
     * @param output   Requires a path and filename with the .zip extension like: worlds/world.zip
     * @param compress Should the zip file be compressed or just store the world
     * @return true if the world was saved successful and false if not
     */
    boolean saveMap(String world, File output, boolean compress);

    /**
     * Loads a world directory if its already in the server folder.
     * If not it will create a new void world.
     *
     * @param worldname Name of the world, this has to be uniq
     */
    void loadAsVoid(String worldname);
```
