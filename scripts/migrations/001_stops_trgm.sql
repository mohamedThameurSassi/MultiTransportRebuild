CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- Run outside a transaction.
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_stops_name_trgm
    ON stops USING GIN (stop_name gin_trgm_ops);

ANALYZE stops;
