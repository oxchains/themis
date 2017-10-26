/**
 * Created by zhangxiaojing on 2017/10/20.
 */
import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Field, reduxForm} from 'redux-form';
import {Alert,Modal,Button,Form,FormGroup,Col,ControlLabel,FormControl,Image} from 'react-bootstrap';
import Chat from './chat';
import TabsControl from "./react_tab";
import {fetchOrdersDetails,fetchTradePartnerMessage} from '../actions/order'

var that;

class OrderProgress extends Component {
    constructor(props) {
        super(props);
        that = this;
        this.state = {
            index: 0,
            i:0,
            orderStatus:1,
            isFormSubmit: false,
            alertVisible: false,
            chatVisible: true,
            show: false,
            shownext:false,
            x:''
        };
        this.nextPage = this.nextPage.bind(this);
        this.partnerMessage=this.partnerMessage.bind(this);
        this.renderDangerAlert = this.renderDangerAlert.bind(this);
        this.orderMessageDetails=this.orderMessageDetails.bind(this);
    }
    componentWillMount() {
        const message = this.props.location.state;
        const data={id:message.id,userId:message.userId}
        this.props.fetchOrdersDetails({data},(msg)=>{
            this.setState({orderStatus:msg.orderStatus});
            switch(this.state.orderStatus){
                case 1:
                    this.setState({x:"买家已拍下，等待卖家确认"});
                    break;
                case 2:
                    this.setState({x:"卖家已确认，等待买家付款，30分钟内，如逾期订单将自动取消"});
                    break;
                case 3:
                    this.setState({x:"买家已经标记为付款，等待卖家确认并释放比特币"});
                    break;
                case 4:
                    this.setState({x:"卖家已释放比特币，交易即将完成，双方进行评价"});
                    break;
                case 5:
                    this.setState({x:"交易完成"});
                    break;
                case 5:
                    this.setState({x:"交易已取消"});
                    break;
            }
        });

        const partner={userId:message.partnerId}
        console.log(partner)
        this.props.fetchTradePartnerMessage({partner})
    }
    nextPage() {
        this.setState({orderStatus:this.state.orderStatus+1});
    }
    partnerMessage(partner){
        if(partner==null){
            return <div>loading...</div>
        }
        return(
            <div className="container-fluid" style={{"height":"381.42px"}}>
                <ul className="row text-left g-pt-40">
                    <li className="col-sm-12 ">
                        刘瑞超
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
                        <li>交易金额:<span>{msg.money}</span>BTC</li>
                        <li>订单编号:<span>{msg.id}</span></li>
                        <li>支付方式:<span>{msg.payment.payment_name}</span></li>
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
                    <button type="button" className="btn btn-primary btn-flat" onClick={this.nextPage}>
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
    render(){
        console.log(this.props.partner)

        let close = () => this.setState({ show:false,shownext:false});
        let next = () => this.setState({show:false,shownext:true})
        if(this.props.orders_details===null){
            return <div></div>
        }
        const orders_details = this.props.orders_details;
        const orderType = orders_details && orders_details.orderType;
        const amount=orders_details && orders_details.amount;
        const money=orders_details && orders_details.money;
        const partner=this.props.partner;
        console.log(orderType)
        return (
            <div className="order-main g-pt-50 g-pb-50">
                <div className="order-header container text-center">
                    <div className="row">
                        <div className="col-sm-2 col-md-offset-1">
                            <div>
                                <div className="row-header">
                                    <div className="row-title">
                                        <h4 className={`order-flow ${this.state.orderStatus >=1 ? 'order-active' : ''}`}><span>{this.state.orderStatus <=1 ? '买家已拍下' : '买家已拍下'}</span></h4>
                                        <img className="g-ml-25" src="/public/img/arrow-r.png"
                                             style={{width: '17px', height: '29px'}}></img>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="col-sm-2">
                            <div className="row-header">
                                <div className="row-title">
                                    <h4 className={`order-flow ${this.state.orderStatus >=1? 'order-active' : ''}`}> <span>{this.state.orderStatus <=1 ? '等待卖家确认' : '卖家已确认'}</span></h4>
                                    <img className="g-ml-25" src="/public/img/arrow-r.png"
                                         style={{width: '17px', height: '29px'}}></img>
                                </div>
                            </div>
                        </div>

                        <div className="col-sm-2">
                            <div className="row-header">
                                <div className="row-title">
                                    <h4 className={`order-flow ${this.state.orderStatus >=2 ? 'order-active' : ''}`}><span>{this.state.orderStatus <=2 ? '等待买家付款' : '买家已付款'}</span></h4>
                                    <img className="g-ml-25" src="/public/img/arrow-r.png"
                                         style={{width: '17px', height: '29px'}}></img>
                                </div>
                            </div>
                        </div>

                        <div className="col-sm-2">
                            <div className="row-header">
                                <div className="row-title">
                                    <h4 className={`order-flow ${this.state.orderStatus >= 3 ? 'order-active' : ''}`}><span>{this.state.orderStatus <=3 ? '等待买家收货' : '买家已收货'}</span></h4>
                                    <img className="g-ml-25" src="/public/img/arrow-r.png"
                                         style={{width: '17px', height: '29px'}}></img>
                                </div>
                            </div>
                        </div>
                        <div className="col-sm-2">
                            <div className="row-header">
                                <div className="row-title">
                                    <h4 className={`order-flow ${this.state.orderStatus >=4 ? 'order-active' : ''}`}><span>{this.state.orderStatus <=4 ? '等待双方评价' : '已评价'}</span></h4>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="text-left order-message container g-pt-30 g-pb-40" >
                    <div className="row">
                        <div className="col-sm-12 order-status">
                            <span className="g-pr-20">已拍下</span>
                            <span className="g-pr-20">|</span>
                            <span>{this.state.x}</span>
                        </div>
                        <div className="col-sm-12 order-details g-mt-20 clearfix">
                            <ul>
                                <li className="col-sm-1">订单信息</li>
                                <li className="col-sm-2">交易价格:<span>{money}</span>CNY</li>
                                <li className="col-sm-2">交易数量:<span>{amount}</span>BTC</li>
                                <li className="col-sm-2">交易金额:<span>{money}</span>CNY</li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div className="order-content container">
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

                                                {orderType == "购买" ? "": <div><button onClick={() => this.setState({ show: true })}>填写付款信息</button><button type="button" className="btn btn-primary btn-flat" onClick={this.nextPage}>确认</button></div>}
                                                <button type="button" className="btn btn-primary btn-flat" disabled>
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
                                                {orderType == "购买" ? <div><button type="button" className="btn btn-primary btn-flat" onClick={this.showAlert.bind(this)}>标记为已经付款</button><button type="button" className="btn btn-primary btn-flat" disabled>取消订单</button></div>:<div><button type="button" className="btn btn-primary btn-flat" onClick={this.nextPage}>释放比特币</button><button type="button" className="btn btn-primary btn-flat" disabled>取消订单</button></div>}

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
                                            <div className="order-tip">{orderType=="购买"?"卖家已经" :""}</div>
                                            <div>
                                                {orderType == "购买" ? <button type="button" className="btn btn-primary btn-flat" onClick={this.nextPage}>
                                                    已收到比特币
                                                </button> :''}
                                                <br/>
                                                <a href="/arbitrationbuyer">交易有疑问？点此联系THEMIS仲裁</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                {/*  第三页  */}
                                {/*  第四页  */}
                                <div className={`order-page4 ${this.state.orderStatus == 4 ? "show" : "hidden"}`}>
                                    <div className="row order-operation">
                                        <div className="col-sm-12">
                                            {this.orderMessageDetails(orders_details)}
                                            <div className="comment">
                                                <h5>对卖家饿32额额用户进行评价</h5>
                                                <label htmlFor="good" className="radio-inline">
                                                    <input type="radio" name="comment" id="good" value="option1" defaultChecked/>好评
                                                </label>
                                                <label htmlFor="medium" className="radio-inline">
                                                    <input type="radio" name="comment" id="medium" value="option2" />中评
                                                </label>
                                                <label htmlFor="bad" className="radio-inline">
                                                    <input type="radio" name="comment" id="bad" value="option3"/>差评
                                                </label>
                                            </div>
                                            <a className="btn btn-primary btn-block btn-flat" href="/ordercompleted">
                                                提交评论
                                            </a>
                                            <br/>
                                            <a href="/arbitrationbuyer">交易有疑问？点此联系THEMIS仲裁</a>
                                        </div>
                                    </div>
                                </div>
                                {/*  第四页  */}
                            {/*  第四页  */}
                            <div className={`order-page4 ${this.state.orderStatus == 5 ? "show" : "hidden"}`}>
                                <div className="row order-operation">
                                    <div className="col-sm-12">
                                        {this.orderMessageDetails(orders_details)}
                                    </div>
                                </div>
                            </div>
                            {/*  第四页  */}
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
                                    <FormControl type="email" placeholder="请输入公钥地址" />
                                </Col>
                            </FormGroup>

                            <FormGroup controlId="formHorizontalPassword">
                                <Col componentClass={ControlLabel} sm={2}>
                                    托管私钥
                                </Col>
                                <Col sm={10}>
                                    <FormControl type="text" placeholder="请输入私钥地址" />
                                </Col>
                            </FormGroup>
                        </Form>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button onClick={next}>下一步</Button>
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
                                <div> <Image src="/assets/thumbnail.png" responsive /></div>
                                <div className="text-center"> 扫码支付</div>
                            </FormGroup>
                            <FormGroup controlId="formHorizontalEmail">
                                <Col componentClass={ControlLabel} sm={2}>
                                    托管公钥
                                </Col>
                                <Col sm={10}>
                                   <span>dgfuoehwofinejlnbfkjibewkobgkejw</span>
                                </Col>
                            </FormGroup>
                            <FormGroup controlId="formHorizontalPassword">
                                <Col componentClass={ControlLabel} sm={2}>
                                    托管私钥
                                </Col>
                                <Col sm={10}>
                                    <span>dgfuoehwofinejlnbfkjibewkobgkejw</span>
                                </Col>
                            </FormGroup>
                        </Form>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button onClick={close}>确定</Button>
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
        partner:state.order.partner_message
    }
}
export default connect(mapStateToProps, {fetchOrdersDetails,fetchTradePartnerMessage})(OrderProgress);


