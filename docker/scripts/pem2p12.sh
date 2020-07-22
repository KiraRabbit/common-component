#!/bin/bash
openssl pkcs12 -export -out cert.p12 -in cert.pem -inkey key.pem -nodes -passout pass:guuidea
