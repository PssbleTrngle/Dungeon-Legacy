package possibletriangle.common.item.spells;

public abstract class Spell extends ForgeRegistryEntry<Spell> {

    public int maxRange() {
        return 5;
    }

    public abstract int maxPower();

    public abstract use(UseContext context);

    public static class UseContext {
        private final Vec3d pos;
        private final World world;
        private final Player user;
        private final Optional<Entity> hit;
        private final int power;

        public UseContext(Vec3d pos, World world, Player user, Optional<Entity> hit, int strength) {
            this.pos = pos;
            this.world = world;
            this.user = user;
            this.hit = hit;
            this.strength = strength;
        }

        public UseContext(Vec3d pos, World world, Player user, int strength) {
            this(pos, world, user, Optional.empty(), strength);
        }

        public UseContext(Vec3d pos, World world, Player userm, @Nullable Entity hit, int strength) {
            this(pos, world, user, Optional.ofNullable(hit), strength);
        }
    }

}