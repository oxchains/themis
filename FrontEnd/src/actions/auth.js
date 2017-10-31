/**
 * Created by oxchain on 2017/10/18.
 */
import axios from 'axios';
import { browserHistory ,hashHistory} from 'react-router';
import {
    ROOT_URLC,
    AUTH_USER,
    UNAUTH_USER,
    AUTH_ERROR,
    FETCH_VERIFY_CODE
} from './types';


/**
 * 登录
 */
export function signinAction({mobilephone, password}) {
    console.log(`点击登录按钮传过来的数据是 ${mobilephone},${password}`)
    return function(dispatch) {
        axios.post(`${ROOT_URLC}/user/login`, { mobilephone, password})
            .then(response => {
                console.log(response)
                if(response.data.status == 1) {
                    localStorage.setItem('token', response.data.data.token);
                    localStorage.setItem('userId', response.data.data.id); //用户ID
                    localStorage.setItem('loginname',response.data.data.loginname); //用户登录名
                    localStorage.setItem('mobilephone',response.data.data.mobilephone);//手机号
                    localStorage.setItem('createTime',response.data.data.createTime);//注册时间
                    localStorage.setItem('email',response.data.data.email);//邮箱
                    localStorage.setItem('firstBuyTime',response.data.data.userTxDetail.firstBuyTime) //第一次交易时间
                    localStorage.setItem('txNum',response.data.data.userTxDetail.txNum) //交易次数
                    localStorage.setItem('believeNum',response.data.data.userTxDetail.believeNum) //信任人数
                    localStorage.setItem('sellAmount',response.data.data.userTxDetail.sellAmount) //出售的累计交易数量
                    localStorage.setItem('buyAmount',response.data.data.userTxDetail.buyAmount) //购买的累计交易数量

                    dispatch({type: AUTH_USER});
                    // browserHistory.push('/');
                } else {
                    dispatch(authError(response.data.message));
                }
            })
            .catch( (err) => dispatch(authError(err.message)) );
    }
}

export function authError(error) {
    return {
        type: AUTH_ERROR,
        payload: error
    };
}

// 登出
export function signoutUser() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    localStorage.removeItem('username');
    return { type: UNAUTH_USER };
}

/**
 * 注册
 */

export function signupUser({ loginname, mobilephone, email,password }, callback) {
    console.log(`注册传送的数据: ${loginname}, ${mobilephone},${email}, ${password}`);
    return function(dispatch) {
        axios.post(`${ROOT_URLC}/user/register`, { loginname, mobilephone, email,password })
            .then(response => {
                console.log(response)
                if(response.data.status == 1) {
                    callback();
                } else {
                    callback(response.data.message);
                }
            })
            .catch(err => callback(err.message));
    }
}

/**
 * 获取验证码
 */

export function GetverifyCode({phonenum},callback) {
    console.log("点击发送验证码带过来的手机号" + phonenum)
    return function(dispatch) {
        axios.get(`${ROOT_URLC}/verifyCode`,{phonenum})
            .then(response => {
                console.log("获取验证码的接口通了")
                console.log(response)
                dispatch({ type: FETCH_VERIFY_CODE, payload:response })

            })
            .catch(err => (err.message));
    }
}