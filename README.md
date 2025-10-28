# Whanos

"Workflow-based Highly-Automated Network Orchestration Service"

## How to setup

### Prerequisites

1. Ansible (version >=2.17.0)

### Remote expected configuration

Three remotes with each:
- RAM >= 2Go
- CPU >= 2vcore
- HDD >= 16Go

> [!NOTE]
> Supported distributions: Debian12, Debian13

The first target should be in the hub group, this target will be responsible for running the Jenkins instace and the docker registry.  
The second target should be in the k8s group, this target will host the k8s control plane.  
Finaly the third target will be a k8s worker node.

### Setup instructions

First based on `ansible/group_vars/example.yml` fill the file with the required informations and generate an ansible vault with your secrets in it, you should call it `all.yml`.

```bash
cd ansible
export ANSIBLE_VAULT_PASSWORD_FILE=/tmp/.vault_pass
echo verySecretPassword > /tmp/.vault_pass
ansible-vault encrypt group_vars/all.yml
```

Next populate `ansible/inventory.yml` with your targets ip addresses.  
Run the playbook to deploy whanos on your remotes.

```bash
ansible-galaxy install -r requirements.yml
ansible-playbook -i inventory.yaml playbook.yml
```

## How to use

Navigate to `<hub_target_ip>:8080` to access the jenkins instance interface

Credentials to log on the Jenkins correspond to the value of both `vault_jenkins_admin_username` and `vault_jenkins_admin_password` in the ansible vault file.

> [!Caution]
> Those credentials should only be used for developpement purposes. Do not keep them for deployment on real hardware. Please override them in the ansible vault.

### Adding your project repository to Jenkins

Using the `link-project` job you can register a new project to follow on the whanos infrastructure.  
For each project you need to specify:
- The git url to clone the repository (e.g.: "git@github.com:dawpitech/whanos.git"). This field NEED to be using ssh syntax, https syntax is NOT SUPPORTED
- The project name, that would be used as the display name and the docker image name in the registry. Thus it need to contains only alphanumerical characters, hyphens and underscores.
- The ssh credentials used to clone the repository. By default this value is at `git_ssh_key` which is the system wide ssh keys choosed during installation of the whanos infrastructure.
- The adress of the docker registry to use to push the docker image to (and pull the base image if needed)

## Notes for developpers:

To locally access the docker registry launched on the same remote as the Jenkins instance you need to allow connection with it as an insecure registry

```bash
sudo mkdir -p /etc/docker

echo """{
  "insecure-registries": ["192.168.1.146:5000"]
}""" > /etc/docker/daemon.json

```
