import React, { useState, useRef, useCallback, useEffect } from "react";

import InputGroup from "react-bootstrap/InputGroup";
import FormControl from "react-bootstrap/FormControl";
import Button from "react-bootstrap/Button";
import ListGroup from "react-bootstrap/ListGroup";

import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  // faTrash,
  faTimes,
  // faTimesCircle,
} from "@fortawesome/free-solid-svg-icons";

const LibraryTagBox = (props) => {
  const [elements, setElements] = useState([]);
  const [legal, setLegal] = useState(false); // state to activate/deactivate submit button 

  let nameRef = useRef();
  let linkRef = useRef();

  const handleChange = useCallback((e) => {
    if (nameRef.current.value.length > 1 || linkRef.current.value.length > 4)
      setLegal(true);
    else setLegal(false);
  }, []);

  const handleAdd = useCallback(
    (
      name = nameRef.current.value,
      link = linkRef.current.value
    ) => {
      setElements((prevState) => [
        ...prevState,
        {
          name: name && name.length > 0 ? name : link ,
          link,
        },
      ]);
      nameRef.current.value = "";
      linkRef.current.value = "";
    },
    []
  );

  const handleRemove = useCallback((key) => {
    setElements((prevState) => prevState.filter((obj) => obj.name !== key));
  }, []);

  // Set state of the parent form 
  const {  onChange } = props;
  useEffect(() => {
    onChange(elements);
  }, [elements, onChange]);


  // Register pre-loaded props
  const { load } = props;
  useEffect(() => {
    if (Array.isArray(load)) load.forEach((obj) => handleAdd(obj.name, obj.link));
  }, [load, handleAdd]);

  return (
    <div>
      <div style={{overflow:"auto"}}>
      <ListGroup>
        {elements.map((obj) => (
            <ListGroup.Item action variant="secondary">
              <div
                key={obj.name || obj.link}>
                <a href={obj.link} style={{color:"black", maxWidth:"100%"}}>{obj.name}</a>{' '}
                <Button variant={"danger"} style={{ padding: "0.15rem 0.3rem", float:"right" }} onClick={() => handleRemove(obj.name || obj.link)}>
                  <FontAwesomeIcon icon={faTimes} />
                </Button>
              </div>
              
            </ListGroup.Item>
        ))}
        </ListGroup>
      </div>

      <InputGroup>
        <FormControl
          placeholder="Library Name"
          aria-label="Library Name"
          aria-describedby="basic-addon2"
          onChange={handleChange}
          ref={nameRef}
        />
        <FormControl
          placeholder="Library Link"
          aria-label="Library Link"
          aria-describedby="basic-addon2"
          onChange={handleChange}
          ref={linkRef}
        />
        <InputGroup.Append>
          <Button
            onClick={(e) => handleAdd()}
            disabled={!legal}
            variant="outline-secondary"
          >
            Add Library
          </Button>
        </InputGroup.Append>
      </InputGroup>
    </div>
  );
};

export default React.memo(LibraryTagBox);
