/**
 * Created by zhangxiaojing on 2017/11/09.
 */

import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import {Pagination} from 'antd';
import { connect } from 'react-redux';
import {fetchMessageNotice} from "../../actions/message";
class MessageNotice extends Component{
    constructor(props) {
        super(props);
        this.state = {
            pageNum:1,
            pageSize:8
        }
    }
    componentWillMount(){
        const userId=localStorage.getItem("userId")
        const pageNum=this.state.pageNum;
        const pageSize=this.state.pageSize;
        this.props.fetchMessageNotice({userId, pageNum, pageSize})
    }
    handlePagination(pageNum) {
        const userId=localStorage.getItem("userId")
        const pageSize=this.state.pageSize;
        this.props.fetchMessageNotice({userId, pageNum, pageSize})
    }
    renderList(){
        return this.props.message_notice.pageList.map((item, index)=>{
            return(
                <li className="message-item-list clearfix" key={index}>
                    <div className="col-xs-2">
                        <div className="photo pull-right"></div>
                    </div>
                    <div className="col-xs-10 message-item-content">
                        <div className="message-item-tip"><span>{item.messageType == 2 ? "公告" :""}</span><span>{item.messageText.postDate}</span></div>
                        <div className="message-item-detail">{item.messageText.message}</div>
                    </div>
                </li>
            )
        })
    }
    render(){
        const totalNum = this.props.message_notice && this.props.message_notice.rowCount
        console.log(this.props.message_notice)
        return (
            <div className="message-box">
                <div className="container">
                    <div className="row">
                        <div className="col-xs-12 message-list">
                            <ul>
                                <li className="col-xs-4 text-right">
                                    <Link className="text-center active" to="/messagenotice">公告</Link>
                                </li>
                                <li className="col-xs-4 text-center">
                                    <Link className="text-center" to="/messagesystem">系统</Link>
                                </li>
                                <li className="col-xs-4 text-left">
                                    <Link className="text-center" to="/messageletter">私信</Link>
                                </li>
                            </ul>
                        </div>
                        <div className="col-xs-12 message-item-content">
                           <ul>
                               {this.props.message_notice == null ? <div className="text-center h4">暂无消息</div> : this.renderList()}
                           </ul>
                        </div>
                        <div className="col-xs-12">
                            <div className="pagecomponent">
                                <Pagination  defaultPageSize={this.state.pageSize} total={totalNum}  onChange={e => this.handlePagination(e)}/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}
function mapStateToProps(state) {
    return {
        message_notice:state.message.message_notice,
    };
}

export default connect(mapStateToProps, {fetchMessageNotice})(MessageNotice);