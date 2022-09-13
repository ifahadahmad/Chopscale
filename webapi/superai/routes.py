from errno import errorcode
import os
import secrets
import numpy as np
from superai import app
from flask import request,jsonify
from PIL import Image,ExifTags
import cv2
from superai.bgremover.u2net_test import main as remover
# from superai.upscaler.root import main as upscaler
# from superai.upscaler.app import inference as upscaler
from superai.upscaler.inference import main as upscaler
from superai.util import main as cleanUp







@app.route("/api/<param>",methods=["POST"])
def operate(param):
    print(request.files)
    if 'image' in request.files:
        img = request.files.get("image",'')
        folder_hex = secrets.token_hex(16)
        os.mkdir(os.path.join(app.root_path,"static","input",folder_hex))
        img_hex = secrets.token_hex(8)
        _,f_ext = os.path.splitext(img.filename)
        img_name = img_hex+f_ext
        in_base = os.path.join(app.root_path,"static","input",folder_hex)
        img_path = os.path.join(in_base,img_name)
        out_base = os.path.join(app.root_path,"static","output")
        # try:
        #     image=Image.open(img)

        #     for orientation in ExifTags.TAGS.keys():
        #         if ExifTags.TAGS[orientation]=='Orientation':
        #             break
            
        #     exif = image._getexif()
        

        #     if(exif and exif[orientation] == 3):
        #         image=image.rotate(180, expand=True)
        #     elif exif and exif[orientation] == 6:
        #         image=image.rotate(270, expand=True)
        #     elif exif and exif[orientation] == 8:
        #         image=image.rotate(90, expand=True)

        #     image.save(img_path)
        # except (AttributeError, KeyError, IndexError):
        #     pass
        img = Image.open(img)
        if(param=="upscale"):
            img.thumbnail((900,900),Image.ANTIALIAS)
            basewidth = 256
            wpercent = (basewidth/float(img.size[0]))
            hsize = int((float(img.size[1])*float(wpercent)))
            img = img.resize((basewidth,hsize), Image.ANTIALIAS)
            img.save(img_path)
            try:
                
                out_path = os.path.join(out_base,img_hex+'.png')
                upscaler(in_base,out_path)
                
                # upscaler2(img_path,img_hex)
                return jsonify(
                    error=False,
                    errorCode=200,
                    data=os.path.join("http://localhost:5000","static","output",img_hex+".png"),
                )
            except Exception as err:
                print(err)
                return jsonify(
                    error=True,
                    errorCode=500,
                    message="Error upscaling the image"
                )
            finally:
                cleanUp(in_base,out_base)

        elif(param=="remove"):
            img.save(img_path)
            try:
                
                remover(folder_hex)
                path = os.path.join(out_base,img_hex+".png")
                imgg = cv2.imread(path)
                img2 = cv2.imread(img_path)
                img_gray = cv2.cvtColor(imgg, cv2.COLOR_BGR2GRAY)
                # (threshi, img_bw) = cv2.threshold(img_gray, 0, 255, cv2.THRESH_BINARY | cv2.THRESH_OTSU)
                # img_bw = cv2.cvtColor(img_bw,cv2.COLOR_GRAY2BGR)
                # print(img_bw.shape)
                # print(img2.shape)
                # res = cv2.bitwise_and(img2,img_bw) 
                result = np.zeros((img2.shape[0],img2.shape[1],4),dtype=np.uint8)
                result[:,:,0:3] = img2
                result[:,:,3] = img_gray
                
                cv2.imwrite(path,result)
                
                return jsonify(
                    error=False,
                    errorCode = 200,
                    data = os.path.join("http://localhost:5000","static","output",img_hex+".png")
                )
            except cv2.error as e:
                print(e)
                return jsonify(
                    error=True,
                    errorcode=500,
                    message="Error Removing background"
                )
            finally:
                cleanUp(in_base,out_base)

            
    return jsonify(
        error = True,
        errorCode  = 400,
        message = "Error with image"
    )
