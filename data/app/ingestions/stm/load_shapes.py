import csv
import os
import sys
from datetime import datetime,timezone
from pathlib import Path
from db import get_connection
conn  = None
curosr = None

def load_shapes(path):
    try:
        conn = get_connection()
        cursor  = conn.cursor()

        with open(path,mode = 'r',encoding = 'utf-8-sig') as f:
            reader  = csv.DictReader(f)
            for row in reader:
                shape_id = row['shape_id']
                shape_pt_lat = row['shape_pt_lat']
                shape_pt_lon = row['shape_pt_lon']
                shape_pt_sequence = row['shape_pt_sequence']

                query = """
                        INSERT INTO shapes (shape_id, shape_pt_lat, shape_pt_lon, shape_pt_sequence)
                            VALUES (%s, %s, %s, %s)"""
                cursor.execute(query, (shape_id, shape_pt_lat, shape_pt_lon, shape_pt_sequence))
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
    gtfs_shapes_path = Path(__file__).resolve().parent / "gtfs_stm" / "shapes.txt"
    load_shapes(str(gtfs_shapes_path))