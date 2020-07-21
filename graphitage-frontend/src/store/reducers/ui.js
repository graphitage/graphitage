import * as actionTypes from '../actions/actionTypes';


const initialState = {
    contextMenu: {
        isOpen: false,
        sourceNode: {}
    }
}

const reducer = (state = initialState, action) => {
    switch ( action.type ) {
        case actionTypes.OPEN_CONTEXT_MENU:
            return {
                ...state,
                contextMenu: {
                    isOpen: true,
                    sourceNode: action.sourceNode
                }
            }
        case actionTypes.CLOSE_CONTEXT_MENU:
            return {
                ...state,
                contextMenu: {
                    isOpen: false,
                    sourceNode: {}
                }
            }
        default :
            return state
        
    
    }
}


export default reducer;