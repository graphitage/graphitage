import React, { useState, useCallback } from "react";
import { connect } from "react-redux";

import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";
import ListGroup from "react-bootstrap/ListGroup";
import Form from "react-bootstrap/Form";

import httpReq from "../../store/actions/utils/http";

import * as actionCreators from "../../store/actions/index";

function AddRelatedPapersModal(props) {
  const [checkedElements, setCheckedElements] = useState([]);

  const clickHandler = useCallback(
    (e, obj) => {
      const clone = JSON.parse(JSON.stringify(checkedElements));
      if (e.target.checked) {
        clone.push({ ...obj });
        setCheckedElements(clone);
      } else {
        setCheckedElements(
          clone.filter((cObj) => cObj.paperId !== obj.paperId)
        );
      }
    },
    [checkedElements]
  );

  const {
    onOpenLoadingScreen,
    onCloseLoadingScreen,
    sourcePaperId,
    onClose,
    onOptionsClosed,
    onSimpleExpand,
  } = props;
  const addPapersHandler = useCallback(async () => {
    onOpenLoadingScreen();
    const allNodes = await httpReq("papers", "GET");

    const map = new Map();

    allNodes.data.forEach((obj) => {
      map.set(obj.paperId, 0);
    });

    checkedElements.forEach((obj) => {
      map.set(obj.paperId, 0);
    });

    for (let index = 0; index < checkedElements.length; index++) {
      const result = await httpReq(
        "papers/semantic_api/{paperIdType}:{paperId}?paperId=" +
          checkedElements[index].paperId +
          "&paperIdType=" +
          checkedElements[index].paperIdType,
        "GET"
      );
      if (result.error) {
      } else {
        if (
          Object.keys(result.data).length === 0 &&
          result.data.constructor === Object
        ) {
          await httpReq("papers", "POST", { ...checkedElements[index] });
        } else {
          const relatedWorks = [];
          const referenceClone = JSON.parse(JSON.stringify(result.data));
          const references = referenceClone.references;

          if (Array.isArray(references))
            references.forEach((obj) => {
              if (map.has(obj.paperId)) {
                relatedWorks.push({ ...obj });
              }
            });

          referenceClone["relatedWorks"] = relatedWorks;
          console.log(referenceClone);

          await httpReq("papers", "POST", JSON.stringify(referenceClone));

          const urlStr = `papers/${sourcePaperId}/addRelatedWithRelationShip/${referenceClone.paperId}`;
          await httpReq(urlStr, "POST");
        }
      }
    }

    onCloseLoadingScreen();
    onClose();
    onOptionsClosed();
    onSimpleExpand();
  }, [
    checkedElements,
    sourcePaperId,
    onCloseLoadingScreen,
    onOpenLoadingScreen,
    onClose,
    onOptionsClosed,
    onSimpleExpand,
  ]);

  return (
    <Modal
      show={true}
      onHide={props.onClose}
      animation={false}
      style={{
        position: "fixed",
        left: "50%",
        top: "10%",
        transform: "translate(-50%, 0)",
        zIndex: "9999",
      }}
    >
      <Modal.Header closeButton>
        <Modal.Title>Which related papers would you like to save?</Modal.Title>
      </Modal.Header>

      <Modal.Body>
        <div style={{ height: "300px", overflowY: "auto" }}>
          <ListGroup>
            {props.papers &&
              props.papers.map((obj) => {
                return (
                  <ListGroup.Item>
                    <Form.Group size="sm" controlId="formBasicCheckbox">
                      <Form.Check
                        onClick={(e) => clickHandler(e, { ...obj })}
                        check
                        size="sm"
                        type="checkbox"
                        label={obj.title}
                      />
                    </Form.Group>
                  </ListGroup.Item>
                );
              })}
          </ListGroup>
        </div>
      </Modal.Body>

      <Modal.Footer>
        <Button variant="secondary" onClick={() => props.onClose()}>
          Abort
        </Button>
        <Button
          onClick={async () => {
            await addPapersHandler();
          }}
          variant="primary"
        >
          Save Papers to Database
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

const mapStateToProps = (state) => {
  return {};
};

const mapDispatchToProps = (dispatch) => {
  return {
    onOpenLoadingScreen: () => dispatch(actionCreators.openLoadingScreen()),
    onCloseLoadingScreen: () => dispatch(actionCreators.closeLoadingScreen()),
    onOpenErrorModal: (errorMessage) =>
      dispatch(actionCreators.openErrorModal(errorMessage)),
    onSimpleExpand: () => dispatch(actionCreators.simpleExpand()),
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AddRelatedPapersModal);
