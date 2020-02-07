const { write, MOD } = require('../Generator');

module.exports = async ({ name, full }) => {

    await write('blockstates', name, {
        variants: {
            'facing=east,half=bottom,shape=straight': { model: `${MOD}:block/${name}` },
            'facing=west,half=bottom,shape=straight': { model: `${MOD}:block/${name}`, y: 180, uvlock: true },
            'facing=south,half=bottom,shape=straight': { model: `${MOD}:block/${name}`, y: 90, uvlock: true },
            'facing=north,half=bottom,shape=straight': { model: `${MOD}:block/${name}`, y: 270, uvlock: true },
            'facing=east,half=bottom,shape=outer_right': { model: `${MOD}:block/${name}_outer` },
            'facing=west,half=bottom,shape=outer_right': { model: `${MOD}:block/${name}_outer`, y: 180, uvlock: true },
            'facing=south,half=bottom,shape=outer_right': { model: `${MOD}:block/${name}_outer`, y: 90, uvlock: true },
            'facing=north,half=bottom,shape=outer_right': { model: `${MOD}:block/${name}_outer`, y: 270, uvlock: true },
            'facing=east,half=bottom,shape=outer_left': { model: `${MOD}:block/${name}_outer`, y: 270, uvlock: true },
            'facing=west,half=bottom,shape=outer_left': { model: `${MOD}:block/${name}_outer`, y: 90, uvlock: true },
            'facing=south,half=bottom,shape=outer_left': { model: `${MOD}:block/${name}_outer` },
            'facing=north,half=bottom,shape=outer_left': { model: `${MOD}:block/${name}_outer`, y: 180, uvlock: true },
            'facing=east,half=bottom,shape=inner_right': { model: `${MOD}:block/${name}_inner` },
            'facing=west,half=bottom,shape=inner_right': { model: `${MOD}:block/${name}_inner`, y: 180, uvlock: true },
            'facing=south,half=bottom,shape=inner_right': { model: `${MOD}:block/${name}_inner`, y: 90, uvlock: true },
            'facing=north,half=bottom,shape=inner_right': { model: `${MOD}:block/${name}_inner`, y: 270, uvlock: true },
            'facing=east,half=bottom,shape=inner_left': { model: `${MOD}:block/${name}_inner`, y: 270, uvlock: true },
            'facing=west,half=bottom,shape=inner_left': { model: `${MOD}:block/${name}_inner`, y: 90, uvlock: true },
            'facing=south,half=bottom,shape=inner_left': { model: `${MOD}:block/${name}_inner` },
            'facing=north,half=bottom,shape=inner_left': { model: `${MOD}:block/${name}_inner`, y: 180, uvlock: true },
            'facing=east,half=top,shape=straight': { model: `${MOD}:block/${name}`, x: 180, uvlock: true },
            'facing=west,half=top,shape=straight': { model: `${MOD}:block/${name}`, x: 180, y: 180, uvlock: true },
            'facing=south,half=top,shape=straight': { model: `${MOD}:block/${name}`, x: 180, y: 90, uvlock: true },
            'facing=north,half=top,shape=straight': { model: `${MOD}:block/${name}`, x: 180, y: 270, uvlock: true },
            'facing=east,half=top,shape=outer_right': { model: `${MOD}:block/${name}_outer`, x: 180, y: 90, uvlock: true },
            'facing=west,half=top,shape=outer_right': { model: `${MOD}:block/${name}_outer`, x: 180, y: 270, uvlock: true },
            'facing=south,half=top,shape=outer_right': { model: `${MOD}:block/${name}_outer`, x: 180, y: 180, uvlock: true },
            'facing=north,half=top,shape=outer_right': { model: `${MOD}:block/${name}_outer`, x: 180, uvlock: true },
            'facing=east,half=top,shape=outer_left': { model: `${MOD}:block/${name}_outer`, x: 180, uvlock: true },
            'facing=west,half=top,shape=outer_left': { model: `${MOD}:block/${name}_outer`, x: 180, y: 180, uvlock: true },
            'facing=south,half=top,shape=outer_left': { model: `${MOD}:block/${name}_outer`, x: 180, y: 90, uvlock: true },
            'facing=north,half=top,shape=outer_left': { model: `${MOD}:block/${name}_outer`, x: 180, y: 270, uvlock: true },
            'facing=east,half=top,shape=inner_right': { model: `${MOD}:block/${name}_inner`, x: 180, y: 90, uvlock: true },
            'facing=west,half=top,shape=inner_right': { model: `${MOD}:block/${name}_inner`, x: 180, y: 270, uvlock: true },
            'facing=south,half=top,shape=inner_right': { model: `${MOD}:block/${name}_inner`, x: 180, y: 180, uvlock: true },
            'facing=north,half=top,shape=inner_right': { model: `${MOD}:block/${name}_inner`, x: 180, uvlock: true },
            'facing=east,half=top,shape=inner_left': { model: `${MOD}:block/${name}_inner`, x: 180, uvlock: true },
            'facing=west,half=top,shape=inner_left': { model: `${MOD}:block/${name}_inner`, x: 180, y: 180, uvlock: true },
            'facing=south,half=top,shape=inner_left': { model: `${MOD}:block/${name}_inner`, x: 180, y: 90, uvlock: true },
            'facing=north,half=top,shape=inner_left': { model: `${MOD}:block/${name}_inner`, x: 180, y: 270, uvlock: true }
        }
    });

    await Promise.all(
        ['', 'outer', 'inner'].map(s =>
            write('models/block', `${name}${s ? '_' + s : ''}`, {
                parent: `block/${s ? s + '_' : ''}stairs`,
                textures: {
                    top: `${MOD}:block/${full}`,
                    bottom: `${MOD}:block/${full}`,
                    side: `${MOD}:block/${full}`,
                }
            })
        )
    )
}