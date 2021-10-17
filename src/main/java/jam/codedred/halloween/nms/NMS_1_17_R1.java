package jam.codedred.halloween.nms;

import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFollowEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.util.RayTraceResult;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static jam.codedred.halloween.utils.ChatUtil.colorize;

public class NMS_1_17_R1 implements NMS {

    private final DwellerTargetManger dwellerTargetManger;

    public NMS_1_17_R1(Location... activeDwellerTargetLocations) {
        dwellerTargetManger = new DwellerTargetManger();
        for (Location loc : activeDwellerTargetLocations) {
            dwellerTargetManger.ACTIVE_LOCATIONS_FOR_TARGETS.put(loc, false);
        }
    }

    @Override
    public void spawnDweller(World world, Location spawnLoc, Location targetLoc) {
        EntityDwellerTarget target = new EntityDwellerTarget(targetLoc);
        EntityMazeDweller dweller = new EntityMazeDweller(spawnLoc, target);
        WorldServer worldServer = ((CraftWorld) world).getHandle();
        worldServer.addEntity(dweller);

    }

    @Override
    public void spawnMazeDwellers(World world, Location... spawnLocations) {
        for (Location spawnLoc : spawnLocations) {
            EntityDwellerTarget target = new EntityDwellerTarget(new Location(spawnLoc.getWorld(), 0, 0, 0));
            EntityMazeDweller dweller = new EntityMazeDweller(spawnLoc, target);
            dwellerTargetManger.DWELLERS.put(dweller, target);
            WorldServer worldServer = ((CraftWorld) world).getHandle();
            worldServer.addEntity(dweller);
            dwellerTargetManger.computeAndSetNewTarget(dweller);
        }
    }

    private class DwellerTargetManger {

        private final Map<Location, Boolean> ACTIVE_LOCATIONS_FOR_TARGETS = new HashMap<>();
        private final Map<EntityMazeDweller, EntityDwellerTarget> DWELLERS = new HashMap<>();

        private int index = 0;

        private Map.Entry<Location, Boolean> getNextTarget() {
            Map.Entry<Location, Boolean> target = ACTIVE_LOCATIONS_FOR_TARGETS.entrySet().stream().skip(index).findFirst().get();
            index++;
            if (index > ACTIVE_LOCATIONS_FOR_TARGETS.size()) {
                index = 0;
            }
            return target;

        }

        private void computeAndSetNewTarget(EntityMazeDweller dweller) {
            Map.Entry<Location, Boolean> targetEntry = getNextTarget();
            {
                if (!targetEntry.getValue()) {
                    targetEntry.setValue(true);
                    Location targetLoc = targetEntry.getKey();
                    Location oldTargetLoc = dweller.target.getBukkitEntity().getLocation();
                    ACTIVE_LOCATIONS_FOR_TARGETS.put(oldTargetLoc, false);
                    dweller.target.setHealth(0.0F);
                    EntityDwellerTarget target = new EntityDwellerTarget(targetLoc);
                    dweller.setTarget(target);
                    dweller.setGoalTarget(target, EntityTargetEvent.TargetReason.CUSTOM, true);
                    dweller.bQ.a(0, new PathfinderGoalRoamMaze(dweller, target));
                    DWELLERS.put(dweller, target);
                }
            }
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
                setInvisible(true);
                // TODO: Remove visibility of Targets when Dwellers are functional
                setCustomName(new ChatComponentText(colorize("&6Target ")));
                setCustomNameVisible(true);

            }
        }

        private class EntityMazeDweller extends EntityCreature {

            private EntityDwellerTarget target;

            protected EntityMazeDweller(Location loc, EntityDwellerTarget target) {
                super(EntityTypes.ba, ((CraftWorld) Objects.requireNonNull(loc.getWorld(), "World was null when trying to spawn MazeDweller.")).getHandle());
                setPosition(loc.getX(), loc.getY(), loc.getZ());
                setCustomName(new ChatComponentText(colorize("&4&c&lDweller")));
                setCustomNameVisible(true);
                this.target = target;
                setGoalTarget(target, EntityTargetEvent.TargetReason.CUSTOM, true);
                bQ.a(0, new PathfinderGoalRoamMaze(this, target));
            }

            @Override
            public void tick() {
                super.tick();

                float distanceFromTarget = this.e((Entity) target);

                if (distanceFromTarget < 2f) {
                    World world = target.getBukkitEntity().getWorld();
                    Location targetLocation = new Location(world, target.locX(), target.locY(), target.locZ());
                    //TODO - fix raytracing for no blocks (kill radius)
                    Location dwellerLoc = this.getBukkitEntity().getLocation();
                    Location targetLoc = target.getBukkitEntity().getLocation();
                    RayTraceResult rayTrace = world.rayTraceBlocks(targetLocation, targetLoc.subtract(dwellerLoc).toVector(), 2D);
                    if (rayTrace != null) {
                        dwellerTargetManger.computeAndSetNewTarget(this);
                    }
                }
            }

            public void setTarget(EntityDwellerTarget target) {
                this.target = target;
            }
        }
    }