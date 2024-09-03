FROM node:19-alpine
# Define the build argument
ARG api_base_url
# Set the working directory
WORKDIR /app
# Copy package.json files and install dependencies
COPY package*.json ./
RUN npm install --silent
# Copy the rest of the application code
COPY . .
# Use the build argument to set the environment variable in .env file
RUN echo "VITE_API_BASE_URL=${api_base_url}" > .env
# Expose the port that your application will run on
EXPOSE 5173
# Command to start the application
CMD ["npm", "run", "dev"]