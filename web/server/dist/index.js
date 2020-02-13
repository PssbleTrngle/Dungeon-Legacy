"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const express_1 = __importDefault(require("express"));
const config_1 = require("./config");
const chalk_1 = __importDefault(require("chalk"));
const body_parser_1 = __importDefault(require("body-parser"));
const fs_1 = __importDefault(require("fs"));
exports.error = (...s) => console.log(chalk_1.default.red('❌ ', ...s));
exports.info = (...s) => console.log(chalk_1.default.cyanBright(...s));
exports.success = (...s) => console.log(chalk_1.default.greenBright('✔ ', ...s));
exports.warn = (...s) => console.log(chalk_1.default.yellow('⚠ ', ...s));
exports.debug = config_1.DEBUG ? (...s) => console.log(chalk_1.default.bgGray.white(...s)) : () => { };
exports.exists = (e => !!e);
exports.info('Starting server...');
const app = express_1.default();
app.use(body_parser_1.default.json());
app.use(body_parser_1.default.urlencoded({ extended: true }));
function findPage(page) {
    return fs_1.default.promises.readFile(`${__dirname}/wiki/${page.toLowerCase()}.md`, 'utf8');
}
app.get('/wiki/:page', (req, res) => __awaiter(void 0, void 0, void 0, function* () {
    const { page } = req.params;
    try {
        const md = yield findPage(page);
        res.send(md);
    }
    catch (e) {
        res.status(404).send('Not Found ' + page);
        console.log(e);
    }
}));
function start() {
    return __awaiter(this, void 0, void 0, function* () {
        return new Promise(res => {
            const server = app.listen(config_1.PORT, () => {
                exports.success(`Server started on port ${chalk_1.default.underline(config_1.PORT)}`);
                console.log('');
                res(server);
            });
        });
    });
}
start();
//# sourceMappingURL=index.js.map