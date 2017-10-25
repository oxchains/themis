/**
 * Created by zhangxiaojing on 2017/10/20.
 */
import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Field, reduxForm} from 'redux-form';
import {Alert} from 'react-bootstrap';
import Chat from './chat';
import TabsControl from "./react_tab";

var that;

class OrderProgress extends Component {
    constructor(props) {
        super(props);
        that = this;
        this.state = {
            index: 0,
            i:0,
            isFormSubmit: false,
            alertVisible: false,
            chatVisible: true,
        };

        this.nextPage = this.nextPage.bind(this);
        this.prevPage = this.prevPage.bind(this);
        this.orderMessage=this.orderMessage.bind(this);
        this.renderDangerAlert = this.renderDangerAlert.bind(this);
        this.orderMessageDetails=this.orderMessageDetails.bind(this);
    }
    nextPage() {
        let {index} = this.state;
        index++;
        if (index > 5) {
            index = 5;
        }

        this.setState({
            index
        })
    }

    prevPage() {
        let {index} = this.state;
        index--;

        if (index < 0) {
            index = 0;
        }

        this.setState({
            index
        })
    }
    orderMessage(msg){
            return(
                <div className="text-left order-message container g-pt-30 g-pb-40">
                    <div className="row">
                        <div className="col-sm-12 order-status">
                            <span className="g-pr-20">已拍下</span>
                            <span className="g-pr-20">|</span>
                            <span>等待卖家确认，买家暂时不能操作</span>
                        </div>
                        <div className="col-sm-12 order-details g-mt-20 clearfix">
                            <ul>
                                <li className="col-sm-1">订单信息</li>
                                <li className="col-sm-2">交易价格:<span>{msg.price}</span>CNY</li>
                                <li className="col-sm-2">交易数量:<span>{msg.quantity}</span>BTC</li>
                                <li className="col-sm-2">交易金额:<span>{msg.amount}</span>CNY</li>
                            </ul>
                        </div>
                    </div>
                </div>
            )
    }
    orderMessageDetails(msg){
        return(
            <div>
                <h4 className="h4">订单详情</h4>
                <hr/>
                <div>
                    <ul>
                        <li>交易数量:<span>{msg.quantity}</span>BTC</li>
                        <li>交易金额:<span>{msg.amount}</span>BTC</li>
                        <li>订单编号:<span>{msg.number}</span></li>
                        <li>支付方式:<span>{msg.way}</span></li>
                        <li>广告内容:<span>{msg.info}</span></li>
                    </ul>
                </div>
            </div>
        )
    }
    renderDangerAlert(){
        return (
            <Alert bsStyle="success" onDismiss={() => {
                this.setState({alertVisible: false})
            }}>
                <h5>确定已付款给卖家？</h5>
                <div>
                    <button type="button" className="btn btn-primary btn-flat" onClick={this.showAlert}>
                       取消
                    </button>
                    <button type="button" className="btn btn-primary btn-flat" onClick={this.nextPage}>
                        确定
                    </button>
                </div>
            </Alert>
        )
    }
    showAlert(){
        this.setState({
            alertVisible: !this.state.alertVisible,
        })
    }
    render(){
        const msg= {price:121,quantity:23,amount:232,number:3748937208457038,way:"支付宝",info:"100%信誉，在线10分钟迅速发货。"}
        //todo
        return (
            <div className="order-main g-pt-50 g-pb-50">
                <div className="order-header container text-center">
                    <div className="row">
                        <div className="col-sm-2 col-md-offset-1">
                            <div>
                                <div className="row-header">
                                    <div className="row-title">
                                        <h4 className={`order-flow ${this.state.index == 0 ? 'order-active' : ''}`}><span>买家已拍下</span></h4>
                                        {/*<div className={`icon-circle ${this.state.index == 0 ? 'icon-circle-purple' : ''}`}>1*/}
                                        {/*</div>*/}
                                        <img className="g-ml-25" src="/public/img/arrow-r.png"
                                             style={{width: '17px', height: '29px'}}></img>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="col-sm-2">
                            <div className="row-header">
                                <div className="row-title">
                                    <h4 className={`order-flow ${this.state.index == 1 ? 'order-active' : ''}`}> <span>等待卖家确认</span></h4>
                                    <img className="g-ml-25" src="/public/img/arrow-r.png"
                                         style={{width: '17px', height: '29px'}}></img>
                                </div>
                            </div>
                            <div className="divider-signup"></div>
                        </div>

                        <div className="col-sm-2">
                            <div className="row-header">
                                <div className="row-title">
                                    <h4 className={`order-flow ${this.state.index == 2 ? 'order-active' : ''}`}><span>等待买家付款</span></h4>
                                    <img className="g-ml-25" src="/public/img/arrow-r.png"
                                         style={{width: '17px', height: '29px'}}></img>
                                </div>
                            </div>
                            <div className="divider-signup"></div>
                        </div>

                        <div className="col-sm-2">
                            <div className="row-header">
                                <div className="row-title">
                                    <h4 className={`order-flow ${this.state.index == 3 ? 'order-active' : ''}`}><span>等待买家收货</span></h4>
                                    <img className="g-ml-25" src="/public/img/arrow-r.png"
                                         style={{width: '17px', height: '29px'}}></img>
                                </div>
                            </div>
                            <div className="divider-signup"></div>
                        </div>
                        <div className="col-sm-2">
                            <div className="row-header">
                                <div className="row-title">
                                    <h4 className={`order-flow ${this.state.index == 4 ? 'order-active' : ''}`}><span>等待双方评价</span></h4>
                                </div>
                            </div>
                            <div className="divider-signup"></div>
                        </div>
                    </div>
                </div>
                {this.orderMessage(msg)}
                <div className="order-content container">
                    <div className="row">
                        <div className="col-sm-7">
                            <div className="order-chat clearfix text-center">
                                <TabsControl>
                                    <div name="聊天"><Chat/></div>
                                    <div name="卖家信息">{this.orderMessageDetails(msg)}</div>
                                </TabsControl>
                            </div>
                        </div>
                        <div className="col-sm-5">
                            <form className="form-horizontal">
                                {/*  第一页  */}
                                <div className={`order-page0 ${this.state.index == 0 ? "show" : "hidden"}`}>
                                    <div className="row order-operation">
                                        <div className="col-sm-12">
                                            {this.orderMessageDetails(msg)}
                                            <div className="order-tip">买家已经拍下订单，请等待卖家确定具体的交易金额和数量</div>
                                            <div>
                                                <button type="button" className="btn btn-primary btn-flat" onClick={this.nextPage}>
                                                    等待卖家确认
                                                </button>
                                                <button type="button" className="btn btn-primary btn-flat" disabled>
                                                    取消订单
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                {/*  第一页  */}

                                {/*  第二页  */}
                                <div className={`order-page1 ${this.state.index == 1 ? "show" : "hidden"}`}>
                                    <div className="row order-operation">
                                        <div className="col-sm-12">
                                            {this.orderMessageDetails(msg)}
                                            <div className="order-tip">买家已经确认具体金额和交易，等待卖家付款，30分钟内没有确认支付将自动关闭订单。</div>
                                            <div>
                                                <button type="button" className="btn btn-primary btn-flat" onClick={this.showAlert.bind(this)}>
                                                    标记为已经付款
                                                </button>
                                                <button type="button" className="btn btn-primary btn-flat" disabled>
                                                    取消订单
                                                </button>
                                                {
                                                    this.state.alertVisible ? this.renderDangerAlert() : <div></div>
                                                }
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                {/*  第二页  */}

                                {/*  第三页  */}
                                <div className={`order-page2 ${this.state.index == 2 ? "show" : "hidden"}`}>

                                    <div className="row order-operation">
                                        <div className="col-sm-12">
                                            {this.orderMessageDetails(msg)}
                                            <div className="order-tip">卖家已标记为付款，等待卖家确认并释放比特币。</div>
                                            <div>
                                                <button type="button" className="btn btn-primary btn-flat" onClick={this.nextPage}>
                                                    等待卖家释放比特币
                                                </button>
                                                <br/>
                                                <a href="/arbitrationbuyer">交易有疑问？点此联系THEMIS仲裁</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                {/*  第三页  */}

                                {/*  第四页  */}
                                <div className={`order-page3 ${this.state.index == 3 ? "show" : "hidden"}`}>

                                    <div className="row order-operation">
                                        <div className="col-sm-12">
                                            {this.orderMessageDetails(msg)}
                                            <div className="order-tip">卖家已标记为付款，等待卖家确认并释放比特币。</div>
                                            <div>
                                                <button type="button" className="btn btn-primary btn-flat" onClick={this.nextPage}>
                                                    卖家比特币已释放完成，即将完成此次交易10s
                                                </button>
                                                <br/>
                                                <a href="/arbitrationbuyer">交易有疑问？点此联系THEMIS仲裁</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                {/*  第四页  */}


                                {/*  第五页  */}
                                <div className={`order-page4 ${this.state.index == 4 ? "show" : "hidden"}`}>
                                    <div className="row order-operation">
                                        <div className="col-sm-12">
                                            {this.orderMessageDetails(msg)}
                                            <div className="comment">
                                                <h5>对卖家饿32额额用户进行评价</h5>
                                                <label htmlFor="good" className="radio-inline">
                                                    <input type="radio" name="comment" id="good" value="option1" defaultChecked/>好评
                                                </label>
                                                <label htmlFor="medium" className="radio-inline">
                                                    <input type="radio" name="comment" id="medium" value="option2" />中评
                                                </label>
                                                <label htmlFor="bad" className="radio-inline">
                                                    <input type="radio" name="comment" id="bad" value="option3"/>差评
                                                </label>
                                            </div>
                                            <a className="btn btn-primary btn-block btn-flat" href="/ordercompleted">
                                                提交评论
                                            </a>
                                            <br/>
                                            <a href="/arbitrationbuyer">交易有疑问？点此联系THEMIS仲裁</a>
                                        </div>
                                    </div>
                                </div>
                                {/*  第五页  */}
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

function mapStateToProps(state) {
    return {
        index: state.sign.index,
        types: state.sign.types
    }
}

export default OrderProgress;


