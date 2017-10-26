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
        if(this.props.authenticated) {
            const username= localStorage.getItem('username');
            return (
                <div className="navbar-custom-menu">
                    <ul className="nav navbar-nav">
                        <li className="order-style"><a href="/orderinprogress">订单</a></li>
                        <li className="order-style"><a href="/orderinprogress">钱包</a></li>
                        <li className="dropdown user user-menu">
                            <a href="#" className="dropdown-toggle" data-toggle="dropdown">
                                <span className="hidden-xs">{username}</span>
                            </a>
                            <ul className="dropdown-menu">
                                <li className="info-self ">
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
                <nav  className="header">
<<<<<<< HEAD
                    <div className="navdivimg">
                        <img src="./public/img/nl_logo.png" className="navimg" alt=""/>
                        {/*<img src="./public/img/themis.png" className="navthemis" alt=""/>*/}
                    </div>

                    <ul className="headerul" >
                        <li><a href="/" >首页</a></li>
                        <li ><a href="/buybtc"  >购买比特币</a></li>
                        <li ><a href="/sellbtc" >出售比特币</a></li>
                        <li ><a href="/releaseadvert" >发布广告</a></li>
                        {/*<li ><a href="/myadvert" >我的广告</a></li>*/}
                        {/*<li ><a href="/usercenter" >用户中心</a></li>*/}
                        <li className="registerlia"><a href="/signup" >注册</a></li>
                        <li  className="loginlia"><a href="/signin"  >登录</a></li>
                    </ul>
                    {/*{this.renderUserInfo()}*/}
<<<<<<< HEAD
                   <span>这是导航条</span>
                    <a className="logincolor" href="/">首页 </a>
                    <a className="logincolor" href="/releaseadvert"> 发布广告 </a>
                    <a className="logincolor" href="/buybtc"> 购买比特币 </a>
                    <a className="logincolor" href="/sellbtc"> 出售比特币 </a>
                    <a className="logincolor" href="/signin"> 登录 </a>
                    <a className="logincolor" href="/signup"> 注册 </a>
                    <a className="logincolor" href="/usercenter"> 用户中心 </a>
                    <a className="logincolor" href="/orderinprogress">订单</a>
                    <a className="logincolor" href="/myadvert"> 我的广告 </a>
=======

>>>>>>> c2debf82de0be5f6808de6594277b3af2a1523ac
=======
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
>>>>>>> 68b2492994e1c09fdca23a9941e8720b66b8aa9c
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
