import React, { useEffect, useState } from 'react';
import './style/room.css';
import './style/general.css';
import { Switch, BrowserRouter as Router, Route, useLocation, Link, useParams } from 'react-router-dom';
import { TransitionGroup, CSSTransition } from "react-transition-group";

function App() {

   const doors = [
      { href: '/wiki', text: 'About the mod', component: Wiki },
      { href: '/submit', text: 'Submit a Structure', component: Submit },
      { href: '/download', text: 'Get the Mod', component: Download },
   ];

   return (
      <Router>
         <Header />

         <Transitions>

            <Switch>
               <Route exact path="/">
                  <Home {...{ doors }} />
               </Route>
               {doors.map(({ href, component }) =>
                  <Route key={href} path={`${href}/:page?`} {...{ component }} />
               )}
            </Switch>

         </Transitions>

      </Router>
   );
}

function Transitions({ children }) {
   const location = useLocation();
   const page = location.pathname.match(/^\/(.*?)(\/.*$|$)/)[1];

   return (
      <TransitionGroup className='container'>
         <CSSTransition
            key={location.pathname}
            timeout={{ enter: 300, exit: 300 }}
            classNames='fade'
         >
            <section className={page}>
               {children}
            </section>
         </CSSTransition>
      </TransitionGroup>
   );
}

function Download() {
   return (
      <h1>Download</h1>
   );
}

function Selection({ of, name }) {
   const [value, set] = useState('');
   const select = (e, key) => {
      e.preventDefault();
      set(key);
   }

   return (
      <>
         <div className='row selection'>
            {of.map(({ name, key }) =>
               <button {...{ key }} onClick={e => select(e, key)} className={value === key ? 'selected' : ''}>
                  {name}
               </button>
            )}
            <input type='hidden'{...{ name, value }} />
         </div>
      </>
   );

}

function Input({ validate, placeholder, name }) {

   const [value, setValue] = useState('');
   const [valid, setValid] = useState(true);
   const [reason, setReason] = useState(null);

   const onChange = async e => {
      const { value } = e.target;
      setValue(value);

      if (validate) {
         const valid = await validate(value);
         if (valid === true) {
            setValid(true);
            setReason(null);
         } else {
            setValid(false);
            setReason(valid);
         }
      }
   }

   return (
      <>
         <input
            type='text'
            className={valid ? 'valid' : 'invalid'}
            {...{ value, onChange, placeholder, name }}
         >
         </input>
         {reason && <label className='tooltip'>{reason}</label>}
      </>
   );
}

function Upload({ extension, name }) {
   return (
      <input
         type='file'
         accept={extension}
         {...{ name }}
      />
   )
}

function Submit() {
   const types = [
      { name: 'Room', key: 'room' },
      { name: 'Hallway', key: 'hallway' },
      { name: 'Base', key: 'base' },
      { name: 'Big Door', key: 'door/big' },
      { name: 'Small Door', key: 'door/small' },
   ];

   const nameTaken = async name => {
      const existing = ['well', 'fountain', 'treasure'].map(s => s.toLowerCase());
      return existing.some(e => e === name.toLowerCase());
   }

   return (
      <>
         <h1>Submit a structure</h1>

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

         </form>
      </>
   );
}

function Wiki() {
   const { page } = useParams();

   return (
      <>
         <h1>Wiki</h1>
         <p>{page}</p>
      </>
   );
}

function Home({ doors }) {
   return (
      <div className='scene'>
         <div className='room'>

            <div className='ceiling' />
            <div className='floor' />
            <div className='wall'>

               <div className='doors'>
                  {doors.map(({ href, text }) =>
                     <Link className='door' to={href}>{text}</Link>
                  )}
               </div>

            </div>
         </div>
      </div>
   );
}

function useInterval(callback, timeout) {
   useEffect(() => {
      const i = setInterval(callback, timeout);
      return () => clearInterval(i);
   }, [callback, timeout]);
}

function Header() {

   const images = ['header', 'header2'].map(i => require(`./img/${i}.png`))
   const [banner, setBanner] = useState(0);
   const cycle = () => setBanner((banner + 1) % images.length);
   useInterval(cycle, 1000 * 6);

   return (
      <header>

         <TransitionGroup className='banners'>
            <CSSTransition
               key={banner}
               timeout={1000}
            >
               <img alt='Banner' src={images[banner]} />
            </CSSTransition>
         </TransitionGroup>


         <h1>Dungeon</h1>
      </header>
   );
}

export default App;
