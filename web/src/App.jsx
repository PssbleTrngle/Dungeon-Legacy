import React, { useEffect, useState } from 'react';
import './style/room.css';
import './style/general.css';
import './style/wiki.css';
import './style/submit.css';
import { Switch, BrowserRouter as Router, Route, useLocation, Link } from 'react-router-dom';
import { TransitionGroup, CSSTransition } from "react-transition-group";
import Submit from './Submit';
import Wiki from './Wiki';

export const Popup = ({ children, hovered }) => {
    return hovered ? (
        <div className='popup' style={{
            top: hovered.y,
            left: hovered.x,
        }}>
            {children}
        </div>
    ) : null;
}

function App() {

   const doors = [
      { href: '/wiki', text: 'About the mod', component: Wiki },
      { href: '/submit', text: 'Submit a Structure', component: Submit },
      { href: '/download', text: 'Get the Mod', link: 'https://www.cursforge.com/DieIDHaltMannKP' },
   ];

   return (
      <Router>
         {/* <Header /> */}

         <Transitions>

            <Switch>
               <Route exact path="/">
                  <Home {...{ doors }} />
               </Route>
               {doors.filter(d => !!d.component).map(({ href, component }) =>
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

function Home({ doors }) {
   return (
      <div className='scene'>
         <div className='room'>

            <div className='ceiling' />
            <div className='floor' />
            <div className='wall'>

               <div className='doors'>
                  {doors.map(({ href, text, link }) =>
                     link
                        ? <Link className='door' to={href}>{text}</Link>
                        : <a className='door' href={link}>{text}</a>
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
