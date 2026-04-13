
from db import get_connection


def load_transfers(max_distance_m=300, walking_speed_mps=1.4):
    conn = None
    cursor = None
    try:
        conn = get_connection()
        cursor = conn.cursor()

        query = """
            INSERT INTO transfers (from_stop_id, to_stop_id, transfer_type, min_transfer_time)
            SELECT
                a.stop_id AS from_stop_id,
                b.stop_id AS to_stop_id,
                2 AS transfer_type,
                CEIL(ST_Distance(a.location::geography, b.location::geography) / %s)::INTEGER AS min_transfer_time
            FROM stops a
            JOIN stops b ON a.stop_id != b.stop_id
            WHERE ST_DWithin(a.location::geography, b.location::geography, %s)
            ON CONFLICT (from_stop_id, to_stop_id)
            DO UPDATE SET
                transfer_type = EXCLUDED.transfer_type,
                min_transfer_time = EXCLUDED.min_transfer_time;
        """

        cursor.execute(query, (walking_speed_mps, max_distance_m))
        conn.commit()
        print(
            f"Successfully generated transfers for stops within {max_distance_m}m "
            f"(walking speed: {walking_speed_mps} m/s)."
        )
    except Exception as error:
        if conn:
            conn.rollback()
        print(f"An error occurred while loading transfers: {error}")
    finally:
        if cursor:
            cursor.close()
        if conn:
            conn.close()


if __name__ == "__main__":
    load_transfers()
