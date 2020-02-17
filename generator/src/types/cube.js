const { write, MOD } = require('../Generator');

module.exports = async ({ name }) => {

	await write('models/block', name, {
		parent: `block/cube_all`,
		textures: {
			all: `${MOD}:block/${name}`
		}
	});

}