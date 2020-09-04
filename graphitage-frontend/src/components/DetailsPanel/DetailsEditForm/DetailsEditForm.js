import React, { useCallback, useReducer, useState } from "react";
import { connect } from "react-redux";

import Button from "react-bootstrap/Button";
import Container from "react-bootstrap/Container";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";
import Form from "react-bootstrap/Form";
import InputGroup from "react-bootstrap/InputGroup";
import FormControl from "react-bootstrap/FormControl";
import TagBox from "../../TagBox/TagBox";
import LibraryTagBox from "./LibraryTagBox/LibraryTagBox";
import DatasetTagBox from "./DatasetTagBox/DatasetTagBox";
import RelatedWorksTagBox from "./RelatedWorksTagBox/RelatedWorksTagBox";
import AddRelatedPapersModal from "../../AddRelatedPapersModal/AddRelatedPapersModal";

import { useForm } from "react-hook-form";

import * as actionCreators from "../../../store/actions/index";

import httpReq from "../../../store/actions/utils/http";

const initialState = {};

function reducer(state, action) {
  const stateClone = JSON.parse(JSON.stringify(state));
  const payloadClone = JSON.parse(JSON.stringify(action.payload));
  let result;
  if (action.type === "clone") {
    result = payloadClone;
  } else {
    stateClone[action.type] = payloadClone;
    result = stateClone;
  }
  return result;
}

