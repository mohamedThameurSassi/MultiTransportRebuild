import httpx
from db import get_connection

STATION_INFO_URL = "https://gbfs.velobixi.com/gbfs/2-2/en/station_information.json"
STATION_STATUS_URL = "https://gbfs.velobixi.com/gbfs/2-2/en/station_status.json"

conn = None
cursor = None

def load_json(url):
    try:
        response = httpx.get(url)
        response.raise_for_status()
        data = response.json()
        return data
    except httpx.HTTPError as e:
        print(f"HTTP error occurred: {e}")
    except Exception as e:
        print(f"An error occurred: {e}")
    return None

def build_station_info_dict(data):
    station_info_dict = {}
    for station in data['data']['stations']:
        station_id = station['station_id']
        station_info_dict[station_id] = {
            'name': station['name'],
            'lat': station['lat'],
            'lon': station['lon'],
            'capacity': station.get('capacity', 0)
        }
    return station_info_dict

def build_station_status_dict(data):
    station_status_dict = {}
    for station in data['data']['stations']:
        station_id = station['station_id']
        station_status_dict[station_id] = {
            'num_bikes_available': station['num_bikes_available'],
            'num_ebikes_available': station['num_ebikes_available'],
            'num_docks_available': station['num_docks_available'],
            'last_reported': station['last_reported']
        }
    return station_status_dict
def insert_data(statusdict, infodict):
    try:
        conn = get_connection()
        cursor = conn.cursor()

        for station_id, info in infodict.items():
            status = statusdict.get(station_id, {})
            query = """
                INSERT INTO bixi_stations (station_id, name, location, capacity, bikes_available, ebikes_available, num_docks_available, last_updated)
                VALUES (%s, %s, ST_SetSRID(ST_MakePoint(%s, %s), 4326), %s, %s, %s, %s, to_timestamp(%s))
                ON CONFLICT (station_id) DO UPDATE SET
                    name = EXCLUDED.name,
                    location = EXCLUDED.location,
                    capacity = EXCLUDED.capacity,
                    bikes_available = EXCLUDED.bikes_available,
                    ebikes_available = EXCLUDED.ebikes_available,
                    num_docks_available = EXCLUDED.num_docks_available,
                    last_updated = EXCLUDED.last_updated;
            """
            cursor.execute(query, (
                station_id,
                info['name'],
                info['lat'],
                info['lon'],
                info['capacity'],
                status.get('num_bikes_available', 0),
                status.get('num_ebikes_available', 0),
                status.get('num_docks_available', 0),
                status.get('last_reported', 0)
            ))
        conn.commit()
        print("Data successfully loaded into the database.")
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
    station_info_data = load_json(STATION_INFO_URL)
    station_status_data = load_json(STATION_STATUS_URL)

    if station_info_data and station_status_data:
        station_info_dict = build_station_info_dict(station_info_data)
        station_status_dict = build_station_status_dict(station_status_data)
        insert_data(station_status_dict, station_info_dict)
