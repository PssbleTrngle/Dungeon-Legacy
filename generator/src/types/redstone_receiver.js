const { write, cycleProps, MOD } = require('../Generator');

module.exports = async ({ name }) => {

    const powered = {
        true: { model: n => n + '_powered' },
        false: {},
    }

    await write('blockstates', name, cycleProps(
        { powered },
        { uvlock: true, model: `${MOD}:block/${name}` }
    ));

    await Promise.all(['', '_powered']
        .map(suffix =>
            write('models/block', name + suffix, {
                parent: `block/cube_all`,
                textures: {
                    all: `${MOD}:block/${name}${suffix}`
                }
            })
        )
    );

}