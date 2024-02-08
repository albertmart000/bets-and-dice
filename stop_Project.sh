# Delete all images dangling
docker image prune

# Delete all images unused
docker image prune --filter="label=unused"

# Delete all images dangling and unused
docker image prune -a

# Delete all volume dangling
docker volume prune

# Delete all
docker system prune -a --volumes

#WARNING! This will remove:
#        - all stopped containers
#        - all networks not used by at least one container
#        - all anonymous volumes not used by at least one container
#        - all images without at least one container associated to them
#        - all build cache

# To not prompt for confirmation add -f