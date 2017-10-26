/**
 * Created by oxchain on 2017/10/18.
 */
import { combineReducers } from 'redux';
import { reducer as formReducer } from 'redux-form'
import authReducer from './auth_reducer';
import orderReducer from './order_reducer'


const rootReducer = combineReducers({
    form: formReducer,
    auth: authReducer,
    order:orderReducer
});

export default rootReducer;