/**
 * Created by oxchain on 2017/10/20.
 */
/**
 * Created by oxchain on 2017/10/20.
 */
import React, { Component } from 'react';

import { Field } from 'redux-form';
import { connect } from 'react-redux';
import { signinAction } from '../actions/auth'

class Buybtc extends Component {
    constructor(props) {
        super(props);
        this.state = {
            currentIndex:0
        }
        this.handleRow = this.handleRow.bind(this)
    }
    handleFormSubmit(){
        this.props.signinAction({  },()=>{});
    }
    handleRow( item,index){
        return(
            <tr key={index} className="contentborder">
                <td className="tabletitle">{item.name}</td>
                <td className="tabletitle">交易 {item.num}| 好评度 {item.bili} | 信任 {item.trust}</td>
                <td className="tabletitle"> {item.payway} </td>
                <td className="tabletitle"> {item.money} CNY</td>
                <td className="tabletitle">{item.jiage}</td>
                <td className="tabletitle "><button className="tablebuy">购买</button></td>
            </tr>

        )
    }
    render() {
        const TableLinks = [
            {name:"liuruichao",num:"1",bili:"100%",trust:"1",payway:"支付宝",money:"1000-1000",jiage:"323232.88 CNY" },
            {name:"liuruichao",num:"1",bili:"100%",trust:"1",payway:"支付宝",money:"1000-1000",jiage:"323232.88 CNY" },
            {name:"liuruichao",num:"1",bili:"100%",trust:"1",payway:"支付宝",money:"1000-1000",jiage:"323232.88 CNY" },
            {name:"liuruichao",num:"1",bili:"100%",trust:"1",payway:"支付宝",money:"1000-1000",jiage:"323232.88 CNY" },
        ]

        return (
            <div className="mainbuy">
             <div>
                 <select className="titleslect">
                     <option value="0">搜用户&nbsp; ></option>
                     <option value="1">搜广告 ></option>
                 </select>
                 <select className="titleslect">
                     <option value="1">中国 </option>
                     <option value="2">日本 </option>
                     <option value="1">美国 </option>
                     <option value="2">韩国 </option>
                 </select>
                 <select className="titleslect">
                     <option value="1">人民币 </option>
                     <option value="2">美元 </option>
                     <option value="1">韩元 </option>
                     <option value="2">日元 </option>
                 </select>
                 <select className="titleslect">
                     <option value="1">支付方式 </option>
                     <option value="2">微信支付 </option>
                     <option value="1">Apple pay </option>
                     <option value="2">银联 </option>
                 </select>
                 <button type="submit" className="  form-seach">搜索</button>
             </div>
                <div>
                    <table className="tableborder">
                        <tbody>
                        <tr className="titlemargin">
                            <th className="tabletitle">昵称</th>
                            <th className="tabletitle">信用</th>
                            <th className="tabletitle">付款方式</th>
                            <th className="tabletitle">限额</th>
                            <th className="tabletitle">价格</th>
                        </tr>

                      {TableLinks.map(this.handleRow)}
                      </tbody>

                    </table>
                </div>


            </div>
        );
    }
}



function mapStateToProps(state) {
    return {
        success: state.auth.authenticated,
        errorMessage: state.auth.error
    };
}
export default connect(mapStateToProps,{signinAction})(Buybtc);
