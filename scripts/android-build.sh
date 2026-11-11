#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
source scripts/env.sh
# Gradle/JVM does not automatically use HTTP(S)_PROXY. Apply it only to this process.
python3 - "$@" <<'PY'
import os,shlex,subprocess,sys
from urllib.parse import urlparse
env=os.environ.copy(); opts=shlex.split(env.get('JAVA_OPTS',''))
for proto in ('http','https'):
    value=env.get(proto.upper()+'_PROXY') or env.get(proto+'_proxy')
    if value:
        proxy=urlparse(value)
        if proxy.username or proxy.password:
            raise SystemExit('Authenticated proxy: configure Gradle credentials locally instead.')
        if proxy.scheme!='http' or not proxy.hostname:
            raise SystemExit('This helper supports HTTP proxies; configure other proxy types locally.')
        opts += [f'-D{proto}.proxyHost={proxy.hostname}',f'-D{proto}.proxyPort={proxy.port or 80}']
opts += ['-Dhttp.nonProxyHosts=localhost|127.*|[::1]']
env['JAVA_OPTS']=shlex.join(opts)
sys.exit(subprocess.call(['./gradlew']+(sys.argv[1:] or ['assembleDebug']),cwd='android',env=env))
PY
