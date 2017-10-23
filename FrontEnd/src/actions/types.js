/**
 * Created by oxchain on 2017/10/18.
 */
export const ROOT_URLC = 'http://192.168.1.111:8081';

export const ROOT_URLL = 'http://192.168.1.170:8083';


export const AUTH_USER = 'auth_user';                               //登录
export const UNAUTH_USER = 'unauth_user';                           //退出登录
export const AUTH_ERROR = 'auth_error';                             //登录失败
export const REQUEST_SUCCESS = 'request_success';                   //http请求正确
export const REQUEST_ERROR = 'request_error';                       //http请求返回错误
export const FETCH_VERIFY_CODE = 'request_verifycode';              //获取验证码
export const FETCH_ADVERT = 'FETCH_ADVERT'                         //发布广告

export function getAuthorizedHeader() {
    return { authorization: 'Bearer '+localStorage.getItem('token') }
}

export function requestError(error) {
    return {
        type: REQUEST_ERROR,
        payload: error
    };
}