const { write, MOD } = require('../Generator');

module.exports = async ({ name }) => {

    await write('models/block', name, {
        parent: 'block/cross',
        textures: {
            cross: `${MOD}:block/${name}`
        }
    });

}