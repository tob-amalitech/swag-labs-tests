# ─────────────────────────────────────────────────────────────
# Stage 1: Build – compile the project and download dependencies
# ─────────────────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-11 AS build

WORKDIR /app

# Copy POM first for layer caching (dependencies won't re-download
# unless pom.xml changes)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and TestNG suite
COPY src ./src
COPY testng.xml .

# Compile only (tests run in the final stage)
RUN mvn compile test-compile -B

# ─────────────────────────────────────────────────────────────
# Stage 2: Runtime – install Chrome and run the tests
# ─────────────────────────────────────────────────────────────
FROM eclipse-temurin:11-jdk

# Install Chrome and required system libraries
RUN apt-get update && apt-get install -y \
    wget \
    gnupg \
    ca-certificates \
    unzip \
    fonts-liberation \
    libappindicator3-1 \
    libasound2 \
    libatk-bridge2.0-0 \
    libatk1.0-0 \
    libcups2 \
    libdbus-1-3 \
    libgdk-pixbuf2.0-0 \
    libnspr4 \
    libnss3 \
    libx11-xcb1 \
    libxcomposite1 \
    libxdamage1 \
    libxrandr2 \
    xdg-utils \
    --no-install-recommends \
    && wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | apt-key add - \
    && echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" \
       >> /etc/apt/sources.list.d/google-chrome.list \
    && apt-get update \
    && apt-get install -y google-chrome-stable --no-install-recommends \
    && rm -rf /var/lib/apt/lists/*

# Copy Maven repo and compiled project from build stage
COPY --from=build /root/.m2 /root/.m2
COPY --from=build /app /app

WORKDIR /app

# Install Maven CLI in runtime stage
RUN apt-get update && apt-get install -y maven --no-install-recommends \
    && rm -rf /var/lib/apt/lists/*

# Expose surefire reports directory as a volume (optional)
VOLUME ["/app/target/surefire-reports"]

# Default command: run the full TestNG suite
CMD ["mvn", "test", "-B", "--no-transfer-progress"]
