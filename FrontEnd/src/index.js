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
import Signinemail from  './components/auth/signin_email';

import Singup from  './components/auth/signup';
import Signout from  './components/auth/signout';
import Forgetpsw from './components/auth/forget_psw';
import Emialforget from './components/auth/forget_email';
import Resetpsw from './components/auth/reset_psw';

import Usercenter from './components/user_center';
import Buybtc from './components/buy_btc';
import Sellbtc from './components/sell_btc';
import Selldetail from './components/sell_detail';
import Buydetail from './components/buy_detail';
import Myadvert from './components/my_advert';
import Home from './components/home';

import MessageNotice from './components/message/message_notice'
import OrderInProgress from './components/order_inprogress';
import OrderCompleted from './components/order_completed';
import OrderProgress from './components/order_process';
import ArbitrationBuyer from './components/arbitration_buyer';
import ArbitrationManage from './components/arbitration_manage';
import RefereeList from './components/referee_list';
import StubList from './components/stub_list';
import Releaseadvert from './components/release_advert';


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
                    <Route path="/signinemail" component={Signinemail}/>
                    <Route path="/signup" component={Singup}/>
                    <Route path="/signout" component={Signout} />
                    <Route path="/usercenter" component={Usercenter}/>
                    <Route path="/orderinprogress" component={OrderInProgress}/>
                    <Route path="/ordercompleted" component={OrderCompleted}/>
                    <Route path="/orderprogress" component={OrderProgress}/>
                    <Route path="/arbitrationbuyer" component={ArbitrationBuyer}/>
                    <Route path="/arbitrationmanage" component={ArbitrationManage}/>
                    <Route path="/refereelist" component={RefereeList}/>
                    <Route path="/stublist" component={StubList}/>
                    <Route path="/releaseadvert" component={Releaseadvert}/>
                    <Route path="/buybtc" component={Buybtc}/>
                    <Route path="/sellbtc" component={Sellbtc}/>
                    <Route path="/selldetail:id" component={Selldetail}/>
                    <Route path="/buydetail:id" component={Buydetail}/>
                    <Route path="/myadvert" component={Myadvert}/>
                    <Route path="/messagenotice" component={MessageNotice}/>
                    <Route path="/forgetpsw" component={Forgetpsw}/>
                    <Route path="/resetpsw" component={Resetpsw}/>
                    <Route path="/emailforget" component={Emialforget}/>
                    <Route path="/" component={Home}/>
                </Switch>
                <Footer/>
            </main>
        </div>
    </BrowserRouter>
    </Provider>
    , document.querySelector('.wrapper')
);

