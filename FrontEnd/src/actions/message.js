/**
 * Created by zhangxiaojing on 2017/11/8.
 */
import axios from 'axios';
import {
    ROOT_MESSAGE,
    FETCH_UNREAD_MESSAGES,
    FETCH_READ_MESSAGES,
    getAuthorizedHeader,
    requestError
} from './types';
export function fetchUnreadMessage({userId, pageNum, pageSize}){
    console.log(userId)
    let tip=1;
    return function message(dispatch) {
        console.log(tip)
        axios.get(`${ROOT_MESSAGE}/message/query/privateMsgNoRead?userId=${userId}&pageNum=${pageNum}&pageSize=${pageSize}&tip=${tip}`, { headers: getAuthorizedHeader() })
            .then(response => {
                dispatch({type: FETCH_UNREAD_MESSAGES, payload: response.data.data});
                tip=2;
                message(dispatch);
            })
            .catch(err => dispatch(requestError(err.message)));
    }
}
export function fetchReadMessage({userId, pageNum, pageSize}, calback){
    return function(dispatch) {
        axios.get(`${ROOT_MESSAGE}/message/query/privateMsgYesRead?userId=${userId}&pageNum=${pageNum}&pageSize=${pageSize}`, { headers: getAuthorizedHeader() })
            .then(response => {
                dispatch({type: FETCH_READ_MESSAGES, payload: response.data.data})
            })
            .catch(err => dispatch(requestError(err.message)));
    }
}