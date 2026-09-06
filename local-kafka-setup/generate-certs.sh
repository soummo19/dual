set -euo pipefail

OUT_DIR="${OUT_DIR:-/secrets}"
STORE_PASS="${STORE_PASS:-password}"
VALIDITY_DAYS="${VALIDITY_DAYS:-3650}"
KEY_ALIAS="${KEY_ALIAS:-bmc}"

# Every name a broker can be reached by: from the host (localhost) and from
# inside the compose network (service names). Without these the client-side
# hostname verification fails.
SAN="SAN=DNS:localhost,DNS:kafka-zk,DNS:kafka-kraft,DNS:host.docker.internal,IP:127.0.0.1"

CA_DNAME="CN=localhost, OU=Jio Home Cloud, O=Jio, L=Ghansoli, ST=MH, C=IN"
BROKER_DNAME="CN=localhost, OU=Jio, O=Jio, L=Ghansoli, ST=Mumbai, C=IN"

cd "$OUT_DIR"

if [[ -f KeyStore.jks && "${FORCE:-0}" != "1" ]]; then
  echo "KeyStore.jks already exists in $OUT_DIR - nothing to do (set FORCE=1 to regenerate)."
  exit 0
fi

rm -f ca.p12 ca-cert.pem broker.csr broker-signed.pem KeyStore.jks truststore.jks

echo "==> Creating local CA"
keytool -genkeypair -noprompt \
  -alias ca -dname "$CA_DNAME" \
  -keyalg RSA -keysize 2048 -validity "$VALIDITY_DAYS" \
  -ext bc:c=ca:true \
  -keystore ca.p12 -storetype PKCS12 \
  -storepass "$STORE_PASS" -keypass "$STORE_PASS"

keytool -exportcert -rfc -alias ca -keystore ca.p12 -storepass "$STORE_PASS" -file ca-cert.pem

echo "==> Creating truststore.jks"
keytool -importcert -noprompt \
  -alias "$KEY_ALIAS" -file ca-cert.pem \
  -keystore truststore.jks -storetype PKCS12 -storepass "$STORE_PASS"

echo "==> Creating KeyStore.jks"
keytool -genkeypair -noprompt \
  -alias "$KEY_ALIAS" -dname "$BROKER_DNAME" \
  -keyalg RSA -keysize 2048 -validity "$VALIDITY_DAYS" \
  -ext "$SAN" \
  -keystore KeyStore.jks -storetype PKCS12 \
  -storepass "$STORE_PASS" -keypass "$STORE_PASS"

keytool -certreq -alias "$KEY_ALIAS" \
  -keystore KeyStore.jks -storepass "$STORE_PASS" -file broker.csr

# serverAuth + clientAuth so the same pair works for the broker and for mTLS clients.
keytool -gencert -rfc -noprompt \
  -alias ca -keystore ca.p12 -storepass "$STORE_PASS" \
  -infile broker.csr -outfile broker-signed.pem \
  -validity "$VALIDITY_DAYS" \
  -ext "$SAN" -ext "EKU=serverAuth,clientAuth"

keytool -importcert -noprompt -alias caroot -file ca-cert.pem \
  -keystore KeyStore.jks -storepass "$STORE_PASS"
keytool -importcert -noprompt -alias "$KEY_ALIAS" -file broker-signed.pem \
  -keystore KeyStore.jks -storepass "$STORE_PASS"

rm -f ca.p12 broker.csr broker-signed.pem

echo "==> Writing credential files"
printf '%s' "$STORE_PASS" > keystore_creds
printf '%s' "$STORE_PASS" > key_creds
printf '%s' "$STORE_PASS" > truststore_creds

cat > client-ssl.properties <<EOF
security.protocol=SSL
ssl.truststore.location=/etc/kafka/secrets/truststore.jks
ssl.truststore.password=$STORE_PASS
ssl.keystore.location=/etc/kafka/secrets/KeyStore.jks
ssl.keystore.password=$STORE_PASS
ssl.key.password=$STORE_PASS
EOF

chmod 644 ./*

echo
keytool -list -keystore KeyStore.jks -storepass "$STORE_PASS"
echo
echo "Done. Files written to $OUT_DIR"