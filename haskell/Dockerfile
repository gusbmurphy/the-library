FROM haskell:9.6 AS build
WORKDIR /app

# Copy all source files
COPY . .

# Build with stack, installing GHC if needed
RUN stack build --install-ghc --copy-bins

FROM debian:bookworm-slim
RUN apt-get update && \
    apt-get install -y --no-install-recommends libgmp10 curl && \
    rm -rf /var/lib/apt/lists/*

COPY --from=build /root/.local/bin/library-app /usr/local/bin/

EXPOSE 8080
CMD ["library-app"]
