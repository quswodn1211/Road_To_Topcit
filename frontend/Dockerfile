FROM nginx:alpine

COPY ./config/nginx.conf /etc/nginx/conf.d/nginx.conf

COPY ./html/ /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
