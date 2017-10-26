/**
 * Created by oxchain on 2017/10/20.
 */
/**
 * Created by oxchain on 2017/10/20.
 */
import React, { Component } from 'react';

import { Field } from 'redux-form';
import { connect } from 'react-redux';
import { fetctBuyBTC ,fetctBuySeach} from '../actions/releaseadvert'
import PageComponent from './pageComponent';
class Buybtc extends Component {
    constructor(props) {
        super(props);
        this.state = {
            currentIndex:0,
            indexList : [], //获取数据的存放数组
            totalNum:'',//总记录数
            totalData:{},
            current: 1, //当前页码
            pageSize:5, //每页显示的条数5条
            goValue:'',
            totalPage:'',//总页数
        }
        this.handleRow = this.handleRow.bind(this)
    }

    componentWillMount(){
        this.props.fetctBuyBTC({}, ()=>{});
    }
    handleSeach(){
        this.props.fetctBuySeach({}, ()=>{});
    }

    handleFormSubmit(){
        // this.props.signinAction({  },()=>{});
    }
    handleRow( item,index){
        return(
            <tr key={index} className="contentborder">
                <td className="tabletitle">{item.name}</td>
                <td className="tabletitle">交易 {item.num} | 好评度 {item.bili} | 信任 {item.trust}</td>
                <td className="tabletitle"> {item.payway} </td>
                <td className="tabletitle"> {item.money} CNY</td>
                <td className="tabletitle">{item.jiage}</td>
                <td className="tabletitle ">
                    <button className="tablebuy" ><a href="/buydetail">购买</a></button>
                </td>
            </tr>

        )
    }
//点击翻页
    pageClick(pageNum){
        let _this = this;
        if(pageNum != _this.state.current){
            _this.state.current = pageNum
        }
        _this.state.indexList=[];//清空之前的数据
        for(var i = (pageNum - 1) * _this.state.pageSize; i< _this.state.pageSize * pageNum; i++){
            if(_this.state.totalData.array[i]){
                _this.state.indexList.push(_this.state.totalData.array[i])
            }
        }
        _this.setState({indexList:_this.state.indexList})
        //console.log(_this.state.indexList)
    }
    //上一步
    goPrevClick(){
        var _this = this;
        let cur = this.state.current;
        if(cur > 1){
            _this.pageClick( cur - 1);
        }
    }
    //下一步
    goNext(){
        var _this = this;
        let cur = _this.state.current;
        //alert(cur+"==="+_this.state.totalPage)
        if(cur < _this.state.totalPage){
            _this.pageClick(cur + 1);
        }
    }
    //跳转到指定页
    goSwitchChange(e){
        var _this= this;
        _this.setState({goValue : e.target.value})
        var value = e.target.value;
        //alert(value+"==="+_this.state.totalPage)
        if(!/^[1-9]\d*$/.test(value)){
            alert('页码只能输入大于1的正整数');
        }else if(parseInt(value) > parseInt(_this.state.totalPage)){
            alert('没有这么多页');
        }else{
            _this.pageClick(value);
        }
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
             <div className="">
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
                 <button type="submit" className="  form-seach" onClick={this.handleSeach}>搜索</button>
             </div>
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
              <div className="pagecomponent">
                  <PageComponent  total={this.state.totalNum}
                                  current={this.state.current}
                                  totalPage={this.state.totalPage}
                                  goValue={this.state.goValue}
                                  pageClick={this.pageClick.bind(this)}
                                  goPrev={this.goPrevClick.bind(this)}
                                  goNext={this.goNext.bind(this)}
                                  switchChange={this.goSwitchChange.bind(this)}/>
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
export default connect(mapStateToProps,{fetctBuyBTC,fetctBuySeach})(Buybtc);
