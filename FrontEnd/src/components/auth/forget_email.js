/**
 * Created by oxchain on 2017/11/09.
 */


import React, { Component } from 'react';
import { Field } from 'redux-form';
import { connect } from 'react-redux';
import { signinAction } from '../../actions/auth';




class Emialforget extends Component {

    handleEmailSubmit()
    {
        const email= this.refs.email.value ;
        const password = this.refs.password.value ;
        if(email && password)
            this.props.signinAction({ email, password }, ()=>{});
    }
    render(){
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
                            <input className="input form-group" type="text" placeholder="请输入邮箱地址" ref="email"/> <br/>
                            <input className="input form-group" type="password" placeholder="请输入密码" ref="password"/><br/>
                            <div className="form-group">
                                <button className="btn form-login" onClick={this.handleEmailSubmit.bind(this)}><a
                                    href="/resetpsw">下一步</a></button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>);
    }
}


function mapStateToProps(state) {
    return {
        success: state.auth.authenticated,
        errorMessage: state.auth.error
    };
}
export default connect(mapStateToProps, { signinAction })(Emialforget);