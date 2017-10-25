/**
 * Created by oxchain on 2017/10/17.
 */
import {
    AUTH_USER
} from './actions/types';

import React from 'react'
import { createStore, applyMiddleware, compose } from 'redux';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';
import { Router, browserHistory } from 'react-router';
import reduxThunk from 'redux-thunk';
import reducers from './reducers';

import {Route, BrowserRouter, Switch, Redirect} from 'react-router-dom';
import Header from  './components/common/header';
import Footer from  './components/common/footer';
import Singin from  './components/auth/signin';
import Singup from  './components/auth/signup';
import Signout from  './components/auth/signout';
import Usercenter from './components/usercenter';
import Buybtc from './components/buybtc';
import Sellbtc from './components/sellbtc';
import Selldetail from './components/selldetail';
import Buydetail from './components/buydetail';
import Myadvert from './components/myadvert';
import Home from './components/home';
import Releaseadvert from './components/releaseadvert';


const createStoreWithMiddleware = compose(
    applyMiddleware(reduxThunk),
    window.devToolsExtension ? window.devToolsExtension() : f => f
)(createStore);
const store = createStoreWithMiddleware(reducers);

const token = localStorage.getItem('token');
// If token exist, singin automatic
if (token) {
    store.dispatch({type: AUTH_USER});
}

ReactDOM.render(
    <Provider store={store} >
    <BrowserRouter>
        <div>
            <main>
                <Header/>
                <Switch>
                    <Route path="/signin" component={Singin}/>
                    <Route path="/signup" component={Singup}/>
                    <Route path="/signout" component={Signout} />
                    <Route path="/usercenter" component={Usercenter}/>
                    <Route path="/releaseadvert" component={Releaseadvert}/>
                    <Route path="/buybtc" component={Buybtc}/>
                    <Route path="/sellbtc" component={Sellbtc}/>
                    <Route path="/selldetail" component={Selldetail}/>
                    <Route path="/buydetail" component={Buydetail}/>
                    <Route path="/myadvert" component={Myadvert}/>
                    <Route path="/" component={Home}/>
                </Switch>
                <Footer/>
            </main>
        </div>
    </BrowserRouter>
    </Provider>
    ,document.querySelector('.wrapper')
);

