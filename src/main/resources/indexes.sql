CREATE INDEX IF NOT EXISTS currency_id ON Currencies(id);
CREATE UNIQUE INDEX IF NOT EXISTS code_index ON Currencies(code);

CREATE INDEX IF NOT EXISTS exchange_rate_id ON ExchangeRates(id);
CREATE UNIQUE INDEX IF NOT EXISTS base_target_index ON ExchangeRates(base_currency_id,target_currency_id);