package possibletriangle.dungeon.common.world.modi;


@Mod.EventbusSubscriber()
public abstract class Gamemodus {

    private static final HashMap<ResourceLocation,Function<GameplayData,Gamemodus>> MODI = new HashMap<>();

    public static Collection<ResourceLocation> keys() {
        return MODI.keys();
    }

    static Optional<Gamemodus> find(ResourceLocation key) {
        return Optional.ofNullable(MODI.get(key));
    }

    public static void register(ResourceLocation key, Function<GameplayData,Gamemodus> supplier) {
        this.MODI.put(key, supplier);
    }

    private final GameplayData data;
    public Gamemodus(GameplayData data) {
        this.data = data;
    }

    /**
     * Called everytime the world is loaded or the modus is set
     * Used to register intervals and apply gamerules
     */
    public static void loadModus(World world) {
        GameplayData.getModus(world).ifPresent(modus -> {
            if(!modus.loaded) {
                modus.onLoad(modus::addInterval, event.getWorld());
                modus.loaded = true;
            }
        });
    }

    @SubscibeEvent
    public static void serverLoad(ServerLoadEvent event) {
        loadModus(event.getWorld());
    }

    public void markDirty() {
        this.data.markDirty();
    }

    @SubscibeEvent
    public static void serverTick(TickEvent.Server event) {
        World world = event.getWorld();

        GameplayData.getModus(world).ifPresent(modus -> {

            modus.INTERVALS.forEach((key, interval) -> {

                if(ticks % interval.ticks) {
                    int execution = modus.INTERVAL_EXECUTIONS.get(key) + 1;
                    interval.consumer.accept(world, execution);
                    modus.INTERVAL_EXECUTIONS.put(key, execution);
                }

            });

            modus.ticks++;
            modus.markDirty();
        });
    }

    @SubscibeEvent
    public static void playerTick(PlayerEvent.Tick event) {
        GameplayData.getModus(event.getWorld()).ifPresent(modus -> 
            modus.onTick(event.getPlayer())
        );
    }

    @SubscibeEvent
    public static void entityConstruction(EntityEvent.Construction event) {
        if(event.getEntity() instanceof PlayerEntity) {

            PlayerEntity player = (PlayerEntity) event.getEntity();
            GameplayData data = GameplayData.getInstace();

            MODI.forEach((key, supplier) -> {

                Gamemodus created = supplier.apply(data);
                created.registerPlayerData(player.getDataManager());

            });
        }
    }

    @SubscibeEvent
    public static void playerJoined(PlayerEvent.Join event) {
    }

    @SubscibeEvent
    public static void playerKilled(PlayerEvent.Die event) {
        PlayerEntity victim = event.getPlayer();
        Optional<PlayerEntity> killer = Optional.empty();

        GameplayData.getModus(event.getWorld()).ifPresent(modus -> 
            modus.onKill(victim, killer)
        );
    }

    private int ticks = 0;
    private boolean loaded = false;
    private final HashMap<String,Interval> INTERVALS = new HashMap<>();
    private final HashMap<String,Integer> INTERVAL_EXECUTIONS = new HashMap<>();

    private final void addInterval(String key, Interval interval) {
        this.INTERVALS.put(key, interval);
        this.INTERVAL_EXECUTIONS.put(key, 0);
    }

    public abstract void onLoad(BiConsumer<String,Interval> intervals, World world);

    /**
     * @return The maximum time in ticks a player can stay in his base
     */
    protected abstract int roomTime();

    protected abstract void onKill(PlayerEntity victim, Optional<PlayerEntity> killer);

    protected abstract void onTick(PlayerEntity player);

    public abstract void stop(World world);

    public abstract void start(World world);

    protected void registerPlayerData(DataManager player) {}

    public final void load(CompoundNBT nbt) {

        this.INTERVAL_EXECUTIONS.clear();
        ListNBT executions = nbt.getList("executions", 10);
        executions.forEach(entry -> {
            try {

                CompoundNBT ex = (CompoundNBT) entry;
                int execution = ex.getInt("execution");
                String key = ex.getString("key");
                this.INTERVAL_EXECUTIONS.put(key, execution);

            } catch(ClassCastExeception ignored) {}
        })

        this.ticks = nbt.getInt("ticks");

        this.loadAdditional(nbt);
    }

    public final CompoundNBT save(CompoundNBT nbt) {

        ListNBT executions = new ListNBT();
        this.INTERVAL_EXECUTIONS.forEach((key, execution) -> {
            CompoundNBT ex = new CompoundNBT();
            ex.putString("key", key);
            ex.putInt("execution", execution);
            executions.add(ex);
        })
        nbt.add("executions", executions);

        nbt.putInt("ticks");

        return this.saveAdditional(nbt);
    }

    public CompoundNBT saveAdditional(CompoundNBT nbt) {
        return nbt;
    }

    public void loadAdditional(CompoundNBT nbt) {}


}