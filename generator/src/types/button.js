const { write, cycleProps, MOD } = require('../Generator');

module.exports = async ({ name, texture }) => {

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
        true: { model: n => n + '_pressed' },
        false: {},
    }

    await write('blockstates', name, cycleProps(
        { facing, face, powered },
        { uvlock: true, model: `${MOD}:block/${name}` }
    ));

    await write('models/item', name, {
        parent: 'block/button_inventory',
        textures: {
            texture: `${MOD}:block/${texture}`
        }
    });

    await Promise.all([['button', ''], ['button_pressed', '_pressed']]
        .map(([parent, suffix]) =>
            write('models/block', name + suffix, {
                parent: `block/${parent}`,
                textures: {
                    texture: `${MOD}:block/${texture}`
                }
            })
        )
    );

}