# Prodotto
**SREG (Servizi per la Rendicontazione degli Enti Gestori dei servizi socio-assistenziali)**
## Versione
1.0.0

## Descrizione del prodotto

La piattaforma SREG (Servizi per la Rendicontazione degli Enti Gestori dei servizi socio-assistenziali) è uno strumento web finalizzato alla raccolta dati e alla rendicontazione annuale dell'attività svolta e della spesa sociale sostenuta dagli enti.

Offre supporto ai vari attori nelle diverse fasi del processo di rendicontazione, costituendo un sistema gestionale completo e flessibile.  

Agli enti gestori offre una guida nella compilazione dei moduli online di rilevazione della spesa delle funzioni socio-assistenziali e organizza in varie scadenze il conferimento dei dati alla Regione.


Agli operatori regionali offre vari strumenti per la gestione delle istruttorie, sia in termini di controlli automatici sui dati, sia in termini di gestione e monitoraggio del processo, nonché nella comunicazione diretta con i referenti compilatori.


Il sistema include anche l’organizzazione e gestione di un catalogo regionale degli interventi e servizi sociali (o prestazioni) in modo flessibile e direttamente traducibile nei parametri di riferimento delle rilevazioni a livello nazionale. Coadiuva quindi gli enti anche nell'adempiere ai debiti informativi verso il Ministero e l'ISTAT.


Il sistema include anche un front-end per la gestione anagrafica degli enti, delle utenze, delle loro profilazioni e di tutte le abilitazioni all’uso dello strumento.

Elenco componenti:

* [GREGWCL](gregwcl) Componente web app per la web app del front office
* [GREGSRV](gregsrv) API di servizi per il back office

## Configurazioni iniziali

Si rimanda ai readme delle singole componenti

* [GREGWCL](gregwcl/README.md)
* [GREGSRV](gregsrv/README.md)

## Prerequisiti di sistema

Server Web:
Apache 2.4.54

Application Server:
Wildfly 23

Tipo di database:
PostgreSQL v9.6

Dipendenze elencate nella cartella docs/wsdl

## Versioning (Obbligatorio)

Per il versionamento del software si usa la tecnica Semantic Versioning (http://semver.org).

## Authors

* Oleg Medeot
* Claudia Pinton
* [Alessandro Trombotto](https://github.com/alessandro-trombotto)

## Copyrights

“© Copyright Regione Piemonte – 2024”

## License

SPDX-License-Identifier: inserire il codice SPDX delle licenza
Vedere il file LICENSE per i dettagli.

