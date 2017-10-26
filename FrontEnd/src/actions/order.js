/**
 * Created by zhangxiaojing on 2017/10/25.
 */
import axios from 'axios';
import {
    ROOT_ORDER,
    FETCH_NOT_COMPLETED_ORDERS,
    FETCH_COMPLETED_ORDERS,
    FETCH_ORDERS_DETAILS,
    FETCH_TRADE_PARTNER_MESSAGE,
    getAuthorizedHeader,
    requestError
} from './types';

export function fetchNoCompletedOrders({formData}) {
    console.log(formData)
    return function(dispatch) {
        axios({
            method:'post',
            url:`${ROOT_ORDER}/order/findNoCompletedOrders`,
            data:formData,
            headers: getAuthorizedHeader()
        }).then((res) => {
            console.log(res)
            if (res.data.status == 1) {
                dispatch({
                    type: FETCH_NOT_COMPLETED_ORDERS,
                    payload: res.data.data
                })
                callback();
            }
        }).catch( err => dispatch(requestError(err.message)) );
    }
}
export function fetchCompletedOrders({userId}) {
    return function(dispatch) {
        axios({
            method:'post',
            url:`${ROOT_ORDER}/order/findCompletedOrders`,
            data:userId,
            headers: getAuthorizedHeader()
        }).then((res) => {
            console.log(res)
            if (res.data.status == 1) {
                dispatch({
                    type: FETCH_COMPLETED_ORDERS,
                    payload: res.data.data
                })
                callback();
            }
        }).catch( err => dispatch(requestError(err.message)) );
    }
}

export function fetchOrdersDetails({data},callback) {
    return function (dispatch) {
        axios({
            method: 'post',
            url: `${ROOT_ORDER}/order/findOrdersDetails `,
            data: data,
            headers: getAuthorizedHeader()
        }).then((res) => {
            if (res.data.status == 1) {
                dispatch({
                    type: FETCH_ORDERS_DETAILS,
                    payload: res.data.data
                })
                callback(res.data.data);
            }
        }).catch(err => dispatch(requestError(err.message)));
    }
}


export function fetchTradePartnerMessage({partner}) {
    return function (dispatch) {
        axios({
            method: 'post',
            url: `${ROOT_ORDER}/order/findUserTxDetail`,
            data: partner,
            headers: getAuthorizedHeader()
        }).then((res) => {
            if (res.data.status == 1) {
                dispatch({
                    type: FETCH_TRADE_PARTNER_MESSAGE,
                    payload: res.data.data
                })
                callback();
            }
        }).catch(err =>{
                dispatch(requestError(err.message))
            });
    }
}
