/**
 * Created by zhangxiaojing on 2017/10/20.
 */
import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Alert} from 'react-bootstrap';
import Chat from './chat';
import QRCode from 'qrcode.react';
import {Upload, Button, Icon, Modal, message} from 'antd';
import TabsControl from "./react_tab";
import {ROOT_ARBITRATE} from '../actions/types';
import {uploadEvidence} from '../actions/arbitrate';
import {fetchOrdersDetails, fetchTradePartnerMessage, addPaymentInfo, addTransactionId, fetchKey, confirmOrder, confirmSendMoney, releaseBtc, confirmGoods, saveComment, cancelOrders} from '../actions/order';
class OrderProgress extends Component {
    constructor(props) {
        super(props);
        this.state = {
            orderStatus:0,
            alertVisible: false,
            error:false,
            errorMessage:"",
            show:false,
            shownext:false,
            tip:'',
            orderId:1,
            p2shAddress:'',
            amount:0,
            txId:'',
            confirm:true,
            releaseBtc:false,
            partnerName:'',
            comment:1,
            evidence:false,
            status:"",
            fileList: [],
            uploading: false,
            loading:false,
            uri:""
        };
        this.renderDangerAlert = this.renderDangerAlert.bind(this);
        this.renderOrderMessageDetails=this.renderOrderMessageDetails.bind(this);
    }
    componentWillMount() {
        const message=JSON.parse(localStorage.getItem("partner"));
        const userId=localStorage.getItem("userId");
        const data={id:message.id, userId:userId};
        const partnerName=message.friendUsername;
        this.setState({partnerName:partnerName});
        this.props.fetchOrdersDetails({data}, (msg)=>{
            console.log(msg);
            this.setState({orderStatus:msg.orderStatus});
            this.setState({orderId:msg.id});
            switch(this.state.orderStatus){
                case 1:
                    this.setState({tip:"买家已拍下，等待卖家确认", status:"等待卖家确认"});
                    break;
                case 2:
                    this.setState({tip:"卖家已确认，等待买家付款，30分钟内，如逾期订单将自动取消", status:"等待买家付款"});
                    break;
                case 3:
                    this.setState({tip:"买家已经标记为付款，等待卖家确认并释放比特币", status:"等待卖家发货"});
                    break;
                case 4:
                    this.setState({tip:"卖家已释放比特币，等待买家确认收货", status:"等待买家收货"});
                    break;
                case 5:
                    this.setState({tip:"买家已经确认收货，交易即将完成，等待双方进行评价。", status:"等待双方评价"});
                    break;
                case 6:
                    this.setState({tip:"交易已完成", status:"已完成"});
                    break;
                case 7:
                    this.setState({tip:"交易已取消", status:"已取消"});
                    break;
                case 8:
                    this.setState({tip:"退款处理中", status:"退款中"});
                    break;
            }
        });
        const partner={userId:message.partnerId};
        this.props.fetchTradePartnerMessage({partner});
        const orderId={
            id:this.state.orderId
        };
    }
    renderOrderStatus1() {
        return (
            <div>
                <div className="col-sm-2">
                    <div>
                        <div className="row-header">
                            <div className="row-title">
                                <div className="content">
                                    <h4 className={`order-flow ${this.state.orderStatus >= 1 ? 'order-active' : ''}`}></h4>
                                </div>
                                <span className={`${this.state.orderStatus >= 1 ? 'orderMsg—active' : ''}`}>{this.state.orderStatus <= 1 ? '买家已拍下' : '买家已拍下'}</span>
                                <div className={`order-bar ${this.state.orderStatus >= 1 ? 'order-bar-active' : ''}`}></div>
                            </div>
                        </div>
                    </div>
                </div>

                <div className="col-sm-2">
                    <div className="row-header">
                        <div className="row-title">
                            <div className="content">
                                <h4 className={`order-flow ${this.state.orderStatus >= 1 ? 'order-active' : ''}`}></h4>
                            </div>
                            <span className={`${this.state.orderStatus >= 1 ? 'orderMsg—active' : ''}`}>{this.state.orderStatus <= 1 ? '等待卖家确认' : '卖家已确认'}</span>
                            <div className={`order-bar ${this.state.orderStatus >= 2 ? 'order-bar-active' : ''}`}></div>
                        </div>
                    </div>
                </div>

                <div className="col-sm-2">
                    <div className="row-header">
                        <div className="row-title">
                            <div className="content">
                                <h4 className={`order-flow ${this.state.orderStatus >= 2 ? 'order-active' : ''}`}></h4>
                            </div>
                            <span className={`${this.state.orderStatus >= 2 ? 'orderMsg—active' : ''}`}>{this.state.orderStatus <= 2 ? '等待买家付款' : '买家已付款'}</span>
                            <div className={`order-bar ${this.state.orderStatus >= 3 ? 'order-bar-active' : ''}`}></div>
                        </div>
                    </div>
                </div>

                <div className="col-sm-2">
                    <div className="row-header">
                        <div className="row-title">
                            <div className="content">
                                <h4 className={`order-flow ${this.state.orderStatus >= 3 ? 'order-active' : ''}`}></h4>
                            </div>
                            <span className={`${this.state.orderStatus >= 3 ? 'orderMsg—active' : ''}`}>{this.state.orderStatus <= 3 ? '等待卖家发货' : '卖家已收货'}</span>
                            <div className={`order-bar ${this.state.orderStatus >= 4 ? 'order-bar-active' : ''}`}></div>
                        </div>
                    </div>
                </div>
                <div className="col-sm-2">
                    <div className="row-header">
                        <div className="row-title">
                            <div className="content">
                                <h4 className={`order-flow ${this.state.orderStatus >= 4 ? 'order-active' : ''}`}></h4>
                            </div>
                            <span className={`${this.state.orderStatus >= 4 ? 'orderMsg—active' : ''}`}>{this.state.orderStatus <= 4 ? '等待买家收货' : '买家已收货'}</span>
                            <div className={`order-bar ${this.state.orderStatus >= 5 ? 'order-bar-active' : ''}`}></div>
                        </div>
                    </div>
                </div>
                <div className="col-sm-2">
                    <div className="row-header">
                        <div className="row-title">
                            <div className="content">
                                <h4 className={`order-flow ${this.state.orderStatus >= 5 ? 'order-active' : ''}`}></h4>
                            </div>
                            <span className={`${this.state.orderStatus >= 5 ? 'orderMsg—active' : ''}`}>{this.state.orderStatus <= 5 ? '等待双方评价' : '已评价'}</span>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
    renderOrderStatus2(){
        return(
              <div className="h2 g-pt-20 g-pb-20">
                  <div className={`${this.state.orderStatus == 7 ? 'show' : 'hidden'}`}>订单已取消</div>
                  <div className={`${this.state.orderStatus == 8 ? 'show' : 'hidden'}`}>订单已取消，退款中 <br/>
                      <button className="ant-btn ant-btn-primary ant-btn-lg" onClick={this.handleEvidence.bind(this)}>THEMIS仲裁</button>
                  </div>
              </div>
            );
    }
    renderPartnerMessage(partner){
        if(partner==null){
            return <div>loading...</div>;
        }
        return(
            <div className="container-fluid" style={{"height":"381.42px"}}>
                <ul className="row text-left g-pt-40">
                    <li className="col-sm-12 ">
                        {partner.loginname}
                    </li>
                    <li className="col-sm-6">
                        <span>交易量:</span><span>{partner.txNum}</span>
                    </li>
                    <li className="col-sm-6">
                        <span>电子邮箱:</span><span>{partner.emailVerify}</span>
                    </li>
                    <li className="col-sm-6">
                        <span>已确认的交易次数:</span><span>{partner.txNum}</span>
                    </li>
                    <li className="col-sm-6">
                        <span>电话号码:</span><span>{partner.mobilePhoneVerify}</span>
                    </li>
                    <li className="col-sm-6">
                        <span>好评度:</span><span>{partner.goodDegree}</span>
                    </li>
                    <li className="col-sm-6">
                        <span>实名认证:</span><span>{partner.usernameVerify}</span>
                    </li>
                    <li className="col-sm-6">
                        <span>第一次购买:</span><span>{partner.firstBuyTime}</span>
                    </li>
                    <li className="col-sm-6">
                        <span>信任:</span><span>{partner.believeNum}</span>
                    </li>
                    <li className="col-sm-6">
                        <span>用户创建时间:</span><span>{partner.createTime}</span>
                    </li>
                </ul>
            </div>
        );
    }
    renderOrderMessageDetails(msg){
        if(msg==null){
            return(
                <div>loading...</div>
            );
        }
        return(
            <div>
                <h4 className="h4">订单详情</h4>
                <hr/>
                <div>
                    <ul>
                        <li>交易数量:<span>{msg.amount}</span>BTC</li>
                        <li>交易金额:<span>{msg.money}</span>CNY</li>
                        <li>订单编号:<span>{msg.id}</span></li>
                        <li>支付方式:<span>{msg.payment.paymentName}</span></li>
                        <li>广告内容:<span>{msg.notice.noticeContent}</span></li>
                    </ul>
                </div>
            </div>
        );
    }
    renderAlert(){
        return(
            <div>
                {this.state.error == true ? <div className="col-xs-12 alert alert-danger alert-dismissable text-center">{this.state.errorMessage}</div> : ""}
            </div>
        );
    }
    renderDangerAlert(){
        return (
            <Alert bsStyle="success" onDismiss={() => {
                this.setState({alertVisible: false});
            }}>
                <h5>确定已付款给卖家？</h5>
                <div>
                    <button type="button" className="ant-btn ant-btn-primary ant-btn-lg" onClick={this.handleAlert}>
                       取消
                    </button>
                    <button type="button" className="ant-btn ant-btn-primary ant-btn-lg" onClick={this.handleSendMoney.bind(this)} >
                        确定
                    </button>
                </div>
            </Alert>
        );
    }
    handleAlert(){
        this.setState({
            alertVisible: !this.state.alertVisible,
        });
    }
    handlePaymentInfo(){
        const orderId={
            id:this.state.orderId
        };
        this.props.fetchKey({orderId}, (msg)=>{
             if(msg.status == 1){
                 this.setState({uri:msg.data.uri, p2shAddress:msg.data.p2shAddress, amount:msg.data.amount, shownext: true});
             }
             else {
                 this.setState({show: true});
             }
        });
    }
    handleNext(){
        const sellerPubAuth=this.refs.sellerPubAuth.value;
        const sellerPriAuth=this.refs.sellerPriAuth.value;
        const orderId=this.state.orderId;
        console.log(sellerPubAuth);
        if(sellerPubAuth && sellerPriAuth){
             const paymentInfo={
                 sellerPubAuth:sellerPubAuth,
                 sellerPriAuth:sellerPriAuth,
                 orderId:orderId
             };
             this.props.addPaymentInfo({paymentInfo}, (msg)=>{
                 console.log(msg);
                 if(msg.status == 1){
                     this.setState({uri:msg.data.uri, error:false, p2shAddress:msg.data.p2shAddress, amount:msg.data.amount, show:false, shownext:true});
                 }
                 else{
                    this.setState({
                        error:true,
                        errorMessage:msg.message
                    });
                 }
             });

        }
    }
    handleTransactionId(){
        const txId=this.refs.txId.value;
        const txIdInfo={
             txId:txId,
             id:this.state.orderId
        };
        const regex=/^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{64}$/;
        if(txId){
             this.props.addTransactionId({txIdInfo}, (msg)=>{
                 console.log(msg);
                 if(regex.test(txId)){
                     this.setState({error:false, show:false, shownext:false, confirm:false});
                 }
                 else{
                     this.setState({
                         error:true,
                         errorMessage:"请输入正确的交易ID"
                     });
                 }
             });
        }
    }
    handleConfirmOrder(){
        const userId=localStorage.getItem('userId');
        const orderId={
            userId:userId,
            id:this.state.orderId
        };
        this.props.confirmOrder({orderId}, (msg)=>{
            console.log(msg);
             if(msg.status == 1){
                  this.setState({ orderStatus:this.state.orderStatus+1});
             }
             else{
                 message("比特币进入themis托管地址需要一定时间，请耐心等待。。。");
             }
       });
    }
    handleSendMoney(){
        const orderId={
            id:this.state.orderId
        };
        this.props.confirmSendMoney({orderId}, (msg)=>{
              if(msg.status == 1){
                  this.setState({ orderStatus:this.state.orderStatus+1});
              }
        });
    }
    handlereleaseBtc(){
        const userId=localStorage.getItem('userId');
        const releaseData={
            id:this.state.orderId,
            userId:userId
        };
        this.props.releaseBtc({releaseData}, (msg) => {
            console.log(msg);
            if(msg.status == 1){
                this.setState({orderStatus:this.state.orderStatus+1});
            }
            else{
                this.setState({
                    error:true,
                    errorMessage:msg.message
                });
            }
        });

    }
    handleConfirmGoods(){
        const userId=localStorage.getItem('userId');
        const confirmGoodsData={
            id:this.state.orderId,
            userId:userId
        };
        this.props.confirmGoods({confirmGoodsData}, (msg)=>{
            console.log(msg);
              if(msg.status==1){
                   this.setState({ orderStatus:this.state.orderStatus+1});
              }
        });
    }
    handleRadioValue(e){
        this.setState({comment:e.target.value});
    }
    handleComment(){
         const userId=localStorage.getItem('userId');
         const commentData={
             id:this.state.orderId,
             status:this.state.comment,
             content:this.refs.comment.value,
             userId:userId
         };

         this.props.saveComment({commentData}, (msg)=>{
             if(msg.status==1){
               this.setState({ orderStatus:this.state.orderStatus+1});
             }
         });
    }
    handleCancleOrders(){
        const userId=localStorage.getItem('userId');
        const cancelData={
            id:this.state.orderId,                    
            userId:userId                             
        };
        if(this.state.orderStatus ==3){
            this.props.cancelOrders({cancelData}, (msg)=>{
                if(msg.status==1){
                    this.setState({ orderStatus:8});
                }
            });
        }
        this.props.cancelOrders({cancelData}, (msg)=>{
             if(msg.status==1){
                 this.setState({ orderStatus:7});
             }
        });
    }
    handleEvidence(){
        this.setState({
            evidence:true,
        });
    }
    handleEvidenceSubmit(){
        const evidenceDes=this.refs.voucherDes.value;
        const {fileList} = this.state;
        if(evidenceDes || fileList){
            const userId= localStorage.getItem('userId');
            const id=this.state.orderId;
            const formData = new FormData();
            fileList.forEach((file) => {
                formData.append('files', file);
            });
            formData.append("id", id);
            formData.append("userId", userId);
            formData.append("content", evidenceDes);

            this.setState({
                uploading: true,
            });
            this.props.uploadEvidence({formData}, (msg)=>{
                if(msg.status==1){
                    this.setState({
                        fileList: [],
                        uploading: false,
                    });
                    window.location.href='/orderinprogress';
                }
                else{
                    this.setState({
                        uploading: false,
                    });
                    alert("上传图片数量超出限制");
                }
            });
        }
    }
    render(){
        console.log('status: ' + this.state.orderStatus);
        let close = () => {
            this.setState({show:false, shownext:false, evidence:false, error:false});
        };
        if(this.props.orders_details===null){
            return <div className="text-center h3">loading....</div>;
        }
        const orders_details = this.props.orders_details;
        const orderType = orders_details && orders_details.orderType;
        const amount=orders_details && orders_details.amount;
        const money=orders_details && orders_details.money;
        const price=orders_details && orders_details.notice.price;
        const partner=this.props.partner;
        console.log(this.props.orders_details);
        const { uploading, evidence, show, shownext, loading} = this.state;
        const props = {
            action: `${ROOT_ARBITRATE}/arbitrate/uploadEvidence`,
            onRemove: (file) => {
                this.setState(({ fileList }) => {
                    const index = fileList.indexOf(file);
                    const newFileList = fileList.slice();
                    newFileList.splice(index, 1);
                    return {
                        fileList: newFileList,
                    };
                });
            },
            beforeUpload: (file) => {
                this.setState(({ fileList }) => ({
                    fileList: [...fileList, file],
                }));
                return false;
            },
            fileList: this.state.fileList,
        };
        return (
            <div className="order-main g-pt-50 g-pb-50" style={{width:"100%"}}>
                <div className="order-header container text-center">
                    <div className="row">
                        {this.state.orderStatus > 6 ? this.renderOrderStatus2() :this.renderOrderStatus1() }
                    </div>
                </div>
                <div className="text-left order-message container g-pt-30 g-pb-40" >
                    <div className="row">
                        <div className="col-sm-12 order-status">
                            <span className="g-pr-20" style={{fontWeight:600}}>{this.state.status}</span>
                            <span className="g-pr-20">|</span>
                            <span>{this.state.tip}</span>
                        </div>
                        <div className="col-sm-12 order-details g-mt-20 clearfix">
                            <ul>
                                <li className="col-sm-2" style={{color:"#2ad0e9", fontWeight: "600"}}>订单信息</li>
                                <li className="col-sm-3">交易价格:<span>{price}</span>CNY</li>
                                <li className="col-sm-3">交易数量:<span>{amount}</span>BTC</li>
                                <li className="col-sm-3">交易金额:<span>{money}</span>CNY</li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div className="order-content container g-pb-100">
                    <div className="row">
                        <div className="col-sm-8">
                            <div className="order-chat clearfix text-center">
                                <TabsControl>
                                    <div name="聊天"><Chat/></div>
                                    <div name="卖家信息">
                                        {this.renderPartnerMessage(partner)}
                                    </div>
                                </TabsControl>
                            </div>
                        </div>
                        <div className="col-sm-4">
                                {/*  第一页  */}
                                <div className={`order-page0 ${this.state.orderStatus == 1 ? "show" : "hidden"}`}>
                                    <div className="row order-operation">
                                        <div className="col-sm-12">
                                            {this.renderOrderMessageDetails(orders_details)}
                                            <div className="order-tip">{orderType == "购买" ? "买家已经拍下订单，请等待卖家确定具体的交易金额和数量":"买家已拍下订单，请卖家确定具体的交易金额和数量，确认好之后，输入你的比特币钱包付款地址，比特币将进入themis托管地址中，等待买家付款，卖家确认收款后，点击释放比特币"}</div>
                                            <div>
                                                {orderType == "购买" ? "":
                                                    <span>
                                                        <button className="ant-btn ant-btn-primary ant-btn-lg g-mb-10" onClick={this.handlePaymentInfo.bind(this)}>填写付款信息</button><br/>
                                                        <button type="button" className="ant-btn ant-btn-primary ant-btn-lg g-mr-10" disabled={this.state.confirm} onClick={this.handleConfirmOrder.bind(this)}>确认</button>
                                                    </span>}
                                                <button type="button" className="ant-btn ant-btn-lg" onClick={this.handleCancleOrders.bind(this)}>
                                                    取消订单
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                {/*  第一页  */}
                                {/*  第二页  */}
                                <div className={`order-page1 ${this.state.orderStatus == 2 ? "show" : "hidden"}`}>
                                    <div className="row order-operation">
                                        <div className="col-sm-12">
                                            {this.renderOrderMessageDetails(orders_details)}
                                            <div className="order-tip">{orderType == "购买" ? "买家已经确认具体金额和交易数量，等待买家付款，30分钟内没有确认支付将自动关闭。":"买家已标记为付款，请卖家确认好是否收到正确的款项，如正确，请及时释放themis托管中的比特币。"}</div>
                                            <div>
                                                {orderType == "购买" ?
                                                    <div>
                                                        <button type="button" className="ant-btn ant-btn-primary ant-btn-lg" onClick={this.handleAlert.bind(this)}>标记为已经付款</button>
                                                        <button type="button" className="ant-btn ant-btn-lg" onClick={this.handleCancleOrders.bind(this)}>取消订单</button>
                                                    </div>
                                                    :
                                                    <div>
                                                        <button type="button" className="ant-btn ant-btn-primary ant-btn-lg" disabled>等待买家付款</button>
                                                        <button type="button" className="ant-btn ant-btn-lg" onClick={this.handleCancleOrders.bind(this)}>取消订单</button>
                                                    </div>
                                                }

                                                {
                                                    this.state.alertVisible ? this.renderDangerAlert() : <div></div>
                                                }
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                {/*  第二页  */}

                                {/*  第三页  */}
                                <div className={`order-page2 ${this.state.orderStatus == 3 ? "show" : "hidden"}`}>
                                    <div className="row order-operation">
                                        <div className="col-sm-12">
                                            {this.renderOrderMessageDetails(orders_details)}
                                            {/*<div className="order-tip">{orderType=="购买"?"卖家已经释放比特币" :""}</div>*/}
                                            <div>
                                                {orderType == "购买" ?
                                                    <div>
                                                        <button type="button" className="ant-btn ant-btn-primary ant-btn-lg" disabled >等待卖家释放比特币</button>
                                                        <button type="button" className="ant-btn ant-btn-lg" onClick={this.handleCancleOrders.bind(this)}>取消订单</button>
                                                    </div>
                                                    :
                                                    <div>
                                                        <button type="button" className="ant-btn ant-btn-primary ant-btn-lg" onClick={this.handlereleaseBtc.bind(this)} >释放比特币</button>
                                                        <button type="button" className="ant-btn ant-btn-lg" onClick={this.handleCancleOrders.bind(this)}>取消订单</button>
                                                        {this.renderAlert()}
                                                    </div>

                                                }
                                                <br/>
                                                <button className="ant-btn ant-btn-primary ant-btn-lg" onClick={this.handleEvidence.bind(this)}>THEMIS仲裁</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                {/*  第三页  */}

                            {/*  第四页  */}
                            <div className={`order-page2 ${this.state.orderStatus == 4 ? "show" : "hidden"}`}>
                                <div className="row order-operation">
                                    <div className="col-sm-12">
                                        {this.renderOrderMessageDetails(orders_details)}
                                        <div className="order-tip">{orderType=="购买"?"卖家已经释放比特币，请确认是否收到比特币" :""}</div>
                                        <div>
                                            {orderType == "购买" ?
                                                <div>
                                                    <button type="button" className="ant-btn ant-btn-primary ant-btn-lg" onClick={this.handleConfirmGoods.bind(this)} >收到比特币</button>
                                                </div>
                                                :
                                                <div>
                                                    <button type="button" className="ant-btn ant-btn-primary ant-btn-lg" onClick={this.handlereleaseBtc.bind(this)} >等待买家收货</button>
                                                </div>
                                            }
                                            <br/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            {/*  第四页  */}
                                {/*  第五页  */}
                                <div className={`order-page4 ${this.state.orderStatus == 5 ? "show" : "hidden"}`}>
                                    <div className="row order-operation">
                                        <div className="col-sm-12">
                                            {this.renderOrderMessageDetails(orders_details)}
                                            <div className="comment">
                                                <h5>对用户{this.state.partnerName}进行评价</h5>
                                                <div>
                                                    <label htmlFor="good" className="radio-inline">
                                                        <input type="radio" name="comment" id="good" value="1" defaultChecked onClick={this.handleRadioValue.bind(this)}/>好评
                                                    </label>
                                                    <label htmlFor="medium" className="radio-inline">
                                                        <input type="radio" name="comment" id="medium" value="2" onClick={this.handleRadioValue.bind(this)} />中评
                                                    </label>
                                                    <label htmlFor="bad" className="radio-inline">
                                                        <input type="radio" name="comment" id="bad" value="3" onClick={this.handleRadioValue.bind(this)}/>差评
                                                    </label>                                                                                   
                                                </div>

                                                <textarea className="form-control" name="" id="" ref="comment" cols="30" rows="10"></textarea>
                                            </div>
                                            <button className="ant-btn ant-btn-primary ant-btn-lg"  onClick={this.handleComment.bind(this)}>
                                                提交评论
                                            </button>
                                            <br/>
                                        </div>
                                    </div>
                                </div>
                                {/*  第五页  */}
                            {/*  第六页  */}
                            <div className={`order-page4 ${this.state.orderStatus >= 6 ? "show" : "hidden"}`}>
                                <div className="row order-operation">
                                    <div className="col-sm-12">
                                        {this.renderOrderMessageDetails(orders_details)}
                                    </div>
                                </div>
                            </div>
                            {/*  第六页  */}
                        </div>
                    </div>
                </div>
                <Modal visible={show} title="付款信息" onOk={this.handleNext.bind(this)} onCancel={close}
                       footer={[<Button key="back" size="large" onClick={close}>取消</Button>,
                           <Button key="submit" type="primary" size="large" loading={loading} onClick={this.handleNext.bind(this)}>确定</Button>,
                       ]}>
                    <span className="pull-left">托管公钥</span>
                    <input className="form-control" type="text" placeholder="请输入公钥地址"  ref="sellerPubAuth"/>
                    <span className="pull-left">托管私钥</span>
                    <input className="form-control" type="text" placeholder="请输入私钥地址" ref="sellerPriAuth"/>
                    {this.renderAlert()}
                </Modal>
                <Modal visible={shownext} title="付款信息" onOk={this.handleTransactionId.bind(this)} onCancel={close}
                       footer={[<Button key="back" size="large" onClick={close}>取消</Button>,
                           <Button key="submit" type="primary" size="large" loading={loading} onClick={this.handleTransactionId.bind(this)}>确定</Button>,
                       ]}>
                    <div className="container-flow">
                        <div className="row">
                            <div className="col-sm-12">
                                <div className="qrcode text-center"> <QRCode  value={this.state.uri} level="H" /></div>
                                <div className="text-center"> 扫码支付</div>
                            </div>
                            <div className="col-sm-12">
                                <div className="col-sm-3">付款金额 </div>
                                <div className="col-sm-9">{this.state.amount}</div>
                            </div>
                            <div className="col-sm-12">
                                <div className="col-sm-3">付款地址 </div>
                                <div className="col-sm-9">{this.state.p2shAddress}</div>
                            </div>
                            <div className="col-sm-12">
                                <div className="col-sm-3">交易ID </div>
                                <div className="col-sm-9">
                                    <input className="form-control" type="text" placeholder="请输入交易id" ref="txId"/>
                                </div>
                            </div>
                            {this.renderAlert()}
                        </div>
                    </div>

                </Modal>
                <Modal visible={evidence} title="证据存根" onOk={this.handleEvidenceSubmit.bind(this)} onCancel={close}
                       footer={[<Button key="back" size="large" onClick={close}>取消</Button>,
                           <Button key="submit" size="large" className="upload-demo-start" type="primary" onClick={this.handleEvidenceSubmit.bind(this)} disabled={this.state.fileList.length === 0} loading={uploading}>
                               {uploading ? '上传中' : '确定' }
                           </Button>
                       ]}>
                    <div className="clearfix">
                        <Upload {...props}>
                            <Button>
                                <Icon type="upload" /> 聊天截图
                            </Button>
                            <div>最多可上传5张图片</div>
                        </Upload>
                    </div>
                    <textarea className="form-control" name="" id="" cols="30" rows="10" placeholder="请输入此次仲裁重要部分证据和备注" ref="voucherDes"></textarea>
                </Modal>
            </div>
        );
    }
}

function mapStateToProps(state) {
    return {                                         
        orders_details: state.order.orders_details,
        partner:state.order.partner_message,
        payment_info:state.order.payment_info
    };
}
export default connect(mapStateToProps, {fetchOrdersDetails, fetchTradePartnerMessage, addPaymentInfo, addTransactionId, fetchKey, confirmOrder, confirmSendMoney, releaseBtc, confirmGoods, saveComment, cancelOrders, uploadEvidence })(OrderProgress);


