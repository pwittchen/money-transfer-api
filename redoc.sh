#!/usr/bin/env bash
sudo docker run -p 9000:80 -e SPEC_URL=http://localhost:8000/openapi redocly/redoc