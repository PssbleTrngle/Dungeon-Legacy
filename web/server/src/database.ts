import { Model, Sequelize, INTEGER, HasManyCreateAssociationMixin, Association, STRING, DATE, ENUM, literal, HasManyRemoveAssociationMixin, BOOLEAN } from 'sequelize';
import { FORCE_DB, DEBUG } from './config';
import { info, success } from './index'

export class Type extends Model {

    public id!: string;
    public key!: string;
    public name!: string;

}

export class Submission extends Model {

    public id!: string;
    public file!: string;
    public name!: string;
    public hasMetadata!: boolean;

    public readonly type!: Type;

    public static associations: {
        type: Association<Submission, Type>;
    };

}

export async function setup() {

    const sequelize = new Sequelize({
        dialect: 'sqlite',
        storage: './database.sqlite',
        logging: false,
    });

    Submission.init({
        id: {
            type: INTEGER(),
            primaryKey: true,
            autoIncrement: true,
        },
        file: {
            type: STRING(128),
            allowNull: false,
        },
        name: {
            type: STRING(64),
            allowNull: false,
        },
        hasMetadata: {
            type: BOOLEAN
        },
    }, {
        sequelize,
        tableName: 'submissions',
        defaultScope: {
            include: [
                { model: Type, as: 'type' }
            ],
            attributes: { exclude: ['file'] },
        }
    });

    Type.init({
        id: {
            type: INTEGER(),
            primaryKey: true,
            autoIncrement: true,
        },
        key: {
            type: STRING(32),
            allowNull: false,
        }
    }, {
        sequelize,
        tableName: 'types',
    });

    Submission.hasOne(Type, {
        sourceKey: 'id',
        foreignKey: 'type',
    });


    if (FORCE_DB) info('Flushing Database...');
    await sequelize.sync({ force: FORCE_DB, alter: DEBUG });

    if (await Type.count() === 0) {
        await Promise.all([
            { name: 'Room', key: 'room' },
            { name: 'Hallway', key: 'hallway' },
            { name: 'Base', key: 'base' },
            { name: 'Big Door', key: 'door/big' },
            { name: 'Small Door', key: 'door/small' },
        ].map(props => Type.create(props)));
    }

    success('Database running');

}