import os
import psycopg2

DB_CONFIG = {
    "dbname": os.environ["DB_NAME"],
    "user": os.environ["DB_USER"],
    "password": os.environ["DB_PASSWORD"],
    "host": os.environ["DB_HOST"],
    "port": os.environ["DB_PORT"],
}

def get_connection():
    """Returns a new database connection."""
    return psycopg2.connect(**DB_CONFIG)
