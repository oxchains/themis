/**
 * Created by zhangxiaojing on 2017/11/10.
 */

import React, { Component } from 'react';
import { Link } from 'react-router';
import {Pagination} from 'antd';
import { connect } from 'react-redux';
import {fetchMessageLetter} from "../../actions/message";
import  MessageList from './message_list'
class MessageLetter extends Component{
    constructor(props) {
        super(props);
        this.state = {
            pageNum:1,
            pageSize:8
        }
    }
    componentWillMount(){
        const userId=localStorage.getItem("userId")
        const pageNum=this.state.pageNum;
        const pageSize=this.state.pageSize;
        this.props.fetchMessageLetter({userId, pageNum, pageSize})
    }
    handlePagination(pageNum) {
        const userId=localStorage.getItem("userId")
        const pageSize=this.state.pageSize;
        this.props.fetchMessageLetter({userId, pageNum, pageSize})
    }
    handleOrder(val){
        console.log(val)
        const userId= localStorage.getItem('userId');
        const orderData={id:val.orderId, userId:userId, partnerId:val.partnerId, friendUsername:val.friendUsername}
        localStorage.setItem("partner", JSON.stringify(orderData));
        window.location.href='/orderprogress';
    }
    renderList(){
        return this.props.message_letter.pageList.map((item, index)=>{
            return(
                <li className="message-item-list clearfix" key={index}>
                    <div className="col-xs-2">
                        <div className="photo pull-right"></div>
                    </div>
                    <div className="col-xs-10 message-item-content" onClick={this.handleOrder.bind(this, item.messageText)}>
                        <div className="message-item-tip"><span>{item.messageType == 1 ? item.messageText.friendUsername:""}</span><span>{item.messageText.postDate}</span></div>
                        <div className="message-item-detail">{item.messageText.message}</div>
                    </div>
                </li>
            )
        })
    }
    render(){
        const totalNum = this.props.message_letter && this.props.message_letter.rowCount
        console.log(this.props.message_letter)
        return (
            <div className="message-box">
                <div className="container">
                    <div className="row">
                        <MessageList/>
                        <div className="col-xs-12 message-item-content">
                            <ul>
                                {this.props.message_letter == null ? <div className="text-center h4">暂无消息</div> : this.renderList()}
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
        )
    }

}
function mapStateToProps(state) {
    return {
        message_letter:state.message.message_letter
    };
}

export default connect(mapStateToProps, {fetchMessageLetter})(MessageLetter);