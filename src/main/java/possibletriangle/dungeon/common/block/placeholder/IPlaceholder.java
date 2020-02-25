package possibletriangle.dungeon.common.block.placeholder;

public interface IPlaceholder {

    /**
     * @return The blocks Placeholder type
     */
    Type getType();

    default void modifyItem(Item.Properties rops) {}

}
