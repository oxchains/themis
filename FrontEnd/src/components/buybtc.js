/**
 * Created by oxchain on 2017/10/20.
 */
/**
 * Created by oxchain on 2017/10/20.
 */
import React, { Component } from 'react';
import { Select } from 'antd';
import 'antd/dist/antd.css';
import { Field } from 'redux-form';
import { connect } from 'react-redux';
import { fetctBuyBTC ,fetcAdvertSeach} from '../actions/releaseadvert'
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
            pageSize:8, //每页显示的条数5条
            goValue:'',
            totalPage:'',//总页数
        }
        this.handleRow = this.handleRow.bind(this)
        this.handleSeach = this.handleSeach.bind(this)
        this.handleChange = this.handleChange.bind(this)
        this.handleChangeCounty = this.handleChangeCounty.bind(this)
        this.handleChangeMoney = this.handleChangeMoney.bind(this)
        this.componentWillMount = this.componentWillMount.bind(this)

    }

    componentWillMount(){
        const userId= localStorage.getItem('userId');
        this.props.fetctBuyBTC({}, ()=>{});


        // this.setState({totalData:this.props.all})
        // this.setState({totalNum:this.props.all.rowCount})
        // let totalPage =Math.ceil( this.state.totalNum / this.state.pageSize);
        // this.setState({totalPage:totalPage})
        // this.pageClick(1);
    }




    handleChange(value){
        console.log(`selected1 ${value}`);
        localStorage.setItem("seachuser",value)
    }
    handleChangeCounty(value){
        console.log(`selected2 ${value}`);
    }
    handleChangeMoney(value){
        console.log(`selected3 ${value}`);
    }
    handleChangeWay(value){
        console.log(`selected4 ${value}`);
    }

    handleSeach(){
        const seachuser = localStorage.getItem("seachuser")
        this.props.fetcAdvertSeach({}, ()=>{});
    }



    handleRow( ){
        const arraydata = this.props.all.pageList || []
        return arraydata.map((item, index) => {
        return(
            <tr key={index} className="contentborder">
                <td className="tabletitle">{item.loginname}</td>
                <td className="tabletitle">交易 {item.txStatus} | 好评度 {item.id} | 信任 {item.trustNum}</td>
                <td className="tabletitle"> {item.payType} </td>
                <td className="tabletitle"> {item.minTxLimit} - {item.maxTxLimit} CNY</td>
                <td className="tabletitle">{item.price}</td>
                <td className="tabletitle ">
                    <button className="tablebuy" ><a href={`/buydetail:${item.id}`}>购买</a></button>
                </td>
            </tr>

        )
    })
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
        const Option = Select.Option;
        return (
            <div className="mainbuy">
             <div className="slece-style">
                 <Select defaultValue="搜用户" style={{ width: 120 }}  onChange={this.handleChange}>
                     <Option value="0">搜用户&nbsp; ></Option>
                     <Option value="1">搜广告 ></Option>
                 </Select>
                 <Select defaultValue="中国" style={{ width: 120 }}  onChange={this.handleChangeCounty}>
                     <Option value="1">中国 </Option>
                     <Option value="2">日本 </Option>
                     <Option value="3">美国 </Option>
                     <Option value="4">韩国 </Option>
                 </Select>
                 <Select defaultValue="美元" style={{ width: 120 }}  onChange={this.handleChangeMoney}>
                     <Option value="1">人民币 </Option>
                     <Option value="2">美元 </Option>
                     <Option value="3">韩元 </Option>
                     <Option value="4">日元 </Option>
                 </Select>
                 <Select defaultValue="微信支付" style={{ width: 120 }}  onChange={this.handleChangeWay}>
                     <Option value="1">支付方式 </Option>
                     <Option value="2">微信支付 </Option>
                     <Option value="3">Apple pay </Option>
                     <Option value="4">银联 </Option>
                 </Select>
                 <button type="submit" className="form-seach" onClick={this.handleSeach}>搜索</button>
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

                      {this.handleRow()}
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
    console.log(state)
    return {
        all:state.advert.all,
        success: state.auth.authenticated,
        errorMessage: state.auth.error
    };
}
export default connect(mapStateToProps,{fetctBuyBTC,fetcAdvertSeach})(Buybtc);
