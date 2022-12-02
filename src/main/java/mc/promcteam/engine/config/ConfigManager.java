package mc.promcteam.engine.config;

import mc.promcteam.engine.NexPlugin;
import mc.promcteam.engine.config.api.JYML;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ConfigManager<P extends NexPlugin<P>> {

    @NotNull
    private P plugin;

    public        JYML configMain;
    public        JYML configLang;
    public static JYML configTemp;

    public ConfigManager(@NotNull P plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        this.extract("lang");

        this.configMain = JYML.loadOrExtract(plugin, "config.yml");
        this.configLang = JYML.loadOrExtract(plugin, "/lang/messages_" + configMain.getString("core.lang", "en").toLowerCase() + ".yml");

        // Load plugin config.
        this.plugin.setConfig();

        if (this.plugin.isEngine()) {
            configTemp = JYML.loadOrExtract(plugin, "temp.yml");
        }
    }

    public void extract(@NotNull String folder) {
        if (!folder.startsWith("/")) {
            folder = "/" + folder;
        }
        if (!folder.endsWith("/")) {
            folder += "/";
        }
        this.extractFullPath(plugin.getDataFolder() + folder);
    }

    public void extractFullPath(@NotNull String path) {
        this.extractFullPath(path, "yml", false);
    }

    public void extractFullPath(@NotNull String path, boolean override) {
        this.extractFullPath(path, "yml", override);
    }

    public void extractFullPath(@NotNull String path, @NotNull String extension) {
        this.extractFullPath(path, extension, false);
    }

    public void extractFullPath(@NotNull String path, @NotNull String extension, boolean override) {
        File   f       = new File(path);
        String jarPath = path.replace(plugin.getDataFolder() + "", "");
        if (jarPath.startsWith("/")) {
            jarPath = jarPath.substring(1, jarPath.length());
        }
        if (jarPath.endsWith("/")) {
            jarPath = jarPath.substring(0, jarPath.length() - 1);
        }

        if (!f.exists()) {
            ResourceExtractor extract = new ResourceExtractor(plugin, f, jarPath, ".*\\.(" + extension + ")$");

            try {
                extract.extract(override, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Nullable
    public static UUID getTempUUID(@NotNull String id) {
        if (id.isEmpty()) return null;
        configTemp.addMissing(id, UUID.randomUUID().toString());
        configTemp.saveChanges();
        try {
            return UUID.fromString(configTemp.getString(id));
        } catch (IllegalArgumentException ex) {
            UUID uid = UUID.randomUUID();
            configTemp.set(id, uid.toString());
            configTemp.saveChanges();
            return uid;
        }
    }
}
