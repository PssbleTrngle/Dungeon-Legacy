const { write, cycleProps, MOD } = require('../Generator');

module.exports = async ({ name, stone }) => {

    const face = {
        floor: {},
        wall: { x: 90 },
        ceiling: { x: 180, y: y => (y + 180) % 360 },
    }

    const facing = {
        east: { y: 90 },
        south: { y: 180 },
        west: { y: 270 },
        north: { y: 0 },
    }

    const powered = {
        true: { model: n => n + '_on' },
        false: {},
    }

    await write('blockstates', name, cycleProps(
        { facing, face, powered },
        { uvlock: true, model: `${MOD}:block/${name}` }
    ));

    await write('models/item', name, {
        parent: 'item/generated',
        textures: {
            layer0: `${MOD}:block/${name}`
        }
    });

    await Promise.all([['lever', ''], ['lever_on', '_on']]
        .map(([parent, suffix]) =>
            write('models/block', name + suffix, {
                parent: `block/${parent}`,
                textures: {
                    base: `${MOD}:block/${stone}`,
                    particle: `${MOD}:block/${stone}`,
                    lever: `${MOD}:block/${name}`,
                }
            })
        )
    );

}