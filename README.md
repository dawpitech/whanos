# Whanos

"Workflow-based Highly-Automated Network Orchestration Service"

## How to setup

### Prerequisites

1. Ansible (version >=2.17.0)
2. Access to a container registry (e.g.: Digital Ocean Container Registry)
3. Access to a kubernetes cluster (e.g.: Digital Ocean Kubernetes)

> [!NOTE]
> Your selected kubernetes cluster should have read access on your docker registry

### Remote expected configuration

- RAM >= 2Go
- CPU >= 2vcore
- HDD >= 16Go

> [!NOTE]
> Supported distributions: Debian12, Debian13

### Setup instructions

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

## How to use

Navigate to `<hub_target_ip>` to access the jenkins instance interface

Credentials to log on the Jenkins correspond to the value of both `vault_jenkins_admin_username` and `vault_jenkins_admin_password` in the ansible vault file.

> [!Caution]
> Those credentials have admin access to the jenkins instance and can thus run arbitrary code, please use more restricteds account for day to day use.

### Adding your project repository to Jenkins

Using the `link-project` job you can register a new project to follow on the whanos infrastructure.  
For each project you need to specify:
- The git url to clone the repository (e.g.: "git@github.com:dawpitech/whanos-example-ts.git"). Note: fetching of remote repository using HTTP/HTTPS is only supported for public repositories.
- The project name, that would be used as the display name and the docker image name in the registry. Thus it need to contains only alphanumerical characters, hyphens and underscores.
- The ssh credentials used to clone the repository. By default this value is at `git_ssh_key` which is the system wide ssh keys choosed during installation of the whanos infrastructure.

## Notes for developpers:

To locally access a service deployed by the whanos interface quickly you can forward your request to the kubernetes cluster
```bash
kubectl port-forward svc/<project_name>-service <port>
```