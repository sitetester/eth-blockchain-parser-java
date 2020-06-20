BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "blocks" (
	"number"	INTEGER NOT NULL,
	"hash"	VARCHAR(254) NOT NULL,
	"difficulty"	INTEGER NOT NULL,
	"transactionsCount"	INTEGER NOT NULL
);
CREATE TABLE IF NOT EXISTS "transactions" (
  "hash"	VARCHAR(254) NOT NULL,
	"blockNumber"	INTEGER NOT NULL,
	"from"	VARCHAR(254),
	"to"	VARCHAR(254)
);
COMMIT;