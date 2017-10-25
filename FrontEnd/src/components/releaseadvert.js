/**
 * Created by oxchain on 2017/10/20.
 */
/**
 * Created by oxchain on 2017/10/20.
 */
import React, { Component } from 'react';

import { Field } from 'redux-form';
import { connect } from 'react-redux';
import { releaseAdvert } from '../actions/releaseadvert'

class Releaseadvert extends Component {
    constructor(props) {
        super(props);
        this.state = {
            currentIndex:0,
        option: "0"
        }
    }
    handleChangecounty(e){
        console.log(e.target.value)
        this.setState({option: e.target.value});
    }

    handleFormSubmit(e){
        if(this.props.authenticated){
            e.preventDefault()
            // const loginname= localStorage.getItem('username');
            const loginname= "fengxiaoli"
            const premium = this.refs.premium.value;
            const price = this.refs.price.value;
            const minPrice = this.refs.minPrice.value;
            const minTxLimit = this.refs.minTxLimit.value;
            const maxTxLimit = this.refs.maxTxLimit.value;
            const noticeContent = this.refs.noticeContent.value;
            const noticeType = "1";
            const location = '中国';
            const currency = '人民币';
            const payType = "微信支付"
            console.log(noticeType)
            this.props.releaseAdvert({loginname ,noticeType  ,location ,currency,premium,price,minPrice, minTxLimit,maxTxLimit,payType  ,noticeContent},()=>{});

        }else {
                alert('请先登录')
        }
    }
    render() {

        return (
            <div className="maincontent">
                <h2 className="h2title">发布一个比特币交易广告</h2>
                <div className="tipcontent">
                    <p>如果您经常交易比特币，可以发布自己的比特币交易广告。如果您只想购买或者出售一次，我们建议您直接从购买或出售列</p>
                    <p>表中下单交易。</p>
                    <p>发布一则交易广告是免费的。</p>
                    <p>您THEMIS钱包中至少需要有0.05 BTC,您的广告才会显示在交易列表中。</p>
                    <p>发布交易广告的THEMIS用户每笔完成的交易需要缴纳0.7%的费用。</p>
                    <p>您必须在交易广告或交易聊天中提供您的付款详细信息，发起交易后，价格会锁定，除非定价有明显错误。</p>
                    <p>所有交流必须在THEMIS上进行，请注意高风险有欺诈的付款方式。</p>
                </div>
                <form action="" onSubmit={this.handleFormSubmit}>
                    <h4 className="h4title">交易类型</h4>
                    <h5 className="h3title">*选择广告类型</h5>
                    <span className="tipspan"> &nbsp;&nbsp;您想要创建什么样的交易广告？如果您希望出售比特币，请确保您在THEMIS的钱包中有比特币。</span>
                    <TabsControl >
                        <Tab name="在线购买比特币"></Tab>
                        <Tab name="在线出售比特币"></Tab>
                    </TabsControl>
                    <div className="clear display"></div>
                    <h5 className="h3title clear">*所在地</h5>
                    <span  className="tipspan"> 请选择你要发布广告的国家。</span>
                    <select name="" id="" className="display slectoption" value={this.state.option}  onChange={this.handleChangecounty}>
                        <option value="0">选择国家</option>
                        <option value="1">中国</option>
                        <option value="2">美国</option>
                        <option value="3">英国</option>
                        <option value="4">韩国</option>
                    </select>
                    <h4 className="h4title">更多信息</h4>
                    <h5 className="h3title clear">*货币:</h5>
                    <span  className="tipspan"> 您希望交易付款的货币类型。</span>
                    <select name="" id="" className="display slectoption">
                        <option value="0">选择货币</option>
                        <option value="1">人民币</option>
                        <option value="2">日元</option>
                        <option value="3">美元</option>
                        <option value="4">韩元</option>
                    </select>
                    <h5 className="h3title clear">*溢价: </h5>
                    <span  className="tipspan">基于市场价的溢出比例，市场价是根据部分大型交易所实时价格得出的，确保您的报价趋于一个相对合理的范围，比如当前价格为7000，溢价比例为10%，那么价格为7700。</span>
                    <input type="text" placeholder="%" className="display slectoption" ref="premium" required/>
                    <h5 className="h3title clear">*价格: </h5>
                    <span  className="tipspan">基于溢价比例得出的报价，10分钟更新一次。</span>
                    <input type="text" placeholder="CNY" className="display slectoption" ref="price" required/>
                    <h5 className="h3title clear">*最低价:</h5>
                    <span  className="tipspan">最低可成交的价格，可帮助您在价格剧烈波动时保持稳定的盈利，比如最低价为12000，市场价处于12000以下时，您的广告将依旧以12000的价格展示出来。</span>
                    <input type="text" placeholder="CNY" className="display slectoption" ref="minPrice" required/>
                    <h5 className="h3title clear">*最小限额: </h5>
                    <span  className="tipspan">一次交易的最低交易限制。</span>
                    <input type="text" placeholder="请输入最小限额 CNY" className="display slectoption" ref="minTxLimit" required/>
                    <h5 className="h3title clear">*最大限额: </h5>
                    <span  className="tipspan">一次交易中的最大交易限制，您的钱包余额也会影响最大量的设置。</span>
                    <input type="text" placeholder="请输入最大限额 CNY" className="display slectoption" ref="maxTxLimit" required/>
                    <h5 className="h3title clear">*收款方式:</h5>
                    <span  className="tipspan"> 您希望交易付款的货币类型。</span>
                    <select name="" id="" className="display slectoption">
                        <option value="0">支付方式</option>
                        <option value="1">微信支付</option>
                        <option value="2">支付宝</option>
                        <option value="3">银联</option>
                        <option value="4">Apple pay</option>
                    </select>
                    <h5 className="h3title clear">*广告内容:</h5>
                    <textarea name="" id="" cols="150" rows="6" className="display text-content" ref="noticeContent" placeholder="请说明有关您交易的相关条款或备注您的支付方式，如微信号，支付宝号等，以便对方可以快速和您交易。(下单前后都可见)" ></textarea>
                    <button type="submit" className="  form-apply">申请发布</button>
                </form>

            </div>
        );
    }
}

let TabsControl = React.createClass({
    getInitialState: function(){
        return {currentIndex: 0}
    },
    getTitleItemCssClasses(index){
        return index === this.state.currentIndex ? "tab-title-item active" : "tab-title-item";
    },
    render(){
        let that = this;
        return (
            <div className="">
                    <ul className=" buytype">
                        {React.Children.map(this.props.children, (element, index) => {
                            return (<li className={` ${that.getTitleItemCssClasses(index)}`} onClick={() => {this.setState({currentIndex: index})}}>
                                {element.props.name}</li>)
                        })}
                    </ul>
            </div>
        )
    }
});
let Tab = React.createClass({
    render(){
        return (<div>{this.props.children}</div>);
    }
});



function mapStateToProps(state) {
    console.log(state)
    return {
        authenticated: state.auth.authenticated
    };
}
export default connect(mapStateToProps,{releaseAdvert})(Releaseadvert);
