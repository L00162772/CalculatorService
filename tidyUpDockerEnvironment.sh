docker stop $(docker ps -aq)
docker container prune -f
docker image prune -a  -f
docker volume prune  -f
docker system prune -a -f