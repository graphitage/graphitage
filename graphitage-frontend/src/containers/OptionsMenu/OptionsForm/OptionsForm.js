import React, { useState, useRef } from "react";

import Form from "react-bootstrap/Form";
import { useForm } from "react-hook-form";

import { connect } from "react-redux";

import * as actionCreators from "../../../store/actions/index";
import httpReq from "../../../store/actions/utils/http";

const OptionsForm = (props) => {
  // const { register, handleSubmit, errors, watch, formState }
  const { register, handleSubmit } = useForm({
    mode: "onChange",
  });
  const [isSuccess, setIsSuccess] = useState(false);
  const [isCorrectFileType, setIsCorrectFileType] = useState(true);
  const form = useRef(null);

  const onSubmit = (data) => {
    const objectURL = URL.createObjectURL(data.jsonFile[0]);
    let fullName = data.jsonFile[0].name;
    let fileExtension = fullName.slice(fullName.lastIndexOf(".") + 1);

    async function getData(url) {
      const response = await fetch(url);
      return response.json();
    }

    async function runConn() {
      props.onOpenLoadingScreen();

      const jsonData = await getData(objectURL);

      httpReq("papers", "POST", JSON.stringify(jsonData)).then((result) => {
        if (!result.error) {
          setIsSuccess(true);
          props.onClearGraph(true);
          props.onSimpleExpand();

          props.onCloseLoadingScreen();
        } else {
          setIsSuccess(false);
          props.onCloseLoadingScreen();
          props.onOpenErrorModal("Either wrong JSON format or server is not responding!");
        }
      });
    }

    if (fileExtension.toLowerCase() === "json") {
      runConn();
    } else {
      //file type not supported
      // props.onOpenErrorModal("File Type Is Not Supported. Please Choose a \"JSON\" File.");
      setIsCorrectFileType(false);
    }
  };

  const onFileChange = (data) => {
    setIsSuccess(false);
    setIsCorrectFileType(true);
    form.current.dispatchEvent(new Event("submit", { cancelable: true }));
  };

  return (
    <Form ref={form} onSubmit={handleSubmit(onSubmit)}>
      <Form.Group controlId="optionsFormJSON">
        <Form.File
          id="jsonFile"
          placeholder="jsonFile"
          onChange={onFileChange}
          name="jsonFile"
          ref={register()}
          accept="application/JSON"
        />
      </Form.Group>

      <div>{isSuccess && <h4 style={{ color: "green" }}>Success!</h4>}</div>
      <div>
        {!isCorrectFileType && (
          <h4 style={{ color: "red", fontSize: "1rem" }}>
            File type is not supported. Please choose a "JSON" file.
          </h4>
        )}
      </div>
    </Form>
  );
};

const mapStateToProps = (state) => {
  return {};
};

const mapDispatchToProps = (dispatch) => {
  return {
    onClearGraph: (bool) => dispatch(actionCreators.clearNodes(bool)),
    onSimpleExpand: (sourceNode) =>
      dispatch(actionCreators.simpleExpand(sourceNode)),
    onOpenErrorModal: (errorMessage) =>
      dispatch(actionCreators.openErrorModal(errorMessage)),
    onOpenLoadingScreen: () => dispatch(actionCreators.openLoadingScreen()),
    onCloseLoadingScreen: () => dispatch(actionCreators.closeLoadingScreen()),
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(OptionsForm);
