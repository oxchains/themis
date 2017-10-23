/**
 * Created by oxchain on 2017/10/23.
 */

import React, { Component } from 'react';

import { Field } from 'redux-form';
import { connect } from 'react-redux';

class Buyadvert extends Component {
    constructor(props) {
        super(props);
        this.state = {}
    }

    render() {

        return (
            <div className="">
                <TabsControl >
                    <Tab name="进行中的广告">
                        <div className="buyadvert" ><Adverting/></div>
                    </Tab>
                    <Tab name="已下架的广告">
                        <div className="selladvert"><Adverted/></div>
                    </Tab>

                </TabsControl>
            </div>
        );
    }
}

let TabsControl = React.createClass({
    getInitialState: function(){
        return {currentIndex: 0}
    },
    getTitleItemCssClasses: function(index){
        return index === this.state.currentIndex ? "ad-title-item active" : "ad-title-item";
    },
    getContentItemCssClasses: function(index){
        return index === this.state.currentIndex ? "tab-content-item active" : "tab-content-item";
    },

    render: function(){
        let that = this;
        return (
            <div className="">
                <div className="">
                    <ul className=" titleul">
                        {React.Children.map(this.props.children, (element, index) => {
                            return (<li className={`title-border ${that.getTitleItemCssClasses(index)}`} onClick={() => {this.setState({currentIndex: index})}}>
                                {element.props.name}</li>)
                        })}
                    </ul>
                </div>
                <div className="">
                    {React.Children.map(this.props.children, (element, index) => {
                        return (<div className={that.getContentItemCssClasses(index)}>{element}</div>)
                    })}
                </div>
            </div>
        )
    }
});
let Tab = React.createClass({
    render: function(){
        return (<div>{this.props.children}</div>);
    }
});


let Adverting = React.createClass({

    handleRow( item,index){
        return(
            <tr key={index} className="contentborder">
                <td>{item.num}</td>
                <td>{item.type}</td>
                <td>{item.country} </td>
                <td>{item.price}</td>
                <td>{item.bili}</td>
                <td>{item.time}</td>
                <td>{item.status}</td>
            </tr>

        )
    },

    render(){
        const TableLinks = [
            {num:"1111111",type:"线下交易",country:"中国",price:"37000 CNY",bili:"1%",time:"2017-9-20",status:"已完成" },
        ]
        return (<div>
            <table className=" tableborder">
                <tbody>
                <tr className="contentborder">
                    <th>编号</th>
                    <th>广告类型</th>
                    <th>国家</th>
                    <th>价格</th>
                    <th>溢价比例</th>
                    <th>创建时间</th>
                    <th>状态</th>
                </tr>
                {TableLinks.map(this.handleRow)}
                <tr className="contentborder bottomcontent">
                    <td> 没有更多内容了</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                </tbody>

            </table>
        </div>);
    }
});

let Adverted = React.createClass({

    handleRow( item,index){
        return(
            <tr key={index} className="contentborder">
                <td>{item.num}</td>
                <td>{item.type}</td>
                <td>{item.country} </td>
                <td>{item.price}</td>
                <td>{item.bili}</td>
                <td>{item.time}</td>
                <td>{item.status}</td>
            </tr>

        )
    },

    render(){
        const TableLinks = [
            {num:"2222222",type:"线下交易",country:"中国",price:"37000 CNY",bili:"1%",time:"2017-9-20",status:"已完成" },
        ]
        return (<div>
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
                </tr>
                {TableLinks.map(this.handleRow)}
                <tr className="contentborder bottomcontent">
                    <td> 没有更多内容了</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                </tbody>

            </table>
        </div>);
    }
});



function mapStateToProps(state) {
    return {
        success: state.auth.authenticated,
        errorMessage: state.auth.error
    };
}
export default connect(mapStateToProps,{})(Buyadvert);
