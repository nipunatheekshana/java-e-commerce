#!/usr/bin/env bash
# Build the Docker image and run the E-Commerce Management System interactively.
# Usage: ./run.sh
set -euo pipefail

IMAGE="ecommerce-mgmt:latest"

echo ">> Building image ($IMAGE) ..."
docker build -t "$IMAGE" .

echo ">> Starting application (Ctrl-C to quit) ..."
docker run --rm -it "$IMAGE"
