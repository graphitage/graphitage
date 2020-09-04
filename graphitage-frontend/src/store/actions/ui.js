import * as actionTypes from "./actionTypes";

export const openContextMenu = (sourceNode) => {
  return {
    type: actionTypes.OPEN_CONTEXT_MENU,
    sourceNode: sourceNode,
  };
};

export const closeContextMenu = () => {
  return {
    type: actionTypes.CLOSE_CONTEXT_MENU,
  };
};

export const openErrorModal = (errorMessage) => {
  return {
    type: actionTypes.OPEN_ERROR_MODAL,
    errorMessage
  }
}

export const closeErrorModal = () => {
  return {
    type: actionTypes.CLOSE_ERROR_MODAL
  }
}

export const openLoadingScreen = (errorMessage) => {
  return {
    type: actionTypes.OPEN_LOADING_SCREEN
  }
}

export const closeLoadingScreen = () => {
  return {
    type: actionTypes.CLOSE_LOADING_SCREEN
  }
}