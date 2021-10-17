package jam.codedred.halloween.nms;

import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFollowEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.util.RayTraceResult;
import java.util.*;

import static jam.codedred.halloween.utils.ChatUtil.colorize;

public class NMS_1_17_R1 implements NMS {


    public NMS_1_17_R1() {
    }

    // Spawns a dweller with 2 locations it will periodically go back and forth to and from
    @Override
    public void spawnDweller(World world, Location spawnLoc, Location... targetLocations) {
        List<EntityDwellerTarget> targets = new ArrayList<>();
        targets.add(new EntityDwellerTarget(targetLocations[0]));
        targets.add(new EntityDwellerTarget(targetLocations[1]));
        EntityMazeDweller dweller = new EntityMazeDweller(spawnLoc, targets);
        WorldServer worldServer = ((CraftWorld) world).getHandle();
        worldServer.addEntity(dweller);
    }

    //
    @Override
    public void spawnDwellers(World world, List<Location> targetLocations, Location... spawnLocations) {
        for (Location spawnLoc : spawnLocations) {
            spawnDweller(world, spawnLoc, targetLocations.toArray(Location[]::new));
        }
    }


    private class PathfinderGoalRoamMaze extends PathfinderGoalFollowEntity {

        private final EntityInsentient mob;
        private EntityInsentient target;

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

            target = (EntityInsentient) mob.getGoalTarget();

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
            super(EntityTypes.be, ((CraftWorld) Objects.requireNonNull(loc.getWorld(), "World was null when trying to spawn MazeDweller.")).getHandle());
            setLocation(loc.getX(), loc.getY(), loc.getZ(), 0f, 0f);
            setInvulnerable(true);
            //setInvisible(true);
            // TODO: Remove visibility of Targets when Dwellers are functional
            setCustomName(new ChatComponentText(colorize("&6Target")));
            setCustomNameVisible(true);

        }
    }

    private class EntityMazeDweller extends EntityCreature {

        private final List<EntityDwellerTarget> targets;
        private EntityDwellerTarget currentTarget;
        // 0 or 1, Dwellers will go from one target to another and back again forever
        private int currentTargetIndex;

        protected EntityMazeDweller(Location loc, List<EntityDwellerTarget> targets) {
            super(EntityTypes.ba, ((CraftWorld) Objects.requireNonNull(loc.getWorld(), "World was null when trying to spawn MazeDweller.")).getHandle());
            setPosition(loc.getX(), loc.getY(), loc.getZ());
            setCustomName(new ChatComponentText(colorize("&4&c&lDweller")));
            setCustomNameVisible(true);
            currentTargetIndex = 0;
            this.targets = targets;
            this.currentTarget = targets.get(currentTargetIndex);
            setGoalTarget(currentTarget, EntityTargetEvent.TargetReason.CUSTOM, true);
            bQ.a(0, new PathfinderGoalRoamMaze(this, currentTarget));
        }

        @Override
        public void tick() {
            super.tick();

            float distanceFromTarget = this.e((Entity) currentTarget);
            Bukkit.getLogger().info("distance: " + distanceFromTarget);
            Bukkit.getLogger().info("Current Target: " + currentTarget.getName());
            Bukkit.getLogger().info("can target even do stuff: " + currentTarget.isAlive());

            if (distanceFromTarget < 2f) {
                World world = currentTarget.getBukkitEntity().getWorld();
                Location targetLocation = new Location(world, currentTarget.locX(), currentTarget.locY(), currentTarget.locZ());
                //TODO - fix raytracing for no blocks (kill radius)
                Location dwellerLoc = this.getBukkitEntity().getLocation();
                Location targetLoc = currentTarget.getBukkitEntity().getLocation();
                RayTraceResult rayTrace = world.rayTraceBlocks(targetLocation, targetLoc.subtract(dwellerLoc).toVector(), 2D);
                if (rayTrace != null) {
                    nextTarget();
                }
            }
        }

        public void nextTarget() {
            currentTargetIndex++;
            if (currentTargetIndex > 1) {
                currentTargetIndex = 0;
            }
            currentTarget = targets.get(currentTargetIndex);
            setGoalTarget(currentTarget, EntityTargetEvent.TargetReason.CUSTOM, true);
            bQ.a(0, new PathfinderGoalRoamMaze(this, currentTarget));
        }

    }

}