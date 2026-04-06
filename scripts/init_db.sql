-- Enable PostGIS
CREATE EXTENSION IF NOT EXISTS postgis;

-- ══════════════════════════════════════════
-- GTFS Core Tables
-- ══════════════════════════════════════════

CREATE TABLE IF NOT EXISTS agencies (
    agency_id   TEXT PRIMARY KEY,
    name        TEXT NOT NULL,
    url         TEXT,
    timezone    TEXT NOT NULL DEFAULT 'America/Montreal'
);

CREATE TABLE IF NOT EXISTS routes (
    route_id    TEXT PRIMARY KEY,
    agency_id   TEXT REFERENCES agencies(agency_id),
    short_name  TEXT,
    long_name   TEXT,
    route_type  INTEGER NOT NULL  -- 0=tram, 1=metro, 2=rail, 3=bus
);

CREATE TABLE IF NOT EXISTS stops (
    stop_id         TEXT PRIMARY KEY,
    stop_code       TEXT,
    stop_name       TEXT NOT NULL,
    location        GEOMETRY(Point, 4326) NOT NULL,
    location_type   INTEGER DEFAULT 0,  -- 0=stop, 1=station
    parent_station  TEXT DEFAULT 0,
    wheelchair      INTEGER DEFAULT 0
);

CREATE TABLE IF NOT EXISTS calendar (
    service_id  TEXT PRIMARY KEY,
    monday      BOOLEAN NOT NULL,
    tuesday     BOOLEAN NOT NULL,
    wednesday   BOOLEAN NOT NULL,
    thursday    BOOLEAN NOT NULL,
    friday      BOOLEAN NOT NULL,
    saturday    BOOLEAN NOT NULL,
    sunday      BOOLEAN NOT NULL,
    start_date  DATE NOT NULL,
    end_date    DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS calendar_dates (
    service_id      TEXT REFERENCES calendar(service_id),
    date            DATE NOT NULL,
    exception_type  INTEGER NOT NULL,  -- 1=added, 2=removed
    PRIMARY KEY (service_id, date)
);

CREATE TABLE IF NOT EXISTS trips (
    trip_id         TEXT PRIMARY KEY,
    route_id        TEXT NOT NULL REFERENCES routes(route_id),
    service_id      TEXT NOT NULL REFERENCES calendar(service_id),
    trip_headsign   TEXT,
    direction_id    INTEGER,
    shape_id        TEXT
);

CREATE TABLE IF NOT EXISTS stop_times (
    trip_id             TEXT NOT NULL REFERENCES trips(trip_id),
    arrival_time        INTERVAL NOT NULL,
    departure_time      INTERVAL NOT NULL,
    stop_id             TEXT NOT NULL REFERENCES stops(stop_id),
    stop_sequence       INTEGER NOT NULL,
    pickup_type         INTEGER DEFAULT 0,
    PRIMARY KEY (trip_id, stop_sequence)
);

CREATE TABLE IF NOT EXISTS shapes (
    shape_id            TEXT NOT NULL,
    shape_pt_lat        DOUBLE PRECISION NOT NULL,
    shape_pt_lon        DOUBLE PRECISION NOT NULL,
    shape_pt_sequence   INTEGER NOT NULL,
    PRIMARY KEY (shape_id, shape_pt_sequence)
);

CREATE TABLE IF NOT EXISTS transfers (
    from_stop_id        TEXT NOT NULL REFERENCES stops(stop_id),
    to_stop_id          TEXT NOT NULL REFERENCES stops(stop_id),
    transfer_type       INTEGER NOT NULL,  -- 0=recommended, 1=timed, 2=min_time, 3=not_possible
    min_transfer_time   INTEGER,           -- seconds
    PRIMARY KEY (from_stop_id, to_stop_id)
);

-- ══════════════════════════════════════════
-- BIXI Tables
-- ══════════════════════════════════════════

CREATE TABLE IF NOT EXISTS bixi_stations (
    station_id      TEXT PRIMARY KEY,
    name            TEXT NOT NULL,
    location        GEOMETRY(Point, 4326) NOT NULL,
    capacity        INTEGER NOT NULL,
    bikes_available INTEGER DEFAULT 0,
    ebikes_available INTEGER DEFAULT 0,
    docks_available INTEGER DEFAULT 0,
    last_updated    TIMESTAMPTZ DEFAULT NOW()
);

-- ══════════════════════════════════════════
-- Indexes
-- ══════════════════════════════════════════

CREATE INDEX IF NOT EXISTS idx_stops_location ON stops USING GIST(location);
CREATE INDEX IF NOT EXISTS idx_bixi_stations_location ON bixi_stations USING GIST(location);
CREATE INDEX IF NOT EXISTS idx_stop_times_stop ON stop_times(stop_id);
CREATE INDEX IF NOT EXISTS idx_stop_times_trip ON stop_times(trip_id);
CREATE INDEX IF NOT EXISTS idx_stop_times_departure ON stop_times(departure_time);
CREATE INDEX IF NOT EXISTS idx_trips_route ON trips(route_id);
CREATE INDEX IF NOT EXISTS idx_trips_service ON trips(service_id);
CREATE INDEX IF NOT EXISTS idx_routes_type ON routes(route_type);