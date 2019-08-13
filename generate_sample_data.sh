#!/usr/bin/env bash

curl -X POST \
  http://localhost:8000/account \
  -F name=John \
  -F surname=Doe \
  -F currency=EUR \
  -F money=100.00

curl -X POST \
  http://localhost:8000/account \
  -F name=Jack \
  -F surname=Kovalsky \
  -F currency=EUR \
  -F money=300.00
