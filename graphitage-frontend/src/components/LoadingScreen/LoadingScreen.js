import React from "react";

import Modal from "react-bootstrap/Modal";
import Spinner from "react-bootstrap/Spinner";

function LoadingScreen(props) {

  return (
    <Modal
        show={true}
        keyboard={false}
        className="d-flex justify-content-center"
        style={{ zIndex:"19999", position:"fixed" }}
      >
        <Modal.Body>
        <Spinner animation="border" role="status" >
            <span className="sr-only">Loading...</span>
        </Spinner>
        </Modal.Body>
      </Modal>
  );
}


export default LoadingScreen