#!/bin/bash

echo "Capturing Kepler metrics before test"
curl -s http://localhost:9102/metrics | grep netquest-api > before.txt

echo "Running k6 load test..."
k6 run wifi_spot_name_scripts.js

sleep 2

echo "Capturing Kepler metrics after test"
curl -s http://localhost:9102/metrics | grep netquest-api > after.txt

echo "Comparing energy and resource usage:"
diff before.txt after.txt