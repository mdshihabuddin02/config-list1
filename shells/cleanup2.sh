#!/bin/bash

# Function to check if the image reference contains "sonarqube" or "postgres"

whitelisted_names=("sonarqube" "postgres")

# Function to check if the name is in the whitelisted names list
function is_whitelisted() {
    local name=$1
    for whitelisted_name in "${whitelisted_names[@]}"; do
        if [[ "$name" == *"$whitelisted_name"* ]]; then
            return 0
        fi
    done
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

