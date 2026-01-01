#!/bin/bash
# Simple E2E Test for Meldecenter

echo -n "Progress: ["

curl -s -X POST -H "Content-Type: application/json" -d @src/test/resources/eingehend/rest/Entgeltbescheinigung-Arbeitsunfähigkeit.json http://localhost:8080/v1/meldung > /dev/null
echo -n "#"
curl -s -X POST -H "Content-Type: application/json" -d @src/test/resources/eingehend/rest/Entgeltbescheinigung-Arbeitsunfähigkeit.json http://localhost:8080/v1/meldung > /dev/null
echo -n "#"
curl -s -X POST -H "Content-Type: application/json" -d @src/test/resources/eingehend/rest/Entgeltbescheinigung-Arbeitsunfähigkeit-2.json http://localhost:8080/v1/meldung > /dev/null
echo -n "#"
curl -s -X POST -H "Content-Type: application/json" -d @src/test/resources/eingehend/rest/Entgeltbescheinigung-Arbeitsunfähigkeit-3.json http://localhost:8080/v1/meldung > /dev/null
echo -n "#"
curl -s -X POST -H "Content-Type: application/json" -d @src/test/resources/eingehend/rest/DEÜV-Anmeldung-3.json http://localhost:8080/v1/meldung > /dev/null
echo -n "#"
curl -s -X POST -H "Content-Type: application/json" -d @src/test/resources/eingehend/rest/DEÜV-Anmeldung-3.json http://localhost:8080/v1/meldung > /dev/null
echo -n "#"
curl -s -X POST -H "Content-Type: application/json" -d @src/test/resources/eingehend/rest/DEÜV-Anmeldung-2.json http://localhost:8080/v1/meldung > /dev/null
echo -n "#"
curl -s -X POST -H "Content-Type: application/json" -d @src/test/resources/eingehend/rest/DEÜV-Anmeldung.json http://localhost:8080/v1/meldung > /dev/null
echo -n "#"
curl -s -X POST http://localhost:8080/v1/meldung/export > /dev/null
echo -n "#"

# Add a delay of 2 seconds
sleep 2
# Add duplicates AFTER the export was successful
curl -s -X POST -H "Content-Type: application/json" -d @src/test/resources/eingehend/rest/DEÜV-Anmeldung.json http://localhost:8080/v1/meldung > /dev/null
echo -n "#"
curl -s -X POST -H "Content-Type: application/json" -d @src/test/resources/eingehend/rest/DEÜV-Anmeldung.json http://localhost:8080/v1/meldung > /dev/null
echo -n "#"
curl -s -X POST -H "Content-Type: application/json" -d @src/test/resources/eingehend/rest/Entgeltbescheinigung-Arbeitsunfähigkeit-3.json http://localhost:8080/v1/meldung > /dev/null
echo -n "#"
curl -s -X POST -H "Content-Type: application/json" -d @src/test/resources/eingehend/rest/Entgeltbescheinigung-Arbeitsunfähigkeit-3.json http://localhost:8080/v1/meldung > /dev/null
echo -n "#"

# Should not create new files
curl -s -X POST http://localhost:8080/v1/meldung/export > /dev/null
echo -n "#"

echo "] Done!"

# Check manually for the presence of 3 xml files in src/test/resources/ausgehend/ftp with no duplicates
