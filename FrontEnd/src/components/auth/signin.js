/**
 * Created by oxchain on 2017/10/18.
 */
import React, { Component } from 'react';
import { Route, Redirect } from 'react-router-dom';
import { connect } from 'react-redux';
import { signinAction } from '../../actions/auth';

class Signin extends Component {
    constructor(props) {
        super(props);
        this.state = {};
    }
    handlePhoneSubmit() {
        const mobilephone= this.refs.loginname.value ;
        const password = this.refs.password.value ;
        if(mobilephone && password) {
            this.props.signinAction({mobilephone, password});
        }
    }
    renderAlert() {
        const { from } = this.props.location.state || { from: { pathname: '/' } };
        if (this.props.loggedIn) {
            location.reload();
            return (
                <Redirect to={from}/>
            );
        }else if(this.props.errorMessage){
            return (
                <div className="alert alert-danger alert-dismissable">
                    {this.props.errorMessage}
                </div>
            );
        }
    }
    render(){
        return (
            <div className="mainbgc">
                <div className="login-box">
                    <div className="login-box-body">

                        <div className=" signinWay text-center g-pt-50">
                            <ul className="row loginul">
                                <li className="col-xs-6 loginli"> <a className="signinTypeBar g-pb-3" href="/signin">手机登录</a></li>
                                <li className="col-xs-6 loginli"><a className=" g-pb-3" href="/signinemail">邮箱登录</a></li>
                            </ul>
                        </div>
                        <div className="form-style">
                            <div className="form-signin"  >
                                <select name="" id="" className="input form-group"> +86
                                    <option value="1">中国 + 86</option>
                                    <option value="2">美国 + 22</option>
                                    <option value="3">英国 + 33</option>
                                    <option value="4">韩国 + 44</option>
                                </select>
                                <input className="input form-group" type="text" placeholder="请输入手机号" ref="loginname"/> <br/>
                                <input className="input form-group" type="password" placeholder="请输入密码" ref="password"/><br/>
                                <div className="form-group">
                                    <button  className="btn form-login" onClick={this.handlePhoneSubmit.bind(this)}>登录</button>
                                </div>
                                <div className="form-group">
                                    <a className="forgetpwd" href="/forgetpsw">忘记密码 ?</a>
                                </div>
                                {this.renderAlert()}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}
function mapStateToProps(state) {
    return {
        loggedIn: state.auth.authenticated,
        errorMessage: state.auth.error
    };
}
export default connect(mapStateToProps, { signinAction })(Signin);






