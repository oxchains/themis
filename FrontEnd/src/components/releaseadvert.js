/**
 * Created by oxchain on 2017/10/20.
 */
/**
 * Created by oxchain on 2017/10/20.
 */
import React, { Component } from 'react';

import { Field } from 'redux-form';
import { connect } from 'react-redux';

class Releaseadvert extends Component {
    constructor(props) {
        super(props);
        this.state = {}
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
                <h4 className="h4title">交易类型</h4>
                <h5 className="h3title">*选择广告类型</h5>
                <span className="tipspan"> &nbsp;&nbsp;您想要创建什么样的交易广告？如果您希望出售比特币，请确保您在THEMIS的钱包中有比特币。</span>
                <ul className="buytype">
                    <li>在线购买比特币</li>
                    <li>在线出售比特币</li>
                </ul>
                <div className="clear display"></div>
                <h5 className="h3title clear">*所在地</h5>
                <span  className="tipspan"> 请选择你要发布广告的国家</span>
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
export default connect(mapStateToProps,{})(Releaseadvert);
