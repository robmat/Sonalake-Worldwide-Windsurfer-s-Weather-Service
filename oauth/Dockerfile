FROM eclipse-temurin:21-jre-alpine

# Set working directory
WORKDIR /app

# Install curl and unzip
RUN apk add --no-cache curl unzip

# Copy the built distribution zip
COPY ./build/distributions/oauth-0.0.1-SNAPSHOT.zip /app

# Unzip and clean up
RUN unzip oauth-0.0.1-SNAPSHOT.zip && rm oauth-0.0.1-SNAPSHOT.zip

# Ensure the startup script is executable
RUN chmod +x oauth-0.0.1-SNAPSHOT/bin/oauth

# Expose the application port
EXPOSE 9000

# Run the app
ENTRYPOINT ["oauth-0.0.1-SNAPSHOT/bin/oauth"]
