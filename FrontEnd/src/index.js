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
import Singin from  './components/auth/signin';
import Singup from  './components/auth/signup';
import Usercenter from './components/usercenter';
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
                    {/*<Route path="/signout" component={Signout} />*/}
                    <Route path="/usercenter" component={Usercenter}/>
                    <Route path="/releaseadvert" component={Releaseadvert}/>
                    <Route path="/" component={Home}/>
                </Switch>
            </main>
        </div>
    </BrowserRouter>
    </Provider>
    ,document.querySelector('.wrapper')
);

