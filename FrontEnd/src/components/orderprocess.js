/**
 * Created by zhangxiaojing on 2017/10/20.
 */
import React, {Component} from 'react';
import {connect} from 'react-redux';
import Dropzone from 'react-dropzone';
import {Alert,Modal,Button,Form,FormGroup,Col,ControlLabel,FormControl,Image} from 'react-bootstrap';
import Chat from './chat';
import TabsControl from "./react_tab";
import {uploadEvidence} from '../actions/arbitrate';
import {fetchOrdersDetails,fetchTradePartnerMessage,addPaymentInfo,addTransactionId,fetchKey,confirmOrder,confirmSendMoney,releaseBtc,confirmGoods,saveComment,cancelOrders} from '../actions/order';
import $ from 'jquery';

class OrderProgress extends Component {
    constructor(props) {
        super(props);
        this.state = {
            orderStatus:0,
            alertVisible: false,
            show: false,
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
            evidence:false
        };
        this.partnerMessage=this.partnerMessage.bind(this);
        this.renderDangerAlert = this.renderDangerAlert.bind(this);
        this.orderMessageDetails=this.orderMessageDetails.bind(this);
    }
    componentWillMount() {
        const partnerName=localStorage.getItem("friendUsername")
        const message = this.props.location.state;
        const data={id:message.id,userId:message.userId}
        this.setState({partnerName:partnerName});

        this.props.fetchOrdersDetails({data},(msg)=>{
            console.log(msg)
            this.setState({orderStatus:msg.orderStatus});
            this.setState({orderId:msg.id});
            switch(this.state.orderStatus){
                case 1:
                    this.setState({tip:"买家已拍下，等待卖家确认"});
                    break;
                case 2:
                    this.setState({tip:"卖家已确认，等待买家付款，30分钟内，如逾期订单将自动取消"});
                    break;
                case 3:
                    this.setState({tip:"买家已经标记为付款，等待卖家确认并释放比特币"});
                    break;
                case 4:
                    this.setState({tip:"卖家已释放比特币，等待买家确认收货"});
                    break;
                case 5:
                    this.setState({tip:"买家已经确认收货，交易即将完成，等待双方进行评价。"});
                    break;
                case 6:
                    this.setState({tip:"交易已完成"});
                    break;
                case 7:
                    this.setState({tip:"交易已取消"});
                    break;
                case 8:
                    this.setState({tip:"退款处理中"});
                    break;
            }
        });
        const partner={userId:message.partnerId}
        this.props.fetchTradePartnerMessage({partner});
        const orderId={
            id:this.state.orderId
        }
    }
    showOrderStatus1() {
        return (
            <div>
                <div className="col-sm-2">
                    <div>
                        <div className="row-header">
                            <div className="row-title">
                                <h4 className={`order-flow ${this.state.orderStatus >= 1 ? 'order-active' : ''}`}>
                                    <span>{this.state.orderStatus <= 1 ? '买家已拍下' : '买家已拍下'}</span></h4>
                                <img className="g-ml-25" src="/public/img/arrow-r.png"
                                     style={{width: '17px', height: '29px'}}></img>
                            </div>
                        </div>
                    </div>
                </div>

                <div className="col-sm-2">
                    <div className="row-header">
                        <div className="row-title">
                            <h4 className={`order-flow ${this.state.orderStatus >= 1 ? 'order-active' : ''}`}>
                                <span>{this.state.orderStatus <= 1 ? '等待卖家确认' : '卖家已确认'}</span></h4>
                            <img className="g-ml-25" src="/public/img/arrow-r.png"
                                 style={{width: '17px', height: '29px'}}></img>
                        </div>
                    </div>
                </div>

                <div className="col-sm-2">
                    <div className="row-header">
                        <div className="row-title">
                            <h4 className={`order-flow ${this.state.orderStatus >= 2 ? 'order-active' : ''}`}>
                                <span>{this.state.orderStatus <= 2 ? '等待买家付款' : '买家已付款'}</span></h4>
                            <img className="g-ml-25" src="/public/img/arrow-r.png"
                                 style={{width: '17px', height: '29px'}}></img>
                        </div>
                    </div>
                </div>

                <div className="col-sm-2">
                    <div className="row-header">
                        <div className="row-title">
                            <h4 className={`order-flow ${this.state.orderStatus >= 3 ? 'order-active' : ''}`}>
                                <span>{this.state.orderStatus <= 3 ? '等待卖家发货' : '卖家已收货'}</span></h4>
                            <img className="g-ml-25" src="/public/img/arrow-r.png"
                                 style={{width: '17px', height: '29px'}}></img>
                        </div>
                    </div>
                </div>
                <div className="col-sm-2">
                    <div className="row-header">
                        <div className="row-title">
                            <h4 className={`order-flow ${this.state.orderStatus >= 4 ? 'order-active' : ''}`}>
                                <span>{this.state.orderStatus <= 4 ? '等待买家收货' : '买家已收货'}</span></h4>
                            <img className="g-ml-25" src="/public/img/arrow-r.png"
                                 style={{width: '17px', height: '29px'}}></img>
                        </div>
                    </div>
                </div>
                <div className="col-sm-2">
                    <div className="row-header">
                        <div className="row-title">
                            <h4 className={`order-flow ${this.state.orderStatus >= 5 ? 'order-active' : ''}`}>
                                <span>{this.state.orderStatus <= 5 ? '等待双方评价' : '已评价'}</span></h4>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
    showOrderStatus2(){
        return(
              <div className="h2 g-pt-20 g-pb-20">
                  <div className={`${this.state.orderStatus == 7 ? 'show' : 'hidden'}`}>订单已取消</div>
                  <div className={`${this.state.orderStatus == 8 ? 'show' : 'hidden'}`}>订单已取消，退款中 <br/>
                      <button className="btn btn-primary" onClick={this.handleEvidence.bind(this)}>THEMIS仲裁</button>
                  </div>
              </div>
            )
    }
    partnerMessage(partner){
        if(partner==null){
            return <div>loading...</div>
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
        )
    }
    orderMessageDetails(msg){
        if(msg==null){
            return(
                <div>loading...</div>
            )
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
                    <button type="button" className="btn btn-primary btn-flat" onClick={this.handleSendMoney.bind(this)} >
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
    addPaymentInfo(){
        const orderId={
            id:this.state.orderId
        }
        this.props.fetchKey({orderId},(msg)=>{
             if(msg.status == 1){
                 $(function(){
                      var qrcode = new QRCode('qrcode', {
                          text: msg.data.uri,
                          width: 150,
                          height: 150,
                          colorDark : '#000000',
                          colorLight : '#ffffff',
                          correctLevel : QRCode.CorrectLevel.H
                      })

                 });                                           
                 this.setState({p2shAddress:msg.data.p2shAddress,amount:msg.data.amount,shownext: true})
             }
             else {
                 this.setState({show: true})
             }
        })
    }
    next(){
        const sellerPubAuth=this.refs.sellerPubAuth.value;
        const sellerPriAuth=this.refs.sellerPriAuth.value;
        const orderId=this.state.orderId;
        if(sellerPubAuth && sellerPriAuth){
             const paymentInfo={
                 sellerPubAuth:sellerPubAuth,
                 sellerPriAuth:sellerPriAuth,
                 orderId:orderId
             }
             this.props.addPaymentInfo({paymentInfo},(msg)=>{
                 this.setState({p2shAddress:msg.p2shAddress,amount:msg.amount})
                 $(function(){                                       
                      var qrcode = new QRCode('qrcode', {            
                          text: msg.uri,
                          width: 150,                                
                          height: 150,                               
                          colorDark : '#000000',                     
                          colorLight : '#ffffff',                    
                          correctLevel : QRCode.CorrectLevel.H       
                      })                                             
                                                                     
                 });                                                 
             })
             this.setState({show:false,shownext:true})                          
        }
    }
    handleTransactionId(){
        const txId=this.refs.txId.value;
        const txIdInfo={
             txId:txId,
             id:this.state.orderId
        }
        if(txId){
             this.props.addTransactionId({txIdInfo})
             this.setState({show:false,shownext:false,confirm:false})
        }

    }
    handleConfirmOrder(){
        const userId=localStorage.getItem('userId')
        const orderId={
            userId:userId,
            id:this.state.orderId
        }
        this.props.confirmOrder({orderId},(msg)=>{
            console.log(msg)
             if(msg.status == 1){
                  this.setState({ orderStatus:this.state.orderStatus+1})
             }
             else{
                 alert("比特币进入themis托管地址需要一定时间，请耐心等待。。。")
             }
       });
    }
    handleSendMoney(){
        const orderId={
            id:this.state.orderId
        }
        this.props.confirmSendMoney({orderId},(msg)=>{
              if(msg.status == 1){
                  this.setState({ orderStatus:this.state.orderStatus+1})
              }
        })
    }
    handlereleaseBtc(){
        const userId=localStorage.getItem('userId')
        const releaseData={
            id:this.state.orderId,
            userId:userId
        }
        this.props.releaseBtc({releaseData}, (msg) => {
            console.log(msg)
            if(msg.status == 1){
                this.setState({orderStatus:this.state.orderStatus+1})
            }
        })

    }
    handleConfirmGoods(){
        const userId=localStorage.getItem('userId')
        const confirmGoodsData={
            id:this.state.orderId,
            userId:userId
        }
        this.props.confirmGoods({confirmGoodsData},(msg)=>{
            console.log(msg)
              if(msg.status==1){
                   this.setState({ orderStatus:this.state.orderStatus+1})
              }
        })
    }
    handleRadioValue(e){
        this.setState({comment:e.target.value})
    }
    handleComment(){
         const userId=localStorage.getItem('userId')
         const commentData={
             id:this.state.orderId,
             status:this.state.comment,
             content:this.refs.comment.value,
             userId:userId
         }

         this.props.saveComment({commentData},(msg)=>{
             if(msg.status==1){
               this.setState({ orderStatus:this.state.orderStatus+1})
             }
         })
    }
    handleCancleOrders(){
        const userId=localStorage.getItem('userId')   
        const cancelData={
            id:this.state.orderId,                    
            userId:userId                             
        }
        if(this.state.orderStatus ==3){
            this.props.cancelOrders({cancelData},(msg)=>{
                if(msg.status==1){
                    this.setState({ orderStatus:8})
                }
            })
        }
        this.props.cancelOrders({cancelData},(msg)=>{
             if(msg.status==1){
                 this.setState({ orderStatus:7})
             }
        })
    }
    evidenceFile(files) {
        console.log('files', files);
        this.setState({
            evidenceOFile: files
        })
    }
    handleEvidence(){
        this.setState({
            evidence:!this.state.evidence
        })
    }
    handleEvidenceSubmit(){
        const evidenceDes=this.refs.voucherDes.value;
        if(evidenceDes){
            const userId= localStorage.getItem('userId');
            const id=this.state.orderId;
            let evidenceOFile = this.state.evidenceOFile[0];
            this.props.uploadEvidence({id,userId,evidenceOFile,evidenceDes},(msg)=>{
                if(msg.status==1){
                    window.location.href='/orderinprogress'
                }
            })
        }
    }
    render(){
         console.log('status: ' + this.state.orderStatus)
        let close = () => {
            this.setState({show:false,shownext:false,evidence:false})
        };
        if(this.props.orders_details===null){
            return <div>loading....</div>
        }
        const orders_details = this.props.orders_details;
        const orderType = orders_details && orders_details.orderType;
        const amount=orders_details && orders_details.amount;
        const money=orders_details && orders_details.money;
        const price=orders_details && orders_details.notice.price;
        const partner=this.props.partner;
        return (
            <div className="order-main g-pt-50 g-pb-50">
                <div className="order-header container text-center">
                    <div className="row">
                        {this.state.orderStatus > 6 ? this.showOrderStatus2() :this.showOrderStatus1() }
                    </div>
                </div>
                <div className="text-left order-message container g-pt-30 g-pb-40" >
                    <div className="row">
                        <div className="col-sm-12 order-status">
                            <span className="g-pr-20">已拍下</span>
                            <span className="g-pr-20">|</span>
                            <span>{this.state.tip}</span>
                        </div>
                        <div className="col-sm-12 order-details g-mt-20 clearfix">
                            <ul>
                                <li className="col-sm-2">订单信息</li>
                                <li className="col-sm-3">交易价格:<span>{price}</span>CNY</li>
                                <li className="col-sm-3">交易数量:<span>{amount}</span>BTC</li>
                                <li className="col-sm-3">交易金额:<span>{money}</span>CNY</li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div className="order-content container g-pb-100">
                    <div className="row">
                        <div className="col-sm-7">
                            <div className="order-chat clearfix text-center">
                                <TabsControl>
                                    <div name="聊天"><Chat/></div>
                                    <div name="卖家信息">
                                        {this.partnerMessage(partner)}
                                    </div>
                                </TabsControl>
                            </div>
                        </div>
                        <div className="col-sm-5">
                                {/*  第一页  */}
                                <div className={`order-page0 ${this.state.orderStatus == 1 ? "show" : "hidden"}`}>
                                    <div className="row order-operation">
                                        <div className="col-sm-12">
                                            {this.orderMessageDetails(orders_details)}
                                            <div className="order-tip">{orderType == "购买" ? "买家已经拍下订单，请等待卖家确定具体的交易金额和数量":"买家已拍下订单，请卖家确定具体的交易金额和数量，确认好之后，输入你的比特币钱包付款地址，比特币将进入themis托管地址中，等待买家付款，卖家确认收款后，点击释放比特币"}</div>
                                            <div>
                                                {orderType == "购买" ? "":
                                                    <span>
                                                        <button className="btn btn-primary btn-flat g-mb-10" onClick={this.addPaymentInfo.bind(this)}>填写付款信息</button><br/>
                                                        <button type="button" className="btn btn-primary btn-flat g-mr-10" disabled={this.state.confirm} onClick={this.handleConfirmOrder.bind(this)}>确认</button>
                                                    </span>}
                                                <button type="button" className="btn btn-primary btn-flat" onClick={this.handleCancleOrders.bind(this)}>
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
                                            {this.orderMessageDetails(orders_details)}
                                            <div className="order-tip">{orderType == "购买" ? "买家已经确认具体金额和交易数量，等待买家付款，30分钟内没有确认支付将自动关闭。":"买家已标记为付款，请卖家确认好是否收到正确的款项，如正确，请及时释放themis托管中的比特币。"}</div>
                                            <div>
                                                {orderType == "购买" ?
                                                    <div>
                                                        <button type="button" className="btn btn-primary btn-flat" onClick={this.showAlert.bind(this)}>标记为已经付款</button>
                                                        <button type="button" className="btn btn-primary btn-flat" onClick={this.handleCancleOrders.bind(this)}>取消订单</button>
                                                    </div>
                                                    :
                                                    <div>
                                                        <button type="button" className="btn btn-primary btn-flat" disabled>等待买家付款</button>
                                                        <button type="button" className="btn btn-primary btn-flat" onClick={this.handleCancleOrders.bind(this)}>取消订单</button>
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
                                            {this.orderMessageDetails(orders_details)}
                                            {/*<div className="order-tip">{orderType=="购买"?"卖家已经释放比特币" :""}</div>*/}
                                            <div>
                                                {orderType == "购买" ?
                                                    <div>
                                                        <button type="button" className="btn btn-primary btn-flat" disabled >等待卖家释放比特币</button>
                                                        <button type="button" className="btn btn-primary btn-flat" onClick={this.handleCancleOrders.bind(this)}>取消订单</button>
                                                    </div>
                                                    :
                                                    <div>
                                                        <button type="button" className="btn btn-primary btn-flat" onClick={this.handlereleaseBtc.bind(this)} >释放比特币</button>
                                                        <button type="button" className="btn btn-primary btn-flat" onClick={this.handleCancleOrders.bind(this)}>取消订单</button>
                                                    </div>

                                                }
                                                <br/>
                                                <button className="btn btn-primary" onClick={this.handleEvidence.bind(this)}>THEMIS仲裁</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                {/*  第三页  */}

                            {/*  第四页  */}
                            <div className={`order-page2 ${this.state.orderStatus == 4 ? "show" : "hidden"}`}>
                                <div className="row order-operation">
                                    <div className="col-sm-12">
                                        {this.orderMessageDetails(orders_details)}
                                        <div className="order-tip">{orderType=="购买"?"卖家已经释放比特币，请确认是否收到比特币" :""}</div>
                                        <div>
                                            {orderType == "购买" ?
                                                <div>
                                                    <button type="button" className="btn btn-primary btn-flat" onClick={this.handleConfirmGoods.bind(this)} >收到比特币</button>
                                                    <button type="button" className="btn btn-primary btn-flat" onClick={this.handleCancleOrders.bind(this)}>取消订单</button>
                                                </div>
                                                :
                                                <div>
                                                    <button type="button" className="btn btn-primary btn-flat" onClick={this.handlereleaseBtc.bind(this)} >等待买家收货</button>
                                                    <button type="button" className="btn btn-primary btn-flat" onClick={this.handleCancleOrders.bind(this)}>取消订单</button>
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
                                            {this.orderMessageDetails(orders_details)}
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
                                            <button className="btn btn-primary btn-block btn-flat"  onClick={this.handleComment.bind(this)}>
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
                                        {this.orderMessageDetails(orders_details)}
                                    </div>
                                </div>
                            </div>
                            {/*  第六页  */}
                        </div>
                    </div>
                </div>
                <Modal show={this.state.show} onHide={close} container={this} aria-labelledby="contained-modal-title">
                    <Modal.Header closeButton>
                        <Modal.Title id="contained-modal-title text-center">付款信息</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <Form horizontal>
                            <FormGroup controlId="formHorizontalEmail">
                                <Col componentClass={ControlLabel} sm={2}>
                                    托管公钥
                                </Col>
                                <Col sm={10}>
                                    <input className="form-control" type="text" placeholder="请输入公钥地址"  ref="sellerPubAuth"/>
                                </Col>
                            </FormGroup>

                            <FormGroup controlId="formHorizontalPassword">
                                <Col componentClass={ControlLabel} sm={2}>
                                    托管私钥
                                </Col>
                                <Col sm={10}>
                                    <input className="form-control" type="text" placeholder="请输入私钥地址" ref="sellerPriAuth"/>
                                </Col>
                            </FormGroup>
                        </Form>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button onClick={this.next.bind(this)}>下一步</Button>
                        <Button onClick={close}>取消</Button>
                    </Modal.Footer>
                </Modal>
                <Modal show={this.state.shownext} onHide={close} container={this} aria-labelledby="contained-modal-title">
                    <Modal.Header closeButton>
                        <Modal.Title id="contained-modal-title text-center">付款信息</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <Form horizontal>
                            <FormGroup controlId="formHorizontalEmail">
                                <div id="qrcode"></div>
                                <div className="text-center"> 扫码支付</div>
                            </FormGroup>
                            <FormGroup controlId="formHorizontalPassword">
                                    <div className="col-sm-2">付款金额 </div>
                                    <div className="col-sm-10">{this.state.amount}</div>
                                 <div className="col-sm-2">付款地址 </div>
                                 <div className="col-sm-10">{this.state.p2shAddress}</div>

                                <Col componentClass={ControlLabel} sm={2}>
                                    交易ID
                                </Col>
                                <Col sm={10}>
                                    <input className="form-control" type="text" placeholder="请输入交易id" ref="txId"/>
                         
                                </Col>
                            </FormGroup>
                        </Form>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button onClick={this.handleTransactionId.bind(this)}>确定</Button>
                        <Button onClick={close}>取消</Button>
                    </Modal.Footer>
                </Modal>
                <Modal show={this.state.evidence} onHide={close} container={this} aria-labelledby="contained-modal-title">
                    <Modal.Header closeButton>
                        <Modal.Title id="contained-modal-title text-center">证据存根</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <Form horizontal>
                            <FormGroup controlId="formHorizontalPassword">
                                <Col componentClass={ControlLabel} sm={2}>
                                    上传截图
                                </Col>
                                <Col sm={10}>
                                    <Dropzone onDrop={this.evidenceFile.bind(this)} className="sign-up"
                                              accept="image/png,image/gif,image/jpeg">
                                        {({isDragActive, isDragReject, acceptedFiles, rejectedFiles}) => {
                                            return (
                                                <div>
                                                    <div className="col-sm-6">
                            <span className="btn btn-default"
                                  style={{color: "white", background: '#a6a5a6', marginLeft: '-15px'}}>选择文件</span>
                                                    </div>
                                                    <div className="col-sm-6">
                                                        <p style={{
                                                            height: '100%',
                                                            color: 'gray',
                                                            fontSize: '8px'
                                                        }}>{acceptedFiles.length > 0 ? acceptedFiles[0].name : ''}</p>
                                                    </div>
                                                </div>
                                            )
                                        }}
                                    </Dropzone>
                                </Col>
                                <Col sm={12}>
                                    <textarea className="form-control" name="" id="" cols="30" rows="10" placeholder="请输入此次仲裁重要部分证据和备注" ref="voucherDes"></textarea>
                                </Col>
                            </FormGroup>
                        </Form>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button onClick={this.handleEvidenceSubmit.bind(this)}>确定</Button>
                        <Button onClick={close}>取消</Button>
                    </Modal.Footer>
                </Modal>
            </div>
        )
    }
}

function mapStateToProps(state) {
    return {                                         
        orders_details: state.order.orders_details,
        partner:state.order.partner_message,
        payment_info:state.order.payment_info
    }
}
export default connect(mapStateToProps, {fetchOrdersDetails,fetchTradePartnerMessage,addPaymentInfo,addTransactionId,fetchKey,confirmOrder,confirmSendMoney,releaseBtc,confirmGoods,saveComment,cancelOrders,uploadEvidence})(OrderProgress);


