/**
 * Created by oxchain on 2017/10/23.
 */

import React, { Component } from 'react';

import { Field } from 'redux-form';
import { connect } from 'react-redux';

class Selldetail extends Component {
    constructor(props) {
        super(props);
        this.state = {
            message:'',
            messagenum:'',
            price:'34567'
        }
        this.handelChangemoney = this.handelChangemoney.bind(this)
        this.handelChangenum = this.handelChangenum.bind(this)
    }
    handelChangemoney(event){
    console.log(event.target);
    this.setState({
        message:(event.target.value) / this.state.price
    })
    }
    handelChangenum(event){
        console.log(event.target);
        this.setState({
            messagenum:(event.target.value) * this.state.price
        })
    }

    render() {
        var messmoney = this.state.message;
        var messnum = this.state.messagenum;
        return (
            <div className="maincontent">
                <div className="detail-title">
                    <div className="col-lg-8 col-xs-12 col-md-12" style={{padding:0}}>
                        <div className="col-lg-3 col-xs-3 col-md-3 title-img">
                            <img src="./public/img/touxiang.jpg" style={{width:100+'px'}} alt=""/>
                        </div>
                        <div className="col-lg-9 col-xs-9 col-md-9 title-img">
                            <h4 style={{marginBottom:10+'px',paddingLeft:15+'px'}}>HALLAY</h4>
                            <ul className="detailul">
                                <li>
                                    <p>390</p>
                                    <p>交易次数</p>
                                </li>
                                <li>
                                    <p>1190</p>
                                    <p>信任人数</p>
                                </li>
                                <li>
                                    <p>99%</p>
                                    <p>好评度</p>
                                </li>
                                <li>
                                    <p>2-98btc</p>
                                    <p>历史成交数</p>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div className="price-detail clear">
                    <div className="col-lg-9 col-xs-9 col-md-9">
                        <div>
                            <ul className="priceul">
                                <li>报价 : &#x3000;&#x3000;&#x3000;&#x3000;&#x3000;34567CNY/BTC</li>
                                <li>交易额度 : &#x3000;&#x3000;&#x3000;2000-2000 CNY</li>
                                <li>付款方式 : &#x3000;&#x3000;&#x3000;支付宝</li>
                                <li>付款期限 : &#x3000;&#x3000;&#x3000;30 分钟</li>
                            </ul>
                            <h4 className="sellwhat">你想出售多少？</h4>
                            <input type="text" className="inputmoney sellmoney" onChange={this.handelChangemoney} name="amount" value={messnum} placeholder="请输入你想出售的金额"/>

                            <i className="fa fa-exchange" aria-hidden="true"></i>
                            <input type="text" className="inputmoney sellmoney" onChange={this.handelChangenum} name="qty" value={messmoney} placeholder="请输入你想出售的数量"/>
                            <button className="form-sell">立刻出售</button>
                        </div>

                    </div>
                    <div className="col-lg-3 col-xs-3 col-md-3">
                          <h5 className="adcontent">广告内容</h5>
                        <div className="ad-info">
                          <p> 秒付款，支付宝优先。【不留支付宝名字的一律视为恶意竞拍】</p>
                            <p>拍下的请您提供支付宝账号:</p>
                            <p>支付宝姓名:</p>
                        </div>
                    </div>
                </div>

                <div className="detail-notice">
                    <h4 className="sellwhat">交易须知</h4>
                    <p>1.交易前请详细了解对方的交易信息。</p>
                    <p>2.请通过平台进行沟通约定，并保存好相关聊天记录。</p>
                    <p>3.如遇到交易纠纷，可通过申诉来解决问题。</p>
                    <p>4.在您发起交易请求后，比特币被锁定在托管中，受到themis保护。如果您是买家，发起交易请求后，请在付款周期内付款并把交易标记为付款已完成。卖家在收到付款后将会放行处于托管中的比特币。</p>
                    <p>交易前请阅读《themis服务条款》以及常见问题，交易指南等帮助文档。</p>
                    <p>5.请注意欺诈风险，交易前请检查该用户收到的评价信息和相关信用信息，并对新近创建的账户多加留意。</p>
                    <p>6.托管服务保护网上交易的买卖双方。在双方发生争议的情况下，我们将评估所提供的所有信息，并将托管的比特币放行给其合法所有者。</p>
                </div>
            </div>
        );
    }
}



function mapStateToProps(state) {
    return {
        success: state.auth.authenticated,
        errorMessage: state.auth.error
    };
}
export default connect(mapStateToProps,{})(Selldetail);
