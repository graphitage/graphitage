import React from "react";
import { connect } from 'react-redux';

import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";

import * as actionCreators from '../../store/actions/index';

function ErrorModal(props) {

  return (
    <Modal 
      show={true} 
      onHide= {props.onCloseErrorModal}
      animation={false}
      style= {{position: 'absolute', left: '50%', top: '10%', transform: 'translate(-50%, 0)', zIndex:'9999'}}
     >
      <Modal.Header closeButton>
        <Modal.Title>Something went wrong!</Modal.Title>
      </Modal.Header>
      <Modal.Body>{props.message}</Modal.Body>
      <Modal.Footer className="d-flex justify-content-center">
        <Button variant="secondary" onClick={props.onCloseErrorModal}>
          Close
        </Button>
      </Modal.Footer>
    </Modal>
  );
}


const mapStateToProps = state => {
    return {
        message: state.ui.errorModal.errorMessage
    }
}

const mapDispatchToProps = dispatch => {
    return {
        onCloseErrorModal: () => dispatch(actionCreators.closeErrorModal())
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(ErrorModal); 