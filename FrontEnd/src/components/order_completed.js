/**
 * Created by zhangxiaojing on 2017/10/20.
 */
import React, { Component }from 'react';
import {connect} from 'react-redux';
import { Link } from 'react-router-dom';
import { Pagination } from 'antd';
import {fetchCompletedOrders} from '../actions/order';

class OrderCompleted extends Component {
    constructor(props) {
        super(props);
        this.renderrow = this.renderrow.bind(this);
        this.state={
            pageSize:8, //每页显示的条数8条
        };
    }
    componentWillMount() {
        const userIdInfo= localStorage.getItem('userId');
        const userId={
            userId:userIdInfo,
            pageNum:1,
            pageSize:this.state.pageSize, //每页显示的条数8条
        };
        this.props.fetchCompletedOrders({userId});
    }
    componentWillReceiveProps(nextProps){
    }
    handlePagination(pageNum) {
        console.log(pageNum);
        const userIdInfo= localStorage.getItem('userId');
        const userId={
            userId:userIdInfo,
            pageNum:pageNum,
            pageSize:this.state.pageSize, //每页显示的条数8条
        };
        this.props.fetchCompletedOrders({userId});
    }
    renderrow(){
        const userId=localStorage.getItem("userId");
        return this.props.completed_orders.data.map((item, index)=>{
            return(
                <tr key={index}>
                    <td><Link to={`/otherInfodetail:${item.partnerUserId}`}>{item.friendUsername}</Link></td>
                    <td>{item.id}</td>
                    <td>{item.orderType}</td>
                    <td>{item.money}</td>
                    <td>{item.amount}</td>
                    <td>{item.createTime}</td>
                    <td>{item.orderStatusName}<span>{item.arbitrate == 2 ? "(仲裁完成)": ""}</span></td>
                    <td><Link className="ant-btn ant-btn-primary ant-btn-lg" to={`/order/progress/${item.id}`}>详情</Link></td>
                </tr>
            );
        });
    }
    render() {
        const completed_orders = this.props.completed_orders;
        const totalNum = completed_orders && completed_orders.pageCount;
        console.log(totalNum);
        return (
            <div className="container g-pb-150">
                <div className="orderType text-center g-pt-50 g-pb-50">
                    <ul className="row">
                        <li className="col-xs-6"> <Link className="g-pb-3" to="/order/inprogress">进行中的交易</Link></li>
                        <li className="col-xs-6"><Link className="orderTypeBar g-pb-3" to="/order/completed">已完成的交易</Link></li>
                    </ul>
                </div>
                <div className="table-responsive">
                    <div className="table table-striped table-hover">
                        <table className="table">
                            <thead>
                            <tr>
                                <th>交易伙伴</th>
                                <th>交易编号</th>
                                <th>类型</th>
                                <th>交易金额</th>
                                <th>交易数量</th>
                                <th>创建时间</th>
                                <th>交易状态</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            { !completed_orders || totalNum == 0  ? <tr><td className="text-center h5" colSpan={8}>暂无订单</td></tr> : this.renderrow()}
                            </tbody>
                        </table>
                    </div>
                </div>
                { !completed_orders || totalNum == 0  ? '' :
                    <div className="pagecomponent">
                    <Pagination  defaultPageSize={this.state.pageSize} total={totalNum}  onChange={e => this.handlePagination(e)}/>
                </div>}

            </div>
        );
    }
}
function mapStateToProps(state) {
    return {
        completed_orders: state.order.completed_orders
    };
}

export default connect(mapStateToProps, { fetchCompletedOrders })(OrderCompleted);