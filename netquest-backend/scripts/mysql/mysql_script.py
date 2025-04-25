from faker import Faker
import uuid
import random
from datetime import datetime, timedelta

# -----------------------------
# CONFIGURAÇÃO
# -----------------------------
faker = Faker('pt_PT')
DATASET_SIZE = 50000

REVIEW_ATTRIBUTES = ['cleanliness', 'wifi_speed', 'comfort', 'ambience', 'service']


def escape_string(value):
    if value is None:
        return '""'
    value = value.replace('"', '""').replace('\n', ' ').replace('\r', ' ')
    return f'"{value.strip()}"'

def write_csv(filename, lines):
    with open(filename, "w", encoding="utf-8") as f:
        f.write("\n".join(lines))


def generate_dataset(size):
    user_id = "d0d325a1-c660-4a20-b5cd-cbef325dd834"
    users = ["user_id,first_name,last_name,mail,address_line1,city,country,zip_code,birthdate,gender,role,user_name"]
    users.append(f"{user_id},{escape_string(faker.first_name())},{escape_string(faker.last_name())},{faker.email()},"
                 f"{escape_string(faker.street_address())},{escape_string(faker.city())},{escape_string(faker.country())},"
                 f"{faker.postcode()},{faker.date_of_birth(minimum_age=18, maximum_age=80)},"
                 f"{random.choice(['MALE', 'FEMALE', 'OTHER'])},USER,{escape_string(faker.user_name())}")
    write_csv(f"{DATASET_SIZE}/users.csv", users)

    # Wi-Fi Spots
    wifi_spots = ["wifi_spot_id,wifi_spot_user_id,wifi_spot_name,wifi_spot_description,wifi_spot_latitude,wifi_spot_longitude,"
                  "wifi_spot_create_date_time,wifi_spot_air_conditioning,wifi_spot_child_friendly,wifi_spot_covered_area,"
                  "wifi_spot_crowded,wifi_spot_disabled_access,wifi_spot_good_view,wifi_spot_noise_level,wifi_spot_outdoor_seating,"
                  "wifi_spot_pet_friendly,wifi_spot_available_power_outlets,wifi_spot_charging_stations,wifi_spot_drink_options,"
                  "wifi_spot_food_options,wifi_spot_parking_availability,wifi_spot_restrooms_available,wifi_spot_location_type,"
                  "wifi_spot_management,wifi_spot_bandwith_limitations,start_time,end_time,wifi_spot_signal_strength,"
                  "wifi_spot_wifi_quality,wifi_spot_heated_in_winter,wifi_spot_open_during_heat,wifi_spot_open_during_rain,"
                  "wifi_spot_outdoor_fans,wifi_spot_shaded_areas,wifi_spot_address_id"]
    wifi_spot_ids = []
    address_ids = []

    for _ in range(size):
        spot_id = str(uuid.uuid4())
        addr_id = str(uuid.uuid4())
        address_ids.append(addr_id)
        wifi_spot_ids.append(spot_id)
        wifi_spots.append(
            f"{spot_id},{user_id},{escape_string(faker.company())},{escape_string(faker.text(max_nb_chars=100))},"
            f"{faker.latitude()},{faker.longitude()},{faker.date_time_this_decade()},"
            f"{random.randint(0, 1)},{random.randint(0, 1)},{random.randint(0, 1)},{random.randint(0, 1)},{random.randint(0, 1)},"
            f"{random.randint(0, 1)},{random.randint(0, 3)},{random.randint(0, 1)},{random.randint(0, 1)},{random.randint(0, 1)},"
            f"{random.randint(0, 1)},{random.randint(0, 1)},{random.randint(0, 1)},{random.randint(0, 1)},{random.randint(0, 1)},{random.randint(0, 6)},"
            f"{random.randint(0, 2)},{random.randint(0, 1)},"
            f"{faker.time()},"
            f"{faker.time()},"
            f"{random.randint(0, 2)},{random.randint(0, 2)},"
            f"{random.randint(0, 1)},{random.randint(0, 1)},{random.randint(0, 1)},"
            f"{random.randint(0, 1)},{random.randint(0, 1)},{addr_id}"
        )
    write_csv(f"{DATASET_SIZE}/wifi_spot.csv", wifi_spots)

    # Wi-Fi Spot Addresses
    addresses = ["wifi_spot_address_id,wifi_spot_address_city,wifi_spot_address_country,wifi_spot_address_district,"
                 "wifi_spot_address_line_1,wifi_spot_address_line_2,wifi_spot_address_zip_code"]
    for addr_id in address_ids:
        addresses.append(
            f"{addr_id},{escape_string(faker.city())},{escape_string(faker.country())},"
            f"{escape_string(faker.city())},{escape_string(faker.street_address())},{escape_string(faker.building_number())},"
            f"{faker.postcode()}"
        )
    write_csv(f"{DATASET_SIZE}/wifi_spot_address.csv", addresses)

    # Wi-Fi Spot Visits
    wifi_spot_visits = ["wifi_spot_visit_id,wifi_spot_visit_user_id,wifi_spot_visit_wifi_spot_id,"
                        "wifi_spot_visit_start_datetime,wifi_spot_visit_end_datetime"]
    for spot_id in wifi_spot_ids:
        num_visits = random.randint(0, max(1, size // 100))
        for _ in range(num_visits):
            visit_id = str(uuid.uuid4())
            start = faker.date_time_this_decade()
            end = start + timedelta(hours=random.randint(1, 5))
            wifi_spot_visits.append(f"{visit_id},{user_id},{spot_id},{start},{end}")
    write_csv(f"{DATASET_SIZE}/wifi_spot_visit.csv", wifi_spot_visits)

    # Reviews
    reviews = ["review_id,review_comment,review_create_date_time,review_overall_classification,review_user_id,review_wifi_spot_id"]
    review_ids = []
    for spot_id in wifi_spot_ids:
        review_id = str(uuid.uuid4())
        review_ids.append((review_id, spot_id))
        reviews.append(
            f"{review_id},{escape_string(faker.text(max_nb_chars=100))},{faker.date_time_this_decade()},"
            f"{random.randint(1, 5)},{user_id},{spot_id}"
        )
    write_csv(f"{DATASET_SIZE}/review.csv", reviews)

    # Review Attributes
    review_attributes = ["review_id,review_attribute_classification_name,review_attribute_classification_value"]
    for review_id, _ in review_ids:
        for attr in random.sample(REVIEW_ATTRIBUTES, k=random.randint(2, 5)):
            review_attributes.append(
                f"{review_id},{attr},{random.choice(['low', 'medium', 'high'])}"
            )
    write_csv(f"{DATASET_SIZE}/review_attribute_classification.csv", review_attributes)

# -----------------------------
# EXECUÇÃO
# -----------------------------
if __name__ == "__main__":
    generate_dataset(DATASET_SIZE)
