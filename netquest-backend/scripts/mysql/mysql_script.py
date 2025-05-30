from faker import Faker
import uuid
import random
from datetime import datetime, timedelta

# -----------------------------
# CONFIGURAÇÃO
# -----------------------------
faker = Faker('pt_PT')
DATASET_SIZE = 100000

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
    wifi_spots = ["wifi_spot_id,wifi_spot_user_id,"
                  "wifi_spot_address_id"]
    wifi_spot_ids = []
    address_ids = []

    for _ in range(size):
        spot_id = str(uuid.uuid4())
        addr_id = str(uuid.uuid4())
        address_ids.append(addr_id)
        wifi_spot_ids.append(spot_id)
        wifi_spots.append(
            f"{spot_id},{user_id},"
            f"{addr_id}"
        )
    write_csv(f"{DATASET_SIZE}/wifi_spot.csv", wifi_spots)

    # Wi-Fi Spot Addresses
    addresses = ["wifi_spot_address_id,"
                 "wifi_spot_address_zip_code"]
    for addr_id in address_ids:
        addresses.append(
            f"{addr_id}"
        )
    write_csv(f"{DATASET_SIZE}/wifi_spot_address.csv", addresses)

    # Wi-Fi Spot Visits
    wifi_spot_visits = ["wifi_spot_visit_id,wifi_spot_visit_user_id,wifi_spot_visit_wifi_spot_id"]
    for spot_id in wifi_spot_ids:
        num_visits = random.randint(0, DATASET_SIZE // 1000)
        for _ in range(num_visits):
            visit_id = str(uuid.uuid4())
            start = faker.date_time_this_decade()
            end = start + timedelta(hours=random.randint(1, 5))
            wifi_spot_visits.append(f"{visit_id},{user_id},{spot_id}")
    write_csv(f"{DATASET_SIZE}/wifi_spot_visit.csv", wifi_spot_visits)

    # Reviews
    reviews = ["review_id,review_user_id,review_wifi_spot_id"]
    review_ids = []
    for spot_id in wifi_spot_ids:
        review_id = str(uuid.uuid4())
        review_ids.append((review_id, spot_id))
        reviews.append(
            f"{review_id},"
            f"{user_id},{spot_id}"
        )
    write_csv(f"{DATASET_SIZE}/review.csv", reviews)

    # Review Attributes
    review_attributes = ["review_id,review_attribute_classification_name"]
    for review_id, _ in review_ids:
        for attr in random.sample(REVIEW_ATTRIBUTES, k=random.randint(2, 5)):
            review_attributes.append(
                f"{review_id},{attr}"
            )
    write_csv(f"{DATASET_SIZE}/review_attribute_classification.csv", review_attributes)

# -----------------------------
# EXECUÇÃO
# -----------------------------
if __name__ == "__main__":
    generate_dataset(DATASET_SIZE)
