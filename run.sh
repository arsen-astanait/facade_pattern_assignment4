#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
mkdir -p out
find src -name '*.java' -print0 | xargs -0 javac --release 17 -d out
echo '--- Demo ---'
java -cp out kz.aitu.theater.Main
echo '--- Tests ---'
java -cp out kz.aitu.theater.HomeTheaterFacadeTest
