const { write, MOD } = require('../Generator');

module.exports = async ({ name, bottom, top }) => {

    await write('blockstates', name, {
        variants: {
            "": [
                { model: `${MOD}:block/${name}` },
                { model: `${MOD}:block/${name}`, y: 90 },
                { model: `${MOD}:block/${name}`, y: 180 },
                { model: `${MOD}:block/${name}`, y: 270 }
            ]
        }
    });

    await write('models/block', name, {
        parent: 'block/cube',
        textures: {
            particle: `${MOD}:block/${bottom}`,
            down: `${MOD}:block/${bottom || name + '_bottom'}`,
            up: `${MOD}:block/${top || name + '_top'}`,
            north: `${MOD}:block/${name}_side`,
            east: `${MOD}:block/${name}_side`,
            south: `${MOD}:block/${name}_side`,
            west: `${MOD}:block/${name}_side`,
        }
    });

}