package jam.codedred.halloween.nms;

import org.bukkit.Location;
import org.bukkit.World;

public interface NMS {

    void spawnDweller(World world, Location spawnLoc, Location targetLoc);

    void spawnMazeDwellers(World world, Location... spawnLocations);

}
