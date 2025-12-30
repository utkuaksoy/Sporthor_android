#!/usr/bin/env bash

# Add Permission: chmod +x preCommitSync.sh

# If error any steps stop the build
set -e

echo "Add Licences - spotlessApply"
./gradlew spotlessApply

echo "Check Ktlint - ktlintCheck"
./gradlew spotlessApply

echo "Check Detekt - app:detekt"
./gradlew app:detekt

echo "Create Module Graph - createModuleGraph"
./gradlew createModuleGraph

echo "Create Multi Module Graph - generateModuleGraphs"
./generateModuleGraphs.sh
