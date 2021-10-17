package jam.codedred.halloween.nms;

import org.bukkit.Location;
import org.bukkit.World;
import java.util.List;

public interface NMS {

    void spawnDweller(World world, Location spawnLoc, Location... targetLocations);

    void spawnDwellers(World world, List<Location> targetLocations, Location... spawnLocations);

}
