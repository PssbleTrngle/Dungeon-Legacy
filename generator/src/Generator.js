const fs = require('fs');
const rimraf = require('rimraf');
const chalk = require('chalk');

const success = (...s) => console.log(chalk.greenBright(['✓', ...s].join('   ')));
const progress = (...s) => console.log(chalk.cyanBright([...s, '...'].join(' ')));
const error = (...s) => console.log(chalk.redBright(['✗', ...s].join('    ')));

const MOD = 'dungeon';
exports.MOD = MOD;
const assets = `${__dirname}/../../src/main/resources/assets/${MOD}`;

async function clean() {
	progress('Cleaning directories')

	const promises = ['blockstates', 'models']
		.map(name => `${assets}/${name}`)
		.map(p => [
			`${p}/**/!(parent)/*.json`,
			`${p}/*.json`
		]).reduce((a, e) => [...a, ...e], [])
		.map(folder => new Promise(res => rimraf(folder, res)));

	await Promise.all(promises);

}

exports.write = async (dir, name, data) => {

	const path = `${assets}/${dir}`;

	await fs.promises.mkdir(path, { recursive: true });
	await fs.promises.writeFile(`${path}/${name}.json`, JSON.stringify(data, null, 2));

}

async function item(name) {
	return exports.write('models/item', name, {
		parent: `${MOD}:block/${name}`
	});
}

async function get(name) {
	const path = `./types/${name}.js`;
	try {
		return require(path);
	} catch (e) {
		throw `Could not find type ${name} at '${path}'`;
	}
}

async function create(params) {
	const { type, name } = params;
	try {
		const func = await get(type);

		await exports.write('blockstates', name, {
			variants: {
				'': { model: `${MOD}:block/${name}` }
			}
		});

		await item(name);
		await func(params);
		success(name);
	} catch (msg) {
		error(name);
		error(msg);
	}
}

function* each(object) {
	for (let key in object)
		yield [key, object[key]];
}

exports.cycleProps = (props, defaults) => {

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