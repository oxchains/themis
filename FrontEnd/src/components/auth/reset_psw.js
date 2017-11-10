/**
 * Created by oxchain on 2017/11/09.
 */


import React, { Component } from 'react';
import { Field } from 'redux-form';
import { connect } from 'react-redux';
import { signinAction } from '../../actions/auth'
import { Modal, Button } from 'antd';



class Resetpsw extends Component {

    handleEmailSubmit()
    {
        const email= this.refs.email.value ;
        const password = this.refs.password.value ;
        if(email && password)
            this.props.signinAction({ email, password },()=>{});
    }
    render(){
        return (
            <div className="login-box">
                <div className="login-box-body">
                    <div className="form-style">
                        <div className=" login-box-msg" style={{fontSize: 24+'px'}}>重置密码</div>
                    </div>
                    <div className="form-style">
                        <div className="form-signin" >
                            <input className="input form-group" type="text" placeholder="请输入新密码" ref="email"/> <br/>
                            <input className="input form-group" type="password" placeholder="请再次输入新密码" ref="password"/><br/>
                            <div className="form-group">
                                <button className="btn form-login" onClick={this.handleEmailSubmit.bind(this)}>确定</button>
                            </div>



                        </div>
                    </div>
                </div>
            </div>);
    }
};


function mapStateToProps(state) {
    return {
        success: state.auth.authenticated,
        errorMessage: state.auth.error
    };
}
export default connect(mapStateToProps,{signinAction})(Resetpsw);