FROM node:22.14 as build
RUN mkdir frontend-container
WORKDIR /frontend-container
COPY . .
RUN npm install
RUN npm run build

FROM nginx:alpine

COPY --from=build /frontend-container/dist /usr/share/nginx/html
COPY ./nginx/nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80