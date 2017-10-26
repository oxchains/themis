/**
 * Created by zhangxiaojing on 2017/10/25.
 */

import {
    FETCH_NOT_COMPLETED_ORDERS,
    FETCH_COMPLETED_ORDERS,
    FETCH_ORDERS_DETAILS,
    FETCH_TRADE_PARTNER_MESSAGE
} from '../actions/types';


const INITIAL_STATE = {not_completed_orders: null,completed_orders:null};

export default function (state = INITIAL_STATE, action) {
    switch (action.type) {
        case FETCH_NOT_COMPLETED_ORDERS:
            return {...state, not_completed_orders: action.payload};
        case FETCH_COMPLETED_ORDERS: {
            return {...state, completed_orders: action.payload};
        }
        case FETCH_ORDERS_DETAILS: {
            return {...state, orders_details: action.payload};
        }
        case FETCH_TRADE_PARTNER_MESSAGE:{
            return {...state, partner_message: action.payload};
        }

    }

    return state;
}