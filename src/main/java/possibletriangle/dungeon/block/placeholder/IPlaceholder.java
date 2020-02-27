package possibletriangle.dungeon.block.placeholder;

import net.minecraft.item.Item;

public interface IPlaceholder {

    /**
     * @return The blocks Placeholder type
     */
    Type getType();

    default void modifyItem(Item.Properties props) {}

}
