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
            status:1,
            adstatus:1,
            pageSize:5, //每页显示的条数5条
            pageNum: 1,//默认的当前第一页
        }
        this.handleRowsbuy = this.handleRowsbuy.bind(this)
        this.handleRowssell = this.handleRowssell.bind(this)
        this.handleRowsadverting = this.handleRowsadverting.bind(this)
        this.handleRowadverted = this.handleRowadverted.bind(this)
        this.handleRow = this.handleRow.bind(this)
    }

    onPagination(pageNo) {
        console.log( "当前页数"+ pageNo)
        this.state.pageNum = pageNo

        const userId = localStorage.getItem("userId")
        const noticeType = this.state.status
        const txStatus = this.state.adstatus
        const pageNum = this.state.pageNum
        this.props.fetctMyAdvert({userId,noticeType,txStatus,pageNum}, ()=>{});
    }

    handleRowsbuy(){
        this.state.status = 1
        const userId = localStorage.getItem("userId")
        const noticeType = this.state.status
        const txStatus = this.state.adstatus
        const pageNum = this.state.pageNum
        this.props.fetctMyAdvert({userId,noticeType,txStatus,pageNum},()=>{});
    }
    handleRowssell(){
        this.state.status = 2
        const userId = localStorage.getItem("userId")
        const noticeType = this.state.status
        const txStatus = this.state.adstatus
        const pageNum = this.state.pageNum
        this.props.fetctMyAdvert({userId,noticeType,txStatus,pageNum},()=>{});
    }

    handleRowsadverting(){
        this.state.adstatus = 1
        const userId = localStorage.getItem("userId")
        const noticeType = this.state.status
        const txStatus = this.state.adstatus
        const pageNum = this.state.pageNum
        this.props.fetctMyAdvert({userId,noticeType,txStatus,pageNum},()=>{});
    }

    handleRowadverted(){
        this.state.adstatus = 2
        const userId = localStorage.getItem("userId")
        const noticeType = this.state.status
        const txStatus = this.state.adstatus
        const pageNum = this.state.pageNum
        this.props.fetctMyAdvert({userId,noticeType,txStatus,pageNum},()=>{});
    }
    componentWillMount(){
        const userId = localStorage.getItem("userId")
        const noticeType = this.state.status
        const txStatus = this.state.adstatus
        const pageNum = this.state.pageNum
        this.props.fetctMyAdvert({userId,noticeType,txStatus,pageNum},()=>{});
    }

    handleRow(){
        const arraydata = this.props.all.pageList || []    //列表数组的数据
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
        const {id} = item
        this.props.fetctOffMyAd({id},err=>{
            this.setState({ isModalOpen: true , error: err , actionResult: err||'下架成功!'})
        })
    }
    render() {
        const totalNum = this.props.all.rowCount
            return (
                    <div className="mainbar">
                        <div className="col-lg-2 col-md-2 col-xs-2">
                        <ul className=" adtypeul">
                            <li className={` adtype ${this.state.status == 1 ? "tab-title-item active" :" tab-title-item"} `}   onClick={this.handleRowsbuy}><p>购买广告</p></li>
                            <li className={` adtype ${this.state.status == 2 ? "tab-title-item active" :" tab-title-item "}`} onClick={this.handleRowssell}><p>出售广告</p></li>
                        </ul>
                        </div>
                        <div className="col-lg-10 col-md-10 col-xs-10">
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
                                    <a className="close-modal" href="" >关闭</a>
                                </button>
                            </ModalFooter>
                        </Modal>


                </div>
            );
        }
    }
function mapStateToProps(state) {
    return {
        all:state.advert.all,     //我的广告
        data:state.advert.data   // 下架我的广告
    };
}
export default connect(mapStateToProps,{  fetctMyAdvert,fetctOffMyAd})(Myadvert);

