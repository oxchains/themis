/**
 * Created by zhangxiaojing on 2017/11/10.
 */

import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import {Pagination} from 'antd';
import { connect } from 'react-redux';
import {fetchMessageLetter} from "../../actions/message";
class MessageLetter extends Component{
    constructor(props) {
        super(props);
        this.state = {
            pageNum:1,
            pageSize:8
        };
    }
    componentWillMount(){
        const userId=localStorage.getItem("userId");
        const pageNum=this.state.pageNum;
        const pageSize=this.state.pageSize;
        this.props.fetchMessageLetter({userId, pageNum, pageSize});
    }
    handlePagination(pageNum) {
        const userId=localStorage.getItem("userId");
        const pageSize=this.state.pageSize;
        this.props.fetchMessageLetter({userId, pageNum, pageSize});
    }
    handleOrder(val){
        const userId= localStorage.getItem('userId');
        const orderData={id:val.orderId, userId:userId, partnerId:val.partnerId, friendUsername:val.friendUsername};
        localStorage.setItem("partner", JSON.stringify(orderData));
        window.location.href='/orderprogress';
    }
    renderList(){
        return this.props.message_letter.pageList.map((item, index)=>{
            return(
                <li className="message-item-list clearfix" key={index}>
                    <div className="col-xs-2">
                        <div className="photo pull-right">
                            <img src="../public/img/touxiang.png" alt=""/>
                        </div>
                    </div>
                    <div className="col-xs-10 message-item-content" onClick={this.handleOrder.bind(this, item.messageText)}>
                        <div className="message-item-tip"><span>{item.messageType == 1 ? item.messageText.friendUsername:""}</span><span>{item.messageText.postDate}</span></div>
                        <div className="message-item-detail">{item.messageText.message}</div>
                    </div>
                </li>
            );
        });
    }
    render(){
        console.log(this.props.message_letter);
        const totalNum = this.props.message_letter && this.props.message_letter.rowCount;
        return (
            <div className="message-box">
                <div className="container">
                    <div className="row">
                        <div className="col-xs-12 message-list">
                            <ul>
                                <li className="col-xs-4 text-right">
                                    <Link className="text-center" to="/messagenotice">公告</Link>
                                </li>
                                <li className="col-xs-4 text-center">
                                    <Link className="text-center" to="/messagesystem">系统</Link>
                                </li>
                                <li className="col-xs-4 text-left">
                                    <Link className="text-center active" to="/messageletter">私信</Link>
                                </li>
                            </ul>
                        </div>
                        <div className="col-xs-12 message-item-content">
                            <ul>
                                { totalNum == 0 || !this.props.message_letter  ? <div className="text-center h4">目前没有新消息</div> : this.renderList()}
                            </ul>
                        </div>
                        <div className="col-xs-12">
                            <div className="pagecomponent">
                                <Pagination  defaultPageSize={this.state.pageSize} total={totalNum}  onChange={e => this.handlePagination(e)}/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}
function mapStateToProps(state) {
    return {
        message_letter:state.message.message_letter
    };
}
export default connect(mapStateToProps, {fetchMessageLetter})(MessageLetter);