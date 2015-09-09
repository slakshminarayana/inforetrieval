                    ==========
					  ReadMe
				    ==========

The Parser.pl program tokenizes the unstructured data

The Porter_Stemmer.pl program performs stemming on the tokenized words using the porter Stemmer algorithm.

Porter.java is the java version of Porter Stemmer algorithm

Index_Construction_Lemmatization.java program creates an inverted index by lemmatizing the tokens obtained from the tokenizer using the Stanford coreNLP Java APIs. This index is then used to build a retrieval system. This inturn has two version within the same program. One version is the uncomressed version whereas the other is the compressed version which results in a lot of savings in space required to store the inverted index. 

Index_Construction_Stemmer.java program creates an inverted index from the tokens obtained from the tokenizer by passing it to the stemmer algorithm. This inverted index is then used to build a retrieval system. This inturn has two version within the same program. One version is the uncomressed version whereas the other is the compressed version which results in a lot of savings in space required to store the inverted index. 

Retrieval_System.java is a statistical retrieval system that accepts user queries, parses the query, removes the stop words, stems the tokens, calculates the score and retrieves the top 10 documents based on their scores.