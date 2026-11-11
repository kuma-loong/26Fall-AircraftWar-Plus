#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
source scripts/env.sh
javac --release 17 -encoding UTF-8 -cp out/production -d out/test scripts/validation/PerformanceSmoke.java
java -Djava.awt.headless=true -cp out/production:out/test PerformanceSmoke
