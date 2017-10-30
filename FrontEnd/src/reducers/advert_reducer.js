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
    FETCH_SELL_BTC_DETAIL,
    FETCH_ARRAY,
    FETCH_HOME,
    FETCH_SELL_NOW,
    FETCH_BUY_NOW,
    FETCH_MY_ADVERT,
    FETCH_OFF_MYBTC,
    FETCH_BASE_INFO
} from '../actions/types';

const INITIAL_STATE = { all: [] ,array: [] ,data:[],refuse:null};

export default function(state = INITIAL_STATE, action) {
    switch(action.type) {
        case FETCH_ADVERT:
            return { ...state, all:action.payload.data.data};
        case FETCH_BUY_NOW:
            return { ...state, data:action.payload.data.data};
        case FETCH_SELL_NOW:
            return { ...state, data:action.payload.data.data};
        case FETCH_BUY_SECAT:
            return { ...state, all:action.payload.data.data};
        case FETCH_SELL_SECAT:
            return { ...state, all:action.payload.data.data};
        case FETCH_BUY_BTC_DETAIL:
            return { ...state, all:action.payload.data.data};
        case FETCH_SELL_BTC_DETAIL:
            return { ...state, all:action.payload.data.data};
        case FETCH_ARRAY:
            return { ...state, array:action.payload.data.data};
        case FETCH_HOME:
            return { ...state, all:action.payload.data.data};
        case FETCH_MY_ADVERT:
            return { ...state, all:action.payload.data.data};
        case FETCH_OFF_MYBTC:
            return { ...state, data:action.payload.data.data};
        case FETCH_BASE_INFO:
            return { ...state, all:action.payload.data.data};

    }
    return state;
}


