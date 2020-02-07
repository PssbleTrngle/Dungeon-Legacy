const { write, MOD } = require('../Generator');

module.exports = async ({ name, full }) => {

    await write('blockstates', name, {
        multipart: [
            { apply: { model: `${MOD}:block/${name}_post` } },
            {
                when: { north: 'true' },
                apply: { model: `${MOD}:block/${name}_side`, uvlock: true }
            },
            {
                when: { east: 'true' },
                apply: { model: `${MOD}:block/${name}_side`, y: 90, uvlock: true }
            },
            {
                when: { south: 'true' },
                apply: { model: `${MOD}:block/${name}_side`, y: 180, uvlock: true }
            },
            {
                when: { west: 'true' },
                apply: { model: `${MOD}:block/${name}_side`, y: 270, uvlock: true }
            }
        ]
    });

    await write('models/item', name, {
        parent: 'block/fence_inventory',
        textures: {
            texture: `${MOD}:block/${full}`
        }
    });

    await Promise.all(['side', 'post']
        .map(suffix =>
            write('models/block', `${name}_${suffix}`, {
                parent: `block/fence_${suffix}`,
                textures: {
                    texture: `${MOD}:block/${full}`
                }
            })
        )
    );

}