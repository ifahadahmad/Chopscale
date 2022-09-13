import { useEffect,useState,useRef } from "react"

import '../assets/css/showoptions.css'
import { Error } from "./Error"
import { Operate } from "./Operate"

export const ShowOptions = ({image,setImage})=>{

    const [url,setUrl] = useState(0)
    const [response,setResponse] = useState({
        present:false,
        loading:false,
        error:false,
        errors:null
    })
	const imageRef = useRef()
    const fetchImage =async (slug) =>{
		setResponse({...response,loading:true})
        const formData = new FormData()

        formData.append("image",image)
		if(image===undefined)
			return
        const options = {
            method: 'POST',
            headers: {
				'Access-Control-Allow-Origin': 'http://localhost:3000',
				'Access-Control-Allow-Credentials': 'true',
			},
            body : formData,
            
        };
       
        
        const resp = await fetch('http://localhost:5000/api/'+slug,options).catch((err)=>{
			setResponse({...response,present:true,loading:false,error:true,errors:"Failed to Connect,Make Sure to active Internet Connection!"})
			return
		})
        if(!resp.ok){
            const res = await resp.json()
            setResponse({...response,present:true,loading:false,error:true,errors:res.message})
            return
        }
        const res = await resp.json()
		const url = res.data;
		console.log(url)
		const imageBlob = await fetch(url,
			{
				method:'GET',
				headers: {
					'Access-Control-Allow-Origin': 'http://localhost:3000',
					'Access-Control-Allow-Credentials': 'true',
				},
			}).then((ress)=>ress.blob())
		
		imageRef.current = imageBlob
		setResponse({...response,present:true,loading:false})

    }

    useEffect(()=>{
        
        setUrl(URL.createObjectURL(image))

    },[])
    const View = ({isLoading})=>{


        return (
            <div className="optionContainer">
                <div className={"imageContainer"+(isLoading?" shimmer":"")}>
    
                    <img id="optionimg" src={url}/>
    
                </div>
    
                <div className="options">
                    <button style={{display:isLoading?"none":"block"}} onClick={()=>fetchImage("upscale")} className="upscale">Upscale</button>
                    <button style={{display:isLoading?"none":"block"}} onClick={()=>fetchImage("remove")} className="removebg">RemoveBG</button>
                </div>
    
    
    
            </div>
        )
    
    }
    
    

    return <>
       
        {response.present?response.error? <Error message={response.errors} setImage={setImage}/>:<Operate image={image} setImage={setImage} imageBlob={imageRef.current}/>:response.loading?<View isLoading={true}/>:<View isLoading={false}/>}
    
    
    </>


}
