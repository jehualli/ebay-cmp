#!/bin/sh
# Build and run the EJB edition on http://localhost:8086/
# (the servlet-only original stays on 8081 from ~/ebay-jsp)
set -e
cd "$(dirname "$0")"
docker build -t ebay-cmp .
docker rm -f ebay-cmp >/dev/null 2>&1 || true
docker run -d --name ebay-cmp -p 8086:8080 ebay-cmp
echo "eBay (EJB edition) starting on http://localhost:8086/   (guest / ebay)"
echo "Container comparison:  http://localhost:8086/contenedores"
echo "Logs:  docker logs -f ebay-cmp"
echo "Stop:  docker rm -f ebay-cmp"
