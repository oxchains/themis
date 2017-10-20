/**
 * Created by oxchain on 2017/10/18.
 */
import React, { Component } from 'react';
import { Field } from 'redux-form';
import { connect } from 'react-redux';
import { signinAction } from '../../actions/auth'

class Signin extends Component {
    constructor(props) {
        super(props);
        this.state = {}
        this.handleFormSubmit = this.handleFormSubmit.bind(this)

    }

    handleFormSubmit(e) {
        e.preventDefault()
        const loginname= this.refs.loginname.value ;
        const password = this.refs.password.value ;
        if(loginname && password)
            this.props.signinAction({ loginname, password },()=>{});
    }

    renderAlert() {
        if (this.props.errorMessage) {
            return (
                <div className="alert alert-danger alert-dismissable">
                    {this.props.errorMessage}
                </div>
            );
        }
    }
    render() {
        // const { handleSubmit} = this.props;
        return (
            <div>
                <div className="login-box">

                    <div className="login-box-body">
                        <p className="login-box-msg form-style" style={{fontSize: 20+'px'}}>用户登陆</p>
                        {this.renderAlert()}
                        <div className="form-style">
                            <form className="form-signin" action="" onSubmit={this.handleFormSubmit}>
                                <input className="input form-group" type="text" placeholder="请输入登录名" ref="loginname"/> <br/>
                                <input className="input form-group" type="password" placeholder="请输入密码" ref="password"/><br/>
                                <div className="form-style">
                                  <button type="submit" className="btn form-login">登录</button>
                                </div>
                            </form>
                        </div>

                    </div>
                </div>
            </div>
        );
    }
}



function mapStateToProps(state) {
    return {
        success: state.auth.authenticated,
        errorMessage: state.auth.error
    };
}
export default connect(mapStateToProps,{signinAction})(Signin);
// export default Signin