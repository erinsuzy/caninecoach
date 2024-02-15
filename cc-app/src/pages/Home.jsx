import React from 'react'
import vid1 from '../Components/Assets/vid1.mp4';


import { Header, Navbar} from '../Components/common'

function Home() {
  return (
    <div>
        <Navbar/>
        <Header/>

         <div className='App>'>
            <video autoplay loop muted
              style={{
                position: 'absolute',
                width: '100%',
                left: '50%',
                top: '50%',
                height:'100%',
                objectFit:'cover',  
                transform:'translate((-50%, -50%)',
                zIndex:'-1'
              }} 
            >
              <source src={vid1} type='video/mp4'/>
            </video>

         </div>
        <h2>Home Page</h2>
    </div>
  )
}
export default Home;