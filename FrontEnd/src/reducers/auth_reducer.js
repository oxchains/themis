/**
 * Created by oxchain on 2017/10/18.
 */

import {
    AUTH_USER,
    UNAUTH_USER,
    AUTH_ERROR,
    FETCH_VERIFY_CODE
} from '../actions/types';

export default function(state = {}, action) {
    switch(action.type) {
        case AUTH_USER:
            return { ...state, error: '', authenticated: true };
        case UNAUTH_USER:
            return { ...state, authenticated: false };
        case AUTH_ERROR:
            return { ...state, error: action.payload, authenticated: false };
        case FETCH_VERIFY_CODE:
            return { ...state, all: action.payload.data.data };
    }

    return state;
}