/**
 * Created by zhangxiaojing on 2017/10/20.
 */
import React,{ Component }from 'react';

class OrderCompleted extends Component {
    constructor(props) {
        super(props);
        this.renderrow = this.renderrow.bind(this);
    }

    renderrow(val, index) {
        return (
            <tr key={index}>
                <td>{val.src}</td>
                <td>{val.name}</td>
                <td>{val.info}</td>
                <td>{val.src}</td>
                <td>{val.name}</td>
                <td>{val.info}</td>
                <td>{val.src}</td>
                <td><a className="btn btn-primary" href="/orderprogress">详情</a></td>
                <td><a href="/arbitrationbuyer">THEMIS仲裁</a></td>
            </tr>
        )
    }
    render() {
        const imgLinks = [
            { src: 2, name: 2, info: "haha"},
            { src: 2, name: 2, info: "haha"},
            { src: 1, name: 2, info: "haha"},
            { src: 2, name: 2, info: "haha"},
            { src: 4, name: 2, info: "haha"},
        ]
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
                                <th></th>
                            </tr>
                            </thead>
                            <tbody>
                            {imgLinks.map(this.renderrow)}
                            </tbody>
                        </table>
                    </div>
                </div>
                <div className="text-center">没有更多内容了</div>
            </div>
        )
    }
}
export default  OrderCompleted;