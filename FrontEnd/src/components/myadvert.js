/**
 * Created by oxchain on 2017/10/23.
 */
import React, { Component } from 'react';
import { connect } from 'react-redux';

import {fetctMyAdvert} from '../actions/releaseadvert'
class Myadvert extends Component {
    constructor(props) {
        super(props);
        this.state = {
            status:'1',
            adstatus:'1'
        }
        this.handleRowsbuy = this.handleRowsbuy.bind(this)
        this.handleRowssell = this.handleRowssell.bind(this)
        this.handleRowsadverting = this.handleRowsadverting.bind(this)
        this.handleRowadverted = this.handleRowadverted.bind(this)
        this.handleRow = this.handleRow.bind(this)

    }

    handleRowsbuy(){
        this.state.status = 1
        const userId = localStorage.getItem("userId")
        const noticeType = this.state.status
        const txStatus = this.state.adstatus
        this.props.fetctMyAdvert({userId,noticeType,txStatus},()=>{});
    }
    handleRowssell(){
        this.state.status = 2
        const userId = localStorage.getItem("userId")
        const noticeType = this.state.status
        const txStatus = this.state.adstatus
        this.props.fetctMyAdvert({userId,noticeType,txStatus},()=>{});
    }

    handleRowsadverting(){
        this.state.adstatus = 1
        const userId = localStorage.getItem("userId")
        const noticeType = this.state.status
        const txStatus = this.state.adstatus
        this.props.fetctMyAdvert({userId,noticeType,txStatus},()=>{});
    }

    handleRowadverted(){
        this.state.adstatus = 2
        const userId = localStorage.getItem("userId")
        const noticeType = this.state.status
        const txStatus = this.state.adstatus
        this.props.fetctMyAdvert({userId,noticeType,txStatus},()=>{});
    }

    handleRow(){
        const arraydata = this.props.all || []    //列表数组的数据
        return arraydata.map((item, index) => {

        return(<tr key={index} className="contentborder">
                <td>{item.id}</td>
                <td>{item.noticeType}</td>
                <td>{item.location} </td>
                <td>{item.price}</td>
                <td>{item.price}</td>
                <td>{item.createTime}</td>
                <td>{item.txStatus}</td>
            </tr>)
        })
    }
    componentWillMount(){
        const userId = localStorage.getItem("userId")
        console.log(userId)
        const noticeType = this.state.status
        const txStatus = this.state.adstatus
        this.props.fetctMyAdvert({userId,noticeType,txStatus},()=>{});
    }
    render() {
            return (
                    <div className="mainbar">
                        <div className="col-lg-3 col-md-3 col-xs-3">
                        <ul className=" sildbar">
                            <li className={` liheight ${this.state.status == 1 ? "tab-title-item active" :" tab-title-item"} `}   onClick={this.handleRowsbuy}>购买广告</li>
                            <li className={` liheight ${this.state.status == 2 ? "tab-title-item active" :" tab-title-item "}`} onClick={this.handleRowssell}>出售广告</li>
                        </ul>
                        </div>
                        <div className="col-lg-9 col-md-9 col-xs-9">
                            <ul className=" titleul">
                                <li className={` title-border ${this.state.adstatus == 1 ? "ad-title-item active" :" ad-title-item"} `}   onClick={this.handleRowsadverting}>进行中的广告</li>
                                <li className={` title-border ${this.state.adstatus == 2 ? "ad-title-item active" :" ad-title-item "}`} onClick={this.handleRowadverted}>已下架的广告</li>
                            </ul>

                            <div>
                                <table className="tableborder">
                                    <tbody>
                                    <tr className="contentborder ">
                                        <th>编号</th>
                                        <th>广告类型</th>
                                        <th>国家</th>
                                        <th>价格</th>
                                        <th>溢价比例</th>
                                        <th>创建时间</th>
                                        <th>状态</th>
                                    </tr>
                                    {this.handleRow()}
                                    <tr className="contentborder bottomcontent">
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td>没有更多内容了</td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                    </tr>
                                    </tbody>

                                </table>
                            </div>
                        </div>

                </div>
            );
        }
    }
function mapStateToProps(state) {
    console.log(state.advert.all)
    return {
        all:state.advert.all       //我的广告
    };
}
export default connect(mapStateToProps,{  fetctMyAdvert})(Myadvert);

