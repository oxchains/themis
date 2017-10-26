/**
 * Created by oxchain on 2017/10/25.
 */

import {
    ROOT_URLL,
    FETCH_ADVERT,
    FETCH_BUY_BTC,
    FETCH_SELL_BTC,

} from '../actions/types';

const INITIAL_STATE = { all: [] ,data:null,refuse:null};

export default function(state = INITIAL_STATE, action) {
    switch(action.type) {
        case FETCH_ADVERT:
            return { ...state, all:action.payload.data.data};
        case FETCH_BUY_BTC:
            return { ...state, all:action.payload.data.data};
        case FETCH_SELL_BTC:
            return { ...state, all:action.payload.data.data};
    }

    return state;
}


