package possibletriangle.dungeon.common.world.modi;


public class Interval {

   public final int ticks;
   public final BiConsumer<World,Integer> consumer;
   
   public Interval(int ticks, BiConsumer<World,Integer> consumer) {
       this.ticks = ticks;
       this.consumer = consumer;
   }

}