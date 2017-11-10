/**
 * Created by zhangxiaojing on 2017/11/8.
 */
import {
    FETCH_UNREAD_MESSAGES,
    FETCH_READ_MESSAGES,
} from '../actions/types';


const INITIAL_STATE = {FETCH_UNREAD_MESSAGES: null,FETCH_READ_MESSAGES:null};

export default function (state = INITIAL_STATE, action) {
    switch (action.type) {
        case FETCH_UNREAD_MESSAGES:
            return {...state, unread_message: action.payload};
        case FETCH_READ_MESSAGES:
            return {...state, read_message: action.payload};
    }
    return state;
}