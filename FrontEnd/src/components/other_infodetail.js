/**
 * Created by oxchain on 2017/11/10.
 */
import React, { Component } from 'react';
import { Modal, Button } from 'antd';
import { connect } from 'react-redux';
// import { } from '../actions/releaseadvert'
class OtherInfodetail extends Component {
    constructor(props) {
        super(props);
        this.state = {
            status:1,
            buttonStatus:0,
            message:'',
            visible: false
        }
        this.onlineBuy =  this.onlineBuy.bind(this)
        this.onlineSell =  this.onlineSell.bind(this)
        this.toggleButton =  this.toggleButton.bind(this)
    }
    onlineBuy(){
        this.setState({
            status : 1
        })
    }
    onlineSell(){
        this.setState({
            status : 2
        })
    }
    toggleButton(buttonStatus) {
        const lastStatus = this.state.buttonStatus;
        const buttonText = buttonStatus === 1?'信任' :'屏蔽'
        const name = 'TA' //从后端得到该用户名
        lastStatus === buttonStatus
            ? this.setState({ visible:true, buttonStatus: 0, message:`您已取消${buttonText}`})
            : this.setState({ visible:true, buttonStatus, message:`您已${buttonText}${name}`})
    }
    handleOk = () => {
        this.setState({
            visible: false
        });
    }
    render() {
        // const datanum = this.props.all || []
        const { visible, message} = this.state;

        return (
            <div className="maincontent">
                <div className="otherdetail-title clear">
                    <div className="detailTitle" style={{padding:0}}>
                        <img src="./public/img/touxiang.png" style={{width:100+'px', borderRadius:50 +'%'}} alt=""/>

                        <h4 style={{marginBottom:10+'px'}}>FENGXIAOLI</h4>
                        <ul className="detailul">
                            <li>
                                {/*<p>{datanum.txNum}</p>*/}
                                <p>100</p>
                                <p>交易次数</p>
                            </li>
                            <li>
                                {/*<p>{datanum.believeNum}</p>*/}
                                <p>100</p>
                                <p>信任人数</p>
                            </li>
                            <li>
                                {/*<p>{datanum.goodDegree}</p>*/}
                                <p>100%</p>
                                <p>好评度</p>
                            </li>
                            <li>
                                {/*<p>{datanum.successCount} BTC</p>*/}
                                <p>100 BTC</p>
                                <p>历史成交数</p>
                            </li>
                        </ul>
                        <ul className="istrust clear">
                            <li className={`${this.state.buttonStatus == 1 ?"trusted" : "trust"}`} onClick={() => this.toggleButton(1)}>{this.state.buttonStatus == 1 ?"已信任" :"信任"}</li>
                            <li className={`${this.state.buttonStatus == 2 ?"shielded" : "shield"}`} onClick={() => this.toggleButton(2)}>{this.state.buttonStatus == 2 ?"已屏蔽" :"屏蔽"}</li>
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
                            <p>{message}</p>
                        </Modal>
                        <Modal className="modal-style"
                               visible={visible}
                               onOk={this.handleOk}
                               footer={[
                                   <Button className="confirmStyle" key="submit" type="primary" size="large" onClick={this.handleOk}>
                                       确认
                                   </Button>,
                               ]}
                        >
                            <p>{message}</p>
                        </Modal>

                    </div>
                </div>
               <div className="clear otherdetail-content">
                   <div className="other-way">
                       <ul className=" titleul">
                           <li className={`${this.state.status == 1 ? "way-title-item active" :" way-title-item"} `}   onClick={this.onlineBuy}>TA的在线购买广告</li>
                           <li className={`${this.state.status == 2 ? "way-title-item active" :" way-title-item "}`} onClick={this.onlineSell}>TA的在线出售广告</li>
                       </ul>
                   </div>
                     <table className="other-tableborder">
                         <tbody>
                         <tr className=" ">
                             <th>交易币种</th>
                             <th>付款方式</th>
                             <th>交易限额</th>
                             <th>价格</th>
                             <th>操作</th>
                         </tr>
                         <tr className="">
                             <td>BTC</td>
                             <td>支付宝</td>
                             <td>30000-100000 CNY</td>
                             <td>48210.28 CNY</td>
                             <td><button className="tablebuy" >出售</button> </td>
                         </tr>
                         </tbody>
                     </table>
               </div>
            </div>
        );
    }
}


function mapStateToProps(state) {
    return {
        all:state.advert.all       //广告详情页面加载时的数据
    };
}
export default connect(mapStateToProps, {  })(OtherInfodetail);
