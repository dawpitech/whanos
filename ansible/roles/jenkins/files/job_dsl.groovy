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
        scm {
            github('dawpitech/whanos')
        }
        steps {
            shell("echo \"Building Docker image for ${language}\"")
            shell("docker build -t whanos-${language} - < images/${language}/Dockerfile.base")
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
