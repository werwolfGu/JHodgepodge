
###https 证书
openssl pkcs12 -in /Users/chengen.gu/file.p12/ -nodes -out private_key.pem  

openssl pkcs12 -in /Users/chengen.gu/file.p12/ -clcerts -out public_key.pem  