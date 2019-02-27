package possibletriangle.dungeon.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.world.World;

public class PathNavigateCurse extends PathNavigateGround {

    public PathNavigateCurse(EntityLiving entitylivingIn, World worldIn) {
        super(entitylivingIn, worldIn);
    }

    @Override
    protected PathFinder getPathFinder() {
        this.nodeProcessor = new CurseNodeProcessor();
        this.nodeProcessor.setCanEnterDoors(false);
        return new PathFinder(this.nodeProcessor);
    }
}
