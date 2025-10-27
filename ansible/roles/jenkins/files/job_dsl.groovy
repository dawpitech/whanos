folder('Whanos base images') {
    description('Folder for whanos base images.')
}

folder('Projects') {
    description('Folder for all projects.')
}

def languages = ['befunge', 'c', 'java', 'javascript', 'python']

languages.each { language ->
    freeStyleJob("Whanos base images/whanos-${language}") {
        wrappers {
            preBuildCleanup()
        }
        steps {
            shell("echo \"Building Docker image for ${language}\"")
            shell("docker build -t whanos-${language} - < /var/lib/jenkins/custom_data/docker_images/${language}/Dockerfile.base")
            shell("docker tag whanos-${language} \${DOCKER_REGISTRY}/whanos-${language}")
            shell("docker push \${DOCKER_REGISTRY}/whanos-${language}")
        }
    }
}

freeStyleJob('Whanos base images/Build all base images') {
    publishers {
        downstream(
            languages.collect { language -> "Whanos base images/whanos-${language}" }
        )
    }
}

freeStyleJob('link-project') {
    parameters {
        stringParam('GIT_REPOSITORY_URL', null, 'Repository git URL (e.g.: "git@github.com:dawpitech/whanos.git")')
        stringParam('PROJECT_NAME', null, 'Project name (e.g.: "whanos"). Can only contains alphanumeric characters, hyphens, and underscores!')
        stringParam('ID_SSH_CREDENTIALS', 'git_ssh_key', 'ID of the SSH credentials to use to access the git repository.')
    }
    steps {
        dsl {
            text('''
                freeStyleJob("Projects/${PROJECT_NAME}") {
                    wrappers {
                        preBuildCleanup()
                    }
                    scm {
                        git {
                            remote {
                                name('origin')
                                url(GIT_REPOSITORY_URL)
                                credentials(ID_SSH_CREDENTIALS)
                            }
                        }
                    }
                    triggers {
                        scm("* * * * *")
                    }
                    steps {
                        shell("echo Building project " + PROJECT_NAME)
                        shell("python3 /var/lib/jenkins/custom_data/whanosInterpreter.py ${DOCKER_REGISTRY} ${PROJECT_NAME}")
                        conditionalSteps {
                            condition {
                                fileExists('whanos.yml', BaseDir.WORKSPACE)
                            }
                            steps {
                                shell("helm upgrade --install ${PROJECT_NAME} /var/lib/jenkins/custom_data/mychart -f whanos.yml --set deployment.image.repository=${DOCKER_REGISTRY} --set deployment.image.name=whanos-project-${PROJECT_NAME}")
                            }
                        }
                    }
                }
            ''')
        }
    }
}
