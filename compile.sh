#!/bin/zsh
# Compile the project using the local JavaFX jars in lib/.
set -e
cd "$(dirname "$0")"

rm -rf out
mkdir -p out

javac \
  --module-path lib \
  --add-modules javafx.controls,javafx.graphics,javafx.base \
  -d out \
  $(find src -name "*.java")

echo "Compiled successfully into out/"
echo "Run with: ./run.sh"
