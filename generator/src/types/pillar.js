const { write, MOD } = require('../Generator');

module.exports = async ({ name }) => {

    await write('blockstates', name, {
        variants: {
            'axis=y': { model: `${MOD}:block/${name}` },
            'axis=z': { model: `${MOD}:block/${name}`, x: 90 },
            'axis=x': { model: `${MOD}:block/${name}`, x: 90, y: 90 }
        }
    });

    await write('models/block', name, {
        parent: 'block/cube_column',
        textures: {
            end: `${MOD}:block/${name}_top`,
            side: `${MOD}:block/${name}_side`
        }
    });

}