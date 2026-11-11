#!/usr/bin/env python3
"""Install pinned Linux x86_64 tools into a user-owned directory; no sudo."""
import hashlib,json,os,pathlib,platform,subprocess,tarfile,tempfile,urllib.request,zipfile
if platform.system()!='Linux' or platform.machine()!='x86_64':
    raise SystemExit('This setup supports Linux x86_64 only.')
root=pathlib.Path(os.environ.get('AIRCRAFT_TOOLS_ROOT',str(pathlib.Path.home()/'.local/share/aircraftwar')))
root.mkdir(parents=True,exist_ok=True)
for item in json.loads(pathlib.Path(__file__).with_name('tools-lock.json').read_text()):
    dest=root/item['name'] if item['name']!='android-tools' else root/'android-sdk/cmdline-tools/latest'
    if dest.exists():
        print('Keeping existing installation:',dest)
        continue
    request=urllib.request.Request(item['url'],headers={'User-Agent':'AircraftWar-setup'})
    with urllib.request.urlopen(request,timeout=180) as response:
        data=response.read()
    if hashlib.sha256(data).hexdigest()!=item['sha256']:
        raise SystemExit('Checksum mismatch: '+item['name'])
    with tempfile.TemporaryDirectory() as staging:
        folder=pathlib.Path(staging);archive=folder/'download';archive.write_bytes(data)
        if item['archive']=='tar.gz':
            with tarfile.open(archive) as package:
                name=package.getnames()[0].split('/')[0]
                package.extractall(root,filter='data')
            dest.symlink_to(name)
        else:
            dest.parent.mkdir(parents=True,exist_ok=True)
            with zipfile.ZipFile(archive) as package:package.extractall(folder/'sdk')
            (folder/'sdk/cmdline-tools').rename(dest)
            for binary in (dest/'bin').iterdir():binary.chmod(0o755)
env=os.environ.copy();env['JAVA_HOME']=str(root/'jdk-17');env['ANDROID_HOME']=str(root/'android-sdk')
sdk=root/'android-sdk/cmdline-tools/latest/bin/sdkmanager'
# Run the license prompt interactively so a new machine's owner can read it.
subprocess.run([str(sdk),'--licenses'],env=env,check=True)
subprocess.run([str(sdk),'platform-tools','platforms;android-35','build-tools;35.0.0'],env=env,check=True)
print('Ready. From the repository root: source scripts/env.sh')
