package mc.promcteam.engine.utils.actions.actions.list;

import mc.promcteam.engine.NexPlugin;
import mc.promcteam.engine.utils.MsgUT;
import mc.promcteam.engine.utils.actions.actions.IActionExecutor;
import mc.promcteam.engine.utils.actions.actions.IActionType;
import mc.promcteam.engine.utils.actions.params.IParamResult;
import mc.promcteam.engine.utils.actions.params.IParamType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class Action_Sound extends IActionExecutor {

    public Action_Sound(@NotNull NexPlugin<?> plugin) {
        super(plugin, IActionType.SOUND);
    }

    @Override
    @NotNull
    public List<String> getDescription() {
        return plugin.lang().Core_Editor_Actions_Action_Sound_Desc.asList();
    }

    @Override
    public void registerParams() {
        this.registerParam(IParamType.TARGET);
        this.registerParam(IParamType.NAME);
    }

    @Override
    protected void execute(@NotNull Entity exe, @NotNull Set<Entity> targets, @NotNull IParamResult result) {
        String name = result.getParamValue(IParamType.NAME).getString(null);
        if (name == null) return;

        for (Entity e : targets) {
            if (e.getType() == EntityType.PLAYER) {
                MsgUT.sound((Player) e, name);
            }
        }
    }

    @Override
    public boolean mustHaveTarget() {
        return true;
    }

}
