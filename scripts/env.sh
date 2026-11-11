# Source this file from the project root: source scripts/env.sh
export AIRCRAFT_TOOLS_ROOT="${AIRCRAFT_TOOLS_ROOT:-$HOME/.local/share/aircraftwar}"
export JAVA_HOME="$AIRCRAFT_TOOLS_ROOT/jdk-17"
export ANDROID_HOME="$AIRCRAFT_TOOLS_ROOT/android-sdk"
export PATH="$JAVA_HOME/bin:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools:$AIRCRAFT_TOOLS_ROOT/gh/bin:$PATH"
