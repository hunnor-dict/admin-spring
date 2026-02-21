# Apache2 reverse proxy setup (Docker app on port 8080)

This example assumes Apache terminates HTTPS on `dict-admin.example.com` and proxies to the container on `127.0.0.1:8080`.

## Required Apache modules

```bash
sudo a2enmod proxy proxy_http headers ssl rewrite
```

## VirtualHost example

```apache
<VirtualHost *:443>
    ServerName dict-admin.example.com

    SSLEngine on
    SSLCertificateFile /etc/letsencrypt/live/dict-admin.example.com/fullchain.pem
    SSLCertificateKeyFile /etc/letsencrypt/live/dict-admin.example.com/privkey.pem

    ProxyPreserveHost On
    RequestHeader set X-Forwarded-Proto "https"
    RequestHeader set X-Forwarded-Port "443"

    ProxyPass        / http://127.0.0.1:8080/
    ProxyPassReverse / http://127.0.0.1:8080/
</VirtualHost>
```

## Spring profile activation

Run with the prod profile so cookie + proxy settings are enforced:

```bash
SPRING_PROFILES_ACTIVE=prod
REMEMBER_ME_KEY="<long-random-secret>"
```

For Docker, pass these as environment variables to the container.
