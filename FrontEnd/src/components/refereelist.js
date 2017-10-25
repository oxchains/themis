/**
 * Created by zhangxiaojing on 2017/10/24.
 */
import React,{ Component }from 'react';

class RefereeList extends Component {
    constructor(props) {
        super(props);
        this.renderrow = this.renderrow.bind(this);
    }
    renderrow(val, index) {
        return (
            <tr key={index}>
                <td >
                    <div>买方:{val.src[0]}</div>
                    <div>卖方:{val.src[1]}</div>
                </td>
                <td>{val.name}</td>
                <td>{val.info}</td>
                <td>{val.src}</td>
                <td>{val.name}</td>
                <td>{val.info}</td>
                <td>{val.src}</td>
                <td><a href="">THEMIS仲裁</a></td>
            </tr>
        )
    }
    render() {
        const imgLinks = [
            { src:["zhangxiaojing","hahahah"], name: 2, info: "haha"},
            { src:["zhangxiaojing","hahahah"], name: 2, info: "haha"},
            { src:["zhangxiaojing","hahahah"], name: 2, info: "haha"},
            { src:["zhangxiaojing","hahahah"], name: 2, info: "haha"},
            { src:["zhangxiaojing","hahahah"], name: 2, info: "haha"}
        ]
        return (
            <div className="container">
                <div className="referee-list  g-pt-50 g-pb-50">
                    <h3 className="h3 text-center g-pb-20">仲裁人消息列表</h3>
                    <div className="table-responsive">
                        <div className="table table-striped table-bordered table-hover">
                            <table className="table">
                                <thead>
                                <tr>
                                    <th>交易人</th>
                                    <th>订单编号</th>
                                    <th>类型</th>
                                    <th>交易金额</th>
                                    <th>交易数量</th>
                                    <th>创建时间</th>
                                    <th>交易状态</th>
                                    <th>仲裁操作</th>
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
            </div>
        )
    }
}
export default  RefereeList;