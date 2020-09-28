CREATE TABLE IF NOT EXISTS parking (uniqueId VARCHAR(255) not null, pfloor INTEGER,
prow INTEGER, RegNum VARCHAR(255),
color VARCHAR(255), entryDate VARCHAR(255),
entryTime VARCHAR(255), exitDate VARCHAR(255),
exitTime VARCHAR(255), PRIMARY KEY(uniqueId));
