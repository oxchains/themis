/**
 * Created by oxchain on 2017/10/20.
 */
import React, { Component } from 'react';

import Base  from  './baseinfo'
import Safe  from  './safeset'
import Trust from './trusts'
class Usercenter extends Component {
    constructor(props) {
        super(props);
        this.state = {}

    }
    render() {
        return (
           <div >
               <TabsControl >
                   <Tab name="基本信息">
                       <div className="baswinfo" ><Base/></div>
                   </Tab>
                   <Tab name="安全设置">
                       <div className="safeset"><Safe/></div>
                   </Tab>
                   <Tab name="受信任的">
                       <div className="trust"><Trust/></div>
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
        return index === this.state.currentIndex ? "tab-title-item active" : "tab-title-item";
    },
    getContentItemCssClasses: function(index){
        return index === this.state.currentIndex ? "tab-content-item active" : "tab-content-item";
    },

    render: function(){
        let that = this;
        return (
            <div className="mainbar">
               <div className="col-lg-4">
                   <ul className="sildbar ">
                       {React.Children.map(this.props.children, (element, index) => {
                           return (<li className={`liheight ${that.getTitleItemCssClasses(index)}`} onClick={() => {this.setState({currentIndex: index})}}>
                               {element.props.name}</li>)
                       })}
                   </ul>
               </div>
                <div className="col-lg-8 ">
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
export default Usercenter