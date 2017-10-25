/**
 * Created by oxchain on 2017/10/20.
 */

import React, { Component } from 'react';

import { Field } from 'redux-form';
import { connect } from 'react-redux';

class Baseinfo extends Component {
    constructor(props) {
        super(props);
        this.state = {}
    }

    render() {

        return (
            <div>
                <h1>主页</h1>
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
export default connect(mapStateToProps,{})(Baseinfo);
