import Err from '../assets/images/error.jpg'
import '../assets/css/showoptions.css'


export const Error = ({message,setImage})=>{

    return (

        <div className='optionContainer'>

            <div className='imageContainer'>
                <img id='optionimg' src={Err}/>
            </div>
            <div style={{marginTop:"30px",fontSize:"20px",fontFamily:"sans-serifs",fontWeight:"500"}} className='errorMessage'>

                {message}

            </div>
            <div style={{marginTop:"17px"}} className='options'>
                <button onClick={()=>setImage(null)}>Go Back</button>
            </div>
        </div>

    )

}