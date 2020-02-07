const { write, MOD } = require('../Generator');

module.exports = async ({ name, texture }) => {

	await write('blockstates', name, {
		variants: {
			'powered=false': { model: `${MOD}:block/${name}` },
			'powered=true': { model: `${MOD}:block/${name}_down` }
		}
	});

	await Promise.all([['pressure_plate_down', '_down'], ['pressure_plate_up', '']]
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