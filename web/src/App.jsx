import React, { useEffect, useState } from 'react';
import './style/room.css';
import './style/general.css';
import { Switch, BrowserRouter as Router, Route, useLocation } from 'react-router-dom';
import { TransitionGroup, CSSTransition } from "react-transition-group";

function App() {

   const doors = [
      { href: 'wiki', text: 'About the mod', component: Wiki },
      { href: 'submit', text: 'Submit a Structure', component: Submit },
      { href: 'download', text: 'Get the Mod', component: Download },
   ].map(d => ({ ...d, href: `/${d.href}` }));

   return (
      <Router>
         <Header />

         <Transitions>

            <Switch>
               <Route exact path="/">
                  <Home {...{ doors }} />
               </Route>
               {doors.map(({ href, component }) =>
                  <Route path={href} {...{ component }} />
               )}
            </Switch>

         </Transitions>

      </Router>
   );
}

function Transitions({ children }) {
   const location = useLocation();

   return (
      <TransitionGroup className='container'>
         <CSSTransition
            key={location.key}
            timeout={{ enter: 300, exit: 300 }}
            classNames='fade'
         >
            <section>
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

function Submit() {
   return (
      <h1>Submit</h1>
   );
}

function Wiki() {
   return (
      <h1>Wiki</h1>
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
                     <a className='door' {...{ href }}>{text}</a>
                  )}
               </div>

            </div>
         </div>
      </div>
   );
}

function Header() {

   const images = ['header', 'header2'].map(i => require(`./img/${i}.png`))
   const [banner, setBanner] = useState(0);

   useEffect(() => {
      const i = setInterval(() => setBanner((banner + 1) % images.length), 1000 * 6);
      return () => clearInterval(i);
   });

   return (
      <header style={{
         backgroundImage: `url('${images[banner]}')`
      }}>
         <h1>Dungeon</h1>
      </header>
   );
}

export default App;
