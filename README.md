# Prodotto

La piattaforma **SREG (Servizi per la Rendicontazione degli Enti Gestori dei servizi socio-assistenziali)** è uno strumento web finalizzato alla raccolta dati e alla rendicontazione annuale dell'attività svolta e della spesa sociale sostenuta dagli enti. Offre supporto ai vari attori nelle diverse fasi del processo di rendicontazione, costituendo un sistema gestionale completo e flessibile.
Agli enti gestori offre una guida nella compilazione dei moduli online di rilevazione della spesa delle funzioni socio-assistenziali e organizza in varie scadenze il conferimento dei dati alla Regione.
Agli operatori regionali offre vari strumenti per la gestione delle istruttorie, sia in termini di controlli automatici sui dati, sia in termini di gestione e monitoraggio del processo, nonché nella comunicazione diretta con i referenti compilatori.
Il sistema include anche l’organizzazione e gestione di un catalogo regionale degli interventi e servizi sociali (o prestazioni) in modo flessibile e direttamente traducibile nei parametri di riferimento delle rilevazioni a livello nazionale. Coadiuva quindi gli enti anche nell'adempiere ai debiti informativi verso il Ministero e l'ISTAT.
Il sistema include anche un front-end per la gestione anagrafica degli enti, delle utenze, delle loro profilazioni e di tutte le abilitazioni all’uso dello strumento.

# Getting started  
I moduli attualmente disponibili sono:  
  
| Componente | Descrizione |  
| ---------: | :---------- |  
| [**Admin Console**](https://github.com/regione-piemonte/gescovid19-adminconsole) | Consente al gestore del servizio di amministrare le utenze e i profili di accesso agli applicativi |  
| [**Admin Utenti**](https://github.com/regione-piemonte/gescovid19-adminutenti) | Console di amministrazione utenti/profili del sistema |  
| [**Dataview**](https://github.com/regione-piemonte/gescovid19-dataview) | Visualizzatore dati per Prefettura e Forze dell'Ordine |  
| [**Esito Tampone**](https://github.com/regione-piemonte/gescovid19-esitotampone) | Inserimento manuale esito tampone e data esito |  
| [**Admim Ricoveri**](https://github.com/regione-piemonte/gescovid19-adminricoveri) | Gestione pazienti ricoverati / dimessi dalla strutture sanitarie |  
| [**Gestione Strutture**](https://github.com/regione-piemonte/gescovid19-gestionestrutture) | Gestione strutture e posti letto |  
| [**Dashboard Posti Letto**](https://github.com/regione-piemonte/gescovid19-pazientiweb) | Dashboard sull'occupazione dei posti letto in tempo reale |  
|[**Gestione Pazienti Web**](https://github.com/regione-piemonte/gescovid19-gestionepazientiweb) | Inserimento dati anagrafici paziente, gestione della richiesta tampone, gestione del decorso della malattia, visualizzazione delle segnalazioni in arrivo da MMG/PLS |  
| [**Visura MMG Web**](https://github.com/regione-piemonte/gescovid19-pazientiweb) | Gestione degli assistiti di un MMG covid-positivi. Gestisce la segnalazione dei sospetti positivi al SISP/USCA territorialmente competente|  
| [**Visura Pazienti Web**](https://github.com/regione-piemonte/gescovid19-pazientiweb) | Visura pazienti covid-positivi per i sindaci dei Comuni |  
| [**Acquisti UDC**](https://github.com/regione-piemonte/gescovid19-acquistiudc) | Sistema di controllo unificato del fabbisogno di DPI. Gestisce l'acquisto centralizzato, il magazzino e logistica della distribuzione alle ASL |
| [**Acquisti API**](https://github.com/regione-piemonte/gescovid19-acquistiapi) | API di gestione acquisti centralizzati, magazzino e logistica della distribuzione alle ASL|
| [**HR Personale Sanitario ASR**](https://github.com/regione-piemonte/gescovi19-hrperssanitasr) | HR - Inserimento fabbisogni personale sanitario per le Aziende Sanitarie |
| [**HR Personale Sanitario UDC**](https://github.com/regione-piemonte/gescovid19-hrauthudc) | HR - Approvazione fabbisogni di personale |
| [**HR Personale Sanitario Dett**](https://github.com/regione-piemonte/gescovid19-hrinsdettpers) | HR - Inserimento figure professionali richieste |
| [**Assenza medici**](https://github.com/regione-piemonte/gescovid19-assmedici) | Censimento personale sanitario assente per covid19 |  
| [**USCA**](https://github.com/regione-piemonte/gescovid19-uscammgapi) | Gestione degli USCA e interazioni con MMG. Gestisce il diario clinico del paziente |    
| [**RSA**](https://github.com/regione-piemonte/gescovid19-rsa) | Gestione delle RSA (posti letto, dipendenti, degenti, ...) |
| [**HR Rilevazione**](https://github.com/regione-piemonte/gescovid19-hrril) | Sistema di raccolta delle informazioni legate alle attività svolte dal personale delle ASL sul territorio come richiesto dagli indicatori ministeriali |
| [**Gestrutturesresapi**](https://github.com/regione-piemonte/gestruttureresapi-) | Componente a servizi per la gestione delle rilevazioni e delle rendicontazioni relative a buono welfare delle strutture residenziali|
| [**Gestrutturesresfe**](https://github.com/regione-piemonte/gestruttureresfe) | Frontend per la gestione delle rilevazioni e delle rendicontazioni relative a buono welfare delle strutture residenziali|

  
# Prerequisites  
## Software  
- [PHP 7.1](https://www.php.net)  
- [RedHat JBoss 6.4 GA](https://developers.redhat.com/products/eap/download)  
- [OpenJDK 8](https://openjdk.java.net/install/) o equivalenti  
- [PostgreSQL 9.6](https://www.postgresql.org/download/)  
- [Apache 2.4](https://www.apache.org)  
  
I linguaggi di programmazione utilizzati sono:  
  
### Front-end web:  
  
- VUE.js per le PWA (progressive webapp) adattive sui diversi dispositivi (mobile, web, ...)  
- PHP  
- PHPRunner (RAD per lo sviluppo rapido di applicazioni PHP)  
  
### API/rest:  
  
- Java/JDK 1.8  
  
### Batch:  
  
- Java  
- Pentaho  
- pl/pgsql  
  
Si rimanda ai file README.md delle singole componenti di prodotto per i dettagli specifici.  
  
# Configurations  
Nei file README.md delle singole componenti sono fornite informazioni per la configurazione  
  
# Installing  
Nei file README.md delle singole componenti sono fornite informazioni per la compilazione e installazione  
  
# Versioning  
Per la gestione del codice sorgente può essere utilizzata qualsiasi piattaforma di source versioning (p.es GIT, Subversion, ...) . Per il versionamento del codice sono utilizzate le regole del [Semantic Versioning](http://semver.org/).
  
# Copyrights
© Copyright Regione Piemonte – 2020

# License
SPDX-License-Identifier: EUPL-1.2
See the LICENSE file for details  

# Authors  
Gli autori della piattaforma GESCOVID19 sono:  
  
- Paolo Arvati  
- Annalina Vitelli  
- Carlo Fortunato  
- Guido Coutandin  
- Alessandro Trombotto  
- Fabrizio Corsanego  
- Alessandro Franceschetti  
- Claudio Parodi  
- Davide Portinaro  
- Giuliano Iunco  
- Giuseppe Vezzetti  
- Alessandro Elia  
- Davide Elia  
- Giovanni Pennone  
- Andrea Borrelli  
- Egidio Bosio  
- Raffaela Montuori
