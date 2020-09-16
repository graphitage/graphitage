import React, { useState, useCallback } from "react";
import { connect } from "react-redux";

import SearchMenu from "../../containers/SearchMenu/SearchMenu";
import Toolbar from "../../components/Navigation/Toolbar/Toolbar";
import DetailsPanel from "../../components/DetailsPanel/DetailsPanel";
import OptionsMenu from "../../containers/OptionsMenu/OptionsMenu";
import ErrorModal from "../../components/ErrorModal/ErrorModal";
import LoadingScreen from "../../components/LoadingScreen/LoadingScreen";

import * as actionTypes from "../../store/actions/actionTypes";

import * as actionCreators from "../../store/actions/index";

const styles = {
  content: { height: "calc(100% - 60px)" },
};

const Layout = (props) => {
  const [searchMenu, setSearch] = useState(false);
  const [optionsMenu, setOptions] = useState(false);
  const [detailsMenu, setdetailsMenu] = useState(false);
  const [nodeId, setNodeId] = useState("");
  const [detailOnEdit, setDetailOnEdit] = useState(false);
  const [collapsedDetails, setCollapsedDetails] = useState(true);

  const searchClosedHandler = useCallback(() => {
    setSearch(false);
  }, []);

  const searchClickedHandler = useCallback(() => {
    setSearch(true);
    setOptions(false);
    setdetailsMenu(false);
  }, []);

  const optionsClosedHandler = useCallback(() => {
    setOptions(false);
  }, []);

  const optionsClickedHandler = useCallback(() => {
    setOptions(true);
    setSearch(false);
    setdetailsMenu(false);
  }, []);

  const { switchClearGraph } = props;
  const clearGraph = useCallback(() => {
    switchClearGraph(true);
  }, [switchClearGraph]);

  const detailsPanelOpenedHandler = useCallback((id) => {
    setdetailsMenu(true);
    setOptions(false);
    setSearch(false);
    setNodeId(id);
    setDetailOnEdit(false);
  }, []);

  const detailsPanelClosedHandler = useCallback(() => {
    setdetailsMenu(false);
    setDetailOnEdit(false);
    // props.onClearDetails();
  }, []);

  return (
    <React.Fragment>
      <SearchMenu
        search={searchMenu}
        searchClosed={searchClosedHandler}
      ></SearchMenu>
      <OptionsMenu
        options={optionsMenu}
        optionsClosed={optionsClosedHandler}
        collapsedDetails={collapsedDetails}
        setCollapsedDetails={setCollapsedDetails}
      ></OptionsMenu>
      <DetailsPanel
        detailsClosed={detailsPanelClosedHandler}
        details={detailsMenu}
        nodeId={nodeId}
        isEditing={detailOnEdit}
        setEditing={setDetailOnEdit}
      />
      <Toolbar
        searchClick={searchClickedHandler}
        optionsClick={optionsClickedHandler}
        clearGraph={clearGraph}
      ></Toolbar>
      <main style={styles.content}>
        {React.cloneElement(props.children, {
          detailsMenuHandler: detailsPanelOpenedHandler,
        })}
      </main>
      {props.showError && <ErrorModal />}
      {props.loading && <LoadingScreen />}
    </React.Fragment>
  );
};

const mapStateToProps = (state) => {
  return {
    clr: state.clearNodes,
    showError: state.ui.errorModal.error,
    loading: state.ui.loadingScreen.loading,
    toHideNodeId: state.graph.toHideNodeId,
  };
};

const mapDispatchToProps = (dispatch) => {
  return {
    switchClearGraph: (isActive) =>
      dispatch({ type: actionTypes.CLEAR_NODES, bool: isActive }),
    onSetToHideNodeId: (id) => dispatch(actionCreators.setToHideNodeId(id)),
    onClearDetails: () => dispatch(actionCreators.clearDetails()),
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(Layout);
