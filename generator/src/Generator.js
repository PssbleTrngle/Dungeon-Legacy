const fs = require('fs');
const rimraf = require('rimraf');
const chalk = require('chalk');

const success = (...s) => console.log(chalk.greenBright(['✅', ...s].join('   ')));
const progress = (...s) => console.log(chalk.cyanBright([...s, '...'].join(' ')));
const error = (...s) => console.log(chalk.redBright(['❌', ...s].join('    ')));

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
	const func = TYPES.get(type);

	if (func) {
		await func(params);
		await item(name);
		success(name);
	} else {
		error(name);
	}
}

async function cube({ name }) {

	await write('blockstates', name, {
		variants: {
			'': { model: `${MOD}:block/${name}` }
		}
	});

	await write('models/block', name, {
		parent: 'block/cube_all',
		textures: {
			all: `${MOD}:block/${name}`
		}
	});

}

const TYPES = new Map();

async function stairs({ name, full }) {

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

async function slab({ name, full }) {

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

}

async function pillar({ name }) {

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

}

async function grass({ name }) {

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
			particle: `${MOD}:block/${name}_bottom`,
			down: `${MOD}:block/${name}_bottom`,
			up: `${MOD}:block/${name}_top`,
			north: `${MOD}:block/${name}_side`,
			east: `${MOD}:block/${name}_side`,
			south: `${MOD}:block/${name}_side`,
			west: `${MOD}:block/${name}_side`,
		}
	});

}

[cube, stairs, slab, pillar, grass].forEach(f => TYPES.set(f.name, f));

const blocks = require('./blocks.json')

async function run() {

	console.log('');
	await clean();

	progress(`Started Generator for ${blocks.length} blocks`)

	Promise.all(
		blocks.map(create)
	).then(() => success('Done'));

};
run();