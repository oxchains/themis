/**
 * Created by oxchain on 2017/10/25.
 */

import {
    FETCH_ADVERT,
    FETCH_BUY_BTC,
    FETCH_SELL_BTC,
    FETCH_BUY_SECAT,
    FETCH_SELL_SECAT,
    FETCH_BUY_BTC_DETAIL,
    FETCH_ARRAY,

} from '../actions/types';

const INITIAL_STATE = { all: [] ,array: [] ,data:null,refuse:null};

export default function(state = INITIAL_STATE, action) {
    switch(action.type) {
        case FETCH_ADVERT:
            return { ...state, all:action.payload.data.data};
        case FETCH_BUY_BTC:
            return { ...state, all:action.payload.data.data};
        case FETCH_SELL_BTC:
            return { ...state, all:action.payload.data.data};
        case FETCH_BUY_SECAT:
            return { ...state, all:action.payload.data.data};
        case FETCH_SELL_SECAT:
            return { ...state, all:action.payload.data.data};
        case FETCH_BUY_BTC_DETAIL:
            return { ...state, all:action.payload.data.data};
        case FETCH_ARRAY:
            return { ...state, array:action.payload.data.data};
    }

    return state;
}


