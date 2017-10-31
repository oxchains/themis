/**
 * Created by zhangxiaojing on 2017/10/20.
 */
import React,{ Component }from 'react';
import {connect} from 'react-redux';
import {Link} from 'react-router-dom';
import Dropzone from 'react-dropzone';
import {Alert,Modal,Button,Form,FormGroup,Col,ControlLabel,FormControl,Image} from 'react-bootstrap';
import {uploadEvidence} from '../actions/arbitrate';
import {fetchNoCompletedOrders} from '../actions/order';

class OrderInProgress extends Component {
    constructor(props) {
        super(props);
        this.state = {
            show:false,
            evidenceOFile: [],
            id:1,
            isEvidenceFileDone:false
        }
        this.renderrow = this.renderrow.bind(this);
    }
    componentWillMount() {
        const userId= localStorage.getItem('userId');
        const formData={
            userId:userId
        }
        this.props.fetchNoCompletedOrders({formData});
    }
    handleEvidence(item){
        console.log(item)
        this.setState({
            id:item,
            show:!this.state.show
        })
    }
    evidenceFile(files) {
        console.log('files', files);
        this.setState({
            evidenceOFile: files
        })
    }
    handleSubmit(){
        const evidenceDes=this.refs.voucherDes.value;
        if(evidenceDes){
            let {isEvidenceFileDone} = this.state;
            console.log(`isEvidenceFileDone ${isEvidenceFileDone}`)
            const userId= localStorage.getItem('userId');
            console.log(this.state.evidenceOFile)
            let evidenceOFile = this.state.evidenceOFile[0];
            const evidenceData={
                id:this.state.id,
                userId:userId,
                multipartFile:evidenceOFile,
                content:evidenceDes

            }
            this.props.uploadEvidence({evidenceData})
        }
    }
    renderrow(){
        const userId= localStorage.getItem('userId');
        return this.props.not_completed_orders.map((item,index) =>{
            const data = {id:item.id,userId:userId,partnerId:item.orderType == "购买"?item.sellerId:item.buyerId};
            const path = {
                pathname:'/orderprogress',
                state:data,
            }
            return(
                <tr key={index}>
                    <td>{item.friendUsername}</td>
                    <td>{item.id}</td>
                    <td>{item.orderType}</td>
                    <td>{item.money}</td>
                    <td>{item.amount}</td>
                    <td>{item.createTime}</td>
                    <td>{item.orderStatusName}</td>
                    <td><Link className="btn btn-primary" to={path} onClick={localStorage.setItem("receiverId",item.orderType == "购买" ? item.sellerId : item.buyerId)}>详情</Link></td>
                    <td>{item.orderStatus == 3 || item.orderStatus == 8 ? <button className="btn btn-primary" onClick={this.handleEvidence.bind(this,item.id)}>THEMIS仲裁</button> : <div></div>}</td>
                </tr>
                )
        })

    }
    render() {
        let close = () => {
            this.setState({show:false})
        };
        let {not_completed_orders} = this.props;
        if(this.props.not_completed_orders===null){
            return <div className="container">
                <div className="h1 text-center">Loading...</div>
            </div>
        }
        return (
        <div className="container g-pb-150">
            <div className="orderType text-center g-pt-50 g-pb-50">
                <ul className="row">
                    <li className="col-xs-6"> <a className="orderTypeBar g-pb-3" href="/orderinprogress">进行中的交易</a></li>
                    <li className="col-xs-6"><a href="/ordercompleted">已完成的交易</a></li>
                </ul>
            </div>
            <div className="table-responsive">
                <div className="table table-striped table-bordered table-hover">
                    <table className="table">
                        <thead>
                        <tr>
                            <th>交易伙伴</th>
                            <th>订单编号</th>
                            <th>类型</th>
                            <th>交易金额</th>
                            <th>交易数量</th>
                            <th>创建时间</th>
                            <th>交易状态</th>
                            <th>操作</th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        {this.props.not_completed_orders == null ? <tr><td colSpan={8}>暂无数据</td></tr> : this.renderrow()}
                        {/*{this.renderrow()}*/}
                        </tbody>
                    </table>
                </div>
            </div>
            <Modal show={this.state.show} onHide={close} container={this} aria-labelledby="contained-modal-title">
                <Modal.Header closeButton>
                    <Modal.Title id="contained-modal-title text-center">证据存根</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form horizontal>
                        <FormGroup controlId="formHorizontalPassword" >
                            <Col componentClass={ControlLabel} sm={2}>
                                上传截图
                            </Col>
                            <Col sm={10}>
                                <Dropzone onDrop={this.evidenceFile.bind(this)} className="sign-up"
                                          accept="image/png,image/gif,image/jpeg">
                                    {({isDragActive, isDragReject, acceptedFiles, rejectedFiles}) => {
                                        return (
                                            <div>
                                                <div className="col-sm-6">
                            <span className="btn btn-default"
                                  style={{color: "white", background: '#a6a5a6', marginLeft: '-15px'}}>选择文件</span>
                                                </div>
                                                <div className="col-sm-6">
                                                    <p style={{
                                                        height: '100%',
                                                        color: 'gray',
                                                        fontSize: '8px'
                                                    }}>{acceptedFiles.length > 0 ? acceptedFiles[0].name : ''}</p>
                                                </div>
                                            </div>
                                        )
                                    }}
                                </Dropzone>
                            </Col>
                            <Col sm={12}>
                                <textarea className="form-control" name="" id="" cols="30" rows="10" placeholder="请输入此次仲裁重要部分证据和备注" ref="voucherDes"></textarea>
                            </Col>
                        </FormGroup>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button onClick={this.handleSubmit.bind(this)}>确定</Button>
                    <Button onClick={close}>取消</Button>
                </Modal.Footer>
            </Modal>
        </div>
        )
    }
}
function mapStateToProps(state) {
    return {
        not_completed_orders: state.order.not_completed_orders
    };
}

export default connect(mapStateToProps, { fetchNoCompletedOrders,uploadEvidence})(OrderInProgress);