CREATE TABLE IF NOT EXISTS Currencies
(
    id       INTEGER PRIMARY KEY AUTOINCREMENT,
    code     VARCHAR(3) UNIQUE NOT NULL,
    full_name VARCHAR(64),
    sign     VARCHAR(6)
);
CREATE TABLE IF NOT EXISTS ExchangeRates
(
    id               INTEGER PRIMARY KEY AUTOINCREMENT,
    base_currency_id   INT REFERENCES Currencies (id),
    target_currency_id INT REFERENCES Currencies (id),
    rate             DECIMAL(6)
);


