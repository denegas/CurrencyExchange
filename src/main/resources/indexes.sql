CREATE INDEX IF NOT EXISTS code_index ON Currencies(code);
CREATE UNIQUE INDEX IF NOT EXISTS base_target_index ON ExchangeRates(base_currency_id,target_currency_id);