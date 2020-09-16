import * as actionTypes from "../actions/actionTypes";

const initialState = {
  clearNodes: false,
  elements: [],
  error: false,
  toHideNodeId: "",
};

const reducer = (state = initialState, action) => {
  switch (action.type) {
    case actionTypes.CLEAR_NODES:
      return {
        ...state,
        elements: [],
        clearNodes: action.bool,
        error: false,
        toHideNodeId: "",
      };
    case actionTypes.SET_ELEMENTS:
      return {
        ...state,
        elements: [...action.elements],
        error: false,
        toHideNodeId: "",
      };
    case actionTypes.SET_ERROR:
      return {
        ...state,
        elements: [],
        error: action.error,
        toHideNodeId: "",
      };

    case actionTypes.SET_TO_HIDE_NODE:
      return {
        ...state,
        elements: [],
        error: false,
        toHideNodeId: action.id,
      };

      case actionTypes.UNEXPAND_NODE:
      return {
        ...state,
        elements: [],
        error: false,
        toHideNodeId: action.id,
        unExpand: action.unExpand,
      };

    default:
      return state;
  }
};

export default reducer;
