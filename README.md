# Whanos

"Workflow-based Highly-Automated Network Orchestration Service"

## How to setup

First based on `ansible/group_vars/example.yml` fill the file with the required informations and generate an ansible vault with your secrets in it, you should call it `all.yml`.

```bash
cd ansible
export ANSIBLE_VAULT_PASSWORD_FILE=/tmp/.vault_pass
echo verySecretPassword > /tmp/.vault_pass
ansible-vault encrypt group_vars/all.yml
```

Next populate `ansible/inventory.yml` (and more especially the hub-1 remote) with your target ip address.  
Run the playbook to apply this configuration to your target

```bash
ansible-galaxy install -r requirements.yml
ansible-playbook -i inventory.yaml playbook.yml
```

Navigate to `<target_ip>:8080` to access the jenkins instance interface

## Notes for developpers:

To push docker images using HTTP :
sudo mkdir -p /etc/docker

Create /etc/docker/daemon.json with:
   {
     "insecure-registries": ["192.168.1.146:5000"]
   }
