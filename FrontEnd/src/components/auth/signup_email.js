/**
 * Created by oxchain on 2017/12/01.
 */


import React, { Component } from 'react';
import { Field, reduxForm } from 'redux-form';
import { Link } from 'react-router-dom';
import { connect } from 'react-redux';
import { EmialsignupUser } from '../../actions/auth';
import { Route, Redirect } from 'react-router-dom';
import { Alert } from 'antd';
class SignupEmial extends Component {
    constructor(props) {
        super(props);
        this.state = {
            spin: false,
        };
    }
    handleEmailSubmit({loginname, email, password}) {
        // e.preventDefault();
        // const loginname = this.refs.loginname.value;
        // const email = this.refs.email.value;
        // const password = this.refs.password.value;
        console.log(loginname, email, password);

        // if (email && password)
        //     this.props.EmailsigninAction({ email, password }, () => { });
         }
    renderField({ input, label, type, icon, meta: { touched, error } }) {
        return (
            <div className={`form-style ${touched && error ? 'has-error' : ''}`}>
                <input {...input} placeholder={label} type={type} className="form-control " />
                <div className="help-block ">{touched && error ? error : ''}</div>
            </div>
        );
    }
    render() {
        const { handleSubmit } = this.props;
        return (
            <div className="login-box">
                    <div className="signinWay text-center g-pt-50">
                        <ul className="row loginul">
                            <li className="col-xs-6 loginli"> <Link className="g-pb-3" to="/signup">手机注册</Link></li>
                            <li className="col-xs-6 loginli"><Link className="signinTypeBar g-pb-3" to="/signupemail">邮箱注册</Link></li>
                        </ul>
                    </div>
                    <div className="login-box-body">
                        <form className="form-signin" onSubmit={handleSubmit(this.handleFormSubmit.bind(this))}>
                            <Field name="loginname" component={this.renderField} type="text" label="请输入用户名" />
                            <Field name="email" component={this.renderField} type="text" label="请输入邮箱" />
                            <Field name="password" component={this.renderField} type="password" label="请输入密码" icon="lock" />
                            <div style={{width:85 +'%'}}>
                                <div className=" checkbox-margin">
                                    <input type="checkbox" defaultChecked className="checkbox-width" /><span> 我已阅读themis用户手册及相关法律</span>
                                </div>
                                <div className="">
                                    <button type="submit" className="btn   form-register"><i className={`fa fa-spinner fa-spin ${this.state.spin ? '' : 'hidden'}`}></i> 注册</button>
                                </div>
                                <div className="form-group clicklogin">
                                    <a className="" href="/signin">已有账户 ? 点击登录</a>
                                </div>
                            </div>
                        </form>
                    </div>
            </div>);
    }
}
const validate = values => {
    const errors = {};

    if (!values.loginname) {
        errors.loginname = ' *不能为空';
    }
    if (!values.email) {
        errors.email = ' *不能为空';
    }
    if (!values.password) {
        errors.password = ' *不能为空';
    }
    return errors;
};

function mapStateToProps(state) {
    return {
        loggedIn: state.auth.authenticated,
        errorMessage: state.auth.error
    };
}
const reduxSignupForm = reduxForm({
    form: 'SignForm',
    validate
})(SignupEmial);
export default connect(mapStateToProps, { EmialsignupUser })(reduxSignupForm);