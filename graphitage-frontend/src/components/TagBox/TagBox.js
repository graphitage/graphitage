import { List } from "immutable";
import React, { useState, useEffect } from "react";
import { TagBoxAsync } from "react-tag-box";
import "./styles.scss";

const fetch = (input, tags) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve(tags.filter((t) => t.label !== undefined && t.label.includes(input)).toJS());
    }, 1500);
  });
};

const TagBox = (props) => {
  const [selected, setSelected] = useState(List());

  const { onChange } = props;
  useEffect(() => {
    onChange(
      selected
        .valueSeq()
        .map((seq) => seq.value)
        .toArray()
    );
  }, [selected, onChange]);


  const { load } = props
  useEffect(() => {
    if(Array.isArray(load) && load.length > 0)
      setSelected(List(load.map((t) => ({
        label: t,
        value: t,
      }))))
  }, [load])
  
  let tags = List();
  if(Array.isArray(props.tags)){
    tags = List()
    props.tags.map((t) => ({
      label: t,
      value: t,
    }))
  }

  const onSelect = (tag) => {
    const newTag = {
      label: tag.label,
      value: tag.value || tag.label,
    };

    if (selected.map((t) => t.value).includes(newTag.value)) {
      return;
    }

    setSelected(selected.push(newTag));
  };

  const remove = (tag) => {
    setSelected(selected.filter((t) => t.value !== tag.value));
  };

  // const placeholder = selected.isEmpty()
  //   ? props.placeholder
  //   : "Use the backspace key to delete the last tag";

  const placeholder = props.placeholder;

  return (
    <TagBoxAsync
      fetch={(input) => fetch(input, tags)}
      selected={selected.toJS()}
      onSelect={onSelect}
      removeTag={remove}
      backspaceDelete={true}
      placeholder={placeholder}
    />
  );
};

export default TagBox;
