/**
 * Created by oxchain on 2017/12/04.
 */


import React, { Component } from 'react';

class JumpTip extends Component {

    render() {
        const tip = localStorage.getItem("tip");
        const message = localStorage.getItem("message");
        const tipstatus = localStorage.getItem("tipstatus");
        return (
            <div className="jumptip">
                <div className="text-center">
                    <h1>{tip}</h1>
                </div>
            </div>);
    }
}

export default JumpTip;