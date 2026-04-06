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

def get_gtfs_file(filename: str) -> Path:
    path = GTFS_DIR / filename
    if not path.exists():
        raise FileNotFoundError(f"GTFS file not found: {path}")
    return path

def load_calendar(path):
    try:
        conn = get_connection()
        cursor = conn.cursor()
        request_time = datetime.now(timezone.utc)
        with open(path, mode = 'r', encoding = 'utf-8-sig') as f:
            reader = csv.DictReader(f)

            for row in reader:
                service_id = row['service_id']
                monday = row['monday']
                tuesday = row['tuesday']
                wednesday = row['wednesday']
                thursday = row['thursday']
                friday = row['friday']
                saturday = row['saturday']
                sunday = row['sunday']
                start_date = row['start_date']
                end_date = row['end_date']

                query = """
                        INSERT INTO calendar(service_id,monday,tuesday,wednesday,thursday,friday,saturday,sunday,start_date,end_date)
                        VALUES (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)
                        ON CONFLICT (service_id)
                        DO UPDATE SET
                            monday = EXCLUDED.monday,
                            tuesday = EXCLUDED.tuesday,
                            wednesday = EXCLUDED.wednesday,
                            thursday = EXCLUDED.thursday,
                            friday = EXCLUDED.friday,
                            saturday = EXCLUDED.saturday,
                            sunday = EXCLUDED.sunday,
                            start_date = EXCLUDED.start_date,
                            end_date = EXCLUDED.end_date;
                        """
                cursor.execute(query,(service_id,monday,tuesday,wednesday,thursday,friday,saturday,sunday,start_date,end_date))
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
    gtfs_calendar_path = get_gtfs_file("calendar.txt")
    load_calendar(str(gtfs_calendar_path))
