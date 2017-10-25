/**
 * Created by oxchain on 2017/10/23.
 */
import axios from 'axios';
import { browserHistory ,hashHistory} from 'react-router';
import {
    ROOT_URLL,
    FETCH_ADVERT,
    AUTH_USER,
    UNAUTH_USER,
    AUTH_ERROR,
    FETCH_VERIFY_CODE,
    getAuthorizedHeader
} from './types';



export function releaseAdvert({ loginname ,noticeType  ,location ,currency,premium,price,minPrice, minTxLimit,maxTxLimit,payType  ,noticeContent }, callback) {
    console.log(`发布广告传送的数据: ${loginname}, ${noticeType},${location}, ${currency},${premium},${price},${minPrice},${minTxLimit},${maxTxLimit},${payType},${noticeContent}`);
    return function(dispatch) {
        axios.post(`${ROOT_URLL}/notice/broadcast`, { loginname ,noticeType  ,location ,currency,premium,price,minPrice, minTxLimit,maxTxLimit,payType  ,noticeContent},{ headers: getAuthorizedHeader() })
            .then(response => {
                console.log(response)
                dispatch({type: FETCH_ADVERT, payload: response})
            })
            .catch(err => callback(err.message));
    }
}