import React from "react";
import { connect } from "react-redux";

import Button from "react-bootstrap/Button";
import Container from "react-bootstrap/Container";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";

import * as actionCreators from "../../../store/actions/index";

import httpReq from "../../../store/actions/utils/http";

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faEdit } from '@fortawesome/free-solid-svg-icons'


const stringFromArray = (array, sep = ", ") => {
  return array.reduce((acc, cur, index) => {
    return acc + cur + sep;
  }, "");
};

const pFromArray = (array, key, htmlEl = "p") => {
  if (htmlEl === "p") {
    return array.reduce((acc, cur, index) => {
      acc.push(<p key={key + String(index)}>{cur}</p>);
      return acc;
    }, []);
  } else if (htmlEl === "list") {
    const listArr = array.reduce((acc, cur, index) => {
      acc.push(<li key={key + String(index)}>{cur}</li>);
      return acc;
    }, []);

    return <ul>{listArr}</ul>;
  }
};

const Details = (props) => {
  const handleHideButton = () => {
    props.onSetToHideNodeId(props.nodeId);
    props.detailsClosed();
  };

  const handleDeleteButton = () => {
    props.onOpenLoadingScreen();
    httpReq("papers/" + props.nodeId, "DELETE")
      .then((result) => {
        props.onClearGraph(true);
        props.onSimpleExpand();
        props.onCloseLoadingScreen();
      })
      .catch((error) => {
        props.onCloseLoadingScreen();
        props.onOpenErrorModal("Could not delete node!");
      });
  };

  if (props.dtl.prepDetails) {
    return (
      <div style={{ color: "#142850" }} className="Details">
        <h2>Dataset Name: {props.dtl.datasetName}</h2>
        <h3>Dataset ID: {props.dtl.datasetId}</h3>
        <h4>Preprocessing ID: {props.dtl.prepId}</h4>
        <h3>Steps:</h3>
        {pFromArray(props.dtl.prepSteps)}
      </div>
    );
  } else if (props.dtl.paperDetails) {
    return (
      <div style={{ color: "#142850" }} className="Details">
        <h2>{props.dtl.title}{' '}
        <Button variant="info" onClick={() => props.setEditing(true)}><FontAwesomeIcon icon={faEdit}/></Button>
        {/* <button><FontAwesomeIcon icon={faEdit} /></button> */}
        </h2>
        
        {props.dtl.year && (
          <div className="year">
            <h4>Year: {props.dtl.year}</h4>
          </div>
        )}
        <p>
          ID: {props.dtl.paperId}/{props.dtl.paperIdType}
        </p>
        <div className="body">
          {props.dtl.keywords && (
            <div className="keywords">
              <h3>Keywords</h3>
              <p>{stringFromArray(props.dtl.keywords, ", ")}</p>
            </div>
          )}
          {props.dtl.abstractOfPaper && (
            <div className="abstract">
              <h3>Abstract</h3>
              <p>{props.dtl.abstractOfPaper}</p>
            </div>
          )}
          {props.dtl.targets && (
            <div className="targets">
              <h3>Targets</h3>
              <p>{stringFromArray(props.dtl.targets, ", ")}</p>
            </div>
          )}
          {props.dtl.problems && (
            <div className="problems">
              <h3>Problems</h3>
              <p>{stringFromArray(props.dtl.problems, ", ")}</p>
            </div>
          )}
          {props.dtl.applicationDomains && (
            <div className="applicationDomains">
              <h3>Application Domains</h3>
              <p>{stringFromArray(props.dtl.applicationDomains, ", ")}</p>
            </div>
          )}
          {props.dtl.components && (
            <div className="components">
              <h3>Components</h3>
              <p>{stringFromArray(props.dtl.components, ", ")}</p>
            </div>
          )}
          {props.dtl.highlights && (
            <div className="highlights">
              <h3>Highlights</h3>
              {pFromArray(props.dtl.highlights)}
            </div>
          )}
          {props.dtl.contributions && (
            <div className="contributions">
              <h3>Contributions</h3>
              {pFromArray(props.dtl.contributions, "cont")}
            </div>
          )}
          {props.dtl.pros && (
            <div className="pros">
              <h3>Pros</h3>
              {pFromArray(props.dtl.pros, "pros", "list")}
            </div>
          )}
          {props.dtl.cons && (
            <div className="cons">
              <h3>Cons</h3>
              {pFromArray(props.dtl.pros, "cons", "list")}
            </div>
          )}
          {props.dtl.futureWorks && (
            <div className="futureWorks">
              <h3>Future Works</h3>
              {pFromArray(props.dtl.futureWorks, "futureWorks", "p")}
            </div>
          )}
          {props.dtl.notes && (
            <div className="notes">
              <h3>Notes</h3>
              {pFromArray(props.dtl.notes, "notes")}
            </div>
          )}
          {props.dtl.data && (
            <div className="data">
              <h3>Datasets</h3>
              <p>
                {stringFromArray(props.dtl.data.map((obj) => obj.datasetName))}
              </p>
            </div>
          )}
          {props.dtl.evaluations && (
            <div className="evaluations">
              <h3>Evaluations</h3>
              <p>{stringFromArray(props.dtl.evaluations, ", ")}</p>
            </div>
          )}
          {props.dtl.libraries && (
            <div className="libraries">
              <h3>Libraries</h3>
              <ul>
                {props.dtl.libraries.map((obj, index) => (
                  <li key={"libs" + String(index)}>
                    <a href={obj.link}>{obj.name || obj.link}</a>
                  </li>
                ))}
              </ul>
            </div>
          )}
          {props.dtl.datasets && (
            <div className="datasets">
              <h3>Datasets</h3>
              <ul>
                {props.dtl.datasets.map((obj, index) => (
                  <li key={"dataset" + String(index)}>
                    {obj.dataset.datasetName}
                  </li>
                ))}
              </ul>
            </div>
          )}
          {props.dtl.relatedWorks && (
            <div className="relatedWorks">
              <h3>Related Works</h3>
              <ul>
                {props.dtl.relatedWorks.map((obj, index) => (
                  <li key={"relatedW" + String(index)}>
                    {obj.title} ({obj.paperId}/{obj.paperIdType})
                  </li>
                ))}
              </ul>
            </div>
          )}
          {props.dtl.reader && (
            <div className="reader">
              <h3>Readers</h3>
              <ul>
                {props.dtl.reader.map((obj, index) => (
                  <li key={"readers" + String(index)}>{obj.readerName}</li>
                ))}
              </ul>
            </div>
          )}

          {/* hide node button */}
          <br />
          <Container>
            <Row>
              <Col>
                <Button
                  onClick={handleHideButton}
                  variant="warning"
                  size="lg-2"
                  block
                >
                  Hide Node
                </Button>
              </Col>
              <Col xs={1}></Col>
              <Col>
                <Button
                  onClick={handleDeleteButton}
                  variant="danger"
                  size="lg-2"
                  block
                >
                  Delete Node
                </Button>
              </Col>
            </Row>
          </Container>
          <br />
        </div>
      </div>
    );
  }

  return (<div></div>)
} 



const mapStateToProps = (state) => {
  return {
    dtl: state.details,
  };
};

const mapDispatchToProps = (dispatch) => {
  return {
    onSetToHideNodeId: (id) => dispatch(actionCreators.setToHideNodeId(id)),
    onOpenLoadingScreen: () => dispatch(actionCreators.openLoadingScreen()),
    onCloseLoadingScreen: () => dispatch(actionCreators.closeLoadingScreen()),
    onOpenErrorModal: (errorMessage) =>
      dispatch(actionCreators.openErrorModal(errorMessage)),
    onClearGraph: (bool) => dispatch(actionCreators.clearNodes(bool)),
    onSimpleExpand: (sourceNode) =>
      dispatch(actionCreators.simpleExpand(sourceNode)),
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(Details);
