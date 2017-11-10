/**
 * Created by oxchain on 2017/11/10.
 */

import React, { Component } from 'react';

import { connect } from 'react-redux';
import { fetctBuyBtcDetail, fetctBuynow} from '../actions/releaseadvert'
import {
    Modal,
    ModalHeader,
    ModalTitle,
    ModalClose,
    ModalBody,
    ModalFooter
} from 'react-modal-bootstrap';

class Buydetail extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isModalOpen: false,
            error: null,
            actionResult: '',
            messmoney:'',
            messnum:'',
        }
    }

    componentWillMount(){
        const noticeId = this.props.match.params.id.slice(1)
        console.log( this.props.match.params.id.slice(1))
        this.props.fetctBuyBtcDetail({noticeId});
    }
    render() {
        const datanum = this.props.all || []
        return (
            <div className="maincontent">
                <div className="detail-title">
                    <div className="detailTitle" style={{padding:0}}>
                        <img src="./public/img/touxiang.png" style={{width:100+'px', borderRadius:50 +'%'}} alt=""/>

                        <h4 style={{marginBottom:10+'px', paddingLeft:15+'px'}}>{datanum.loginname}</h4>
                        <ul className="detailul">
                            <li>
                                <p>{datanum.txNum}</p>
                                <p>交易次数</p>
                            </li>
                            <li>
                                <p>{datanum.believeNum}</p>
                                <p>信任人数</p>
                            </li>
                            <li>
                                <p>{datanum.goodDegree}</p>
                                <p>好评度</p>
                            </li>
                            <li>
                                <p>{datanum.successCount} BTC</p>
                                <p>历史成交数</p>
                            </li>
                        </ul>
                        {/*</div>*/}
                    </div>
                </div>
            </div>
        );
    }
}


function mapStateToProps(state) {
    return {
        data:state.advert.data,     //点击立刻购买返回的data
        all:state.advert.all       //广告详情页面加载时的数据
    };
}
export default connect(mapStateToProps, { fetctBuyBtcDetail, fetctBuynow })(Buydetail);
