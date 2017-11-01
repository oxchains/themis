/**
 * Created by oxchain on 2017/10/20.
 */
import React, { Component } from 'react';

import { Field } from 'redux-form';
import { connect } from 'react-redux';

class Safeset extends Component {
    constructor(props) {
        super(props);
        this.state = {}
    }

    render() {

        return (
            <div >
               <div className="changeStyle">
                  <span className="fa fa-mobile"></span>
                   <div className="bindPhone">
                       <p>绑定手机</p>
                       <p className="bindinfo">提现,修改密码,及安全设置时用以收验证短信</p>
                   </div>
                   <button className="changePhone">修改</button>
               </div>
                <div className="changeStyle">
                    <span> <i className="fa fa-unlock-alt"></i></span>
                    <div className="bindPhone">
                        <p>登录密码</p>
                        <p className="bindinfo">用于登录账户时输入</p>
                    </div>
                    <button className="changePhone">修改</button>
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
export default connect(mapStateToProps,{})(Safeset);
