import csv
import os
import sys
from datetime import datetime,timezone
from pathlib import Path
from db import get_connection
conn  = None
curosr = None

def load_agencies(path):
    try:
        conn = get_connection()
        cursor  = conn.cursor()

        request_time = datetime.now(timezone.utc)

        with open(path,mode = 'r',encoding = 'utf-8-sig') as f:
            reader  = csv.DictReader(f)

            for row in reader:
                agency_id = row['agency_id']
                name = row['agency_name']
                url = row['agency_url']
                
                query = """
                    INSERT INTO agencies(agency_id,name, url,timezone)
                    VALUES (%s, %s, %s, %s)
                    ON CONFLICT (agency_id)
                    DO UPDATE SET
                        name = EXCLUDED.name,
                        url = EXCLUDED.url;
                        """
                cursor.execute(query, (agency_id, name, url, request_time))
        conn.commit()
        print(f"Successfully loaded {path} into the database.")
    except Exception as e:
            print(f"An error occurred: {e}")
            if conn:
                conn.rollback()
                
    finally:
        if cursor:
            cursor.close()
        if conn:
            conn.close()

if __name__ == "__main__":
    gtfs_agency_path = Path(__file__).resolve().parent / "gtfs_stm" / "agency.txt"
    load_agencies(str(gtfs_agency_path))