package jam.codedred.halloween.nms;

import jam.codedred.halloween.utils.NMSUtil;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFollowEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.event.entity.EntityTargetEvent;
import java.util.Objects;
import static jam.codedred.halloween.utils.ChatUtil.colorize;

public class NMS_1_17_R1 implements jam.codedred.halloween.nms.NMS {

    @Override
    public void spawnDweller(World world, Location spawnLoc, Location targetLoc) {
        EntityDwellerTarget target = new EntityDwellerTarget(targetLoc);
        MazeDweller dweller = new MazeDweller(spawnLoc, target);
        WorldServer worldServer = ((CraftWorld) world).getHandle();
        worldServer.addEntity(target);
        worldServer.addEntity(dweller);

    }

    // TODO - Somehow do pathfinding through walls, otherwise settle for a flat maze
    // TODO - NOTE: When enclosed with 1 path only to Target, Dweller will successfully follow path . . .
    private class PathfinderGoalRoamMaze extends PathfinderGoalFollowEntity {

        private final EntityInsentient mob;
        private final EntityInsentient target;

        private double x;
        private double y;
        private double z;


        public PathfinderGoalRoamMaze(EntityInsentient mob, EntityInsentient target) {
            super(mob, 1, 1000, 1000);
            this.mob = mob;
            this.target = target;
        }

        // runs every tick - checks if mob can do pathfindergoal, and updates if it can
        @Override
        public boolean a() {

            if (target == null)
                return false;

            x = target.locX();
            y = target.locY();
            z = target.locZ();

            return true; // runs c();
        }

        // Runs if b() is true (keeps navigating mob).
        @Override
        public void c() {
            mob.getNavigation().a(x, y, z, 1f);
        }

        // Checks if pathfinder goal is finished or not.
        @Override
        public boolean b() {
            return !mob.getNavigation().m();
        }
    }

    private class EntityDwellerTarget extends EntityCreature {

        protected EntityDwellerTarget(Location loc) {
            super(EntityTypes.be, ( (CraftWorld) Objects.requireNonNull(loc.getWorld(), "World was null when trying to spawn MazeDweller.")).getHandle());
            setLocation(loc.getX(), loc.getY(), loc.getZ(), 0f, 0f);
            // TODO: Remove visibility of Targets when Dwellers are functional
            setCustomName(new ChatComponentText(colorize("&6Dweller Target")));
            setCustomNameVisible(true);

        }
    }

     private class MazeDweller extends EntityCreature {

        protected MazeDweller(Location loc, EntityDwellerTarget target) {
            super(EntityTypes.ba, ((CraftWorld) Objects.requireNonNull(loc.getWorld(), "World was null when trying to spawn MazeDweller.")).getHandle());
            setPosition(loc.getX(), loc.getY(), loc.getZ());
            setCustomName(new ChatComponentText(colorize("&4&c&lDwellers")));
            setCustomNameVisible(true);
            setGoalTarget(target, EntityTargetEvent.TargetReason.CUSTOM, true);
            bQ.a(0, new PathfinderGoalRoamMaze(this, target));

        }

        // Get rid of wither skeletons' normal pathfinders.
        @Override
        protected void initPathfinder() {

        }

    }


}
