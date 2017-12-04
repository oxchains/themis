/**
 * Created by oxchain on 2017/11/09.
 */


import React, { Component } from 'react';
import { Field } from 'redux-form';
import { connect } from 'react-redux';
import { EmialAction } from '../../actions/auth';

class Emialforget extends Component {

    handleEmailSubmit() {
        const email = this.refs.email.value;
        const vcode = this.refs.vcode.value;
        console.log('222');
        if (email){
            this.props.EmialAction({ email }, () => { });
           }else{
               alert('请先输入邮箱地址');
           }
        }
    render() {
        const Imgurl = this.props.all;
        console.log(Imgurl);
        return (
            <div className="login-box">
                <div className="login-box-body">
                    <div className="signinWay text-center g-pt-50">
                        <ul className="row loginul">
                            <li className="col-xs-6 loginli"> <a className="g-pb-3" href="/forgetpsw">手机找回</a></li>
                            <li className="col-xs-6 loginli"><a className="signinTypeBar g-pb-3" href="/emailforget">邮箱找回</a></li>
                        </ul>
                    </div>
                    <div className="form-style">
                        <div className="form-signin" >
                            <input className="input form-group" type="text" placeholder="请输入邮箱地址" ref="email" /> <br />
                            <input className="vcode form-group" type="password" placeholder="请输入验证码" ref="vcode" />
                            <img src={Imgurl ? Imgurl : "./public/img/touxiang.png"} className="imgVcode" onClick={this.handleEmailSubmit.bind(this)} alt="" />
                            <div className="form-group">
                                <button className="btn form-login" ><a href="">发送</a></button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>);
    }
}


function mapStateToProps(state) {
    console.log(state.auth.all);
    return {
        all: state.auth.all
    };
}
export default connect(mapStateToProps, { EmialAction })(Emialforget);