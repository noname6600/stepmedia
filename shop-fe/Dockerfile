# Stage 1: Build frontend
FROM node:22 AS build
WORKDIR /app

COPY package.json yarn.lock ./
RUN yarn install --frozen-lockfile
COPY . .
COPY .env .env
RUN yarn build  

# Stage 2: 
FROM alpine
WORKDIR /output
COPY --from=build /app/dist ./
CMD ["sh", "-c", "cp -r /output/* /frontend-dist/"]
