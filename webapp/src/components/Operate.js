import React, { useEffect, useMemo, useState } from "react";
import { Loader } from "./Loader";
import '../assets/css/operate.css'
import FileSaver from "file-saver";
import main from "../assets/js/operate";
import {ShowOptions} from "./ShowOptions";
// import '../assets/js/operate.js'

const ImageFile = ({file,image}) => {
    const [src, setSrc] = React.useState([0,0]);
    
    React.useEffect(() => {
		const reader = new FileReader();
		reader.onload = (e) => {
			setSrc([e.target.result,URL.createObjectURL(image)]);
		};
		reader.readAsDataURL(file);
	}, [setSrc, file]);
    
    React.useEffect(()=>{
        main()
},[])

    if(!src) return null;
    
    return (
        <div id="image-comparison-slider">
            <img src={src[0]} alt="after"/>
            <div class="img-wrapper">
                <img src={src[1]} alt="before"/>
            </div>
            <span class="label label-after">After</span>
            <span class="label label-before">Before</span>
            <div class="handle">
                <div class="handle-line"></div>
                <div class="handle-circle">
                <i class="fas fa-chevron-left"></i>
                <i class="fas fa-chevron-right"></i>
                </div>
                <div class="handle-line"></div>
            </div>
        </div>



    )


    
}

export const Operate = ({image,setImage,imageBlob}) => {
   
    const download = () => {

        FileSaver.saveAs(imageBlob,"download.png")

    }
    const back = () => {

        setImage(null)

    }
    
    return (
        <>
        
        
        <div className="container">
        
            <ImageFile image={image} file={imageBlob}/>
            <div className="sub-container">
                <div onClick={download} className="download">Download</div>
                <div onClick={back} className="back">Go back</div>
            </div>
            
            
        </div>
        
        
        
        
        </>
    );
}