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
        scm {
            github('dawpitech/whanos', null, 'https', 'github.com', null)
        }
        steps {
            shell("echo \"Building Docker image for ${language}\"")
            shell("docker build -t whanos-${language} images/${language}/Dockerfile.base")
        }
    }
}
