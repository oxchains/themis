/**
 * Created by oxchain on 2017/10/23.
 */
import axios from 'axios';
import { browserHistory ,hashHistory} from 'react-router';
import {
    ROOT_URLL,
    FETCH_ADVERT,
    FETCH_BUY_BTC,
    FETCH_SELL_BTC,
    FETCH_SELL_SECAT,
    FETCH_BUY_SECAT,
    AUTH_USER,
    UNAUTH_USER,
    AUTH_ERROR,
    FETCH_VERIFY_CODE,
    getAuthorizedHeader
} from './types';

// 发布公告

export function releaseAdvert({ userId ,noticeType  ,location ,currency,premium,price,minPrice, minTxLimit,maxTxLimit,payType  ,noticeContent }, callback) {
    console.log(`发布广告传送的数据: ${userId}, ${noticeType},${location}, ${currency},${premium},${price},${minPrice},${minTxLimit},${maxTxLimit},${payType},${noticeContent}`);
    return function(dispatch) {
        axios.post(`${ROOT_URLL}/notice/broadcast`, { userId ,noticeType  ,location ,currency,premium,price,minPrice, minTxLimit,maxTxLimit,payType  ,noticeContent},{ headers: getAuthorizedHeader() })
            .then(response => {
                console.log(response)
                dispatch({type: FETCH_ADVERT, payload: response})
            })
            .catch(err => callback(err.message));
    }
}

// 购买比特币

export function fetctBuyBTC({  }, callback) {
    console.log(`购买比特币传送的数据: `);
    return function(dispatch) {
        axios.post(`${ROOT_URLL}/notice/broadcast`, { },{ headers: getAuthorizedHeader() })
            .then(response => {
                console.log(response)
                dispatch({type: FETCH_BUY_BTC, payload: response})
            })
            .catch(err => callback(err.message));
    }
}

// 购买比特币搜索

export function fetctBuySeach({  }, callback) {
    console.log(`购买比特币传送的数据: `);
    return function(dispatch) {
        axios.post(`${ROOT_URLL}/notice/broadcast`, { },{ headers: getAuthorizedHeader() })
            .then(response => {
                console.log(response)
                dispatch({type: FETCH_BUY_SECAT, payload: response})
            })
            .catch(err => callback(err.message));
    }
}

// 出售比特币

export function fetctSellBTC({ n}, callback) {
    console.log(`出售比特币传送的数据: ${n}`);
    return function(dispatch) {
        axios.post(`${ROOT_URLL}/notice/broadcast`, { },{ headers: getAuthorizedHeader() })
            .then(response => {
                console.log(response)
                dispatch({type: FETCH_SELL_BTC, payload: response})
            })
            .catch(err => callback(err.message));
    }
}

// 出售比特币搜索

export function fetctSellSeach({  }, callback) {
    console.log(`购买比特币传送的数据: `);
    return function(dispatch) {
        axios.post(`${ROOT_URLL}/notice/broadcast`, { },{ headers: getAuthorizedHeader() })
            .then(response => {
                console.log(response)
                dispatch({type: FETCH_SELL_SECAT, payload: response})
            })
            .catch(err => callback(err.message));
    }
}