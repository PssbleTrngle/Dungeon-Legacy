import React, { useState, useContext, createContext, useEffect, useRef, useMemo } from 'react';
import { Link } from 'react-router-dom';
import querystring from 'querystring';
import { useApi } from './App';

function Selection({ of, name }) {
   const [value, set] = useState('');
   const ref = useRef();
   const { valid, onChange, className } = useValidation(name, ({ value }) => {
      console.log(value);
      return value.length > 0;
   }, ref);

   const select = (e, key) => {
      e.preventDefault();
      set(key);
      onChange({ target: key });
   }

   return (
      <>
         <div className={`row selection ${className}`}>
            {of.map(({ name, key }) =>
               <button {...{ key }} onClick={e => select(e, key)} className={value === key ? 'selected' : ''}>
                  {name}
               </button>
            )}
            <input type='hidden'{...{ name, value, ref }} />
         </div>
      </>
   );

}

function Input({ validate, placeholder, name }) {

   const [value, setValue] = useState('');
   const [reason, setReason] = useState(null);

   const onChange = async e => {
      const { value } = e.target;
      setValue(value);

      if (validate) {
         const valid = await validate(value);
         if (valid === true) {
            setReason(null);
         } else {
            setReason(valid);
         }
      }
   }

   return (
      <>
         <Validating
            type='text'
            validate={async ({ value }) => {
               const v = await validate(value || '');
               return v === true;
            }}
            {...{ value, placeholder, name, onChange }}
         />
         {reason && <label className='tooltip'>{reason}</label>}
      </>
   );
}

function Upload({ extension, name }) {
   const [files, setFiles] = useState([]);
   const [, , isValid] = useForm();
   const valid = isValid(name);

   const validate = ({ files }) => files.length > 0 && [...files].every(f => f.name.endsWith(extension));

   const choose = e => {
      const v = validate(e.target);
      setFiles(v ? e.target.files : []);
   }

   const slice = t => {
      return t.length < 15 ? t : t.slice(0, 9) + '..';
   }

   return (<>
      <Validating
         type='file'
         accept={extension}
         {...{ name, validate }}
         onChange={choose}
      />
      <label htmlFor={name}>
         {name} File
         <span>{files.length > 0
            ? slice(files[0].name)
            : valid
               ? 'Choose File'
               : 'Invalid File'
         }</span>
      </label>
   </>)
}

const FormContext = createContext();
function useForm() {
   const state = useContext(FormContext);

   if (!state) return [true, () => { }];
   const [map, setMap] = state;

   const setValid = (name, valid) => {
      setMap(m => ({ ...m, [name]: valid }));
   }

   const valid = Object.values(map).every(v => v);
   const isValid = name => map[name] !== false;

   return [valid, setValid, isValid];
}

function useValidation(name, validate, ref) {
   const [, setValid, isValid] = useForm();
   const valid = isValid(name);
   const [changed, setChanged] = useState(false);

   const onChange = async () => {
      setChanged(true);
      setValid(name, await validate(ref.current));
   }

   useEffect(() => {
      const v = async () => {
         const valid = await validate(ref.current);
         setValid(name, valid);
      }
      if (ref.current) v();
   }, [name, ref]);

   const className = (!valid && changed) ? 'invalid' : 'valid';
   return { valid, onChange, changed, className };
}

function Validating({ name, validate, type, placeholder, onChange: change, ...props }) {
   const ref = useRef();
   const { onChange, className } = useValidation(name, validate, ref);

   return <input
      id={name}
      onChange={e => {
         if (change) change(e);
         onChange(e);
      }}
      {...{ name, ref, type, placeholder, className, ...props }}
   />
}

function SubmitButton() {
   const [valid] = useForm();
   const [, hover] = useState(false);

   return (<>
      <input
         disabled={!valid}
         type='submit'
         value='Submit'
         onMouseLeave={() => hover(false)}
         onMouseMove={e => hover({ x: e.clientX, y: e.clientY })}
      />
      {!valid &&
         <p>You are missing something</p>
      }
   </>);
}

function Submit() {

   const types = useApi('types') ?? [];
   const submissions = useApi('submissions') ?? []
   const names = useMemo(() => submissions.map(s => s.name).map(s => s.toLowerCase()), [submissions]);

   const nameTaken = async name => {
      return names.some(e => e === name.toLowerCase());
   }

   const valid = useState({});

   return (
      <FormContext.Provider value={valid}>
         <h1>Submit a structure</h1>

         <p>If you need any information about how to build or submit, visit the <Link to='/wiki/submit'>Wiki</Link></p>

         <form method='POST' action='/submit'>

            <label>Select a type</label>
            <Selection name='type' of={types} />

            <Input name='name' placeholder='Structure Name' validate={async value => {
               if (!/^[a-zA-Z]{3,20}$/.test(value)) return 'Invalid Name';
               if (await nameTaken(value)) return 'Name Taken';
               return true;
            }} />

            <div className='row'>
               <Upload name='structure' extension='.nbt' />
               <Upload name='metadata' extension='.nbt.mcmeta' />
            </div>

            <SubmitButton />

         </form>
      </FormContext.Provider>
   );
}

export default Submit;