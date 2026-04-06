import csv
import os
import sys
from datetime import datetime,timezone
from pathlib import Path
from db import get_connection
conn  = None
curosr = None

def load_trips(path):
    try:
        conn = get_connection()
        cursor  = conn.cursor()

        with open(path,mode = 'r',encoding = 'utf-8-sig') as f:
            reader  = csv.DictReader(f)

            for row in reader:
                trip_id = row['trip_id']
                route_id = row['route_id']
                service_id = row['service_id']
                trip_headsign = row['trip_headsign']
                direction_id = row['direction_id']
                shape_id = row['shape_id']

                query = """
                        INSERT INTO trips (trip_id, route_id, service_id, trip_headsign, direction_id, shape_id)
                            VALUES (%s, %s, %s, %s, %s, %s)"""
                cursor.execute(query, (trip_id, route_id, service_id, trip_headsign, direction_id, shape_id))
            conn.commit()
            print(f"Successfully loaded {path} into the database.")
    except Exception as e:
        if conn:
            conn.rollback()
        print(f"An error occurred: {e}")
    finally:
        if cursor:
            cursor.close()
        if conn:
            conn.close()
if __name__ == "__main__":
    gtfs_trips_path = Path(__file__).resolve().parent / "gtfs_stm" / "trips.txt"
    load_trips(str(gtfs_trips_path))