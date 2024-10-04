# Componente di Prodotto

GREGSRV

## Versione

1.0.0

## Descrizione del prodotto

Si tratta di servizi REST la componente di presentation : [GREGWCL](../gregwcl). Questi servizi sono la componente di business del prodotto SREG.


## Configurazioni iniziali

Per generare il pacchetto lanciare il comando ant -Dtarget prod.

## Prerequisiti di sistema

Java:
Jdk 11

ANT:
Ant version 1.6.5 o superiori

Server Web:
Apache 2.4.54

Application Server:
Wildfly 23

Tipo di database:
PostgreSQL v9.6

## Installazione

Per generare il pacchetto lanciare il comando ant -Dtarget prod  per generare l'ear

## Deployment

Inserire il file ear generato durante l'installazione sotto la cartella deployments del Wildfly

## Versioning

Per il versionamento del software si usa la tecnica Semantic Versioning (http://semver.org).

## Authors

* Giorgio Lupo
* [Alessandro Trombotto](https://github.com/alessandro-trombotto)


## Copyrights

“© Copyright Regione Piemonte – 2023”