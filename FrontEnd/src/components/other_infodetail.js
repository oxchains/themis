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
            index :0,
            count :0,
            status:1,
            isTrusted:1,
            isShield:1,
            trustmess:"",
            shieldmess:"",
            visible: false,
            visibleshield:false
        }
        this.onlineBuy =  this.onlineBuy.bind(this)
        this.onlineSell =  this.onlineSell.bind(this)
        this.JudgeisTrust =  this.JudgeisTrust.bind(this)
        this.JudgeisShield =  this.JudgeisShield.bind(this)
    }

    componentWillMount(){

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
    JudgeisTrust(){
        let {index} = this.state;
        index++;
        this.setState({
            index
        })
        if (index%2 == 1) {
            if(this.state.isTrusted == 1){
                this.setState({
                    isTrusted : 2,
                    isShield : 1,
                    visible : true,
                    trustmess:'您已信任TA'
                })
            }
        }else if(index%2 == 0){
            this.setState({
                isTrusted : 1,
                visible : true,
                trustmess:'您已取消信任'
            })
        }

        console.log('是否信任' + index)

    }
    JudgeisShield(){
        let {index} = this.state;
        index++;
        this.setState({
            index
        })
        if (index%2 == 0) {
            if(this.state.isShield == 1){
                this.setState({
                    isShield : 2,
                    isTrusted : 1,
                    visibleshield : true,
                    shieldmess:'您已屏蔽TA'
                })
            }
        }else if(index%2 == 1){
            this.setState({
                isShield : 1,
                visibleshield : true,
                shieldmess:'您取消屏蔽TA'
            })
        }

        console.log('是否屏蔽' + index)
    }
    handleOk = () => {
        this.setState({
            visible: false
        });
    }
    handleOkShield = () => {
        this.setState({
            visibleshield: false
        });
    }
    render() {
        // const datanum = this.props.all || []
        const { visible, visibleshield, loading, trustmess, shieldmess} = this.state;

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
                            <li className={`${this.state.isTrusted == 2 ?"trusted" : "trust"}`} onClick={this.JudgeisTrust}>{this.state.isTrusted == 2 ?"已信任" :"信任"}</li>
                            <li className={`${this.state.isShield == 2 ?"shielded" : "shield"}`} onClick={this.JudgeisShield}>{this.state.isShield == 2 ?"已屏蔽" :"屏蔽"}</li>
                        </ul>
                        <Modal className="modal-style"
                            visible={visible}
                            onOk={this.handleOk}
                            footer={[
                                <Button className="confirmStyle" key="submit" type="primary" size="large" loading={loading} onClick={this.handleOk}>
                                    确认
                                </Button>,
                            ]}
                        >
                            <p>{trustmess}</p>
                        </Modal>
                        <Modal className="modal-style"
                               visible={visibleshield}
                               onOk={this.handleOkShield}
                               footer={[
                                   <Button className="confirmStyle" key="submit" type="primary" size="large" loading={loading} onClick={this.handleOkShield}>
                                       确认
                                   </Button>,
                               ]}
                        >
                            <p>{shieldmess}</p>
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
