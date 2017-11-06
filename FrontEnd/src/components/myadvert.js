/**
 * Created by oxchain on 2017/10/23.
 */
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Pagination } from 'antd';
import 'antd/dist/antd.css';
import {fetctMyAdvert,fetctOffMyAd} from '../actions/releaseadvert'
import {
    Modal,
    ModalHeader,
    ModalTitle,
    ModalClose,
    ModalBody,
    ModalFooter
} from 'react-modal-bootstrap';

class Myadvert extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isModalOpen: false,
            error: null,
            actionResult: '',
            status:'1',
            adstatus:'1',
            pageSize:8, //每页显示的条数8条
            pageNo: 1,//默认的当前第一页
        }
        this.handleRowsbuy = this.handleRowsbuy.bind(this)
        this.handleRowssell = this.handleRowssell.bind(this)
        this.handleRowsadverting = this.handleRowsadverting.bind(this)
        this.handleRowadverted = this.handleRowadverted.bind(this)
        this.handleRow = this.handleRow.bind(this)
    }
    componentWillMount(){
        const userId = localStorage.getItem("userId")
        const noticeType = this.state.status
        const txStatus = this.state.adstatus
        const pageNo = this.state.pageNo
        this.props.fetctMyAdvert({userId,noticeType,txStatus,pageNo},()=>{});
    }
    onPagination(pageNum) {
        console.log( "当前页数"+ pageNum)
        this.state.pageNo = pageNum
        const userId = localStorage.getItem("userId")
        const noticeType = this.state.status
        const txStatus = this.state.adstatus
        const pageNo = this.state.pageNo
        this.props.fetctMyAdvert({userId,noticeType,txStatus,pageNo}, ()=>{});
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
                <td>{item.noticeType == 1?"购买" : "出售"}</td>
                <td>{item.location == 1 ? "中国" : item.location == 2 ?"美国" :""} </td>
                <td>{item.price}</td>
                <td>{item.premium}</td>
                <td>{item.createTime}</td>
                <td>{item.txStatus ==0 ?"未交易" :item.txStatus == 1?"交易中" :item.txStatus ==2?"已完成":""}</td>
                <td className="tabletitle">
                    <button className={`tablebuy ${item.txStatus == 2 ? "hidden":""}`} onClick={() => this.handleOff(item)}>下架</button>
                </td>
            </tr>)
        })
    }
    hideModal = () => {
        this.setState({
            isModalOpen: false
        });
    };

    handleOff = (item) =>{
        // const id = item.id

        const {id} = item
        this.props.fetctOffMyAd({id},err=>{
            this.setState({ isModalOpen: true , error: err , actionResult: err||'下架成功!'})
        })
    }
    render() {
        const totalNum = this.props.all.length
            return (
                    <div className="mainbar">
                        <div className="col-lg-3 col-md-3 col-xs-3">
                        <ul className=" adtypeul">
                            <li className={` adtype ${this.state.status == 1 ? "tab-way-item active" :" tab-way-item"} `}   onClick={this.handleRowsbuy}>购买广告</li>
                            <li className={` adtype ${this.state.status == 2 ? "tab-way-item active" :" tab-way-item "}`} onClick={this.handleRowssell}>出售广告</li>
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
                                        <th className={`${this.state.adstatus == 1 ? "hidden" :""}`}></th>
                                        <th className={`${this.state.adstatus == 2 ? "hidden" :""}`}>操作</th>
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
                                        <td></td>
                                        {/*<td className={`${this.state.adstatus == 2 ? "hidden" :""}`}></td>*/}
                                    </tr>
                                    </tbody>

                                </table>
                                <div className="pagecomponent">
                                    <Pagination  defaultPageSize={this.state.pageSize} total={totalNum}  onChange={e => this.onPagination(e)}/>
                                </div>
                            </div>
                        </div>



                        <Modal isOpen={this.state.isModalOpen} onRequestHide={this.hideModal}>
                            <ModalHeader>
                                <ModalClose onClick={this.hideModal}/>
                                <ModalTitle>提示:</ModalTitle>
                            </ModalHeader>
                            <ModalBody>
                                <p className={this.state.error?'text-red':'text-green'}>
                                    {this.state.actionResult}
                                </p>
                            </ModalBody>
                            <ModalFooter>
                                <button className='btn btn-default' onClick={this.hideModal}>
                                    {/*<a href="/myadvert" >关闭</a>*/}
                                    <a className="close-modal" href="" >关闭</a>
                                </button>
                            </ModalFooter>
                        </Modal>


                </div>
            );
        }
    }
function mapStateToProps(state) {
    console.log(state.advert.all.length)
    return {
        all:state.advert.all,     //我的广告
        data:state.advert.data   // 下架我的广告
    };
}
export default connect(mapStateToProps,{  fetctMyAdvert,fetctOffMyAd})(Myadvert);

