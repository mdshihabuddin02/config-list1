#!/bin/bash

# Array list of whitelisted names (containers, volumes, and networks)
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

# Stop and remove all running containers (except whitelisted)
container_list=$(docker ps -a --format "{{.Names}}")
while read -r container_name; do
    if ! is_whitelisted "$container_name"; then
        docker stop "$container_name" >/dev/null
        docker rm "$container_name" >/dev/null
    fi
done <<< "$container_list"

# Remove all volumes (except whitelisted)
volume_list=$(docker volume ls --format "{{.Name}}")
while read -r volume_name; do
    if ! is_whitelisted "$volume_name"; then
        docker volume rm "$volume_name" >/dev/null
    fi
done <<< "$volume_list"

# Remove all networks (except whitelisted)
network_list=$(docker network ls --format "{{.Name}}")
while read -r network_name; do
    if ! is_whitelisted "$network_name"; then
        docker network rm "$network_name" >/dev/null
    fi
done <<< "$network_list"

# Clean up any leftover resources
docker system prune -f

