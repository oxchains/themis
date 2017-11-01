/**
 * Created by oxchain on 2017/10/17.
 */

import React, { Component } from 'react';
import { Link } from 'react-router';
import { connect } from 'react-redux';
class Header extends Component{
    constructor(props) {
        super(props);
        this.state = {}
        this.renderUserInfo = this.renderUserInfo.bind(this)
    }
    renderUserInfo() {
        const role=localStorage.getItem('role')
        if(this.props.authenticated) {
            const loginname= localStorage.getItem('loginname');
            return (
                <div className="navbar-custom-menu">
                    <ul className="nav navbar-nav">
                        {role == 3 ?  <li className="order-style" style={{width:"135px"}}><a href="/refereelist">仲裁人消息列表</a></li> : "" }
                        <li className="order-style"><a href="/orderinprogress">订单</a></li>
                        <li className="order-style"><a href="/orderinprogress">钱包</a></li>
                        <li className="dropdown user user-menu">
                            <a href="#" className="dropdown-toggle" data-toggle="dropdown">
                                <span className="hidden-xs">{loginname}</span>
                            </a>
                            <ul className="dropdown-menu">
                                <li className="info-self">
                                    <div className="info-style">
                                        <a  href="/usercenter" >用户中心</a>
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
                </div>);
        }
    }
    render(){
        const username= localStorage.getItem('username');
        return (
            <div className="headerwidth">
                <nav  className="header ">
                   <div className="header-position">
                       <div className="navdivimg">
                           <img src="./public/img/nl_logo.png" className="navimg" alt=""/>
                       </div>
                       <ul className="headerul" >
                           <li><a href="/" >首页</a></li>
                           <li ><a href="/buybtc"  >购买比特币</a></li>
                           <li ><a href="/sellbtc" >出售比特币</a></li>
                           <li ><a href="/releaseadvert" >发布广告</a></li>
                           <li className={`registerlia ${this.props.authenticated?"hidden":""}`} ><a href="/signup" >注册</a></li>
                           <li className={`loginlia ${this.props.authenticated?"hidden":""}`}><a href="/signin"  >登录</a></li>
                       </ul>
                   </div>
                    {this.renderUserInfo()}
                </nav>
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

export default connect(mapStateToProps)(Header);
