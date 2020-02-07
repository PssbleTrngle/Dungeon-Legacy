const { write, MOD } = require('../Generator');

module.exports = async ({ name, dirt }) => {

	await write('models/block', name, {
		parent: 'block/template_farmland',
		textures: {
			particle: `${MOD}:block/${dirt}`,
			dirt: `${MOD}:block/${dirt}`,
			top: `${MOD}:block/${name}`,
		}
	});

}