const Details = (props) => {
  const { register, handleSubmit } = useForm({
    mode: "onChange",
  });
  const [semanticState, setSemanticState] = useState({});
  const [addRelatedPaperModal, setAddRelatedPaperModal] = useState(false);

  const [state, dispatch] = useReducer(reducer, initialState);

  const handleKeywordsChange = useCallback((array) => {
    dispatch({ type: "keywords", payload: array });
  }, []);
  const handleTargetsChange = useCallback((array) => {
    dispatch({ type: "targets", payload: array });
  }, []);
  const handleProblemsChange = useCallback((array) => {
    dispatch({ type: "problems", payload: array });
  }, []);
  const handleApplicationDomainsChange = useCallback((array) => {
    dispatch({ type: "applicationDomains", payload: array });
  }, []);
  const handleComponentsChange = useCallback((array) => {
    dispatch({ type: "components", payload: array });
  }, []);
  const handleHighlightsChange = useCallback((array) => {
    dispatch({ type: "highlights", payload: array });
  }, []);
  const handleContributionsChange = useCallback((array) => {
    dispatch({ type: "contributions", payload: array });
  }, []);
  const handleProsChange = useCallback((array) => {
    dispatch({ type: "pros", payload: array });
  }, []);
  const handleConsChange = useCallback((array) => {
    dispatch({ type: "cons", payload: array });
  }, []);
  const handleFutureWorksChange = useCallback((array) => {
    dispatch({ type: "futureWorks", payload: array });
  }, []);
  const handleNotesChange = useCallback((array) => {
    dispatch({ type: "notes", payload: array });
  }, []);
  const handleDatasetsChange = useCallback((array) => {
    dispatch({ type: "datasets", payload: array });
  }, []);
  const handleEvaluationsChange = useCallback((array) => {
    dispatch({ type: "evaluations", payload: array });
  }, []);
  const handleLibrariesChange = useCallback((array) => {
    dispatch({ type: "libraries", payload: array });
  }, []);
  const handleAuthorsChange = useCallback((array) => {
    dispatch({ type: "authors", payload: array });
  }, []);
  const handleCommentsChange = useCallback((array) => {
    dispatch({ type: "comments", payload: array });
  }, []);
  const handleConstraintsChange = useCallback((array) => {
    dispatch({ type: "constraints", payload: array });
  }, []);
  const handleRelatedWorksChange = useCallback((array) => {
    dispatch({ type: "relatedWorks", payload: array });
  }, []);

  const onCloseAddRelatedPapersModal = useCallback(() => {
    setAddRelatedPaperModal(false);
  }, []);

  const onOpenAddRelatedPapersModal = useCallback(() => {
    setAddRelatedPaperModal(true);
  }, []);

  const {
    onOpenErrorModal,
    onCloseLoadingScreen,
    onSimpleExpand,
    onClearGraph,
    onOpenLoadingScreen,
  } = props;
  const onAddSubmit = useCallback(
    (data, event) => {
      event.preventDefault();
      onOpenLoadingScreen();
      console.log(data);

      const references = JSON.parse(JSON.stringify(state.relatedWorks));
      const relatedWorks = [];
      const map = new Map();
      httpReq("papers", "GET").then((res) => {
        res.data.forEach((obj) => {
          map.set(obj.paperId, 0);
        });

        references.forEach((obj) => {
          if (map.has(obj.paperId)) {
            relatedWorks.push(obj);
          }
        });
      });

      let jsonToSend = { ...state };
      jsonToSend["abstractOfPaper"] = data.abstract;
      jsonToSend["year"] = data.year;
      jsonToSend["linkOfPaper"] = data.linkOfPaper;
      jsonToSend["paperId"] = semanticState.paperId;
      jsonToSend["paperIdType"] = data.paperIdType
        ? data.paperIdType
        : document.getElementById("paperIdType").value;
      jsonToSend["reader"] = [];
      jsonToSend["summaries"] = [];
      jsonToSend["title"] = data.paperTitle;
      jsonToSend["relatedWorks"] = relatedWorks;
      httpReq("papers", "POST", JSON.stringify(jsonToSend)).then((result) => {
        if (result.error) {
          onCloseLoadingScreen();
          console.log(result.error);
          onOpenErrorModal("Connection Error!"); //TODO: Fix - Returns response status 200, but this opens anyway.
        } else {
          onClearGraph(true);
          onSimpleExpand();
          onCloseLoadingScreen();
          onOpenAddRelatedPapersModal();
        }
      });
    },

    [
      state,
      onOpenLoadingScreen,
      onOpenErrorModal,
      onOpenAddRelatedPapersModal,
      onClearGraph,
      onSimpleExpand,
      onCloseLoadingScreen,
      semanticState
    ]
  );

  const { dtl, detailsClosed } = props;
  const onUpdateSubmit = useCallback(
    (data, event) => {
      event.preventDefault();
      console.log("update command");

      let jsonToSend = { ...state };
      jsonToSend["abstractOfPaper"] = data.abstract;
      jsonToSend["year"] = data.year;
      jsonToSend["linkOfPaper"] = data.linkOfPaper;
      jsonToSend["paperId"] = dtl.paperId;
      jsonToSend["paperIdType"] = dtl.paperIdType;
      jsonToSend["reader"] = dtl.reader ? dtl.reader : [];
      jsonToSend["summaries"] = [];
      jsonToSend["title"] = dtl.title;

      httpReq("papers", "PUT", JSON.stringify(jsonToSend)).then((result) => {
        if (result.error) {
          onOpenErrorModal("Connection Error!");
        } else {
          detailsClosed();
        }
      });
    },
    [state, onOpenErrorModal, dtl, detailsClosed]
  );

  const { setCollapsed } = props;
  const handleGetDetailsButton = useCallback(() => {
    onOpenLoadingScreen();
    const paperId = document.getElementById("paperId").value;
    const paperIdType = document.getElementById("paperIdType").value;

    httpReq(
      "papers/semantic_api/{paperIdType}:{paperId}?paperId=" +
        paperId +
        "&paperIdType=" +
        paperIdType,
      "GET"
    ).then((result) => {
      onCloseLoadingScreen();
      if (result.error) {
        onOpenErrorModal("Connection Error!");
        setCollapsed(true);
      } else {
        if (
          Object.keys(result.data).length === 0 &&
          result.data.constructor === Object
        ) {
          onOpenErrorModal("No such paper found!");
          setCollapsed(true);
        } else {
          setCollapsed(false);
          result.data.relatedWorks = result.data.references;
          setSemanticState(result.data);
          console.log('semantic response', result.data)
        }
      }
    });
  }, [
    setCollapsed,
    onOpenErrorModal,
    onOpenLoadingScreen,
    onCloseLoadingScreen,
  ]);

  let submitFuc;
  let source;
  if (!props.setEditing) {
    submitFuc = onAddSubmit;
    source = semanticState;
  } else {
    submitFuc = onUpdateSubmit;
    source = props.dtl;
  }

  return (
    <div style={{ color: "#142850" }} className="DetailsEditForm">
      {props.setEditing && <h2>{source.title}</h2>}

      {props.setEditing && (
        <p>
          ID: {source.paperId}/{source.paperIdType}
        </p>
      )}

      {addRelatedPaperModal && (
        <AddRelatedPapersModal
          papers={source.references}
          sourcePaperId={source.paperId}
          onClose={onCloseAddRelatedPapersModal}
          onOptionsClosed={props.optionsClosed}
        />
      )}

      <Form style={{ color: "#142850" }} onSubmit={handleSubmit(submitFuc)}>
        {!props.setEditing && (
          <>
            <h3 className="menuHeader">Use Scholar API</h3>
            <InputGroup>
              <FormControl
                placeholder="Paper ID"
                aria-label="Paper ID"
                aria-describedby="basic-addon2"
                name="paperId"
                ref={register()}
                id="paperId"
              />
              <Form.Control
                name="paperIdType"
                ref={register()}
                as="select"
                custom
                id="paperIdType"
              >
                <option>ARXIV</option>
                <option>DOI</option>
                <option>SEMANTIC</option>
              </Form.Control>
              <InputGroup.Append>
                <Button
                  onClick={handleGetDetailsButton}
                  variant="outline-secondary"
                >
                  Get Details
                </Button>
              </InputGroup.Append>
            </InputGroup>

            <br></br>
          </>
        )}

        {!props.setEditing && (
          <>
            <h3 className="menuHeader">Paper Title</h3>
            <FormControl
              placeholder="Paper Title"
              aria-label="Paper Title"
              aria-describedby="basic-addon2"
              name="paperTitle"
              ref={register()}
              defaultValue={source.title}
            />
          </>
        )}

        <h4 className="menuHeader">Paper Link</h4>
        <FormControl
          placeholder="Paper Link"
          aria-label="Paper Link"
          aria-describedby="basic-addon2"
          name="paperLink"
          ref={register()}
          defaultValue={source.linkOfPaper}
        />

        <br></br>

        <h4 className="menuHeader">Year</h4>
        <Form.Group controlId="year">
          <Form.Control
            defaultValue={source.year}
            as="input"
            type="text"
            name="year"
            ref={register()}
          />
        </Form.Group>

        <h4 className="menuHeader">Abstract</h4>
        <Form.Group controlId="abstract">
          <Form.Control
            defaultValue={source.abstractOfPaper}
            as="textarea"
            rows="8"
            type="text"
            name="abstract"
            ref={register()}
          />
        </Form.Group>

        <h4 className="menuHeader">Authors</h4>
        <Form.Group controlId="authors" className="noAutocomplete">
          <TagBox
            load={source.authors}
            tags={source.authors}
            onChange={handleAuthorsChange}
          />
        </Form.Group>

        <h4 className="menuHeader">Keywords</h4>
        <Form.Group controlId="keywords" className="noAutocomplete">
          <TagBox
            load={source.keywords}
            tags={source.keywords}
            onChange={handleKeywordsChange}
          />
        </Form.Group>

        <h4 className="menuHeader">Targets</h4>
        <Form.Group controlId="targets">
          <TagBox
            load={source.targets}
            tags={source.targets}
            onChange={handleTargetsChange}
          />
        </Form.Group>

        <h4 className="menuHeader">Problems</h4>
        <Form.Group controlId="problems" className="noAutocomplete">
          <TagBox
            load={source.problems}
            tags={source.problems}
            onChange={handleProblemsChange}
          />
        </Form.Group>

        <h4 className="menuHeader">Application Domains</h4>
        <Form.Group controlId="applicationDomains" className="noAutocomplete">
          <TagBox
            load={source.applicationDomains}
            tags={source.applicationDomains}
            onChange={handleApplicationDomainsChange}
          />
        </Form.Group>

        <h4 className="menuHeader">Components</h4>
        <Form.Group controlId="components" className="noAutocomplete">
          <TagBox
            load={source.components}
            tags={source.components}
            onChange={handleComponentsChange}
          />
        </Form.Group>

        <h4 className="menuHeader">Highlights</h4>
        <Form.Group controlId="highlights" className="noAutocomplete">
          <TagBox
            load={source.highlights}
            tags={source.highlights}
            onChange={handleHighlightsChange}
          />
        </Form.Group>

        <h4 className="menuHeader">Contributions</h4>
        <Form.Group controlId="contributions" className="noAutocomplete">
          <TagBox
            load={source.contributions}
            tags={source.contributions}
            onChange={handleContributionsChange}
          />
        </Form.Group>

        <h4 className="menuHeader">Pros</h4>
        <Form.Group controlId="pros" className="noAutocomplete">
          <TagBox
            load={source.pros}
            tags={source.pros}
            onChange={handleProsChange}
          />
        </Form.Group>

        <h4 className="menuHeader">Cons</h4>
        <Form.Group controlId="cons" className="noAutocomplete">
          <TagBox
            load={source.cons}
            tags={source.cons}
            onChange={handleConsChange}
          />
        </Form.Group>

        <h4 className="menuHeader">Future Works</h4>
        <Form.Group controlId="futureWorks" className="noAutocomplete">
          <TagBox
            load={source.futureWorks}
            tags={source.futureWorks}
            onChange={handleFutureWorksChange}
          />
        </Form.Group>

        <h4 className="menuHeader">Notes</h4>
        <Form.Group controlId="notes" className="noAutocomplete">
          <TagBox
            load={source.notes}
            tags={source.notes}
            onChange={handleNotesChange}
          />
        </Form.Group>

        <h4 className="menuHeader">Datasets</h4>
        <DatasetTagBox onChange={handleDatasetsChange} load={source.datasets} />

        <h4 className="menuHeader">Libraries</h4>
        <Form.Group controlId="libraries">
          <LibraryTagBox
            load={source.libraries}
            onChange={handleLibrariesChange}
          />
        </Form.Group>

        <h4 className="menuHeader">Evaluations</h4>
        <Form.Group controlId="evaluations" className="noAutocomplete">
          <TagBox
            load={source.evaluations}
            tags={source.evaluations}
            onChange={handleEvaluationsChange}
          />
        </Form.Group>

        <h4 className="menuHeader">Constraints</h4>
        <Form.Group controlId="constraints" className="noAutocomplete">
          <TagBox
            load={source.constraints}
            tags={source.constraints}
            onChange={handleConstraintsChange}
          />
        </Form.Group>

        <h4 className="menuHeader">Comments</h4>
        <Form.Group controlId="comments" className="noAutocomplete">
          <TagBox
            load={source.comments}
            tags={source.comments}
            onChange={handleCommentsChange}
          />
        </Form.Group>

        <h4 className="menuHeader">Related Works</h4>
        <Form.Group controlId="comments" className="noAutocomplete">
          <RelatedWorksTagBox
            onChange={handleRelatedWorksChange}
            load={source.relatedWorks}
          ></RelatedWorksTagBox>
        </Form.Group>

        <br></br>
        <br></br>

        <br />
        <Container>
          <Row>
            <Col>
              <Button type="submit" variant="success" size="lg-2" block>
                Save Changes
              </Button>
            </Col>
            <Col xs={1}></Col>
            <Col>
              <Button
                onClick={() => props.setEditing(false)}
                variant="danger"
                size="lg-2"
                block
              >
                Cancel
              </Button>
            </Col>
          </Row>
        </Container>
        <br />
      </Form>
    </div>
  );
};

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
