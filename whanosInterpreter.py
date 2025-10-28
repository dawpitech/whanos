import os
import subprocess

if len(os.sys.argv) != 3:
    print("Usage: python whanosInterpreter.py <docker_registry_url> <project_name>")
    exit(1)

print("""
Running whanos interpreter...
                     __       __
                     '.'--.--'.-'
       .,_------.___,   \' r'
       ', '-._a      '-' .'
        '.    '-'Y \._  /
          '--;____'--.'-,
           /..'       '''
""")

languages = []

if os.path.exists('Makefile'):
    languages.append('c')
if os.path.exists('app/pom.xml'):
    languages.append('java')
if os.path.exists('package.json'):
    languages.append('javascript')
if os.path.exists('requirements.txt'):
    languages.append('python')
if os.path.exists('main.bf'):
    languages.append('befunge')

if not languages:
    print("No supported programming languages detected.")
    exit(1)
if len(languages) > 1:
    print("Multiple programming languages detected: " + ", ".join(languages))
    exit(1)

REGISTRY_URL = os.sys.argv[1]

image_name = f"whanos-{os.sys.argv[2]}"

try:
    if os.path.exists('Dockerfile'):
        subprocess.check_call(['docker', 'build', '-t', image_name, '.'])
    else:
        subprocess.check_call(['docker', 'build', '-f', f'/var/lib/jenkins/custom_data/docker_images/{languages[0]}/Dockerfile.base', '-t', image_name, '.'])

    subprocess.check_call(['docker', 'tag', image_name, f'{REGISTRY_URL}/{image_name}'])
    subprocess.check_call(['docker', 'push', f'{REGISTRY_URL}/{image_name}'])
    print(f"Successfully built and pushed image: {image_name}")
except subprocess.CalledProcessError as e:
    print(f"Error: {e}")
    exit(e)
