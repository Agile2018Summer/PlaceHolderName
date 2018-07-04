package edu.harvard.integration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.*;

/**
 * A basic wrapper for Properties.
 * This allows converting values to types such as Booleans, without having to repeat logic.
 */
public class Config extends Properties {
    public Config() {
        File config = new File("config.cfg");
        if (!config.exists()) {
            try {
                if (!config.createNewFile()) {
                    Integrator.getLogger().warn("Failed to create config file.");
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (FileInputStream input = new FileInputStream(config)) {//Load current settings
            load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        addMissingValues(config);
    }

    /**
     * Adds any missing values to the config file.
     * @param config The file to check for missing values, and add any that are missing.
     */
    private void addMissingValues(File config) {
        try (OutputStream output = new FileOutputStream(config)) {
            if (!containsKey("SLACK_TOKEN"))
                setProperty("SLACK_TOKEN", "token");
            if (!containsKey("INFO_CHANNEL"))
                setProperty("INFO_CHANNEL", "info_channel");

            store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    /**
     * Gets an object with the specified type from the config with the given key, or returns the given default if the key is not found.
     * <p>
     * <p>This is a wrapper for {@link #getOrDefault(Class, String, Object)} so that if the default is not null it does not require the class type.</p>
     * @param key          The key to search the config for.
     * @param defaultValue The value to return if the key is not found in the config.
     * @param <T>          The type of the object to retrieve from the config.
     * @return The value in the config for the specified key, or returns the given value if the key is not found.
     */
    @Nonnull
    public <T> T getOrDefault(@Nonnull String key, @Nonnull T defaultValue) {
        T v = getOrDefault((Class<T>) defaultValue.getClass(), key, defaultValue);
        return v == null ? defaultValue : v;//v should never be able to be null but just in case
    }

    /**
     * Gets an object with the specified type from the config with the given key, or returns the given default if the key is not found.
     * <p>
     * <p>To add create new objects that this method can read from a config create a valueOf({@link String}) method.</p>
     * @param c            The class that determines the type that will be returned.
     * @param key          The key to search the config for.
     * @param defaultValue The value to return if the key is not found in the config.
     * @param <T>          The type of the object to retrieve from the config.
     * @return The value in the config for the specified key, or returns the given value if the key is not found.
     */
    @Nullable
    public <T> T getOrDefault(@Nonnull Class<T> c, @Nonnull String key, @Nullable T defaultValue) {
        T value = defaultValue;
        if (!containsKey(key))
            return value;
        String cValue = getProperty(key);
        if (String.class.isAssignableFrom(c))
            return (T) cValue;
        try {
            value = (T) c.getMethod("valueOf", String.class).invoke(null, cValue);
        } catch (InvocationTargetException | SecurityException | IllegalAccessException e) { //Should not be thrown
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            if (Collection.class.isAssignableFrom(c)) {
                String[] split = cValue.split(",");
                Collection<String> t = getCollection(c);
                if (t == null)
                    Integrator.getLogger().error("Unable to find a default Collection implementation for class: " + c.getSimpleName() + '.');
                else {
                    Collections.addAll(t, split);
                    value = (T) t;
                }
            } else if (Map.class.isAssignableFrom(c)) {
                String[] split = cValue.split(",");
                Map<String, String> t = getMap(c);
                if (t == null)
                    Integrator.getLogger().error("Unable to find a default Map implementation for class: " + c.getSimpleName() + '.');
                else {
                    for (String info : split) {
                        String[] s = info.split(";");
                        if (s.length == 2)
                            t.put(s[0], s[1]);
                        else //Error
                            Integrator.getLogger().error("Unable to add: '" + info + "' to map.");
                    }
                    value = (T) t;
                }
            } else {//TODO: Add more wrappers if needed
                System.out.println("No method found, key is: " + key + ". Passed class type: " + c.getSimpleName());
            }
        } catch (IllegalArgumentException ignored) {//Invalid param, return the default
        }
        return value;
    }

    /**
     * Creates an empty {@link Map} from the given type.
     * @param c The class to attempt to create a {@link Map} from.
     * @return An empty {@link Map} from the given type.
     */
    private Map<String, String> getMap(Class<?> c) {//TODO: Figure out how to have keys or values that are not strings
        Map<String, String> t = null;//Once that is figured out change to just be Map instead of Map<String, String>?
        try {
            t = (Map<String, String>) c.newInstance();
        } catch (InstantiationException ignored) {
            if (ConcurrentNavigableMap.class.isAssignableFrom(c))
                t = new ConcurrentSkipListMap<>();
            else if (ConcurrentMap.class.isAssignableFrom(c))
                t = new ConcurrentHashMap<>();
            else if (SortedMap.class.isAssignableFrom(c))
                t = new TreeMap<>();
            else
                t = new HashMap<>();
        } catch (IllegalAccessException e1) { //Should not happen
            e1.printStackTrace();
        }
        return t;
    }

    /**
     * Creates an empty {@link Collection} from the given type.
     * @param c The class to attempt to create a {@link Collection} from.
     * @return An empty {@link Collection} from the given type.
     */
    private Collection<String> getCollection(Class<?> c) {//TODO: Figure out how to do types such as List<Integer>
        Collection<String> t = null;//Once that is figured out change to just be Collection instead of Collection<String>?
        try {
            t = (Collection<String>) c.newInstance();
        } catch (InstantiationException ignored) {
            if (List.class.isAssignableFrom(c))
                t = new ArrayList<>();
            else if (Set.class.isAssignableFrom(c)) {
                if (SortedSet.class.isAssignableFrom(c))
                    t = new TreeSet<>();
                else
                    t = new HashSet<>();
            } else if (Queue.class.isAssignableFrom(c)) {
                if (BlockingQueue.class.isAssignableFrom(c)) {
                    if (BlockingDeque.class.isAssignableFrom(c))
                        t = new LinkedBlockingDeque<>();
                    else if (TransferQueue.class.isAssignableFrom(c))
                        t = new LinkedTransferQueue<>();
                } else //It is a Queue or a Dequeue
                    t = new LinkedList<>();
            }
        } catch (IllegalAccessException e1) { //Should not happen
            e1.printStackTrace();
        }
        return t;
    }
}