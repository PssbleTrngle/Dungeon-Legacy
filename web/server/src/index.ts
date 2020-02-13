import express from 'express';
import { PORT, DEBUG } from './config';
import chalk from 'chalk';
import bodyParser from 'body-parser';
import { Server } from 'http';
import fs from 'fs'
import fileUpload, { UploadedFile } from 'express-fileupload';
import { v4 as uuid } from 'uuid';
import { Type, Submission } from './database';
import { AssertionError } from 'assert';

export const error = (...s: unknown[]) => console.log(chalk.red('❌ ', ...s));
export const info = (...s: unknown[]) => console.log(chalk.cyanBright(...s));
export const success = (...s: unknown[]) => console.log(chalk.greenBright('✔ ', ...s));
export const warn = (...s: unknown[]) => console.log(chalk.yellow('⚠ ', ...s));
export const debug = DEBUG ? (...s: unknown[]) => console.log(chalk.bgGray.white(...s)) : () => { }

export const exists = (e => !!e) as <T>(e: T | null | undefined | void) => e is T;

info('Starting server...');

const app = express();
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

app.use(fileUpload({
    limits: { fileSize: 50 * 1024 * 1024 },
    abortOnLimit: true,
    createParentPath: true,
    safeFileNames: true,
    preserveExtension: 6,
    debug: DEBUG,
}));

app.get('/wiki/:page', async (req, res) => {
    const { page } = req.params;
    const file = page.toLowerCase() + '.md';

    try {
        const md = await fs.promises.readFile(`${__dirname}/wiki/${file}`, 'utf8');
        res.send(md);
    } catch {
        res.status(404).send('Not Found');
    }

});

function move(uuid: string, file: UploadedFile) {
    const ext = file.name.slice(0, file.name.indexOf('.'));
    return new Promise((yes, no) => file.mv(`./submissions/${uuid}.${ext}`, e => {
        if (e) no(e);
        else yes();
    }))
}

app.get('/submissions', async (req, res) => {
    const limit = Number.parseInt(req.query.count) || 10
    const submissions = await Submission.findAll({ limit });
    res.send(submissions);
});

app.get('/types', async (req, res) => {
    const limit = Number.parseInt(req.query.count) || 10
    const types = await Type.findAll({ limit });
    res.send(types);
});

app.post('/submit', async (req, res) => {

    try {

        /* Validate for Structure Type */
        const typeKey = (req.body.type ?? '').toString();
        const type = await Type.findOne({ where: { key: typeKey } });
        if (!type) throw 'Not a valid structure type';

        /* Validate for Structure Name */
        const name = (req.body.name ?? '').toString();
        if (!/^[a-zA-Z]{3,20}$/.test(name)) throw 'Not a valid name';
        if(await Submission.findOne({ where: { name } })) throw 'Name taken';

        /* Validate for Structure Files */
        if (!req.files) throw 'No files added';
        const { m, s } = req.files;
        const [metadata, structure] = [m, s].map(f => Array.isArray(f) ? f[0] : f);

        /* Move Files */
        const file = uuid();
        Promise.all([metadata, structure]
            .filter(exists)
            .filter(f => f.size > 0)
            .map(f => move(file, f))
        ).catch(e => error(e));

        /* Create Submission Entry */
        await Submission.create({ file, type, name });

        res.send({ success: true, message: `Added '${name} as ${type} using ${structure.size}b` })

    } catch (e) {
        if (typeof e === 'string') res.send({ success: false, reason: e });
        else res.status(505).send({ success: false, reason: 'Internal Server Error' });
    }

});

async function start() {

    return new Promise<Server>(res => {
        const server = app.listen(PORT, () => {
            success(`Server started on port ${chalk.underline(PORT)}`);
            console.log('');
            res(server);
        })
    });

}

start();
