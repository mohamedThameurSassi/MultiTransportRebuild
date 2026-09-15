#!/usr/bin/env bash
set -e

echo "Running BIXI ingestion..."
docker compose exec data python app/ingestions/bixi/load_bixi.py

echo "Running STM GTFS ingestions..."
docker compose exec data python app/ingestions/stm/load_agencies.py
docker compose exec data python app/ingestions/stm/load_calendar.py
docker compose exec data python app/ingestions/stm/load_calendar_dates.py
docker compose exec data python app/ingestions/stm/load_route.py
docker compose exec data python app/ingestions/stm/load_stops.py
docker compose exec data python app/ingestions/stm/load_shapes.py
docker compose exec data python app/ingestions/stm/load_trips.py
docker compose exec data python app/ingestions/stm/load_stop_times.py
docker compose exec data python app/ingestions/stm/load_transfers.py

echo "Checking row counts..."
docker compose exec db sh -c 'psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" -c "
SELECT '\''agencies'\'' AS table_name, COUNT(*) FROM agencies
UNION ALL SELECT '\''calendar'\'', COUNT(*) FROM calendar
UNION ALL SELECT '\''calendar_dates'\'', COUNT(*) FROM calendar_dates
UNION ALL SELECT '\''routes'\'', COUNT(*) FROM routes
UNION ALL SELECT '\''stops'\'', COUNT(*) FROM stops
UNION ALL SELECT '\''shapes'\'', COUNT(*) FROM shapes
UNION ALL SELECT '\''trips'\'', COUNT(*) FROM trips
UNION ALL SELECT '\''stop_times'\'', COUNT(*) FROM stop_times
UNION ALL SELECT '\''transfers'\'', COUNT(*) FROM transfers
UNION ALL SELECT '\''bixi_stations'\'', COUNT(*) FROM bixi_stations;
"'