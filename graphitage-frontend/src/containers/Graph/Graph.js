import React, { useEffect, useRef } from 'react';
import CytoscapeComponent from 'react-cytoscapejs';
import { connect } from 'react-redux';
import Cytoscape from 'cytoscape';
import CoseBilkent from 'cytoscape-cose-bilkent';



import * as actionCreators from '../../store/actions/index';

import ContextMenu from '../../components/ContextMenu/ContextMenu';

const layout = {
    name: 'cose-bilkent',
    randomize: true,
    idealEdgeLength: 200,
};

Cytoscape.use(CoseBilkent);

const Graph = (props) => {
    const outerRef = useRef(null);

    const { onSimpleExpand } = props;
    useEffect(() => {
        onSimpleExpand();
    }, [onSimpleExpand]);


    const { onOpenContextMenu, detailsMenuHandler, onFetchDetails } = props;
    useEffect(() => {
        Graph.cy.on('click', 'node', (event) => {
            // console.log(event.target._private.data);
            // var j = Graph.cy.$('#' + event.target._private.data.id);
            // Graph.cy.remove(j);
            if(event.target._private.data.type === "paper") {
                detailsMenuHandler(event.target._private.data.id);
                onFetchDetails(event.target._private.data.id);
            }
        });

        Graph.cy.on('cxttapend', 'node', (event) => {
            onOpenContextMenu(event.target._private);
        });

        // Add funcs like event listeners here! Example:
        //
        // document.body.addEventListener('click', function () {
        //     console.log('click');
        //     Graph.cy.add([{ data: { id: 'sdads', label: 'Node 5' }, position: { x: 400, y: 0 } }]);
        // });

        Graph.cy.minZoom(0.1);
        Graph.cy.maxZoom(3);
        
    }, [onOpenContextMenu, detailsMenuHandler, onFetchDetails])


    const { elements } = props
    useEffect(() => {
        // Graph.cy.elements().remove();
        
        // elements.forEach(function(element) {
        //     if(element.data.type === "paper"){
        //         console.log(element.data.id);
        //         paperStyle += 'node[id= "' + element.data.id + '"] { background-color: red ; label: data(label); text-wrap: ellipsis; text-max-width: 150; height: 40; width: 40; }';
        //     }
        // });

        // farkli turdeki nodelara farkli style uygulama
        let paperStyle = 'node[type = "paper"] { background-color: white ; label: data(label); text-wrap: ellipsis; text-max-width: 140; height: 50; width: 50; shape: circle; }';
        paperStyle += 'node[type = "dataset"] { background-color: white ; label: data(label); text-wrap: ellipsis; text-max-width: 140; height: 50; width: 50; shape: circle; }';
        paperStyle += 'node[type = "reader"] { background-color: white ; label: data(label); text-wrap: ellipsis; text-max-width: 140; height: 50; width: 50; shape: circle; }';
        paperStyle += 'node[type = "library"] { background-color: white ; label: data(label); text-wrap: ellipsis; text-max-width: 140; height: 50; width: 50; shape: circle; }';
        paperStyle += 'node[type = "keyword"] { background-color: white ; label: data(label); text-wrap: ellipsis; text-max-width: 140; height: 50; width: 50; shape: circle; }';
           
        Graph.cy.add(
            elements
        );
        Graph.cy.style(paperStyle);
        
        Graph.cy.style()
        .selector('node[type = "paper"]') // paper
            .style({
            'background-image': 'https://image.flaticon.com/icons/png/512/806/806177.png',
            'background-fit': 'cover',
            // 'background-fit': 'contain',
            // 'background-opacity': 0.8
        })
        .selector('node[type = "dataset"]') // dataset
            .style({
            'background-image': 'https://images.squarespace-cdn.com/content/v1/5547aa7fe4b0952dec8a18f5/1444868462425-F9MVV8BZ62L0WMTNKM77/ke17ZwdGBToddI8pDm48kA46J1yzDTVkrJE5ISoxNz1Zw-zPPgdn4jUwVcJE1ZvWEtT5uBSRWt4vQZAgTJucoTqqXjS3CfNDSuuf31e0tVGIgozllW4stDUfVSBhUmtGKqC1mNzDmg41LBEs9gWFXWbSd6kfRtgWHgNMDgGnmDY/image-asset.png',
            'background-fit': 'cover',
        })
        .selector('node[type = "reader"]') // reader
            .style({
            'background-image': 'https://image.flaticon.com/icons/svg/1459/1459381.svg',
            'background-fit': 'cover'
        })
        .selector('node[type = "library"]') // library
            .style({
            'background-image': 'https://icons-for-free.com/iconfiles/png/512/bookshelf+library+icon-1320087270870761354.png',
            'background-fit': 'contain'
        })
        .selector('node[type = "keyword"]') // keyword
            .style({
            'background-image': 'data:image/png;base64,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',
            'background-fit': 'contain'
        })

        .update() // indicate the end of your new stylesheet so that it can be updated on elements
        ;

        Graph.cy.elements().layout(layout).run();
    }, [elements]);

    if (props.clr) { // Clears graph
        Graph.cy.elements().remove();
        props.onSwitchClearGraph(false);
    }

   

    return (
        <div ref={outerRef} style={{ height: '100%' }}>
            <CytoscapeComponent cy={(cy) => { Graph.cy = cy }} elements={[]} layout={layout} style={{ width: '100%', height: '100%' }} />
            <ContextMenu outerRef={outerRef}  />
        </div>
    );

}


const mapStateToProps = state => {
    return {
        clr: state.graph.clearNodes,
        elements: state.graph.elements
    }
}

const mapDispatchToProps = dispatch => {
    return {
        onSwitchClearGraph: (isActive) => dispatch(actionCreators.clearNodes(isActive)),
        onFetchDetails: (id) => dispatch(actionCreators.fetchDetails(id)),
        onOpenContextMenu: (sourceNode) => dispatch(actionCreators.openContextMenu(sourceNode)),
        onCloseContextMenu: () => dispatch(actionCreators.closeContextMenu()),
        onSimpleExpand: (sourceNode) => dispatch(actionCreators.simpleExpand(sourceNode)),
        onAddElements: (data) => dispatch(actionCreators.addElements(data)),
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(Graph); 