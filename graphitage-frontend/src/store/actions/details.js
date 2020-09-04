import * as actionTypes from "./actionTypes";
import httpReq from "./utils/http";

const saveDetails = (res) => {
  return {
    type: actionTypes.FETCH_DETAILS,
    details: res,
  };
};

export const fetchDetails = (id = 0) => {
  return (dispatch, getState) => {
    httpReq("papers/" + id, "GET").then((result) => {
      dispatch(saveDetails({...result.data, prepDetails: false, paperDetails: true}));
    });
  };
};

export const fetchPreprocessing = (sourceNodeId, datasetId) => {
  return (dispatch, getState) => {
    httpReq("papers/" + sourceNodeId, "GET").then((result) => {
      let preps = {};
      result.data.datasets.forEach((obj) => {
        if (String(obj.dataset.datasetId) === datasetId)
          preps = {
            datasetName: obj.dataset.datasetName,
            datasetId: obj.dataset.datasetId,
            prepId: obj.preprocessingId,
            prepSteps: [...obj.preprocessingSteps],
          };
      });
      dispatch(saveDetails({...preps, prepDetails: true, paperDetails: false}));
    });
  };
};

export const clearDetails = () => {
  return {
    type: actionTypes.CLEAR_DETAILS,
    details: "",
  };
};
