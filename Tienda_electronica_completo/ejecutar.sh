#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
rm -rf out
mkdir -p out
javac -encoding UTF-8 -d out src/ec/edu/puce/modelo/*.java
java -cp out ec.edu.puce.modelo.ServidorWeb
