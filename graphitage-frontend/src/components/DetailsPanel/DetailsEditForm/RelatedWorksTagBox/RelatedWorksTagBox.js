import React, { useState, useCallback, useEffect } from "react";
import { useForm } from "react-hook-form";

import InputGroup from "react-bootstrap/InputGroup";
import FormControl from "react-bootstrap/FormControl";
import Button from "react-bootstrap/Button";
import Card from "react-bootstrap/Card";
import Form from "react-bootstrap/Form";

import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
//   faTrash,
  faTimes,
//   faTimesCircle,
} from "@fortawesome/free-solid-svg-icons";

const RelatedWorkdsTagBox = (props) => {
  const [elements, setElements] = useState([]);
  //   const [legal, setLegal] = useState(false); // state to activate/deactivate submit button
  const { register, handleSubmit } = useForm();

  const handleAdd = useCallback((obj) => {
    setElements((prevState) => {
      let cloneState = JSON.parse(JSON.stringify(prevState));
      cloneState.push({ ...obj });
      return cloneState;
    });
  }, []);

  const handleRemove = useCallback((key) => {
    setElements((prevState) => prevState.filter((obj) => obj.paperId !== key));
  }, []);

  const handleFormSubmit = useCallback(
    (data, event) => {
      event.stopPropagation();
      handleAdd({
        paperId: data.paperId,
        paperTitle: data.paperTitle,
        paperIdType: data.paperIdType,
      });
    },
    [handleAdd]
  );

  // Set state of the parent form
  const { onChange } = props;
  useEffect(() => {
    onChange(elements);
  }, [elements, onChange]);

  // Register pre-loaded props
  const { load } = props;
  useEffect(() => {
    if (Array.isArray(load)) load.forEach((obj) => handleAdd(obj));
  }, [load, handleAdd]);

  return (
    <>
    <br/>
      <h5 className="menuHeader">Add New Related Paper</h5>
      <div onSubmit={handleSubmit(handleFormSubmit)}>
        <InputGroup>
          <FormControl
            placeholder="Paper ID"
            aria-label="Paper ID"
            name="paperId"
            aria-describedby="basic-addon2"
            ref={register({ required: true })}
          />
          <Form.Control
            name="paperIdType"
            ref={register({ required: true })}
            as="select"
            custom
          >
            <option>ARXIV</option>
            <option>DOI</option>
            <option>SEMANTIC</option>
          </Form.Control>
          <FormControl
            placeholder="Paper Title"
            aria-label="Paper Title"
            name="paperTitle"
            aria-describedby="basic-addon2"
            ref={register({ required: true })}
          />
          <InputGroup.Append>
            <Button onClick={handleSubmit(handleFormSubmit)} variant="outline-secondary">
              Add
            </Button>
          </InputGroup.Append>
        </InputGroup>
      </div>

    <br/>
    <h5 className="menuHeader">Edit Related Papers</h5>
    <div style={{'maxHeight': 'calc(100vh - 210px)', 'overflowY': 'auto', 'border': 'solid gray 1px'}}>
      {elements.map((obj) => (
        <Card body key={obj.paperId}>
          {obj.title} ({obj.paperId}/{obj.paperIdType}){' '}
          {/* <FontAwesomeIcon onClick={() => handleRemove(obj.paperId)} style={{cursor: "pointer"}} icon={faTrash} /> */}
          <Button variant={"danger"} style={{ padding: "0.15rem 0.3rem" }} onClick={() => handleRemove(obj.paperId)}>
            <FontAwesomeIcon icon={faTimes} />
          </Button>
        </Card>
      ))}

      <br></br>

    </div>
    </>
  );
};

export default React.memo(RelatedWorkdsTagBox);
