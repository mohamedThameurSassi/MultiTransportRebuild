import csv
import os
import sys
from datetime import datetime,timezone
from pathlib import Path
from db import get_connection
conn  = None
curosr = None

def load_stops(path):
    try:
        conn = get_connection()
        cursor  = conn.cursor()

        with open(path,mode = 'r',encoding = 'utf-8-sig') as f:
            reader  = csv.DictReader(f)

            for row in reader:
                stop_id = row['stop_id']
                stop_name = row['stop_name']
                stop_lat = row['stop_lat']
                stop_lon = row['stop_lon']
                stop_code = row['stop_code']
                location_type = row['location_type']
                parent_station = row['parent_station']
                wheelchair_boarding = row['wheelchair_boarding']


                query = """
                        INSERT INTO stops (stop_id, stop_code, stop_name, location, location_type, parent_station, wheelchair)
                            VALUES (%s, %s, %s, ST_SetSRID(ST_MakePoint(%s, %s), 4326), %s, %s, %s)"""
                cursor.execute(query, (stop_id, stop_code, stop_name, stop_lat, stop_lon, location_type, parent_station, wheelchair_boarding))
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
    gtfs_stops_path = Path(__file__).resolve().parent / "gtfs_stm" / "stops.txt"
    load_stops(str(gtfs_stops_path))    