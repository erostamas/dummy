#/bin/bash

echo "Building dummy"

REPO_ROOT=$(git rev-parse --show-toplevel)

cd $REPO_ROOT/
mkdir -p build
cd build
cmake ../
make -j

echo "Done building dummy"
