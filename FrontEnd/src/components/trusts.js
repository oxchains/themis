
/**
 * Created by oxchain on 2017/10/20.
 */
import React, { Component } from 'react';

import { Field } from 'redux-form';
import { connect } from 'react-redux';

class Trust extends Component {
    constructor(props) {
        super(props);
        this.state = {}
    }

    render() {

        return (
            <div className="">
                <TabsControl >
                    <Tab name="信任您的人">
                        <div className="buyadvert" ><TRUSTED/></div>
                    </Tab>
                    <Tab name="您信任的人">
                        <div className="selladvert"><TRUSTYOU/></div>
                    </Tab>
                    <Tab name="被屏蔽的人">
                        <div className="selladvert"><SHIELD/></div>
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


let TRUSTED = React.createClass({

    handleRow( item,index){
        return(
            <tr key={index} className="contenttrust">
                <td>{item.name}</td>
                <td>{item.num}</td>
                <td>{item.trustnum} </td>
                <td>{item.bili}</td>
                <td>{item.btc}</td>
                <td>{item.time}</td>
                <td>{item.status}</td>
            </tr>

        )
    },

    render(){
        const TableLinks = [
            {name:"呵呵呵呵",num:"2交易次数",trustnum:"1信任人数",bili:"100%好评度",btc:"0-0.5 BTC历史交易",time:"10 min响应时间",status:"跟TA交易过1次" },
        ]
        return (<div>
            <table className=" tableborder">
                <tbody>
                {TableLinks.map(this.handleRow)}
                </tbody>

            </table>
        </div>);
    }
});

let TRUSTYOU = React.createClass({

    handleRow( item,index){
        return(
            <tr key={index} className="contenttrust">
                <td>{item.name}</td>
                <td>{item.num}</td>
                <td>{item.trustnum} </td>
                <td>{item.bili}</td>
                <td>{item.btc}</td>
                <td>{item.time}</td>
                <td>{item.status}</td>
            </tr>

        )
    },

    render(){
        const TableLinks = [
            {name:"啦啦啦啦",num:"2交易次数",trustnum:"1信任人数",bili:"100%好评度",btc:"0-0.5 BTC历史交易",time:"10 min响应时间",status:"跟TA交易过1次" },
        ]
        return (<div>
            <table className="tableborder">
                <tbody>
                {TableLinks.map(this.handleRow)}
                </tbody>

            </table>
        </div>);
    }
});
let SHIELD = React.createClass({

    handleRow( item,index){
        return(
            <tr key={index} className="contenttrust">
                <td>{item.name}</td>
                <td>{item.num}</td>
                <td>{item.trustnum} </td>
                <td>{item.bili}</td>
                <td>{item.btc}</td>
                <td>{item.time}</td>
                <td>{item.status}</td>
            </tr>

        )
    },

    render(){
        const TableLinks = [
            {name:"哈哈哈哈",num:"2交易次数",trustnum:"1信任人数",bili:"100%好评度",btc:"0-0.5 BTC历史交易",time:"10 min响应时间",status:"跟TA交易过1次" },
        ]
        return (<div>
            <table className="tableborder">
                <tbody>
                {TableLinks.map(this.handleRow)}
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
export default connect(mapStateToProps,{})(Trust);
