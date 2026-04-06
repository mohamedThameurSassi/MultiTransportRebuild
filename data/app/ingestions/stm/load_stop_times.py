import csv
import os
import sys
from datetime import datetime,timezone
from pathlib import Path
from db import get_connection
conn  = None
curosr = None

def load_stop_times(path):
    try:
        conn = get_connection()
        cursor  = conn.cursor()

        with open(path,mode = 'r',encoding = 'utf-8-sig') as f:
            reader  = csv.DictReader(f)

            for row in reader:
                trip_id = row['trip_id']
                arrival_time = row['arrival_time']
                departure_time = row['departure_time']
                stop_id = row['stop_id']
                stop_sequence = row['stop_sequence']

                query = """
                        INSERT INTO stop_times (trip_id, arrival_time, departure_time, stop_id, stop_sequence)
                            VALUES (%s, %s, %s, %s, %s)"""
                cursor.execute(query, (trip_id, arrival_time, departure_time, stop_id, stop_sequence))
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
    gtfs_stop_times_path = Path(__file__).resolve().parent / "gtfs_stm" / "stop_times.txt"
    load_stop_times(str(gtfs_stop_times_path))