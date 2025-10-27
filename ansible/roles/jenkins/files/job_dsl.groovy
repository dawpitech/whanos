folder('Whanos base images') {
    description('Folder for whanos base images.')
}

folder('Projects') {
    description('Folder for all projects.')
}

def languages = ['befunge', 'c', 'java', 'javascript', 'python']

languages.each { language ->
    freeStyleJob("Whanos base images/whanos-${language}") {
        parameters {
            stringParam('DOCKER_REGISTRY', 'localhost:5000', 'Docker registry to push the image to')
        }
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
                        shell("python3 /var/lib/jenkins/custom_data/whanosInterpreter.py ${PROJECT_NAME}")
                    }
                }
            ''')
        }
    }
}
