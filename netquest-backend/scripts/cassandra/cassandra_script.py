from faker import Faker
import uuid
import random
from datetime import datetime, timedelta

faker = Faker('pt_PT')

DATASET_SIZE = 200000

REVIEW_ATTRIBUTES = ['cleanliness', 'wifi_speed', 'comfort', 'ambience', 'service']


def escape_string(value):
    if value is None:
        return ''
    value = value.replace('"', '""')
    value = value.replace('\n', ' ').replace('\r', ' ')
    return value.strip()


def write_csv(filename, lines):
    with open(filename, "w", encoding="utf-8") as f:
        f.write("\n".join(lines))


def generate_dataset(size):
    # 1 Utilizador
    user_id = "d0d325a1-c660-4a20-b5cd-cbef325dd834"
    users = ["user_id,first_name,last_name,user_name,mail,birthdate,gender,role,address_line1,address_line2,city,district,country,zip_code,vat_number"]
    users.append(f"{user_id},{escape_string(faker.first_name())},{escape_string(faker.last_name())},{faker.user_name()},{faker.email()},{faker.date_of_birth()},{random.choice(['MALE','FEMALE','OTHER'])},USER,{faker.street_address()},,{faker.city()},{faker.administrative_unit()},{faker.country()},{faker.postcode()},PT123456789")
    write_csv(f"{DATASET_SIZE}/cassandra_users.csv", users)

    # Wi-Fi Spots (endereços embutidos)
    wifi_spots = [
        "wifi_spot_id,"
        "wifi_spot_user_id"
    ]
    wifi_spot_ids = []
    for _ in range(size):
        spot_id = str(uuid.uuid4())
        wifi_spot_ids.append(spot_id)
        wifi_spots.append(
            f"{spot_id},"
            f"{user_id}"
        )
    write_csv(f"{DATASET_SIZE}/cassandra_wifi_spot.csv", wifi_spots)

    # Visitas (com info do spot embutida)
    wifi_spot_visits = ["user_id,visit_id,wifi_spot_id"]
    visit_ids = []
    for spot_id in wifi_spot_ids:
        num_visits = random.randint(0, DATASET_SIZE // 1000)
        for _ in range(num_visits):
            visit_id = str(uuid.uuid4())
            wifi_spot_visits.append(f"{user_id},{visit_id},{spot_id}")
            visit_ids.append((visit_id, spot_id))
    write_csv(f"{DATASET_SIZE}/cassandra_wifi_spot_visit.csv", wifi_spot_visits)

    # Reviews
    reviews = ["wifi_spot_id,review_id,user_id"]
    review_ids = []
    for spot_id in wifi_spot_ids:
        review_id = str(uuid.uuid4())
        review_ids.append(review_id)
        reviews.append(f"{spot_id},{review_id},{user_id}")
    write_csv(f"{DATASET_SIZE}/cassandra_review.csv", reviews)

    # Atributos das reviews
    review_attributes = ["review_id,attribute_name"]
    for review_id in review_ids:
        for attr in random.sample(REVIEW_ATTRIBUTES, k=random.randint(2, 5)):
            review_attributes.append(f"{review_id},{attr}")
    write_csv(f"{DATASET_SIZE}/cassandra_review_attribute_classification.csv", review_attributes)


# Executar
if __name__ == "__main__":
    generate_dataset(DATASET_SIZE)
