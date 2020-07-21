import React from 'react';
import './App.css';

import Layout from './hoc/Layout/Layout';
import Graph from './containers/Graph/Graph';

function App() {
  

  return (
    <div className="App">
      <Layout >  
        <Graph></Graph>
      </Layout>
    </div>
  );
}

export default App;
