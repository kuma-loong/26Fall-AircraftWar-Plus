#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
source scripts/env.sh
# Remove stale classes so deleted features cannot survive in build output.
python3 -c 'import shutil; shutil.rmtree("out/production", ignore_errors=True); shutil.rmtree("out/test", ignore_errors=True)'
mkdir -p out/production out/test
find src/edu/hitsz -name '*.java' -print0 | xargs -0 javac --release 17 -encoding UTF-8 -d out/production
find test -name '*.java' -print0 | xargs -0 javac --release 17 -encoding UTF-8 -cp lib/junit-platform-console-standalone-1.12.2.jar:out/production -d out/test
java -Djava.awt.headless=true -jar lib/junit-platform-console-standalone-1.12.2.jar execute --class-path out/test:out/production --scan-class-path --disable-banner
