/**
 * Created by oxchain on 2017/10/17.
 */
import React from 'react'

import ReactDOM from 'react-dom';

import {Route, BrowserRouter, Switch, Redirect} from 'react-router-dom';
import Header from  './components/common/header';

//
// const createStoreWithMiddleware = compose(
//     applyMiddleware(reduxThunk),
//     window.devToolsExtension ? window.devToolsExtension() : f => f
// )(createStore);
// const store = createStoreWithMiddleware(reducers);

ReactDOM.render(
    <BrowserRouter>
        <div>
            <main>
                <Header/>
                <Switch>
                    {/*<Route path="/ico" component={Ico}/>*/}
                    {/*<Route path="/team" component={Team}/>*/}
                    {/*<Route path="/law" component={Law}/>*/}
                    {/*<Route path="/" component={Home}/>*/}

                </Switch>
                {/*<Footer/>*/}
            </main>
        </div>
    </BrowserRouter>
    ,document.querySelector('.app')
);

