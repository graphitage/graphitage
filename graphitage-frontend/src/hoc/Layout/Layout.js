import React, { useState, useCallback } from 'react';
import { connect } from 'react-redux';

import SearchMenu from '../../components/Navigation/SearchMenu/SearchMenu';
import Toolbar from '../../components/Navigation/Toolbar/Toolbar';
import DetailsPanel from '../../components/DetailsPanel/DetailsPanel';


import classes from './Layout.module.css'
import OptionsMenu from '../../components/OptionsMenu/OptionsMenu';

import * as actionTypes from '../../store/actions/actionTypes';


const Layout = (props) => {
    const [searchMenu, setSearch] = useState(false);
    const [optionsMenu, setOptions] = useState(false);
    const [detailsMenu, setdetailsMenu] = useState(false);
    const [nodeId, setNodeId] = useState('');

    const searchClosedHandler = useCallback(() => {
        setSearch(false);
    }, [])



    const searchClickedHandler = useCallback(() => {
        setSearch(true);
        setOptions(false);
        setdetailsMenu(false);
    }, []);

    const optionsClosedHandler = useCallback(() => {
        setOptions(false);
    }, []);

    const optionsClickedHandler = useCallback(() => {
        setOptions(true);
        setSearch(false);
        setdetailsMenu(false);
    }, []);

    const { switchClearGraph } = props;
    const clearGraph = useCallback(() => {
        switchClearGraph(true);
    }, [switchClearGraph]);

    const detailsPanelOpenedHandler = useCallback((id) => {
        setdetailsMenu(true);
        setOptions(false);
        setSearch(false);
        setNodeId(id);
    }, []);

    const detailsPanelClosedHandler = useCallback(() => {
        setdetailsMenu(false);
    }, []);




    return (
        <React.Fragment>
            <SearchMenu search={searchMenu} searchClosed={searchClosedHandler}></SearchMenu>
            <OptionsMenu options={optionsMenu} optionsClosed={optionsClosedHandler}></OptionsMenu>
            <DetailsPanel detailsClosed={detailsPanelClosedHandler} details={detailsMenu} nodeId={nodeId} />
            <Toolbar searchClick={searchClickedHandler} optionsClick={optionsClickedHandler} clearGraph={clearGraph}></Toolbar>
            <main className={classes.Content}>
                {React.cloneElement(props.children, { detailsMenuHandler: detailsPanelOpenedHandler })}
            </main>
        </React.Fragment>
    );
}


const mapStateToProps = state => {
    return {
        clr: state.clearNodes
    }
}

const mapDispatchToProps = dispatch => {
    return {
        switchClearGraph: (isActive) => dispatch({ type: actionTypes.CLEAR_NODES, bool: isActive })
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(Layout); 