![Build](https://github.com/hunnor-dict/admin-spring/workflows/Build/badge.svg)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=net.hunnor.dict%3Aadmin-spring&metric=alert_status)](https://sonarcloud.io/dashboard?id=net.hunnor.dict%3Aadmin-spring)
[![Known Vulnerabilities](https://snyk.io/test/github/hunnor-dict/admin-spring/badge.svg)](https://snyk.io/test/github/hunnor-dict/admin-spring)

# Migration

Database version 2 mirrors the structure of FULLFORMSLISTE, and adds a table to store translations. The structure is the same for both languages.

![Database V2](docs/database-v2.png)

The top level in version 3 is the dictionary entry, which includes translations, and link to lemma, both from Norsk ordbank and a custom lemma table. Inflection is defined for Norwegian words by connecting the lemma to paradigme. (Tables with green header are part of Norsk ordbank.)

![Database V3](docs/database-v3.png)

# Production deployment notes

- Apache reverse proxy setup and headers: [docs/apache-reverse-proxy.md](docs/apache-reverse-proxy.md)
- Use `SPRING_PROFILES_ACTIVE=prod` to apply hardened cookie and proxy settings from `application-prod.properties`
