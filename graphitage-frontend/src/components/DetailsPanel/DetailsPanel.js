import React from 'react';

import { slide as Menu } from 'react-burger-menu';
import DetailsForm from './DetailsForm/DetailsForm';
import DetailsEditForm from './DetailsEditForm/DetailsEditForm';

var searchMenuStyles = {
    bmBurgerButton: {
        position: 'fixed',
        width: '36px',
        height: '30px',
        left: '36px',
        top: '36px',
        display: 'none'
    },
    bmBurgerBars: {
        background: '#373a47'
    },
    bmBurgerBarsHover: {
        background: '#a90000'
    },
    bmCrossButton: {
        height: '24px',
        width: '24px'
    },
    bmCross: {
        background: '#bdc3c7'
    },
    bmMenuWrap: {
        position: 'fixed',
        height: '100%',
        width: '50%',
        zIndex: "1000"
    },
    bmMenu: {
        background: '#ebedf0',
        padding: '2.5em 1.5em 0',
        fontSize: '1.15em'
    },
    bmMorphShape: {
        fill: '#373a47'
    },
    bmItemList: {
        color: '#b8b7ad',
        padding: '0.8em'
    },
    bmItem: {
        display: 'inline-block'
    },
    bmOverlay: {
        background: 'rgba(0, 0, 0, 0.3)'
    }
}


const detailsPanel = (props) => (
    <Menu right onClose={() => props.detailsClosed()} isOpen={props.details} styles={searchMenuStyles}>
    {props.isEditing
    ?    <DetailsEditForm detailsClosed={props.detailsClosed} setEditing={props.setEditing} collapsed={false} />
    :   <DetailsForm nodeId={props.nodeId} setEditing={props.setEditing} detailsClosed={props.detailsClosed} />
    }
    </Menu>
);


export default detailsPanel;