import React from "react";

import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";

import logo from "../../../logo.png";

const styles = {
  links: {
    color: "#27496d",
  },
  navbar: {
    height: "60px",
  },
  img: { 
    height: "42px", 
    width: "56px",
    position: 'absolute',
    transform: 'translate(-50%, 0)',
    left: '50%'
  },
};

const toolbar = (props) => (
  <header className="Toolbar">
    <Navbar
      style={styles.navbar}
      className={"d-flex justify-content-between bg-transparent border-bottom"}
    >
      <Nav>
        <Nav.Link style={styles.links} onClick={() => props.searchClick()}>
          Search
        </Nav.Link>
      </Nav>
      <img alt="logo" style={styles.img} src={logo}></img>
      <Nav>
        <Nav.Link style={styles.links} onClick={() => props.clearGraph()}>
          Clear
        </Nav.Link>
        <Nav.Link style={styles.links} onClick={() => props.optionsClick()}>
          Add Paper
        </Nav.Link>
      </Nav>
    </Navbar>
  </header>
);

export default React.memo(toolbar);
