REQUISITI

installare pig-0.14.0

Log-in a Bing 

Avere a disposizione circa 20 mila query Bing
	(2000 nella prima fase)
	(200 * #EXACT_MATCH(circa 90 + 30(nuovo Language Model)))

CONFIGURAZIONE

Aggiungere nel file di configurazione:

1 - I Path per i file di INPUT
	film_file_path : path del file dei film da completare con il direttore
	director_file_path : path del file contente l'elenco dei direttori
	lm_file_path : path del file Language Model
	triple_file_path : path del file contente le triple film-direttore-relazione
2 - La chiave per le query BING

Package:

UTIL
Contiene classi per scrivere su file una Lista di string e classi per leggere tali file.
In piu è presente la classe Property che legge il file di configurazione

LUCENE
Usato per indicizzare il file contentente il Language Model e per effettuare su di esso delle
ricerche sfruttando l indicizzazione

Nella Seconda fase è utilizzato per indicizzare il file dei direttori e per eseguire su di esso query
due termini in AND

PARSE
Contiene una sola classe utile per parsare le descrizioni ottenute dalla richiesta GET,
ovvero prende il testo compreso tra film e direttore e lo ripulisce da caratteri speciali,
date, etc...

Nella seconda fase è utlizzata per parsare il testo ottenuto dalle query "film pattern"

QUERY
Contiene la classe QUERYPIG che effettua varie trasformazioni, elaborozioni(join), analisi tramite
query Pig

REQUEST
La classe MakeRequest ha il compito di effettuare richieste get tramite query Bing.

MAIN
Esegue l'intera applicazione (ExecuteQuery) o solo la prima fase o solo la seconda
