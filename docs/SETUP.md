# How to setup

## Prerequisites

1. Ansible (version >=2.17.0)
2. Access to a container registry (e.g.: Digital Ocean Container Registry)
3. Access to a kubernetes cluster (e.g.: Digital Ocean Kubernetes)

> [!NOTE]
> Your selected kubernetes cluster should have read access on your docker registry

## Remote expected configuration

- RAM >= 2Go
- CPU >= 2vcore
- HDD >= 16Go

> [!NOTE]
> Supported distributions: Debian12, Debian13

You should have remote ssh access to your target with root capabilities.
If you choose to log as another user than `root` to apply your ansible playbook, please edit the corresponding line in `ansible/ansible.cfg`

```ini
remote_user=<username>
```

## Setup instructions

First based on `ansible/group_vars/example.yml` fill the file with the required informations and generate an ansible vault with your secrets in it, you should call it `all.yml`.  
You also need to provide `ansible/group_vars/k8s-config.yml`, this file is the kubectl config file loaded by the jenkins instance that will be used to deploy your projects.

```bash
cd ansible
export ANSIBLE_VAULT_PASSWORD_FILE=/tmp/.vault_pass
echo verySecretPassword > /tmp/.vault_pass
ansible-vault encrypt group_vars/all.yml
ansible-vault encrypt group_vars/k8s-config.yml
```

Next populate `ansible/inventory.yml` with your target ip address.  

Install the ansible galaxy dependencies and run the playbook to deploy.
```bash
ansible-galaxy install -r requirements.yml
ansible-playbook -i inventory.yaml playbook.yml
```