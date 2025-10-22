def docker_image_builder_dsl = 
"""
freeStyleJob('whanos-' + LANGUAGE_NAME) {
    steps {
        shell('echo "Building Docker image for ' + LANGUAGE_NAME + '"')
        shell('docker build -t whanos-' + LANGUAGE_NAME + ' .')
    }
}
"""

folder('Whanos base images') {
    description('Folder for whanos base images.')
}

folder('Projects') {
    description('Folder for all projects.')
}

freeStyleJob('Whanos base images/whanos-befunge') {
    parameters {
        stringParam('LANGUAGE_NAME', 'befunge', 'Name of the programming language docker image to build')
    }
    steps {
        dsl(docker_image_builder_dsl)
    }
}
