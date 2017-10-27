/**
 * Created by oxchain on 2017/10/23.
 */
import axios from 'axios';
import { browserHistory ,hashHistory} from 'react-router';
import {
    ROOT_URLL,
    ROOT_URLZ,
    FETCH_ADVERT,
    FETCH_BUY_BTC,
    FETCH_SELL_BTC,
    FETCH_SELL_SECAT,
    FETCH_BUY_SECAT,
    FETCH_BUY_BTC_DETAIL,
    FETCH_SELL_BTC_DETAIL,
    FETCH_ARRAY,
    FETCH_HOME,
    getAuthorizedHeader
} from './types';

// 首页

export function fetctHome(callback) {
    return function(dispatch) {
        axios.get(`${ROOT_URLL}/notice/query/part`, { headers: getAuthorizedHeader() })
            .then(response => {
                console.log("首页")
                console.log(response)
                dispatch({type: FETCH_HOME, payload: response})
            })
            .catch(err => {
                console.error(err)
            });
    }
}


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

export function fetctBuyBTC({page},callback) {
    console.log(`购买比特币传送的数据: ${page}`);
    return function(dispatch) {
        axios.get(`${ROOT_URLL}/notice/search/default/buy?noticeType=1&pageNum=${page}`, { headers: getAuthorizedHeader() })
            .then(response => {
                console.log(response)
                dispatch({type: FETCH_BUY_BTC, payload: response})
            })
            .catch(err => {
                console.error(err)
            });
    }
}
// 购买比特币广告详情

export function fetctBuyBtcDetail({noticeId},callback) {
    console.log(`购买比特币广告详情:${noticeId} `);
    return function(dispatch) {
        axios.post(`${ROOT_URLZ}/order/findUserTxDetailAndNotice`,{noticeId},{ headers: getAuthorizedHeader() })
            .then(response => {
                console.log(response)
                dispatch({type: FETCH_BUY_BTC_DETAIL, payload: response})
            })
            .catch(err => {
                console.error(err)
            });
    }
}

// 购买比特币搜索
// 搜索广告

export function fetcAdvertSeach({searchType,location,currency,payType,currentPage }, callback) {
    console.log(`点击购买搜索传送的数据:${searchType},${location},${currency} ,${payType},${currentPage}`);
    return function(dispatch) {
        axios.post(`${ROOT_URLL}/notice/search/page/buy`, {searchType,location,currency,payType,currentPage },{ headers: getAuthorizedHeader() })
            .then(response => {
                console.log(response)
                dispatch({type: FETCH_BUY_BTC, payload: response})
            })
            .catch(err => callback(err.message));
    }
}

// 出售比特币

export function fetctSellBTC({page}, callback) {
    console.log(`出售比特币传送的数据:`);
    return function(dispatch) {
        axios.get(`${ROOT_URLL}/notice/search/default/sell?noticeType=2&pageNum=${page}`,{ headers: getAuthorizedHeader() })
            .then(response => {
                console.log(response)
                dispatch({type: FETCH_SELL_BTC, payload: response})
            })
            .catch(err => callback(err.message));
    }
}

// 出售比特币广告详情

export function fetctSellBtcDetail({noticeId},callback) {
    console.log(`出售比特币广告详情:${noticeId} `);
    return function(dispatch) {
        axios.post(`${ROOT_URLZ}/order/findUserTxDetailAndNotice`,{noticeId},{ headers: getAuthorizedHeader() })
            .then(response => {
                console.log(response)
                dispatch({type: FETCH_SELL_BTC_DETAIL, payload: response})
            })
            .catch(err => {
                console.error(err)
            });
    }
}

// 出售比特币搜索

export function fetctSellSeach({searchType,location,currency,payType,currentPage }, callback) {
    console.log(`点击出售搜索传送的数据:${searchType},${location},${currency} ,${payType},${currentPage}`);
    return function(dispatch) {
        axios.post(`${ROOT_URLL}/notice/search/page/sell`, {searchType,location,currency,payType,currentPage },{ headers: getAuthorizedHeader() })
            .then(response => {
                console.log(response)
                dispatch({type: FETCH_SELL_BTC, payload: response})
            })
            .catch(err => callback(err.message));
    }
}


// 选择框数据

export function fetctArray( callback) {

    return function(dispatch) {
        axios.get(`${ROOT_URLL}/notice/query/statusKV`, { headers: getAuthorizedHeader() })
            .then(response => {
                // console.log(`选择框返回的数据: `);
                // console.log(response)
                dispatch({type: FETCH_ARRAY, payload: response})
            })
            .catch(err => console.error(err.message));
    }
}

