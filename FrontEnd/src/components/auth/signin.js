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
        const num= this.refs.num.value ;
        const mobilephone= this.refs.loginname.value ;
        const password = this.refs.password.value ;
        if(mobilephone && password)
            this.props.signinAction({ mobilephone, password },()=>{});
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
        return (
            <div>

                <div className="login-box">

                    <div className="login-box-body">

                        <TabsControl >
                            <Tab name="手机登录">
                                <div className="phone" ><Phonelogin/></div>
                            </Tab>
                            <Tab name="邮箱登录">
                                <div className="emial"><Emiallogin/></div>
                            </Tab>
                        </TabsControl>
                    </div>
                </div>
            </div>
        );
    }
}
let TabsControl = React.createClass({
    getInitialState: function(){
        return {currentIndex: 0}
    },
    getTitleItemCssClasses: function(index){
        return index === this.state.currentIndex ? "login-title-item active" : "login-title-item";
    },
    getContentItemCssClasses: function(index){
        return index === this.state.currentIndex ? "tab-content-item active" : "tab-content-item";
    },

    render(){
        let that = this;
        return (
            <div className="">
                <div className="form-style">
                    <ul className=" loginul">
                        {React.Children.map(this.props.children, (element, index) => {
                            return (<li className={`loginli ${that.getTitleItemCssClasses(index)}`} onClick={() => {this.setState({currentIndex: index})}}>
                                {element.props.name}</li>)
                        })}
                    </ul>
                </div>
                <div className=" clear">
                    {React.Children.map(this.props.children, (element, index) => {
                        return (<div className={that.getContentItemCssClasses(index)}>{element}</div>)
                    })}
                </div>
            </div>
        )
    }
});
let Tab = React.createClass({
    render(){
        return (<div>{this.props.children}</div>);
    }
});

let Phonelogin = React.createClass({
    render(){
        return (<div>
            <div className="form-style">
                <form className="form-signin" action="" onSubmit={this.handleFormSubmit}>
                    {/*<input className="input form-group" type="text" placeholder="中国 + 86" ref="num"/> <br/>*/}
                    <select name="" id="" className="input form-group"> +86
                        <option value="1">中国 + 86</option>
                        <option value="2">美国 + 22</option>
                        <option value="3">英国 + 33</option>
                        <option value="4">韩国 + 44</option>
                    </select>
                    <input className="input form-group" type="text" placeholder="请输入手机号" ref="loginname"/> <br/>
                    <input className="input form-group" type="password" placeholder="请输入密码" ref="password"/><br/>
                    <div className="form-group">
                        <button type="submit" className="btn form-login">登录</button>
                    </div>
                    <div className="form-group">
                        <a className="forgetpwd" href="">忘记密码 ?</a>
                    </div>
                </form>
            </div>
        </div>);
    }
});

let Emiallogin = React.createClass({
    render(){
        return (<div>
            <div className="form-style">
                <form className="form-signin" action="" onSubmit={this.handleFormSubmit}>
                    <input className="input form-group" type="text" placeholder="请输入邮箱" ref="email"/> <br/>
                    <input className="input form-group" type="password" placeholder="请输入密码" ref="password"/><br/>
                    <div className="form-group">
                        <button type="submit" className="btn form-login">登录</button>
                    </div>

                </form>
            </div>
        </div>);
    }
});




function mapStateToProps(state) {
    return {
        success: state.auth.authenticated,
        errorMessage: state.auth.error
    };
}
export default connect(mapStateToProps,{signinAction})(Signin);






