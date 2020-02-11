package possibletriangle.dungeon.common.world;

@Mod.EventbusSubscriber()
public class GameplayData {

    private static final int MIN_PLAYERS = 1;

    private GameModus modus;
    private Phase phase = Phase.NONE;

    public Optional<GameModus> getModus() {
        return Optional.ofNullable(this.modus);
    }

    @SubscibeEvent
    public static void serverTick(TickEvent.Server event) {
        GameplayData instance = getInstace();
        instance.tick(event.getWorld());
    }

    private static boolean isParticipant(PlayerEntity player) {
        GameMode m = p.getGameMode();
        return m != GameModes.CREATIVE && m != GameModes.SPECTATOR;
    }

    public static Stream<PlayerEntity> getParticipants(World world) {
        return world.getPlayers().stream().filter(GameplayData::isParticipant);
    }

    public boolean canStart(World world) throws CommandException {

        int count = getParticipants().count();
        if(count < MIN_PLAYERS)
            throw new CommandException("Not enough players ({}/{})", count, MIN_PLAYERS);

        List<Team> teams = getParticipants().map(PlayerEntity::getTeam).collect(Collectors.toList()).distinct();
        
        /* Is there any player in a team */
        boolean withTeam = teams.anyMatch(Objects::NonNull);
        /* Is there any player without a team */
        boolean withoutTeam = teams.anyMatch(Objects::IsNull);

        if(with && without) throw new CommandException("Either all or no players must be in teams");

        if(teams.size() == 1) throw new CommandException("At least two required when playing in teams");        
        
        switch(instance.phase) {
            case NONE: throw new CommandException("No Gamemode choosen");
            case STARTED: throw new CommandException("Game has already started");
        }

        return true;
    }

    /**
     * Validates the Game Phase
     */
    private void checkPhase(World world) {
        switch(this.phase) {
            case NONE:

                this.getModus().ifPresent(g -> {
                    DungeonMod.LOGGER.warn("Gamemodus has not been stopped when setting the Game Phase to NONE");
                    g.stop(world);
                    this.modus = null;
                    this.markDirty();
                });
                break;

            case SETUP:
            case STARTED:

                if(!this.getModus().isPresent()) {
                    DungeonMod.LOGGER.warn("Phase has not been set to NONE when stopping GameModus");
                    this.phase = Phase.NONE;
                    this.markDirty();
                }
                break;
        }
    }

    private void tick(World world) {
        this.checkPhase(world);

        if(this.phase != STARTED) {

            getParticipants(world).forEach(player -> {
                
                /* While the game has not started yet, prevent players from moving */
                Stream.of(Effects.SLOWNESS, Effects.RESISTANCE, Effects.SATURATION)
                    .map(e -> new EffectInstance(e, 3, 100, false, false))
                    .forEach(player::addPotionEffect);

                player.setGameMode(GameModes.ADVENTURE);
            });

        }
    }

    public static void startModus(World world) throws CommandException {
        GameplayData instance = getInstace();
        
        assert instance.canStart(world);

        instance.getModus().orElseThrow(() -> new CommandException("No Gamemode choosen")).start(world);
        instance.phase = STARTED;
        instance.markDirty();

    }

    public static void stopModus(World world) throws CommandException {
        GameplayData instance = getInstace();

        instance.phase = NONE;
        instance.markDirty();

        instance.getModus().orElseThrow(() -> new CommandException("No Gamemodus active")).stop(world);
        instance.modus = null;
        instance.markDirty();
    }

    /**
     * @param world The target world
     * @param key The key of the GameModus
     * @param force Wether the current GameModus should be forced to stop
     */
    public static void chooseModus(World world, ResourceLocation key, boolean force) throws CommandException {
        GameplayData instance = getInstace();
        
        if(!force && instance.phase != Phase.NONE)
            throw new CommandException("Already choosen modus");

        GameModus modus = GameModus.find(key).orElseThrow(() -> new CommandException("No Gamemodus with name " + key));

        if(force) try {
            stopModus(world);
        } catch(CommandException ignored) {}

        instance.modus = modus;
        instance.markDirty();
        GameModus.loadModus(world);

    }

    static GameplayData getInstace(World world) {

    }

    public static Optional<GameModus> getModus(World world) {
        return getInstace(world).getModus();
    }

    /**
     * Apply a given function to the GameModus object if present
     * Return true to call `markDirty` on data and save any changes
     */
    public static void ifModus(World world, Function<GameModus,Boolean> function) {
        GameplayData instance = getInstace(world);
        instance.getModus().ifPresent(modus -> {
            boolean dirty = function.apply(modus);
            if(dirty) instance.markDirty();
        })
    }

    public static enum Phase {
        /**
         * No game has been created yet
         * Default on world creation.
         */
        NONE,
        /**
         * Game created, but not started yet.
         * players can join teams
         */
        SETUP,
        /**
         * Game has started.
         * Teams are locked
         */
        STARTED;
    }

}