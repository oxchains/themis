/**
 * Created by zhangxiaojing on 2017/11/09.
 */

import React, { Component } from 'react';
import { Link } from 'react-router';
import { connect } from 'react-redux';
import {fetchUnreadMessage} from "../../actions/message";
import  MessageList from './message_list'
class MessageUnread extends Component{
    constructor(props) {
        super(props);
        this.state = {}
    }
    render(){
        return (
            <div className="message-box">
                <div className="container">
                    <div className="row">
                        <MessageList/>
                        <div className="col-xs-12 message-content">
                            系统消息
                        </div>
                    </div>
                </div>
            </div>
        )
    }

}
function mapStateToProps(state) {
    return {
        errorMessage:state.auth.error,
        authenticated: state.auth.authenticated,

    };
}

export default connect(mapStateToProps, {fetchUnreadMessage})(MessageUnread);