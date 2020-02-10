const { write, cycleProps, MOD } = require('../Generator');

module.exports = async ({ name }) => {

    const powered = {
        true: { model: n => n + '_powered' },
        false: {},
    }

    const facing = {
        east: { y: 90 },
        south: { y: 180 },
        west: { y: 270 },
        north: { y: 0 },
        up: { x: -90 },
        down: { x: 90 },
    }

    await write('blockstates', name, cycleProps(
        { powered, facing },
        { uvlock: true, model: `${MOD}:block/${name}` }
    ));

    await Promise.all(['', '_powered']
        .map(suffix =>
            write('models/block', name + suffix, {
                parent: `block/cube`,
                textures: {
                    ...['particle', 'down', 'up', 'south', 'west', 'east']
                        .reduce((o, t) => ({...o, [t]: 'block/furnace_top'})),
                    north: `${MOD}:block/${name}_front${suffix}`,
                }
            })
        )
    );

}