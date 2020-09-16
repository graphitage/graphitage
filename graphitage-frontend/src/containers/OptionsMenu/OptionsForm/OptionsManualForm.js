import React from "react";

// import Form from "react-bootstrap/Form";
// import { useForm } from "react-hook-form";

import { connect } from "react-redux";

import * as actionCreators from "../../../store/actions/index";
// import httpReq from "../../../store/actions/utils/http";


const OptionsManualForm = (props) => {
  // const { register, handleSubmit, errors, watch, formState }
  // const { register, handleSubmit } = useForm({
  //   mode: "onChange",
  // });
  // const [isSuccess, setIsSuccess] = useState(false);
  // const [isCorrectFileType, setIsCorrectFileType] = useState(true);
  // const form = useRef(null);

  // const onSubmit = (data) => {
  //   const objectURL = URL.createObjectURL(data.jsonFile[0]);
  //   let fullName = data.jsonFile[0].name;
  //   let fileExtension = fullName.slice((fullName.lastIndexOf('.') + 1));

  //   async function getData(url) {
  //     const response = await fetch(url);
  //     return response.json();
  //   }

  //   async function runConn() {
  //     console.log("loading start");
  //     props.onOpenLoadingScreen();

  //     const jsonData = await getData(objectURL);

  //     httpReq("papers", "POST", JSON.stringify(jsonData))
  //       .then((result) => {
  //         setIsSuccess(true);
  //         props.onClearGraph(true);
  //         props.onSimpleExpand();
          
  //         console.log("loading finish");
  //         props.onCloseLoadingScreen();
  //       })
  //       .catch((error) => {
  //         setIsSuccess(false);

  //         console.log("loading finish");
  //         props.onCloseLoadingScreen();
  //       });
  //   }

  //   if(fileExtension.toLowerCase() === "json"){
  //     runConn();
  //   }else{ //file type not supported
  //     // props.onOpenErrorModal("File Type Is Not Supported. Please Choose a \"JSON\" File.");
  //     setIsCorrectFileType(false);
  //   }
  // };

  // const onFileChange = (data) => {
  //   setIsSuccess(false);
  //   setIsCorrectFileType(true);
  //   form.current.dispatchEvent(new Event("submit", { cancelable: true }));
  // };

  return (
    <p>Form Here</p>
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
    onOpenErrorModal: (errorMessage) => dispatch(actionCreators.openErrorModal(errorMessage)),
    onOpenLoadingScreen: () => dispatch(actionCreators.openLoadingScreen()),
    onCloseLoadingScreen: () => dispatch(actionCreators.closeLoadingScreen()),
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(OptionsManualForm);
