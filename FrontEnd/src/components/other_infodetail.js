/**
 * Created by oxchain on 2017/11/10.
 */
import React, { Component } from 'react';
import { Modal, Button } from 'antd';
import { connect } from 'react-redux';
import { fetctMyAdvert, LookOthersdetail, isTrustAndisShield } from '../actions/releaseadvert';
class OtherInfodetail extends Component {
    constructor(props) {
        super(props);
        this.state = {
            status: 1,
            message: '',
            visible: false
        };
        this.onlineBuy = this.onlineBuy.bind(this);
        this.onlineSell = this.onlineSell.bind(this);
        this.toggleButton = this.toggleButton.bind(this);
    }
    componentWillMount(){
        const fromUserId = localStorage.getItem("userId");
        const toUserId = this.props.match.params.id.slice(1);
        this.props.LookOthersdetail({fromUserId, toUserId});
        const userId = this.props.match.params.id.slice(1);
        const noticeType = this.state.status;
        const txStatus = 1;
        const pageNum = 1;
        this.props.fetctMyAdvert({ userId, noticeType, txStatus, pageNum });
    }
    componentWillReceiveProps(nextProps) {
        // console.log(nextProps === this.props);
        // console.log(this.props);
        // console.log(nextProps);
        this.setState({
            buttonStatus:nextProps.array.userRelation.status
         });
    }
    onlineBuy() {
        this.state.status = 1;
        const userId = this.props.match.params.id.slice(1);
        const noticeType = this.state.status;
        const txStatus = 1;
        const pageNum = 1;
        this.props.fetctMyAdvert({ userId, noticeType, txStatus, pageNum });
    }
    onlineSell() {
        this.state.status = 2;
        const userId = this.props.match.params.id.slice(1);
        const noticeType = this.state.status;
        const txStatus = 1;
        const pageNum = 1;
        this.props.fetctMyAdvert({ userId, noticeType, txStatus, pageNum });
    }
    toggleButton(buttonStatus) {
        const authenticated = this.props.authenticated;
        const datanum = this.props.all || [];
        const data = datanum.userTxDetail || [];
        const name = datanum.username;//从后端得到该用户名

        const lastStatus = this.state.buttonStatus;
        const buttonText = buttonStatus === 1 ? '信任' : '屏蔽';

        if(lastStatus == buttonStatus){
            this.state.buttonStatus = 0;
            this.setState({ visible: true, message: `您已取消${buttonText}${name}` });
        }else{
            this.state.buttonStatus = buttonStatus;
            this.setState({ visible: true, message: `您已${buttonText}${name}` });
        }
        // if(authenticated){
            // lastStatus == buttonStatus
            // ? this.setState({ visible: true, buttonStatus: 0, message: `您已取消${buttonText}${name}` })
            // : this.setState({ visible: true, buttonStatus, message: `您已${buttonText}${name}` });
        // }else{
        //     alert('请先登录');
        // }
        const fromUserId = localStorage.getItem("userId");
        const toUserId = this.props.match.params.id.slice(1);
        const status = this.state.buttonStatus;
        this.props.isTrustAndisShield({fromUserId, toUserId, status });
    }
    handleOk = () => {
        this.setState({
            visible: false
        });
    }
    handleRow(){
        const arraydata = this.props.all.pageList || [];    //列表数组的数据
        return arraydata.map((item, index) => {
            return (<tr key={index} className="contentborder">
                <td>BTC</td>
                <td> {item.payType == 1 ? "现金" : item.payType == 2 ? "转账" : item.payType == 3 ? "支付宝" : item.payType == 4 ? "微信" : item.payType == 5 ? "Apple Pay" : ""} </td>
                <td>{item.minTxLimit} CNY- {item.maxTxLimit} CNY</td>
                <td>{item.price} CNY</td>
                <td>
                    <button className="tablebuy" ><a href={`/buydetail:${item.id}`}>{item.noticeType == 1 ? "购买" : "出售"}</a></button>
                </td>
            </tr>);
        });
    }

    render() {
        const datanum = this.props.array || [];
        const data = datanum.userTxDetail || [];
        const { visible, message } = this.state;
        const arraydata = this.props.all.pageList || [];
        return (
            <div className="maincontent">
                <div className="otherdetail-title clear">
                    <div className="detailTitle" style={{ padding: 0 }}>
                        <img src="./public/img/touxiang.png" style={{ width: 100 + 'px', borderRadius: 50 + '%' }} alt="" />

                        <h4 style={{ marginBottom: 10 + 'px' }}>{datanum.username}</h4>
                        <ul className="detailul">
                            <li>
                                <p>{data.txNum}</p>
                                <p>交易次数</p>
                            </li>
                            <li>
                                <p>{data.believeNum}</p>
                                <p>信任人数</p>
                            </li>
                            <li>
                                <p>{data.goodDesc}</p>
                                <p>好评度</p>
                            </li>
                            <li>
                                <p>{data.successCount} BTC</p>
                                <p>历史成交数</p>
                            </li>
                        </ul>
                        <ul className="istrust clear">
                            <li className={`${this.state.buttonStatus == 1 ? "trusted" : "trust"}`} onClick={() => this.toggleButton(1)}>{this.state.buttonStatus == 1 ? "已信任" : "信任"}</li>
                            <li className={`${this.state.buttonStatus == 2 ? "shielded" : "shield"}`} onClick={() => this.toggleButton(2)}>{this.state.buttonStatus == 2 ? "已屏蔽" : "屏蔽"}</li>
                        </ul>
                        <Modal className="modal-style"
                            visible={visible}
                            onOk={this.handleOk}
                            footer={[
                                <Button className="confirmStyle" key="submit" type="primary" size="large" onClick={this.handleOk}>
                                    确认
                                </Button>,
                            ]}
                        >
                            <p className="text-center">{message}</p>
                        </Modal>
                        <Modal className="modal-style"
                            visible={visible}
                            onOk={this.handleOk}
                            footer={[
                                <Button className="confirmStyle " key="submit" type="primary" size="large" onClick={this.handleOk}>
                                    确认
                                   </Button>,
                            ]}
                        >
                            <p className="text-center">{message}</p>
                        </Modal>

                    </div>
                </div>
                <div className="clear otherdetail-content">
                    <div className="other-way">
                        <ul className=" titleul">
                            <li className={`${this.state.status == 1 ? "way-title-item active" : " way-title-item"} `} onClick={this.onlineBuy}>TA的在线购买广告</li>
                            <li className={`${this.state.status == 2 ? "way-title-item active" : " way-title-item "}`} onClick={this.onlineSell}>TA的在线出售广告</li>
                        </ul>
                    </div>
                    <table className={`other-tableborder ${this.props.all.rowCount == 0 || !arraydata? "hidden":"" }`}>
                        <tbody>
                            <tr className={`contentborder`}>
                                <th>交易币种</th>
                                <th>付款方式</th>
                                <th>交易限额</th>
                                <th>价格</th>
                                <th>操作</th>
                            </tr>
                             {this.handleRow()}
                        </tbody>
                    </table>
                </div>
            </div>
        );
    }
}


function mapStateToProps(state) {
    return {
        array: state.advert.array,   //详细信息
        all: state.advert.all,   //他的广告信息
        data: state.advert.data, //是否信任或屏蔽
        authenticated: state.auth.authenticated  //登录状态
    };
}
export default connect(mapStateToProps, {fetctMyAdvert, LookOthersdetail, isTrustAndisShield })(OtherInfodetail);
