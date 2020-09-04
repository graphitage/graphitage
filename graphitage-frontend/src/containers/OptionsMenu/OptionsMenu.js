import React from "react";

import { slide as Menu } from "react-burger-menu";
import OptionsForm from "./OptionsForm/OptionsForm";
import DetailsEditForm from "../../components/DetailsPanel/DetailsEditForm/DetailsEditForm";

import Card from "react-bootstrap/Card";
import Accordion from "react-bootstrap/Accordion";

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faSortDown } from '@fortawesome/free-solid-svg-icons'

var optionsMenuStyles = {
  bmBurgerButton: {
    position: "fixed",
    width: "36px",
    height: "30px",
    left: "36px",
    top: "36px",
    display: "none",
  },
  bmBurgerBars: {
    background: "#373a47",
  },
  bmBurgerBarsHover: {
    background: "#a90000",
  },
  bmCrossButton: {
    height: "24px",
    width: "24px",
  },
  bmCross: {
    background: "#bdc3c7",
  },
  bmMenuWrap: {
    position: "fixed",
    height: "100%",
    zIndex: "1000",
    width: "50%"
  },
  bmMenu: {
    background: "#ebedf0",
    padding: "2.5em 1.5em 0",
    fontSize: "1.15em",
  },
  bmMorphShape: {
    fill: "#373a47",
  },
  bmItemList: {
    color: "#b8b7ad",
    padding: "0.8em",
  },
  bmItem: {
    display: "inline-block",
  },
  bmOverlay: {
    background: "rgba(0, 0, 0, 0.3)",
  },
};

const optionsMenu = (props) => (
  <Menu
    onClose={() => props.optionsClosed()}
    isOpen={props.options}
    styles={optionsMenuStyles}
    right
  >
    <h3 className="menuHeader" style={{width: "100%"}}>Add Paper</h3>
    <br/><br/>
    <Card className="text-center" style={{width: "100%"}}>
      <Card.Header style={{ color: "black" }}><h3>Add Nodes</h3></Card.Header>
      <Card.Body>
        <Card.Title style={{ color: "black" }}>With JSON File</Card.Title>
        <Card.Text >
          Add a node to the database by selecting a JSON file from local files.
        </Card.Text>
        <div id="jaskdjkdas" style={{ transform: "translate(-50%, 0)", left: "50%", width:'60%', position: "relative"}}>
          <OptionsForm />
        </div>
        <hr/>
        <Card.Title style={{ color: "black" }}>Manually</Card.Title>
        <Card.Text >
          Add a node to the database by entering necessary values by hand.
        </Card.Text>
        <Accordion defaultActiveKey="0">
          <Card>
            <Accordion.Toggle as={Card.Header} eventKey="1" style={{ cursor: "pointer" }}>
              <FontAwesomeIcon icon={faSortDown} />
            </Accordion.Toggle>
            <Accordion.Collapse eventKey="1">
              <Card.Body>
                  {/* <OptionsManualForm /> */}

                  <DetailsEditForm optionsClosed={props.optionsClosed}  collapsed={props.collapsedDetails} setCollapsed={props.setCollapsedDetails} />
                </Card.Body>
            </Accordion.Collapse>
          </Card>
        </Accordion>
    </Card.Body>
</Card>

      
  </Menu>
);

export default React.memo(optionsMenu);
