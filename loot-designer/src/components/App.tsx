import React, { FC, useState, useContext } from 'react';
import '../style/general.css';
import { IData, EntryType, IGroup, IEntry, IStack, IReference } from '../models';

const DataContext = React.createContext<{
  data: IData,
  findGroup(id: string): IGroup,
  addEntry(group: string, entry: IEntry): void,
  deleteEntry(group: string, entry: IEntry): void,
} | null>(null);

const useData = () => {
  const d = useContext(DataContext);
  if (d) return d;
  throw new Error('No data provided');
}

const useDataProvider = () => {
  const [data, setData] = useState<IData>({
    groups: {
      swords: {
        type: EntryType.GROUP,
        entries: [
          {
            type: EntryType.STACK,
            id: 'gold_nugget',
            amount: { min: 2, max: 7 },
          },
          {
            type: EntryType.STACK,
            id: 'gold_ingot',
            amount: 1,
          },
        ]
      }
    }
  });

  const findGroup = (id: string) => ({ ...data.groups[id] });

  const setGroup = (id: string, group: IGroup) => {
    setData({ ...data, groups: { ...data.groups, [id]: group } })
  }

  const addEntry = (group: string, entry: IEntry) => {
    const g = findGroup(group);
    g.entries.push(entry);
    setGroup(group, g);
  }

  const deleteEntry = (group: string, entry: IEntry) => {
    const g = findGroup(group);
    const index = g.entries.indexOf(entry);
    if (index >= 0) g.entries.splice(index);
    setGroup(group, g);
  }

  return { data, addEntry, deleteEntry, findGroup };
}

const App = () => {
  const provider = useDataProvider();

  return (
    <DataContext.Provider value={provider}>
      <div className='container'>

        <Data />

      </div>
    </DataContext.Provider>
  );
}

function toArray<O extends { [key: string]: any }>(o: O) {
  return Object.keys(o).map(k => [k, o[k]]) as [string, O[keyof O]][];
}

const Data = () => {
  const { data } = useData();

  return (
    <div className='data'>
      {toArray(data.groups).map(([key, g]) => ({ ...g, key })).map(group =>
        <Group {...group} />
      )}
    </div>
  )
}

const Group = (group: IGroup & { key: string }) => {
  const { entries, key } = group;
  return (
    <div className='group'>
      <p>{key}</p>
      {entries.map((entry, i) =>
        <Entry key={i} {...entry} />
      )}
    </div>
  );
}

const Stack = (stack: IStack) => {
  const { id, amount } = stack;
  return (
    <p className='stack'>
      {id} ({amount})
    </p>
  );
}

const Reference = (reference: IReference) => {
  const { to: key } = reference;
  const { findGroup } = useData();
  const group = findGroup(key);
  return <Group {...{ ...group, key }} />
}

const Entry = (entry: IEntry) => {

  switch (entry.type) {
    case EntryType.STACK: return <Stack {...entry} />
    case EntryType.GROUP: return <Group {...entry} />
    case EntryType.REFERENCE: return <Reference {...entry} />
    default: return null;
  }
}

export default App;
