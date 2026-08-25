#!/bin/bash

BASE_URL="http://localhost:8080/api/v1/trades"

fmt() {
  echo "$1" | jq . 2>/dev/null || echo "$1"
}

if [ "$1" = "1" ]; then
  response=$(curl -s -X POST "$BASE_URL/optimize" \
    -H "Content-Type: application/json" \
    -d '{
      "maxMargin": 100.0,
      "candidateTrades": [
        { "tradeName": "Trade-A", "marginRequired": 40.0, "expectedPnl": 60.0 },
        { "tradeName": "Trade-B", "marginRequired": 50.0, "expectedPnl": 70.0 },
        { "tradeName": "Trade-C", "marginRequired": 70.0, "expectedPnl": 90.0 }
      ]
    }')
  fmt "$response"

elif [ "$1" = "2" ]; then
  response=$(curl -s "$BASE_URL/$2")
  fmt "$response"

elif [ "$1" = "3" ]; then
  response=$(curl -s "$BASE_URL?page=$2&size=$3")
  fmt "$response"

else
  echo "Usage: $0 1 | 2 <uuid> | 3 <page> <size>"
fi
