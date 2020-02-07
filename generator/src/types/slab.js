const { write, MOD } = require('../Generator');

module.exports = async ({ name, full }) => {

    await write('blockstates', name, {
        variants: {
            'type=bottom': { model: `dungeon:block/${name}` },
            'type=top': { model: `dungeon:block/${name}_top` },
            'type=double': { model: `dungeon:block/${full}` }
        }
    });

    await Promise.all(
        ['', '_top'].map(s =>
            write('models/block', `${name}${s}`, {
                parent: `block/slab${s}`,
                textures: {
                    top: `dungeon:block/${full}`,
                    bottom: `dungeon:block/${full}`,
                    side: `dungeon:block/${full}`,
                }
            })
        )
    )

}