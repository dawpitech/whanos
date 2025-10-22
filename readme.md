To push docker images using HTTP :
sudo mkdir -p /etc/docker

Create /etc/docker/daemon.json with:
   {
     "insecure-registries": ["192.168.1.146:5000"]
   }
