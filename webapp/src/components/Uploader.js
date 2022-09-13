import React, { useCallback, useEffect, useRef, useState } from 'react';
import { Dropbox } from './Dropbox';
import { Operate } from './Operate';
import { useDropzone } from 'react-dropzone';
import { ShowOptions } from './ShowOptions';

export const Uploader = ({mode}) => {
	const [image,setImage] = useState()
	
   
	
	const onDrop = useCallback(
		(Uploadedfiles) => {
			if (Uploadedfiles.length > 0 && Uploadedfiles.length < 2) {
				setImage(Uploadedfiles[0])
			}else{

            }
		},
		[setImage],
	);
	const { getRootProps, getInputProps } = useDropzone({ onDrop, accept: 'image/jpeg, image/png, image/gif, image/svg+xml' });
	return (
		<>{image ? <ShowOptions image={image} setImage={setImage}/> : <Dropbox getInputProps={getInputProps} getRootProps={getRootProps} />}</>
	);
};
