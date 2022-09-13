import os.path as osp
import cv2
import numpy as np
import torch
from superai.upscaler import RRDBNet_arch as arch
import os
from superai import app


model_path = os.path.join(app.root_path,"upscaler","models","RRDB_PSNR_x4.pth") # models/RRDB_ESRGAN_x4.pth OR models/RRDB_PSNR_x4.pth
device = torch.device('cuda')  # if you want to run on CPU, change 'cuda' -> cpu




model = arch.RRDBNet(3, 3, 64, 23, gc=32)
model.load_state_dict(torch.load(model_path), strict=True)
model.eval()
model = model.to(device)




def main(path,image_hex):

    base = osp.splitext(osp.basename(path))[0]
    img = cv2.imread(path, cv2.IMREAD_COLOR)
    img = img * 1.0 / 255
    img = torch.from_numpy(np.transpose(img[:, :, [2, 1, 0]], (2, 0, 1))).float()
    img_LR = img.unsqueeze(0)
    img_LR = img_LR.to(device)
    torch.cuda.empty_cache()
    with torch.no_grad():
        output = model(img_LR).data.squeeze().float().cpu().clamp_(0, 1).numpy()
    output = np.transpose(output[[2, 1, 0], :, :], (1, 2, 0))
    output = (output * 255.0).round()
    output_path = os.path.join(app.root_path,"static","output",f'${image_hex}.png')
    cv2.imwrite(output_path, output)



