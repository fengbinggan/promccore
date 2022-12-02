package mc.promcteam.engine.utils.actions.actions.list;

import mc.promcteam.engine.NexPlugin;
import mc.promcteam.engine.utils.actions.actions.IActionExecutor;
import mc.promcteam.engine.utils.actions.actions.IActionType;
import mc.promcteam.engine.utils.actions.params.IParamResult;
import mc.promcteam.engine.utils.actions.params.IParamType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class Action_Damage extends IActionExecutor {

    public Action_Damage(@NotNull NexPlugin<?> plugin) {
        super(plugin, IActionType.DAMAGE);
    }

    @Override
    @NotNull
    public List<String> getDescription() {
        return plugin.lang().Core_Editor_Actions_Action_Damage_Desc.asList();
    }

    @Override
    public void registerParams() {
        this.registerParam(IParamType.TARGET);
        this.registerParam(IParamType.AMOUNT);
    }

    @Override
    protected void execute(@NotNull Entity exe, @NotNull Set<Entity> targets, @NotNull IParamResult result) {
        double dmg = result.getParamValue(IParamType.AMOUNT).getDouble(0);
        if (dmg == 0) return;

        Entity eDamager = exe;

        Projectile pj = null;

        if (eDamager instanceof Projectile) {
            pj = (Projectile) eDamager;
            ProjectileSource src = pj.getShooter();
            if (src instanceof LivingEntity) {
                eDamager = (Entity) src;
            }
        }

        for (Entity eTarget : targets) {
            if (!(eTarget instanceof LivingEntity)) continue;
            LivingEntity victim = (LivingEntity) eTarget;

            victim.damage(dmg, eDamager);
        }
    }

    @Override
    public boolean mustHaveTarget() {
        return true;
    }

}
