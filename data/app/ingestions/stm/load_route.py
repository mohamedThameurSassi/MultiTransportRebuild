import csv
import psycopg2
from datetime import datetime,timezone
from pathlib import Path
from pathlib import Path
from db import get_connection
conn  = None
curosr = None

# Folder where this .py file lives
BASE_DIR = Path(__file__).resolve().parent
GTFS_DIR = BASE_DIR / "gtfs_stm"

def load_routes(path):
    try:
        conn = get_connection()
        cursor  = conn.cursor()

        with open(path,mode ='r', encoding = 'utf-8-sig') as f:
            reader = csv.DictReader(f)
            for row in reader:
                route_id = row['route_id']
                route_short_name = row['route_short_name']
                route_long_name = row['route_long_name']
                route_type = row['route_type']
                agency_id = row['agency_id']
                query = """
                    INSERT INTO routes(route_id, agency_id, short_name, long_name, route_type) VALUES (%s, %s, %s, %s, %s)"""
                
                cursor.execute(query, (route_id, agency_id, route_short_name, route_long_name, route_type))

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
    gtfs_routes_path = GTFS_DIR / "routes.txt"
    load_routes(str(gtfs_routes_path))