/**
 * Created by oxchain on 2017/10/18.
 */
import { combineReducers } from 'redux';
import { reducer as formReducer } from 'redux-form'
import authReducer from './auth_reducer';
<<<<<<< HEAD
import orderReducer from './order_reducer'
=======
import releaseAdvert from './advert_reducer';
>>>>>>> 053eeff50f23abae9bac89b3ba387ac15e907076


const rootReducer = combineReducers({
    form: formReducer,
    auth: authReducer,
<<<<<<< HEAD
    order:orderReducer
=======
    advert:releaseAdvert
>>>>>>> 053eeff50f23abae9bac89b3ba387ac15e907076
});

export default rootReducer;