#!/bin/bash

echo "Running test cases and generating code coverage..."

./mvnw clean test

./mvnw jacoco:report

REPORT_PATH="target/site/jacoco/index.html"
ABSOLUTE_PATH=$(realpath "$REPORT_PATH")

echo "Code coverage report generated at: $ABSOLUTE_PATH"
