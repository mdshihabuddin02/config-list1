#!/bin/bash

# Function to check if the image reference contains "sonarqube" or "postgres"
function is_whitelisted() {
    local image_reference=$1
    if [[ $image_reference == *"sonarqube"* || $image_reference == *"postgres"* ]]; then
        return 0
    fi
    return 1
}

# Get a list of Docker images
image_list=$(docker images --format "{{.Repository}}:{{.Tag}}")

# Loop through the images and delete non-whitelisted ones
while read -r image_reference; do
    if ! is_whitelisted "$image_reference"; then
        docker rmi "$image_reference"
    fi
done <<< "$image_list"

# Clean up any leftover resources
docker system prune -f
