/**
 * Created by oxchain on 2017/10/17.
 */

import React, { Component } from 'react';
import { Link } from 'react-router';

class Header extends Component{
    constructor(props) {
        super(props);
        this.state = {}
    }
    render(){
        const username= localStorage.getItem('username');
        return (
            <div>
                <nav  className="header">
                   <span>这是导航条</span>
                    <a className="logincolor" href="/">首页 </a>
                    <a className="logincolor" href="/signin">登录 </a>
                    <a className="logincolor" href="/signup"> 注册 </a>
                    <a className="logincolor" href="/usercenter"> 用户中心 </a>
                    <a className="logincolor" href="/orderinprogress">订单</a>
                </nav>
            </div>
    )
    }

}
export default Header;