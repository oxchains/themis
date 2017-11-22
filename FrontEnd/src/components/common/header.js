/**
 * Created by oxchain on 2017/10/17.
 */

import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { Badge } from 'antd';
import { connect } from 'react-redux';
import { fetchMessageNumber } from "../../actions/message";
class Header extends Component {
    constructor(props) {
        super(props);
        this.state = {};
        this.renderUserInfo = this.renderUserInfo.bind(this);
    }
    componentWillMount() {
        const userId = localStorage.getItem("userId");
        if (this.props.authenticated) {
            this.props.fetchMessageNumber({ userId });
        }
    }
    renderUserInfo() {
        const role = localStorage.getItem('role');
        if (this.props.authenticated) {
            const loginname = localStorage.getItem('loginname');
            return (
                <div className="navbar-custom-menu">
                    <ul className="nav navbar-nav">
                        {role == 3 ? <li className="order-style" style={{ width: "135px" }}><Link to="/refereelist">仲裁列表</Link></li> : ""}
                        <li className="order-style">
                            <Link to="/messagenotice">
                                消息{this.props.message_number != undefined && this.props.message_number > 0 ? <Badge count={this.props.message_number} /> : ""}
                            </Link>
                        </li>
                        <li className="order-style"><Link to="/orderinprogress">订单</Link></li>
                        <li className="order-style"><Link to="/orderinprogress">钱包</Link></li>
                        <li className="ordermenu-style dropdown user user-menu">
                            <a href="#" className="dropdown-toggle" data-toggle="dropdown">
                                <span className="hidden-xs">{loginname}</span>
                            </a>
                            <ul className="dropdown-menu">
                                <li className="info-self">
                                    <div className="info-style">
                                        <a href="/usercenter" >用户中心</a>
                                    </div>
                                    <div className="info-style">
                                        <a href="/myadvert" >我的广告</a>
                                    </div>
                                    <div className="info-style">
                                        <a href="/signout" >退出登录</a>
                                    </div>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </div>
            );
        }
    }
    render() {
        const username = localStorage.getItem('username');
        return (
            <div className="headerwidth">
                <nav className="header ">
                    <div className="header-position">
                        <div className="navdivimg">
                            <img src="./public/img/logo4.png" className="navimg" alt="" />
                        </div>
                        <ul className="headerul" >
                            <li><a href="/" >首页</a></li>
                            <li ><a href="/buybtc"  >购买比特币</a></li>
                            <li ><a href="/sellbtc" >出售比特币</a></li>
                            <li ><a href="/releaseadvert" >发布广告</a></li>

                        </ul>
                    </div>
                    <div className={`navbar-custom-menu ${this.props.authenticated ? "hidden" : ""}`}>
                        <ul className="nav navbar-nav">
                            <li className={`registerlia order-style `} ><a href="/signup" >注册</a></li>
                            <li className={`loginlia order-style `}><a href="/signin"  >登录</a></li>
                        </ul>
                    </div>
                    {this.renderUserInfo()}
                </nav>
            </div>
        );
    }
}
function mapStateToProps(state) {
    return {
        errorMessage: state.auth.error,
        authenticated: state.auth.authenticated,
        message_number: state.message.message_number
    };
}

export default connect(mapStateToProps, { fetchMessageNumber })(Header);
