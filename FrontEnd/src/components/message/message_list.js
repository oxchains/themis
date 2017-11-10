/**
 * Created by zhangxiaojing on 2017/11/09.
 */

import React, { Component } from 'react';
import { Link } from 'react-router';
import { Icon } from 'antd';
import { connect } from 'react-redux';
import {fetchUnreadMessage} from "../../actions/message"
class MessageList extends Component{
    constructor(props) {
        super(props);
        this.state = {}
    }
    render(){
        const username= localStorage.getItem('username');
        return (
            <div className="col-xs-12 message-list">
                <ul>
                    <li className="col-xs-4 text-right">
                        <a className="text-center" href="">公告</a>
                    </li>
                    <li className="col-xs-4 text-center">
                        <a href="">系统</a>
                    </li>
                    <li className="col-xs-4 text-left">
                        <a className="text-center" href="">私信</a>
                    </li>
                </ul>
            </div>
        )
    }
}
function mapStateToProps(state) {
    return {
        errorMessage: state.auth.error,
        authenticated: state.auth.authenticated
    };
}
export default connect(mapStateToProps, {fetchUnreadMessage})(MessageList);
