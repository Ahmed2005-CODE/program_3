#!/bin/zsh
# Compile if needed, then run the JavaFX application.
set -e
cd "$(dirname "$0")"

if [ ! -d out ] || [ -z "$(find out -name '*.class' -print -quit)" ]; then
  ./compile.sh
fi

java \
  --module-path lib \
  --add-modules javafx.controls,javafx.graphics,javafx.base \
  --enable-native-access=javafx.graphics \
  -cp out \
  university.Main
