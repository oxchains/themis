/**
 * Created by oxchain on 2017/10/18.
 */
export const ROOT_URLC = 'http://192.168.1.111:8081';
export const ROOT_URLL = 'http://192.168.1.170:8083';

export const ROOT_ORDER= 'http://192.168.1.125:8882';
export const ROOT_URLZ = 'http://192.168.1.125:8882';
export const ROOT_ARBITRATE = 'http://192.168.1.125:8883'

export const AUTH_USER = 'auth_user';                               //登录
export const UNAUTH_USER = 'unauth_user';                           //退出登录
export const AUTH_ERROR = 'auth_error';                             //登录失败
export const REQUEST_SUCCESS = 'request_success';                   //http请求正确
export const REQUEST_ERROR = 'request_error';                       //http请求返回错误
export const FETCH_VERIFY_CODE = 'request_verifycode';              //注册获取验证码
export const FETCH_VERIFY_CODE_PHONE = 'request_verifycode_phone';              //修改手机获取验证码


export const FETCH_ADVERT = 'FETCH_ADVERT'                          //发布广告
export const FETCH_BUY_BTC = 'fetch_buy_btc'                        //购买比特币
export const FETCH_SELL_BTC = 'fetch_sell_btc'                      //出售比特币
export const FETCH_SELL_SECAT = 'fetch_sell_seach'                  //出售比特币搜索广告
export const FETCH_BUY_SECAT = 'fetch_buy_seach'                    //购买比特币搜索广告
export const FETCH_BUY_BTC_DETAIL = 'fetch_buy_btc_detail'           //购买比特币广告详情
export const FETCH_SELL_BTC_DETAIL = 'fetch_sell_btc_detail'          //出售比特币广告详情

export const FETCH_ARRAY = 'fetch_array'                            //获取选择框数据
export const FETCH_HOME = 'fetch_home'                              //首页
export const FETCH_COMPLETED_ORDERS = 'fetch_completed_orders';         //获取已完成订单
export const FETCH_NOT_COMPLETED_ORDERS = 'fetch_not_completed_orders'; //获取未完成订单
export const FETCH_ORDERS_DETAILS = 'fetch_orders_details';             //获取订单详情
export const FETCH_TRADE_PARTNER_MESSAGE='fetch_trade_partner_message'  //获取交易伙伴的详细信息

export const FETCH_BUY_NOW='fetch_buy_now'                             //立刻购买
export const FETCH_SELL_NOW='fetch_sell_now'                             //立刻出售
export const FETCH_MY_ADVERT='fetch_my_advert'                             //我的广告
export const FETCH_KEYS = 'fetch_keys'                                  //获取卖家公钥私钥
export const ADD_PAYMENT_INFO = 'add_payment_info'                      //卖家提交付款公钥私钥
export const ADD_TRANSACTION_ID = 'add_transaction_id'                  //卖家提交交易id
export const CONFIRM_ORDER = 'confirm_order'                            //卖家确认订单
export const CONFIRM_SEND_MONEY = 'confirm_send_money'                  //买家付款
export const RELEASE_BTC = "release_btc"                                //卖家释放比特币
export const CONFIRM_GOODS = 'confirm_goods'                            //买家确认收货
export const CANCEL_ORDERS = 'cancel_orders'                            //取消订单
export const SAVE_COMMENT = 'save_comment'                               //提交评价

export const FETCH_ARBITRATE_LIST = 'fetch_arbitrate_list'               //获取仲裁人消息列表
export const UPLOAD_EVIDENCE = 'upload_evidence'                         //提交仲裁凭证
export const FETCH_EVIDENCE = 'fetch_evidence'                           //获取仲裁凭证
export const ARBITRATE_RESULT = 'arbitrate_result'                       //仲裁结果
export const FETCH_OFF_MYBTC = 'fetch_off_mybtc'                         //下架我的广告
export const FETCH_BASE_INFO = 'fetch_base_info'                         //用户中心基本信息
export const FETCH_TRUSTED = 'fetch_trusted'                             //用户中心受信任的
export const FETCH_PHONE = 'fetch_phone'                             //用户中心修改手机号
export const FETCH_PASSWORD = 'fetch_password'                             //用户中心修改密码


export function getAuthorizedHeader() {
    return { authorization: localStorage.getItem('token') }
}

export function requestError(error) {
    return {
        type: REQUEST_ERROR,
        payload: error
    };
}