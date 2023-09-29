🚗 CarGo 🚗

Benvenuto in CarGo, la tua piattaforma definitiva per il noleggio di veicoli! 🌟 L’obiettivo principale della nostra app è offrire un servizio completo che permetta agli utenti di visualizzare in dettaglio i veicoli disponibili e di gestire le proprie prenotazioni con semplicità ed efficienza. 🛠️

🚀 Funzionalità 🚀

🔒 Sistema di Autenticazione 🔒

CarGo integra un sistema di autenticazione robusto basato su Spring Boot, garantendo sicurezza e protezione delle informazioni personali.

🌐 Interfaccia Utente 🌐

L'UI di CarGo è sviluppata con Angular, assicurando un'esperienza utente fluida e intuitiva.

🎲 Modalità di Utilizzo 🎲

CarGo offre 3 modalità di utilizzo:

 -Non Loggati 🕵️
     Visita il sito e scopri dettagli e prezzi delle automobili disponibili in CarGo, anche senza registrarti!
     
 -User Normale 🛒
     Accedi come user normale e avrai la possibilità di aggiungere veicoli al tuo carrello ed acquistare prenotazioni per diverse auto.
     
 -Admin 👩‍💼
     Crea, modifica o cancella auto.
     Visualizza e modifica tutte le informazioni riguardanti le prenotazioni.
     Gestisce i pagamenti e cambia ruolo agli utenti, con accesso a tutte le funzionalità di un user normale.
     
⚙️ Configurazione ⚙️

Nel progetto BackEnd, dentro il package config, è presente un App Initializer che permette di creare 10 modelli di auto diversi e di cambiare il ruolo degli utenti tramite ID. Per accedere alle funzionalità di CarGo, modifica l'env.properties con le tue impostazioni di PostgreSQL.

📖 Note Importanti 📖

Ogni Auto è intesa come singola; di conseguenza, il programma è stato impostato per evitare accavallamenti di date prenotate. Più utenti possono avere nel proprio carrello prenotazioni con date accavallate per la stessa auto; tuttavia, solo il primo che completa l’acquisto otterrà il noleggio della macchina in quelle date e di conseguenza tutte le prenotazioni non ancora concluse verranno cancellate visto la non disponibilità.
