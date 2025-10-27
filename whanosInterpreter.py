import os

if len(sys.argv) != 2:
    print("Usage: python whanosInterpreter.py <project_name>")
    exit(1)

languages = []

if os.path.exists('Makefile')
    languages.append('c')
if os.path.exists('app/pom.xml')
    languages.append('java')
if os.path.exists('package.json')
    languages.append('javascript')
if os.path.exists('requirements.txt')
    languages.append('python')
if os.path.exists('main.bf')
    languages.append('befunge')

if not languages:
    print("No supported programming languages detected.")
    exit(1)
if len(languages) > 1:
    print("Multiple programming languages detected: " + ", ".join(languages))
    exit(1)

REGISTRY_URL = "localhost:5000"

image_name = f"whanos-project-{sys.argv[1]}"

if os.path.exists('Dockerfile'):
    os.system(f"docker build -t {image_name} .")
else:
    os.system(f"wget https://raw.githubusercontent.com/dawpitech/whanos/refs/heads/main/images/{languages[0]}/Dockerfile.base")
    os.system(f"docker build -f /var/lib/jenkins/custom_data/docker_images/{languages[0]}/Dockerfile.base -t {image_name} .")

os.system(f"docker tag {image_name} {REGISTRY_URL}/{image_name}")
os.system(f"docker push {REGISTRY_URL}/{image_name}")