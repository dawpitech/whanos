# Whanos

"Workflow-based Highly-Automated Network Orchestration Service"

## How to setup

### Prerequisites

1. Ansible (version >=2.17.0)

### Remote expected configuration

At least 2Gb of RAM, 4vcore and 16go of disk space  
Supported distributions: Debian12, Debian13

### Setup instructions

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

## How to use

### Adding your project repository to Jenkins

Will do later, I promise

## Notes for developpers:

To push docker images using HTTP :
sudo mkdir -p /etc/docker

Create /etc/docker/daemon.json with:
   {
     "insecure-registries": ["192.168.1.146:5000"]
   }
