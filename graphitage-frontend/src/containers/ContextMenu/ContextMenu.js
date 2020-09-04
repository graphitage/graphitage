import React, { useEffect, useState, useCallback } from "react";
import { connect } from 'react-redux';

import * as actionCreators from '../../store/actions/index';

import classes from './ContextMenu.module.css';

const ContextMenu = (props) => {
    const [xPos, setXPos] = useState("0px");
    const [yPos, setYPos] = useState("0px");
    const [menu, showMenu] = useState(false);

    const handleContextMenu = useCallback(
        event => {
            event.preventDefault();
            if (props.outerRef && props.outerRef.current.contains(event.target)) {
                setXPos(`${event.pageX}px`);
                setYPos(`${event.pageY}px`);
                showMenu(true);
            } else {
                showMenu(false);
            }
        },
        [showMenu, props.outerRef, setXPos, setYPos]
    );

    const { onCloseContextMenu, contextMenuIsOpen } = props;
    const handleClick = useCallback(() => {
        if (contextMenuIsOpen) {
            showMenu(false);
            onCloseContextMenu();
        }
    }, [onCloseContextMenu, showMenu, contextMenuIsOpen]);

    useEffect(() => {
        document.addEventListener("click", handleClick);
        document.addEventListener("contextmenu", handleContextMenu);
        return () => {
            document.removeEventListener("click", handleClick);
            document.removeEventListener("contextmenu", handleContextMenu);
        };
    }, [handleClick, handleContextMenu]);

    
    if (menu && props.contextMenuIsOpen && props.sourceNode.data.type === 'paper') {
        return (
            <ul className={classes.menu} style={{ top: yPos, left: xPos }}>
                <li onClick={() => props.onSimpleExpand(props.sourceNode)}>Simple Expand</li>
                <li onClick={() => props.onDatasetExpand(props.sourceNode)}>Expand Datasets</li>
                <li onClick={() => props.onReaderExpand(props.sourceNode)}>Expand Readers</li>
                <li onClick={() => props.onLibraryExpand(props.sourceNode)} >Expand Library</li>
                <li onClick={() => props.onKeywordExpand(props.sourceNode)}>Expand Keywords</li>
                <li onClick={() => props.onSetUnExpand(true)}>Un-Expand</li>
            </ul>
        );
    }else if (menu && props.contextMenuIsOpen && props.sourceNode.data.type === 'reader') {
        return (
            <ul className={classes.menu} style={{ top: yPos, left: xPos }}>
                <li onClick={() => props.onSimpleExpandReader(props.sourceNode)}>Simple Expand</li>
                <li onClick={() => props.onSetUnExpand(true)}>Un-Expand</li>
            </ul>
        );
    }else if (menu && props.contextMenuIsOpen && props.sourceNode.data.type === 'library') {
        return (
            <ul className={classes.menu} style={{ top: yPos, left: xPos }}>
                <li onClick={() => props.onSimpleExpandLibrary(props.sourceNode)}>Simple Expand</li>
                <li onClick={() => props.onSetUnExpand(true)}>Un-Expand</li>
            </ul>
        );
    }else if (menu && props.contextMenuIsOpen && props.sourceNode.data.type === 'dataset') {
        return (
            <ul className={classes.menu} style={{ top: yPos, left: xPos }}>
                <li onClick={() => props.onSimpleExpandDataset(props.sourceNode)}>Simple Expand</li>
                <li onClick={() => props.onSetUnExpand(true)}>Un-Expand</li>
            </ul>
        );
    }
    return <></>;
};


const mapStateToProps = state => {
    return {
        contextMenuIsOpen: state.ui.contextMenu.isOpen,
        sourceNode: state.ui.contextMenu.sourceNode
    }
}

const mapDispatchToProps = dispatch => {
    return {
        onCloseContextMenu: () => dispatch(actionCreators.closeContextMenu()),
        onDatasetExpand: (sourceNode) => dispatch(actionCreators.expandByDatasets(sourceNode)),
        onSimpleExpand: (sourceNode) => dispatch(actionCreators.simpleExpand(sourceNode)),
        onReaderExpand: (sourceNode) => dispatch(actionCreators.expandByReaders(sourceNode)),
        onKeywordExpand: (sourceNode) => dispatch(actionCreators.expandByKeywords(sourceNode)),
        onLibraryExpand: (sourceNode) => dispatch(actionCreators.expandByLibraries(sourceNode)),
        onSimpleExpandReader: (sourceNode) => dispatch(actionCreators.expandReaderByPapers(sourceNode)),
        onSimpleExpandLibrary: (sourceNode) => dispatch(actionCreators.expandLibraryByPapers(sourceNode)),
        onSimpleExpandDataset: (sourceNode) => dispatch(actionCreators.expandDatasetByPapers(sourceNode)),
        onSetUnExpand: (bool) => dispatch(actionCreators.setUnExpand(bool)),
    }
}


export default connect(mapStateToProps, mapDispatchToProps)(ContextMenu);


