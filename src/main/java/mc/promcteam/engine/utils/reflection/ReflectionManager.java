package mc.promcteam.engine.utils.reflection;

import mc.promcteam.engine.core.Version;
import org.bukkit.Bukkit;

public class ReflectionManager {

    public static final String         VERSION       = Bukkit.getServer().getClass().getPackage().getName().contains("mockbukkit")
            ? "testing_19"
            : Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
    public static final int            MINOR_VERSION = Integer.parseInt(VERSION.split("_")[1]);
    private static      ReflectionUtil reflection;

    public static ReflectionUtil getReflectionUtil() {
        if (reflection != null) return reflection;

        switch (Version.CURRENT) {
            case V1_17_R1:
            case V1_18_R1:
            case V1_18_R2:
            case V1_19_R1:
            case V1_19_R2:
                reflection = new Reflection_1_17();
                break;
            default:
                reflection = new DefaultReflectionUtil();
        }

        return reflection;
    }

}
