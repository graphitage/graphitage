import * as actionTypes from './actionTypes';
import httpReq from './utils/http';

const baseUrl = 'https://graphitage.herokuapp.com/api.graphitage.com/';


const saveDetails = (res) => {
    return {
        type: actionTypes.FETCH_DETAILS,
        details: res
    }
}

export const fetchDetails = (id = 0) => {
    return (dispatch, getState) => {
        httpReq(baseUrl + 'papers/' + id, 'GET')
        .then((result) => {
                console.log(result.data);
                dispatch(saveDetails(result.data));
            });
    }
}

