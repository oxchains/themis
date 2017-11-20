/**
 * Created by zhangxiaojing on 2017/11/09.
 */

import React, { Component } from 'react';
import { Link } from 'react-router';
import { Icon } from 'antd';
import { connect } from 'react-redux';
import {fetchUnreadMessage} from "../../actions/message";
class MessageList extends Component{
    constructor(props) {
        super(props);
        this.state = {
            messageType:2
        };
    }
    render(){
        console.log(this.props.message_letter);
        const username= localStorage.getItem('username');
        return (
            <div className="col-xs-12 message-list">
                <ul>
                    <li className="col-xs-4 text-right">
                        <a className={`text-center ${this.props.message_notice ? "active" : ""}`} href="/messagenotice">公告</a>
                    </li>
                    <li className="col-xs-4 text-center">
                        <a className={`${this.props.message_system ? "active" : ""}`} href="/messagesystem">系统</a>
                    </li>
                    <li className="col-xs-4 text-left">
                        <a className={`text-center ${this.props.message_letter ? "active" : ""}`} href="/messageletter">私信</a>
                    </li>
                </ul>
            </div>
        );
    }
}
function mapStateToProps(state) {
    return {
        message_letter:state.message.message_letter,
        message_notice:state.message.message_notice,
        message_system:state.message.message_system
    };
}
export default connect(mapStateToProps, {fetchUnreadMessage})(MessageList);
