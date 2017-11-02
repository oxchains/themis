/**
 * Created by oxchain on 2017/10/20.
 */
import React, { Component } from 'react';

import { connect } from 'react-redux';
import { Modal, Button } from 'antd';
import { GetverifyCode } from '../actions/auth'
class Safeset extends Component {
    constructor(props) {
        super(props);
        this.state = {
            loading: false,
            visible: false,
            loadingpsw: false,
            visiblepsw: false,
            count: 60,
            liked: true,
        }
        this.handlesend = this.handlesend.bind(this)
    }
    showModal = () => {
        this.setState({
            visible:true,
        });
    }
    showModalPSW = () => {
        this.setState({
            visiblepsw:true,
        });
    }
    handleOk = () => {
        this.setState({ loading: true });
        setTimeout(() => {
            this.setState({ loading: false, visible: false });
        }, 3000);
    }
    handleOkPSW = () => {
        this.setState({ loadingpsw: true });
        setTimeout(() => {
            this.setState({ loadingpsw: false, visiblepsw: false });
        }, 3000);
    }
    handleCancel = () => {
        this.setState({ visible: false });
    }

    handleCancelPSW = () => {
        this.setState({ visiblepsw: false });
    }
    handlesend(){
        if(this.state.liked){
            this.timer = setInterval(function () {
                var count = this.state.count;
                this.state.liked = false;
                count -= 1;
                if (count < 1) {
                    this.setState({
                        liked: true
                    });
                    count = 60;
                    clearInterval(this.timer);
                }
                this.setState({
                    count: count
                });
            }.bind(this), 1000);
        }
        const phonenum = localStorage.getItem("phonenum")

        this.props.GetverifyCode({phonenum},()=>{})
    }
    phoneChange(e){
        const phonenum = localStorage.setItem("phonenum",e.target.value)
        var regex = /^1[3|4|5|7|8][0-9]\d{4,8}$/
        if (regex.test(e.target.value) ) {

        } else{
            alert('请输入正确的手机号码！');
        }
    }
    render() {
        var text = this.state.liked ? '发送验证码' : this.state.count + ' s ' ;
        const { visible, loading ,visiblepsw,loadingpsw} = this.state;
        return (
            <div >
               <div className="changeStyle">
                  <span className="fa fa-mobile"></span>
                   <div className="bindPhone">
                       <p>绑定手机</p>
                       <p className="bindinfo">提现,修改密码,及安全设置时用以收验证短信</p>
                   </div>
                   <Button className="changePhone" onClick={this.showModal}>修改</Button>
                   <Modal
                       visible={visible}
                       title="请输入新的手机号"
                       onOk={this.handleOk}
                       onCancel={this.handleCancel}
                       footer={[
                           <Button className="confirmStyle" key="submit" type="primary" size="large" loading={loading} onClick={this.handleOk}>
                               确认
                           </Button>,
                       ]}
                   >
                      <div className="modalInput">
                          <input className="formChange" type="text" placeholder="请输入新的手机号码" onBlur={this.phoneChange}/>
                          <div className="Verifycodewidth">
                              <input className="formVerifycode " type="text" placeholder=" 请输入验证码"/>
                              <span className={`send-testcode  ${this.state.liked?"" :"time-color"}`} onClick={this.handlesend}>{text}</span>
                          </div>
                      </div>

                   </Modal>
               </div>
                <div className="changeStyle">
                    <span> <i className="fa fa-unlock-alt"></i></span>
                    <div className="bindPhone">
                        <p>登录密码</p>
                        <p className="bindinfo">用于登录账户时输入</p>
                    </div>
                    <Button className="changePhone" onClick={this.showModalPSW}>修改</Button>
                    <Modal
                        visible={visiblepsw}
                        title="修改登录密码"
                        onOk={this.handleOkPSW}
                        onCancel={this.handleCancelPSW}
                        footer={[
                            <Button className="confirmStyle" key="submit" type="primary" size="large" loading={loadingpsw} onClick={this.handleOkPSW}>
                                确认
                            </Button>,
                        ]}
                    >
                        <input className="formChange" type="text" placeholder="请输入旧密码" />
                        <input className="formChange " type="text" placeholder=" 请输入新密码"/>
                    </Modal>
                </div>

            </div>
        );
    }
}



function mapStateToProps(state) {
    return {

    };
}
export default connect(mapStateToProps,{GetverifyCode})(Safeset);
