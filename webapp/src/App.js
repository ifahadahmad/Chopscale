import './assets/css/app.css';
import { useState } from 'react';
import { Navbar } from './components/Navbar';
import { Uploader } from './components/Uploader';
import { Message } from './components/Message';


function App() {

  const [mode,setMode]  = useState(0)

  return (
    <>
      <Navbar/>
      <div className='App'>
        <Uploader mode={mode}/>
        <Message/>
      </div>
    </>
  );
}

export default App;
