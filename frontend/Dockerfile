FROM node:22.14
RUN mkdir frontend-container
WORKDIR /frontend-container
COPY . .
RUN npm install
RUN npm run build
EXPOSE 4173
ENTRYPOINT [ "npm", "run", "preview" ]