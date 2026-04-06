import csv
import psycopg2
from datetime import datetime,timezone
from pathlib import Path
from pathlib import Path
from db import DB_CONFIG, get_connection
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


def load_calendar_dates(path):
    try:
        conn = get_connection()
        cursor = conn.cursor()
        request_time = datetime.now(timezone.utc)

        with open(path,mode = 'r', encoding = 'utf-8-sig') as f:
            reader = csv.DictReader(f)

            for row in reader:
                service_id = row['service_id']
                date = row['date']
                exception_type = row['exception_type']

                query = """
                        INSERT INTO calendar_dates(service_id,date,exception_type)
                        VALUES (%s,%s,%s)
                        ON CONFLICT (service_id,date)
                        DO UPDATE SET
                            exception_type = EXCLUDED.exception_type;
                        """
                cursor.execute(query,(service_id,date,exception_type))
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
    gtfs_calendar_dates_path = get_gtfs_file("calendar_dates.txt")
    load_calendar_dates(str(gtfs_calendar_dates_path))