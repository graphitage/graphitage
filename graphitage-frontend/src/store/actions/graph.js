import * as actionTypes from "./actionTypes";
import httpReq from "./utils/http";
import {
  librariesElementCreator,
  keywordsElementCreator,
  simpleExpandElements,
  allPapersElementCreator,
  readersElementCreator,
  datasetsElementCreator,
} from "./utils/graphElementCreator";

export const clearNodes = (isActive) => {
  return {
    type: actionTypes.CLEAR_NODES,
    bool: isActive,
  };
};

const setElements = (elements) => {
  return {
    type: actionTypes.SET_ELEMENTS,
    elements: elements,
  };
};

const setError = (bool) => {
  return {
    type: actionTypes.SET_ERROR,
    error: bool,
  };
};

export const setUnExpand = (bool) => {
  return {
    type: actionTypes.UNEXPAND_NODE,
    unExpand: bool,
  };
};

// if nodeId is null, then get all the nodes from the server
export const simpleExpand = (sourceNode) => {
  return (dispatch, getState) => {
    if (sourceNode == null) {
      httpReq("papers", "GET").then((result) => {
        if (result.error === true) {
          dispatch(setError(true));
        } else {
          const graphElements = allPapersElementCreator(result.data);
          dispatch(setElements(graphElements));
        }
      });
    } else {
      httpReq(
        `papers/${sourceNode.data.id}/relatedWorks/`,
        "GET"
      ).then((result) => {
        if (result.error === true) {
          dispatch(setError(true));
        } else {
          const graphElements = simpleExpandElements(result.data, sourceNode);
          dispatch(setElements(graphElements));
        }
      });
    }
  };
};

export const addElements = (data) => {
  return (dispatch, getState) => {
    if (data != null) {
      const newGraphElements = allPapersElementCreator(Object.values(data));
      dispatch(setElements(newGraphElements));
    }
  };
};

export const expandByLibraries = (sourceNode) => {
  return (dispatch, getState) => {
    if (sourceNode != null) {
      httpReq(`papers/${sourceNode.data.id}/libraries/`, "GET").then(
        (result) => {
          if (result.error === true) {
            dispatch(setError(true));
          } else {
            const graphElements = librariesElementCreator(
              result.data,
              sourceNode
            );
            dispatch(setElements(graphElements));
          }
        }
      );
    }
  };
};

export const expandByKeywords = (sourceNode) => {
  return (dispatch, getState) => {
    if (sourceNode != null) {
      httpReq(`papers/${sourceNode.data.id}/keywords/`, "GET").then(
        (result) => {
          if (result.error === true) {
            dispatch(setError(true));
          } else {
            const graphElements = keywordsElementCreator(
              result.data,
              sourceNode
            );
            dispatch(setElements(graphElements));
          }
        }
      );
    }
  };
};

export const expandByReaders = (sourceNode) => {
  return (dispatch, getState) => {
    if (sourceNode != null) {
      httpReq(`papers/${sourceNode.data.id}/readers/`, "GET").then(
        (result) => {
          if (result.error === true) {
            dispatch(setError(true));
          } else {
            const graphElements = readersElementCreator(
              result.data,
              sourceNode
            );
            dispatch(setElements(graphElements));
          }
        }
      );
    }
  };
};

export const expandByDatasets = (sourceNode) => {
  return (dispatch, getState) => {
    if (sourceNode != null) {
      httpReq(`papers/${sourceNode.data.id}/datasets/`, "GET").then(
        (result) => {
          if (result.error === true) {
            dispatch(setError(true));
          } else {
            const graphElements = datasetsElementCreator(
              result.data,
              sourceNode
            );
            dispatch(setElements(graphElements));
          }
        }
      );
    }
  };
};


export const setToHideNodeId = (id) => {
  return {
    type: actionTypes.SET_TO_HIDE_NODE,
    id
  }
}

export const expandReaderByPapers = (sourceNode) => {
  return (dispatch, getState) => {
    if (sourceNode != null) {
      httpReq(`readers/${sourceNode.data.id}/papers`, "GET").then(
        (result) => {
          if (result.error === true) {
            dispatch(setError(true));
          } else {
            const graphElements = simpleExpandElements(
              result.data,
              sourceNode
            );
            dispatch(setElements(graphElements));
          }
        }
      );
    }
  };
};

export const expandLibraryByPapers = (sourceNode) => {
  return (dispatch, getState) => {
    if (sourceNode != null) {
      httpReq(`libraries/${sourceNode.data.id}/papers?libraryId`, "GET").then(
        (result) => {
          if (result.error === true) {
            dispatch(setError(true));
          } else {
            const graphElements = simpleExpandElements(
              result.data,
              sourceNode
            );
            dispatch(setElements(graphElements));
          }
        }
      );
    }
  };
};

export const expandDatasetByPapers = (sourceNode) => {
  return (dispatch, getState) => {
    if (sourceNode != null) {
      httpReq(`datasets/{datasetId}/papers?datasetId=${sourceNode.data.id}`, "GET").then(
        (result) => {
          if (result.error === true) {
            dispatch(setError(true));
          } else {
            const graphElements = simpleExpandElements(
              result.data,
              sourceNode,
              "datasetEdge"
            );
            dispatch(setElements(graphElements));
          }
        }
      );
    }
  };
};
