
/**
 * Created by oxchain on 2017/10/20.
 */
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Pagination } from 'antd';
import 'antd/dist/antd.css';
import { fetctTrusted } from '../actions/releaseadvert'
class Trust extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isTrusted:1,
            pageSize:8, //每页显示的条数8条
            current: 1,//默认的当前第一页
        }
        this.handleTrustYou = this.handleTrustYou.bind(this)
        this.handleYouTrust = this.handleYouTrust.bind(this)
        this.handleShielded = this.handleShielded.bind(this)
        this.handleRow = this.handleRow.bind(this)
    }
    componentWillMount() {
        const userId = localStorage.getItem("userId")
        const type = this.state.isTrusted
        const pageNo = this.state.current
        const pageSize = this.state.pageSize
        this.props.fetctTrusted({userId,type,pageNo,pageSize},()=>{})
    }
    onPagination(pageNum) {
        console.log( "当前页数"+ pageNum) //当前页数

        this.state.current = pageNum

        const userId = localStorage.getItem("userId")
        const type = this.state.isTrusted
        const pageNo = this.state.current
        const pageSize = this.state.pageSize
        this.props.fetctTrusted({userId,type,pageNo,pageSize},()=>{})
    }
    handleTrustYou(){
        this.state.isTrusted = 1
        // this.setState({isTrusted:1})
        const userId = localStorage.getItem("userId")
        const type = this.state.isTrusted
        const pageNo = this.state.current
        const pageSize = this.state.pageSize
        this.props.fetctTrusted({userId,type,pageNo,pageSize},()=>{})
    }
    handleYouTrust(){
        this.state.isTrusted = 2
        const userId = localStorage.getItem("userId")
        const type = this.state.isTrusted
        const pageNo = this.state.current
        const pageSize = this.state.pageSize
        this.props.fetctTrusted({userId,type,pageNo,pageSize},()=>{})
    }
    handleShielded(){
        this.state.isTrusted = 3
        const userId = localStorage.getItem("userId")
        const type = this.state.isTrusted
        const pageNo = this.state.current
        const pageSize = this.state.pageSize
        this.props.fetctTrusted({userId,type,pageNo,pageSize},()=>{})
    }
    handleRow( ){
        const arraydata = this.props.all || []    //列表数组的数据
        return arraydata.map((item, index) => {
        return(
            <tr key={index} className="contenttrust">
                <td>{item.fromUserName }</td>
                <td>交易次数 {item.txNum }</td>
                <td>信任人数 {item.believeNum } </td>
                <td>好评度 {item.goodDesc}</td>
                <td>历史交易 {item.buy } - {item.sell } BTC</td>
                <td>响应时间 {item.txToNum} 分钟</td>
                <td>跟他交易过{item.txToNum } 次</td>
            </tr>)
        })
    }
    render() {
        // const totalNum = this.props.all.length;
        const totalNum = 3
        return (
            <div className="">
                <ul className=" titleul">
                    <li className={` title-border ${this.state.isTrusted == 1 ? "ad-title-item active" :" ad-title-item"} `}   onClick={this.handleTrustYou}>信任您的人</li>
                    <li className={` title-border ${this.state.isTrusted == 2 ? "ad-title-item active" :" ad-title-item "}`} onClick={this.handleYouTrust}>您信任的人</li>
                    <li className={` title-border ${this.state.isTrusted == 3 ? "ad-title-item active" :" ad-title-item "}`} onClick={this.handleShielded}>被屏蔽的人</li>
                </ul>
              <div>
                  <table className=" tableborderTrust">
                      <tbody>
                      {this.handleRow()}
                      </tbody>

                  </table>
                  <div className="pagecomponent">
                      <Pagination  defaultPageSize={this.state.pageSize} total={totalNum}  onChange={e => this.onPagination(e)}/>
                  </div>
              </div>
            </div>
        );
    }
}

function mapStateToProps(state) {
    return {
        all:state.advert.all.result
    };
}
export default connect(mapStateToProps,{fetctTrusted})(Trust);
