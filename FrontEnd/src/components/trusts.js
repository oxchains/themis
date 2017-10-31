
/**
 * Created by oxchain on 2017/10/20.
 */
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { fetctTrusted } from '../actions/releaseadvert'
class Trust extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isTrusted:"1"
        }
        this.handleTrustYou = this.handleTrustYou.bind(this)
        this.handleYouTrust = this.handleYouTrust.bind(this)
        this.handleShielded = this.handleShielded.bind(this)
    }
    componentWillMount() {
       // this.props.fetctTrusted({})
    }

    handleTrustYou(){
        this.state.isTrusted = 1
        const isTrusted = this.state.isTrusted
        // this.props.fetctTrusted({})
    }
    handleYouTrust(){
        this.state.isTrusted = 2
        const isTrusted = this.state.isTrusted
        // this.props.fetctTrusted({})
    }
    handleShielded(){
        this.state.isTrusted = 3
        const isTrusted = this.state.isTrusted
        // this.props.fetctTrusted({})
    }
    handleRow( item,index){
        // const arraydata = this.props.all || []    //列表数组的数据
        // return arraydata.map((item, index) => {
        return(
            <tr key={index} className="contenttrust">
                <td>{item.name}</td>
                <td>{item.num}</td>
                <td>{item.trustnum} </td>
                <td>{item.bili}</td>
                <td>{item.btc}</td>
                <td>{item.time}</td>
                <td>{item.status}</td>
            </tr>)
        // })
    }
    render() {
        const TableLinks = [
            {name:"呵呵呵呵",num:"2交易次数",trustnum:"1信任人数",bili:"100%好评度",btc:"0-0.5 BTC历史交易",time:"10 min响应时间",status:"跟TA交易过1次" },
        ]
        return (
            <div className="">
                <ul className=" titleul">
                    <li className={` title-border ${this.state.isTrusted == 1 ? "ad-title-item active" :" ad-title-item"} `}   onClick={this.handleTrustYou}>信任您的人</li>
                    <li className={` title-border ${this.state.isTrusted == 2 ? "ad-title-item active" :" ad-title-item "}`} onClick={this.handleYouTrust}>您信任的人</li>
                    <li className={` title-border ${this.state.isTrusted == 3 ? "ad-title-item active" :" ad-title-item "}`} onClick={this.handleShielded}>被屏蔽的人</li>
                </ul>
              <div>
                  <table className=" tableborder">
                      <tbody>
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
        // all:state.advert.all
    };
}
export default connect(mapStateToProps,{fetctTrusted})(Trust);
