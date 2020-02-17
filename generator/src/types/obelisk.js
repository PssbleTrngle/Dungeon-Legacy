const { write, MOD } = require('../Generator');

module.exports = async ({ name }) => {

    const states = ['unclaimed', 'claimed', 'invalid'];
    
	await write('models/item', name, {
		parent: `${MOD}:block/${name}_${states[0]}`
	});

    await write('blockstates', name, {
        variants: {
            ...states.map(s => ([`state=${s}`, `${MOD}:block/${name}_${s}`]))
                .reduce((o, [key, model]) => ({
                    ...o,
                    [key]: { model }
                }), {})
        }
    });

    await Promise.all(states.map(s => write('models/block', `${name}_${s}`, {
        parent: `${MOD}:block/parent/obelisk`,
        textures: {
            down: `${MOD}:block/${name}_down`,
            up: `${MOD}:block/${name}_up`,
            bottom: `${MOD}:block/${name}_bottom`,
            top: `${MOD}:block/${name}_top`,
            runes: `${MOD}:block/${name}_runes`,
        }
    })));

}