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
        const formdata = {
            userId:localStorage.getItem("userId"),
            type : this.state.isTrusted,
            pageNo : this.state.current,
            pageSize : this.state.pageSize
        }
        console.log(formdata)
        this.props.fetctTrusted({formdata},()=>{})
    }
    onPagination(pageNum) {
        console.log( "当前页数"+ pageNum) //当前页数

        this.state.current = pageNum
        const formdata = {
            userId:localStorage.getItem("userId"),
            type : this.state.isTrusted,
            pageNo : this.state.current,
            pageSize : this.state.pageSize
        }
        this.props.fetctTrusted({formdata},()=>{})
    }
    handleTrustYou(){
        this.state.isTrusted = 1
        // this.setState({
        //     isTrusted:1
        // })
        const formdata = {
            userId:localStorage.getItem("userId"),
            type : this.state.isTrusted,
            pageNo : this.state.current,
            pageSize : this.state.pageSize
        }
        this.props.fetctTrusted({formdata},()=>{})
    }
    handleYouTrust(){
        this.state.isTrusted = 2
        // this.setState({
        //     isTrusted:2
        // })
        const formdata = {
            userId:localStorage.getItem("userId"),
            type : this.state.isTrusted,
            pageNo : this.state.current,
            pageSize : this.state.pageSize
        }
        this.props.fetctTrusted({formdata},()=>{})
    }
    handleShielded(){
        this.state.isTrusted = 3
        // this.setState({
        //     isTrusted:3
        // })
        const formdata = {
            userId:localStorage.getItem("userId"),
            type : this.state.isTrusted,
            pageNo : this.state.current,
            pageSize : this.state.pageSize
        }
        this.props.fetctTrusted({formdata},()=>{})
    }
    handleRow( item,index){
        // const arraydata = this.props.all || []    //列表数组的数据
        // return arraydata.map((item, index) => {
        return(
            <tr key={index} className="contenttrust">
                <td>{item.name}</td>
                <td>{item.num}</td>
                <td>{item.trustnum} </td>
                <td>{item.bili}</td>
                <td>{item.btc}</td>
                <td>{item.time}</td>
                <td>{item.status}</td>
            </tr>)
        // })
    }
    render() {
        const TableLinks = [
            {name:"呵呵呵呵",num:"2交易次数",trustnum:"1信任人数",bili:"100%好评度",btc:"0-0.5 BTC历史交易",time:"10 min响应时间",status:"跟TA交易过1次" },
        ]

        const totalNum = 10
        return (
            <div className="">
                <ul className=" titleul">
                    <li className={` title-border ${this.state.isTrusted == 1 ? "ad-title-item active" :" ad-title-item"} `}   onClick={this.handleTrustYou}>信任您的人</li>
                    <li className={` title-border ${this.state.isTrusted == 2 ? "ad-title-item active" :" ad-title-item "}`} onClick={this.handleYouTrust}>您信任的人</li>
                    <li className={` title-border ${this.state.isTrusted == 3 ? "ad-title-item active" :" ad-title-item "}`} onClick={this.handleShielded}>被屏蔽的人</li>
                </ul>
              <div>
                  <table className=" tableborder">
                      <tbody>
                      {TableLinks.map(this.handleRow)}
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
    console.log(state.advert.all)
    return {
        all:state.advert.all
    };
}
export default connect(mapStateToProps,{fetctTrusted})(Trust);
