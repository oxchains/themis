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
        const biz= JSON.parse(localStorage.getItem('biz'));
        if(this.props.authenticated) {
            //const user = JSON.parse(localStorage.getItem('user'));
            const username= localStorage.getItem('username');
            const avatar = `https://gravatar.com/avatar/oxchain.org/user/${username}?s=100&d=retro`;
            const biz= JSON.parse(localStorage.getItem('biz'));
            return (
                <div className="navbar-custom-menu">
                    <ul className="nav navbar-nav">
                        <li className="dropdown user user-menu">
                            <ul className="dropdown-menu">
                                <li className="user-header">
                                    <p>liuruichao</p>
                                </li>
                                <li className="user-body">
                                    <div className="row">
                                    </div>
                                </li>
                                <li className="user-footer">
                                    <div className="pull-left">
                                        <a className={`btn btn-default btn-flat`} href="#" >设置</a>
                                    </div>
                                    <div className="pull-right">
                                        <a href="/signout" className="btn btn-default btn-flat">退出登录</a>
                                    </div>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </div>);
        } else {
            return <div></div>
        }
    }
    render(){
        const username= localStorage.getItem('username');
        return (
            <div className="headerwidth">
                <nav  className="header">
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
                </nav>
            </div>
    )
    }

}
function mapStateToProps(state) {
    return {
        authenticated: state.auth.authenticated
    };
}

export default connect(mapStateToProps)(Header);
