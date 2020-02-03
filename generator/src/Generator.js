const fs = require('fs');
const rimraf = require('rimraf');
const chalk = require('chalk');

const success = (...s) => console.log(chalk.greenBright(['✓', ...s].join('   ')));
const progress = (...s) => console.log(chalk.cyanBright([...s, '...'].join(' ')));
const error = (...s) => console.log(chalk.redBright(['✗', ...s].join('    ')));

const MOD = 'dungeon';
const assets = `${__dirname}/../../src/main/resources/assets/${MOD}`;

async function clean() {
	progress('Cleaning directories')

	const promises = ['blockstates', 'models']
		.map(name => `${assets}/${name}`)
		.map(folder => new Promise(res => rimraf(folder, res)));

	await Promise.all(promises);

}

async function write(dir, name, data) {

	const path = `${assets}/${dir}`;

	await fs.promises.mkdir(path, { recursive: true });
	await fs.promises.writeFile(`${path}/${name}.json`, JSON.stringify(data, null, 2));

}

async function item(name) {
	await write('models/item', name, {
		parent: `${MOD}:block/${name}`
	});
}

async function create(params) {
	const { type, name } = params;
	const func = Types.get(type);

	if (func) {
		await item(name);
		await func(params);
		success(name);
	} else {
		error(name);
	}
}

function defaultState(name) {
	return write('blockstates', name, {
		variants: {
			'': { model: `${MOD}:block/${name}` }
		}
	});
}

async function fallback({ name, type }) {

	await defaultState(name);

	await write('models/block', name, {
		parent: `block/${type}`,
		textures: {
			all: `${MOD}:block/${name}`
		}
	});

}

function* each(object) {
	for (let key in object)
		yield [key, object[key]];
}

function cycleProps(props, defaults) {

	const rec = (props, done = { '': defaults }) => {

		const properties = Object.keys(props);
		if (properties.length > 0) {

			const property = properties[0];
			const values = props[property];
			const next = {};

			for (let [value, modifiers] of each(values)) {

				const key = `${property}=${value}`;

				for (let name in done) {

					const composedKey = name.length > 0 ? `${name},${key}` : key;
					const composedValues = { ...done[name] };

					for (let [p, modifier] of each(modifiers)) {
						if (typeof modifier === 'function')
							composedValues[p] = modifier(composedValues[p]);
						else composedValues[p] = modifier;
					}

					next[composedKey] = composedValues;

				}
			}

			delete props[property];
			return rec(props, next);

		}
		return done;

	}
	return { variants: rec(props) };

}

const Types = {

	get(name) {
		return this[name] || fallback;
	},

	async cube({ name }) {
		await fallback({ name, type: 'cube_all' });
	},

	async stairs({ name, full }) {

		/*
		const half = {
			bottom: { x: 0 },
			top: { x: 180 },
		}

		const facing = {
			east: { y: 0 },
			south: { y: 90 },
			west: { y: 180 },
			north: { y: 270 },
		}

		const shape = {
			straight: {},
			outer_right: { model: n => n + '_outer' },
			outer_left: { model: n => n + '_outer', y: y => (y + 270) % 360 },
			inner_right: { model: n => n + '_inner' },
			inner_left: { model: n => n + '_inner', y: y => (y + 270) % 360 },
		}

		await write('blockstates', name, cycleProps(
			{ facing, shape, half },
			{ uvlock: true, model: `${MOD}:block/${name}` }
		)); 
		*/

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
	},

	async slab({ name, full }) {

		await write('blockstates', name, {
			variants: {
				'type=bottom': { model: `dungeon:block/${name}` },
				'type=top': { model: `dungeon:block/${name}_top` },
				'type=double': { model: `dungeon:block/${full}` }
			}
		});

		await Promise.all(
			['', '_top'].map(s =>
				write('models/block', `${name}${s}`, {
					parent: `block/slab${s}`,
					textures: {
						top: `dungeon:block/${full}`,
						bottom: `dungeon:block/${full}`,
						side: `dungeon:block/${full}`,
					}
				})
			)
		)

	},

	async pillar({ name }) {

		await write('blockstates', name, {
			variants: {
				'axis=y': { model: `${MOD}:block/${name}` },
				'axis=z': { model: `${MOD}:block/${name}`, x: 90 },
				'axis=x': { model: `${MOD}:block/${name}`, x: 90, y: 90 }
			}
		});

		await write('models/block', name, {
			parent: 'block/cube_column',
			textures: {
				end: `${MOD}:block/${name}_top`,
				side: `${MOD}:block/${name}_side`
			}
		});

	},

	async farmland({ name, dirt }) {

		await defaultState(name);

		await write('models/block', name, {
			parent: 'block/template_farmland',
			textures: {
				particle: `${MOD}:block/${dirt}`,
				dirt: `${MOD}:block/${dirt}`,
				top: `${MOD}:block/${name}`,
			}
		});

	},

	async cross({ name }) {

		await defaultState(name);

		await write('models/block', name, {
			parent: 'block/cross',
			textures: {
				cross: `${MOD}:block/${name}`
			}
		});

	},

	async grass({ name, bottom, top }) {

		await write('blockstates', name, {
			variants: {
				"": [
					{ model: `${MOD}:block/${name}` },
					{ model: `${MOD}:block/${name}`, y: 90 },
					{ model: `${MOD}:block/${name}`, y: 180 },
					{ model: `${MOD}:block/${name}`, y: 270 }
				]
			}
		});

		await write('models/block', name, {
			parent: 'block/cube',
			textures: {
				particle: `${MOD}:block/${bottom}`,
				down: `${MOD}:block/${bottom || name + '_bottom'}`,
				up: `${MOD}:block/${top || name + '_top'}`,
				north: `${MOD}:block/${name}_side`,
				east: `${MOD}:block/${name}_side`,
				south: `${MOD}:block/${name}_side`,
				west: `${MOD}:block/${name}_side`,
			}
		});

	},

	async fence({ name, full }) {

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

	},

	async pressure_plate({ name, texture }) {

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

	},

	async button({ name, texture }) {

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

	},

	async lever({ name, stone }) {

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

	},

}

const blocks = require('./blocks.json')

async function run() {

	console.clear();
	await clean();

	progress(`Started Generator for ${blocks.length} blocks`)
	console.log('')

	Promise.all(
		blocks.map(create)
	).then(() => {
		console.log('')
		success('Done');
	});

};
run();