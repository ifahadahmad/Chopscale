## ChopScale &middot; [![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

```
1. ChopScale is an Open Source Production Grade Image Background Remover And Upscaler Web Application And Android App.
2. Privacy friendly, no database, no analytics, no logs, no cookies and auto image deletion in two days after upload.
3. It Operates on JPG, PNG images.
4. ChopScale is built with Flask, React.js and Kotlin Android.
5. Some Libraries used are Tensorflow,Pytorch,Opencv,Pillow.
6. ChopScale is Built on Top of U2net And Swinir.
7. Installation docs for GCP Ubuntu 22.04 is given below.
```

## Website

[ChopScale](http://34.124.139.56/)

## Android
[ChopScale](https://github.com/ifahadahmad/chopscale/blob/master/androidapp/app/release/chopscale.apk)

## Built With

* [Flask](https://flask.palletsprojects.com/en/2.2.x/) - Backend
* [TensorFlow](https://www.tensorflow.org/) - ML Library
* [Pytorch](https://pytorch.org/) - ML Library
* [U2net](https://github.com/xuebinqin/U-2-Net/) - Semantic Segmentation
* [Swinir](https://github.com/JingyunLiang/SwinIR/) - Image Super Resolution


## Author

* **Fahad Ahmad** - [LinkedIn](https://www.linkedin.com/in/fahad-ahmad-b042a7112/)   

## Screenshots

![alt text](https://github.com/twoabd/CompressioAPI/blob/main/api/docs/website/first.png?raw=true)   

![alt text](https://github.com/twoabd/CompressioAPI/blob/main/api/docs/api/default.png?raw=true)  

![alt text](https://github.com/twoabd/CompressioAPI/blob/main/api/docs/api/lossy.png?raw=true)  
 
![alt text](https://github.com/twoabd/CompressioAPI/blob/main/api/docs/api/lossless.png?raw=true) 


## How to Use API

#### Installation

```
Clone webapi.
Install anaconda/miniconda on your Machine.
Run conda env create -f environment.yml to install all the packages.
It will also install nvidia cudatoolkit and cudnn for working with Gpu.
Now set environment variable FLASK_APP to run.py and "flask run" to run locally.
```


#### Locally POST Request

```
It expects a Multipart Request with a image in a POST request at http://localhost:5000/upscale and http://localhost:5000/remove.     
Only one image at a Time.  
Size should be less than 5MB.  
```



## Deployment, API & Web

### Install Nginx and gunicorn on Ubuntu 22.04
```
sudo apt update
sudo apt install nginx -y

sudo apt-get install gunicorn
```

### Install all dependency

```

Run 'conda env create -f environment.yml' in your linux server.
conda activate api

```

### Create gunicorn service

```
sudo nano /etc/systemd/system/flaskapi.service


[Unit]
Description=flaskapi
After=network.target

[Service]
User=apple
Group=www-data
WorkingDirectory=path/to/your/working/dir
Environment="PATH=/path/to/environment/bin"
ExecStart=/path/to/environment/bin/gunicorn --workers 4 --bind unix:flaskapi.sock -m 007 run:app --access-logfile '-' --timeout 120

[Install]
WantedBy=multi-user.target


```

### Starting the flaskapi service
```

sudo systemctl start flaskapi.service
sudo systemctl enable flaskapi.service

```


#### Change chopscale.co.in to Your Domain At
```
/webapi/superai/routes.py

/webapp/src/components/ShowOptions.js

```

#### Remove Navbar Links From
```
/webapp/src/Components/Navbar.js
```



#### Updating Nginx conf in etc/nginx/nginx.conf
```
pid /run/nginx.pid;
include /etc/nginx/modules-enabled/*.conf;

events {
        worker_connections 768;
        multi_accept on;
}

http {

        # Basic Settings
        sendfile on;
        tcp_nopush on;
        tcp_nodelay on;
        keepalive_timeout 65;
        types_hash_max_size 2048;
        client_max_body_size 20M;

        include /etc/nginx/mime.types;
        default_type application/octet-stream;


        # SSL Settings
        ssl_protocols TLSv1 TLSv1.1 TLSv1.2 TLSv1.3;
        ssl_prefer_server_ciphers on;


        # Logging Settings
        access_log /var/log/nginx/access.log;
        error_log /var/log/nginx/error.log;


        # Gzip Settings
        gzip on;
        gzip_disable "msie6";
        gzip_vary on;
        gzip_proxied any;
        gzip_comp_level 6;
        gzip_buffers 16 8k;
        gzip_http_version 1.1;
        gzip_types
        application/javascript application/rss+xml application/vnd.ms-fontobject application/x-font
        application/x-font-opentype application/x-font-otf application/x-font-truetype application/x-font-ttf
        application/x-javascript application/xhtml+xml application/xml font/opentype font/otf font/ttf
        image/svg+xml image/x-icon text/css text/html text/javascript text/plain text/xml;

        include /etc/nginx/conf.d/*.conf;
        include /etc/nginx/sites-enabled/*;
}

```

#### Creating API Directory

```
sudo mkdir -p /var/www/api_domain_name/
sudo mkdir -p /var/www/client_domain_name/

sudo chown -R www-data:www-data /var/www/api_domain_name
sudo chown -R www-data:www-data /var/www/client_domain_name
sudo chmod -R 755 /var/www/api_domain_name
sudo chmod -R 755 /var/www/client_domain_name
```


#### Creating Virtual Host for api
```
sudo nano /etc/nginx/sites-available/api_domain_name
server {

    listen 81;
    listen [::]:81;

    server_name _;
    index index.html;
    add_header 'Access-Control-Allow-Origin' 'http://client_domain_name' always;
    add_header 'Access-Control-Allow-Credentials' 'true';
    add_header 'Access-Control-Allow-Methods' 'CONNECT, DEBUG, DELETE, DONE, GET, HEAD, HTTP, HTTP/0.9, HTTP/1.0, HTTP/1.1>    add_header 'Access-Control-Allow-Headers' 'Accept, Accept-CH, Accept-Charset, Accept-Datetime, Accept-Encoding, Accept>    add_header 'Access-Control-Expose-Headers' 'Accept, Accept-CH, Accept-Charset, Accept-Datetime, Accept-Encoding, Accep>


    # API Folder
    location /api {
        proxy_pass http://unix:/var/www/api_domain_name/image_api/flaskapi.sock;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_cache_bypass $http_upgrade;
        proxy_read_timeout 120s;
    }

    # Input/Output Folder
    location /static {
        alias /var/www/34.124.139.56/api_domain_name/superai/static;
        autoindex on;
    }

}

sudo ln -s /etc/nginx/sites-available/api_domain_name /etc/nginx/sites-enabled/
sudo unlink /etc/nginx/sites-enabled/default
sudo rm -rf /var/www/html
sudo systemctl restart nginx
```
#### Creating Virtual Host for client
```
sudo nano /etc/nginx/sites-available/client_domain_name

server{
    listen 80;
    listen [::]:80;

    root /var/www/client_domain_name/build;

    server_name _;
    index index.html index.htm;
    location / {
    }


}

sudo ln -s /etc/nginx/sites-available/client_domain_name /etc/nginx/sites-enabled/
sudo systemctl restart nginx
```



#### Installing SSL
```
sudo apt install certbot python3-certbot-nginx -y
sudo certbot --nginx -d client_domain_name
sudo certbot --nginx -d api_domain_name
sudo systemctl status certbot.timer
sudo certbot renew --dry-run
sudo systemctl restart nginx
```

#### Copy webapp to /var/www/client_domain_name
```

cd /var/www/client_domain_name
npm install
npm run build 
  
Delete everything else inside /var/www/client_domain_name/ except build folder
sudo systemctl restart nginx
```

### Android

```
Clone android.
Open existing project in Android Studio.
Build -> Make Project
```



## Acknowledgments

* Based on [Swinir](https://github.com/JingyunLiang/SwinIR/) and [U2net](https://github.com/xuebinqin/U-2-Net/).
* Webapp is inspired from [Compressio](https://www.compressio.app/)
