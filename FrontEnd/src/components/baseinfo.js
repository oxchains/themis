/**
 * Created by oxchain on 2017/10/20.
 */
import React, { Component } from 'react';

import { Field } from 'redux-form';
import { connect } from 'react-redux';

class Baseinfo extends Component {
    constructor(props) {
        super(props);
        this.state = {}
    }

    render() {

        return (
            <div>
                <div className="maininfo">
                    <div className="display-info">
                        <img className="baseinfoimg" src="./public/img/user.jpg" alt=""/>
                    </div>
                    <div className="display-info">
                        <input className="inputfile" type="file"/>
                    </div>
                    <div className="display-info">
                        <h5 style={{marginBottom:20+'px'}}>TONSION</h5>
                    </div>
                </div>
                <div className="validateinfo">
                     <ul>
                         <li>身份证验证:已验证</li>
                         <li>电子邮件验证:已验证</li>
                         <li>手机号码:已验证</li>
                         <li>注册时间:2017-9-20 11:03:33</li>
                         <li>第一次交易时间：2017-9-20 16:20:38</li>
                         <li>信任人数:被 1 人信任</li>
                         <li>累计交易次数: 1</li>
                         <li>累计交易量:0-0.5 BTC</li>
                     </ul>
                </div>
                <textarea className="textarea-info" name="" id="" cols="53" rows="5" placeholder="简介，在您的公共资料上展示您的介绍信息。纯文本，不超过200字"></textarea>
               <div className="display-save">
                   <button className="form-save">保存</button>
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
export default connect(mapStateToProps,{})(Baseinfo);
