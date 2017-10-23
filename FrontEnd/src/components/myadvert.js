/**
 * Created by oxchain on 2017/10/23.
 */
import React, { Component } from 'react';

import Buyadvert  from  './buyadvert'
import Selladvert  from  './selladvert'

class Myadvert extends Component {
    constructor(props) {
        super(props);
        this.state = {}

    }
    render() {
        return (
            <div >
                <TabsControl >
                    <Tab name="购买广告">
                        <div className="buyadvert" ><Buyadvert/></div>
                    </Tab>
                    <Tab name="出售广告">
                        <div className="selladvert"><Selladvert/></div>
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
                <div className="col-lg-3 col-md-3 col-xs-3">
                    <ul className="sildbar ">
                        {React.Children.map(this.props.children, (element, index) => {
                            return (<li className={`liheight ${that.getTitleItemCssClasses(index)}`} onClick={() => {this.setState({currentIndex: index})}}>
                                {element.props.name}</li>)
                        })}
                    </ul>
                </div>
                <div className="col-lg-9 col-md-9 col-xs-9">
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
export default Myadvert