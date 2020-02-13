import React from 'react';
import { useApi } from './App';

function Submissions() {
    const submissions = useApi('submissions', 100) || [];

    return (
        <table>
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Type</th>
                    <th>Structure</th>
                    <th>Metdata</th>
                </tr>
            </thead>

            <tbody>
                {submissions.map(entry => <Entry {...entry} />)}
            </tbody>
        </table>
    )

}

function Entry({ id, name, type, hasMetadata }) {
    return (
        <tr>
            <td>{name}</td>
            <td>{type.name}</td>
            <td colSpan={hasMetadata ? 1 : 2}><Download file={`${id}.nbt`} /></td>
            {hasMetadata && <td><Download file={`${id}.nbt.mcmeta`} /></td>}
        </tr>
    );
}

function Download({ file }) {
    return (
        <a className='download' href={`/download/${file}`}>🡇</a>
    )
}

export default Submissions;