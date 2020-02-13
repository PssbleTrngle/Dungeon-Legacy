const { write, cycleProps, MOD } = require('../Generator');

module.exports = async ({ name, texture }) => {

    await write('models/item', name, {
        "parent": "minecraft:item/handheld",
        "textures": {
            "layer0": "botania:items/wand_twig",
            "layer1": "botania:items/wand_petal_top",
            "layer2": "botania:items/wand_petal_bottom",
            "layer3": "botania:items/wand_bind"
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