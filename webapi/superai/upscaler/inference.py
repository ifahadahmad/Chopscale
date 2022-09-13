import cv2
import glob
import numpy as np
import os
import torch
from superai import app
from superai.upscaler.models.network_swinir import SwinIR as net


def main(folder,output_path):
    model_path = os.path.join(app.root_path,"upscaler","pretrained_models","003_realSR_BSRGAN_DFO_s64w8_SwinIR-M_x4_GAN")+'.pth'
    scale = 4

    device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
    

    model = define_model(model_path)
    model.eval()
    model = model.to(device)

    window_size = 8
    

    for idx, path in enumerate(sorted(glob.glob(os.path.join(folder, '*')))):
        # read image
        img_lq = get_image_pair(path)  # image to HWC-BGR, float32
        img_lq = np.transpose(img_lq if img_lq.shape[2] == 1 else img_lq[:, :, [2, 1, 0]], (2, 0, 1))  # HCW-BGR to CHW-RGB
        img_lq = torch.from_numpy(img_lq).float().unsqueeze(0).to(device)  # CHW-RGB to NCHW-RGB

        with torch.no_grad():
            # pad input image to be a multiple of window_size
            _, _, h_old, w_old = img_lq.size()
            h_pad = (h_old // window_size + 1) * window_size - h_old
            w_pad = (w_old // window_size + 1) * window_size - w_old
            img_lq = torch.cat([img_lq, torch.flip(img_lq, [2])], 2)[:, :, :h_old + h_pad, :]
            img_lq = torch.cat([img_lq, torch.flip(img_lq, [3])], 3)[:, :, :, :w_old + w_pad]
            output = model(img_lq)
            output = output[..., :h_old * scale, :w_old * scale]

        # save image
        output = output.data.squeeze().float().cpu().clamp_(0, 1).numpy()
        if output.ndim == 3:
            output = np.transpose(output[[2, 1, 0], :, :], (1, 2, 0))  # CHW-RGB to HCW-BGR
        output = (output * 255.0).round().astype(np.uint8)  # float32 to uint8
        cv2.imwrite(output_path, output)

        

def define_model(model_path):

    model = net(upscale=4, in_chans=3, img_size=64, window_size=8,
                img_range=1., depths=[6, 6, 6, 6, 6, 6], embed_dim=180, num_heads=[6, 6, 6, 6, 6, 6],
                mlp_ratio=2, upsampler='nearest+conv', resi_connection='1conv')
    param_key_g = 'params_ema'

   
    
    pretrained_model = torch.load(model_path)
    model.load_state_dict(pretrained_model[param_key_g] if param_key_g in pretrained_model.keys() else pretrained_model, strict=True)
        
    return model





def get_image_pair(path):
    img_lq = cv2.imread(path, cv2.IMREAD_COLOR).astype(np.float32) / 255.
    return img_lq

