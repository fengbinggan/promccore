package mc.promcteam.engine.core.config;

import mc.promcteam.engine.NexEngine;
import mc.promcteam.engine.config.api.IConfigTemplate;
import mc.promcteam.engine.utils.StringUT;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CoreConfig extends IConfigTemplate {

    public static String MODULES_PATH_INTERNAL = "/modules/";
    public static String MODULES_PATH_EXTERNAL = "/modules/_external/";

    private static Map<String, String> LOCALE_WORLD_NAMES;

    public CoreConfig(@NotNull NexEngine plugin) {
        super(plugin);
    }

    @Override
    public void load() {
        this.cfg.addMissing("locale.world-names.world", "World");
        this.cfg.addMissing("locale.world-names.world_nether", "Nether");
        this.cfg.addMissing("locale.world-names.world_the_end", "The End");

        LOCALE_WORLD_NAMES = new HashMap<>();
        this.cfg.getSection("locale.world-names").forEach(world -> {
            String name = StringUT.color(this.cfg.getString("locale.world-names." + world, world));
            LOCALE_WORLD_NAMES.put(world, name);
        });
    }

    @NotNull
    public static String getWorldName(@NotNull String world) {
        return LOCALE_WORLD_NAMES.getOrDefault(world, world);
    }
}
