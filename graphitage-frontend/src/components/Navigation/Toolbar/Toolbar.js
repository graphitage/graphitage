import React from 'react';

import Navbar from 'react-bootstrap/Navbar';
import Nav from 'react-bootstrap/Nav';

import classes from './Toolbar.module.css';
import logo from '../../../logo_b2.png';


const color_link = {
    color: '#27496d'
}

const toolbar = (props) =>  (
    <header className="Toolbar">
        <Navbar style={{height: "60px"}} className={"d-flex justify-content-between bg-transparent border-bottom"} >
          <Nav>
            <Nav.Link style={color_link} onClick={() => props.searchClick()}>Search</Nav.Link>
          </Nav>
            <img alt="logo" style={{height: "42px", width: "56px"}} src={logo}></img>
          <Nav>
            <Nav.Link style={color_link} onClick={() => props.clearGraph()}>Clear</Nav.Link>
            <Nav.Link style={color_link} onClick={() => props.optionsClick()}>Options</Nav.Link>
          </Nav>
        </Navbar>
    </header>
)


export default toolbar;