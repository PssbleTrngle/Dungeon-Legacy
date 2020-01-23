package possibletriangle.dungeon.common.world.structure.metadata.condition;

import possibletriangle.dungeon.common.world.GenerationContext;

public class FloorCondition extends ConditionType {

    @Override
    public boolean test(String f, GenerationContext ctx) {
        int floor = Integer.parseInt(f);
        if(floor >= 0) return ctx.getFloor() == floor;
        int floors = ctx.settings.floors;
        return ctx.getFloor() - floors == floor;
    }
}
