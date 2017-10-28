/**
 * Created by zhangxiaojing on 2017/10/23.
 */

import React, { Component } from 'react';
import { connect } from 'react-redux';
import $ from 'jquery';

class Chat extends Component{

    componentDidMount(){
        const token=localStorage.getItem("token"); //token
        // ws = new WebSocket("ws://192.168.1.121:9999/ws?"+token+"_"+receiverId); //链接websocket
        //发送消息
        function sendMessage(senderName,chatContent){
            $(".chat-message").append('<li class="send-message rightd"><div class="sender rightd_h"><span>'+senderName+'</span></div><div class="content speech right">'+chatContent+'</div></li>');
        }
        $(".send").on("click", function (){
            //发送一个文本消息
            var chatContent = $(".message").val();
            // if(chatContent){
            //     var message = JSON.stringify({msgType:1, senderId: senderId, senderName: senderName, receiverId: receiverId, chatContent: chatContent});
                sendMessage("wo", chatContent);
            //     ws.send(message);
                $(".message").val('');
            //     scrollTop();
            //     if(timeFlag){
            //         showTime();
            //         scrollTop();
            //         timeFlag = false;
            //         time=setTimeout(function(){
            //             timeFlag=true;
            //         },60000)
            //     }
            // }

        })
    }
    render() {
        return (
            <div className="chat">
                <div className="chat-head col-sm-12 h5 text-center"></div>
                <div className="chat-body g-mb-10">
                    <ul className="chat-message clearfix">
                        <li className="text-center"><a href="javascript:(0);" className="gray g-pt-10 g-pb-10 getMore">获取更多聊天记录</a></li>
                    </ul>
                </div>
                <div className="clearfix">
                    <input type="text" className="message"/>
                    <button className="btn btn-primary send float-right">发送</button>
                </div>
            </div>
        );
    }
}

export default Chat;
