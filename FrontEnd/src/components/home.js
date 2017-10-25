/**
 * Created by oxchain on 2017/10/20.
 */

import React, { Component } from 'react';

import { Field } from 'redux-form';
import { connect } from 'react-redux';
import Header from  './common/header';

class Baseinfo extends Component {
    constructor(props) {
        super(props);
        this.state = {}
        this.renderRows = this.renderRows.bind(this)
        this.renderArray = this.renderArray.bind(this)
    }

    renderRows(item,index){
        return(
                <div  key={index} className="list-border">
                    <div className="title-bgc">
                        <img src={item.src} alt=""/>
                        <p>{item.title}</p>
                    </div>
                    <div className="col-lg-4">
                        <p>{item.jiaoyinum}</p>
                        <p>交易次数</p>
                    </div>
                    <div className="col-lg-4">
                        <p>{item.trusted}</p>
                        <p>信任人数</p>
                    </div>
                    <div className="col-lg-4">
                        <p>{item.bili}</p>
                        <p>信誉度</p>
                    </div>
                    <hr/>
                    <div className="home-content">
                        <p>交易价格:{item.price}CNY</p>
                        <p>交易限额:{item.limprice} CNY</p>
                        <p>付款方式:{item.payway}</p>
                    </div>
                    <button className="home-button" ><a href="">购买比特币</a></button>
                </div>

        )
    }
    renderArray(item,index){
        return(
            <div key={index} className="list-item">
                <img src={item.src} alt=""/>
                <p>{item.title}</p>
                <div className="home-content">
                    <p>{item.content}</p>
                </div>
            </div>
        )
    }


    render() {

        const RowLinks = [
            { src:"./public/img/touxiang.png",title:"风一样的女子",jiaoyinum:"6",trusted:"3",bili:"100%",price:"37517.67",limprice:"1000-7878",payway:"现金付款",btn:"购买比特币"},
            { src:"./public/img/touxiang.png",title:"风一样的女子",jiaoyinum:"6",trusted:"3",bili:"100%",price:"37517.67",limprice:"1000-7878",payway:"现金付款",btn:"购买比特币"},
            { src:"./public/img/touxiang.png",title:"风一样的女子",jiaoyinum:"6",trusted:"3",bili:"100%",price:"37517.67",limprice:"1000-7878",payway:"现金付款",btn:"购买比特币"},
            { src:"./public/img/touxiang.png",title:"风一样的女子",jiaoyinum:"6",trusted:"3",bili:"100%",price:"37517.67",limprice:"1000-7878",payway:"现金付款",btn:"购买比特币"}
        ]
        const ArrayLinks = [
            { src:"./public/img/买卖-.png",title:"快速买卖",content:"themis是一个不涉及第三方的P2P交易平台，交易过程方便快捷"},
            { src:"./public/img/安全.png",title:"安全交易",content:"冷存储、SSL、多重加密等银行级别安全技术，十年金融安全经验安全团队"},
            { src:"./public/img/快速.png",title:"及时掌控",content:"行情及时掌握,交易随时随地"},
        ]


        return (
            <div className="">
                <div className="headermain">
                    <div className="bannertitle">
                        <h2>THEMIS</h2>
                        <h4>T H E M I S 比 特 币 场 外 交 易 平 台</h4>
                        <hr/>
                        <h5>去中心化托管更安全</h5>
                    </div>
                </div>
                <div className="homemodle">
                    <div className="model-title">
                        <h4>多方验证 买卖自由 安全可靠</h4>
                        <h5><a href="/buybtc">查看更多的广告</a></h5>
                    </div>
                    <div className="modle-list">
                        {RowLinks.map(this.renderRows)}
                    </div>
                </div>
                <div className="home-bottom">
                   <div className="list-width">
                       {ArrayLinks.map(this.renderArray)}
                   </div>
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
export default connect(mapStateToProps,{})(Baseinfo);
