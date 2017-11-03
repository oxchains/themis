/**
 * Created by zhangxiaojing on 2017/10/20.
 */
import React,{ Component }from 'react';
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
        }
    }
    componentWillMount() {
        const userIdInfo= localStorage.getItem('userId');
        const userId={
            userId:userIdInfo,
            pageNum:1,
            pageSize:this.state.pageSize, //每页显示的条数8条
        }
        this.props.fetchCompletedOrders({userId});
    }
    onPagination(pageNum) {
        const userIdInfo= localStorage.getItem('userId');
        const userId={
            userId:userIdInfo,
            pageNum:pageNum,
            pageSize:this.state.pageSize, //每页显示的条数8条
        }
        this.props.fetchCompletedOrders({userId});
    }
    renderrow(){
        const userId=localStorage.getItem("userId");
        return this.props.completed_orders.map((item,index)=>{
            const data = {id:item.id,userId:userId,partnerId:item.orderType == "购买"?item.sellerId:item.buyerId};
            const path = {
                pathname:'/orderprogress',
                state:data,
            }
            return(
                <tr key={index}>
                    <td>{item.friendUsername}</td>
                    <td>{item.id}</td>
                    <td>{item.orderType}</td>
                    <td>{item.money}</td>
                    <td>{item.amount}</td>
                    <td>{item.createTime}</td>
                    <td>{item.orderStatusName}</td>
                    <td><Link className="ant-btn ant-btn-primary ant-btn-lg" to={path}>详情</Link></td>
                    {/*<td><Link className="btn btn-primary" to="/arbitrationbuyer">THEMIS仲裁</Link></td>*/}
                </tr>
            )
        })
    }
    render() {
        const completed_orders = this.props.completed_orders;
        const totalNum = completed_orders && completed_orders[0].pageCount
        console.log(totalNum)
        console.log(this.props.completed_orders)
        if(this.props.completed_orders===null){
            return(
                <div className="container">
                    <div className="h1 text-center">Loading...</div>
                </div>
            )
        }
        return (
            <div className="container">
                <div className="orderType text-center g-pt-50 g-pb-50">
                    <ul className="row">
                        <li className="col-xs-6"> <a className="g-pb-3" href="/orderinprogress">进行中的交易</a></li>
                        <li className="col-xs-6"><a className="orderTypeBar g-pb-3" href="/ordercompleted">已完成的交易</a></li>
                    </ul>
                </div>
                <div className="table-responsive">
                    <div className="table table-striped table-bordered table-hover">
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
                                {/*<th></th>*/}
                            </tr>
                            </thead>
                            <tbody>
                            {this.props.completed_orders == null ? <tr><td colSpan={8}>暂无数据</td></tr> : this.renderrow()}
                            {/*{this.renderrow()}*/}
                            </tbody>
                        </table>
                    </div>
                    <div className="pagecomponent">
                        <Pagination  defaultPageSize={this.state.pageSize} total={totalNum}  onChange={e => this.onPagination(e)}/>
                    </div>
                </div>
            </div>
        )
    }
}
function mapStateToProps(state) {
    return {
        completed_orders: state.order.completed_orders
    };
}

export default connect(mapStateToProps, { fetchCompletedOrders })(OrderCompleted);