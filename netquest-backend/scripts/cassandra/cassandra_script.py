from faker import Faker
import uuid
import random
from datetime import datetime, timedelta

faker = Faker('pt_PT')

# Altera aqui para 500, 5000 ou 5000000
DATASET_SIZE = 500

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
    user_id = str(uuid.uuid4())
    users = ["user_id,first_name,last_name,user_name,mail,birthdate,gender,role,address_line1,address_line2,city,district,country,zip_code,vat_number"]
    users.append(f"{user_id},{escape_string(faker.first_name())},{escape_string(faker.last_name())},{faker.user_name()},{faker.email()},{faker.date_of_birth()},{random.choice(['MALE','FEMALE','OTHER'])},USER,{faker.street_address()},,{faker.city()},{faker.administrative_unit()},{faker.country()},{faker.postcode()},PT123456789")
    write_csv("cassandra_users.csv", users)

    # Wi-Fi Spots (endereços embutidos)
    wifi_spots = [
        "wifi_spot_id,wifi_spot_name,wifi_spot_latitude,wifi_spot_longitude,wifi_spot_description,"
        "wifi_spot_air_conditioning,wifi_spot_child_friendly,wifi_spot_covered_area,wifi_spot_crowded,"
        "wifi_spot_disabled_access,wifi_spot_good_view,wifi_spot_noise_level,wifi_spot_signal_strength,"
        "wifi_spot_wifi_quality,wifi_spot_bandwidth_limitations,wifi_spot_location_type,wifi_spot_management,"
        "wifi_spot_available_power_outlets,wifi_spot_charging_stations,wifi_spot_pet_friendly,wifi_spot_food_options,"
        "wifi_spot_drink_options,wifi_spot_restrooms_available,wifi_spot_parking_availability,wifi_spot_user_id,"
        "wifi_spot_create_date_time,wifi_spot_address_line_1,wifi_spot_address_line_2,wifi_spot_address_city,"
        "wifi_spot_address_district,wifi_spot_address_country,wifi_spot_address_zip_code"
    ]
    wifi_spot_ids = []
    for _ in range(size):
        spot_id = str(uuid.uuid4())
        wifi_spot_ids.append(spot_id)
        wifi_spots.append(
            f"{spot_id},{escape_string(faker.company())},{faker.latitude()},{faker.longitude()},{escape_string(faker.text(50))},"
            f"{random.choice([True, False])},{random.choice([True, False])},{random.choice([True, False])},{random.choice([True, False])},"
            f"{random.choice([True, False])},{random.choice([True, False])},{random.randint(0,3)},{random.randint(0,2)},{random.randint(0,2)},"
            f"{random.choice([True, False])},{random.randint(0,6)},{random.randint(0,2)},{random.choice([True, False])},{random.choice([True, False])},"
            f"{random.choice([True, False])},{random.choice([True, False])},{random.choice([True, False])},{random.choice([True, False])},"
            f"{random.choice([True, False])},{user_id},{faker.date_time_this_decade()},{faker.street_address()},,"
            f"{faker.city()},{faker.administrative_unit()},{faker.country()},{faker.postcode()}"
        )
    write_csv("cassandra_wifi_spot.csv", wifi_spots)

    # Visitas (com info do spot embutida)
    wifi_spot_visits = ["user_id,visit_id,wifi_spot_id,wifi_spot_name,wifi_spot_location,visit_start,visit_end"]
    visit_ids = []
    for spot_id in wifi_spot_ids:
        num_visits = random.randint(0, max(1, size // 10))
        spot_name = escape_string(faker.company())
        location = f"{faker.city()}, {faker.country()}"
        for _ in range(num_visits):
            visit_id = str(uuid.uuid4())
            start = faker.date_time_this_decade()
            end = start + timedelta(hours=random.randint(1, 5))
            wifi_spot_visits.append(f"{user_id},{visit_id},{spot_id},{spot_name},{location},{start},{end}")
            visit_ids.append((visit_id, spot_id))
    write_csv("cassandra_wifi_spot_visit.csv", wifi_spot_visits)

    # Reviews
    reviews = ["wifi_spot_id,review_id,user_id,review_comment,review_create_date_time,review_overall_classification"]
    review_ids = []
    for spot_id in wifi_spot_ids:
        review_id = str(uuid.uuid4())
        review_ids.append(review_id)
        reviews.append(f"{spot_id},{review_id},{user_id},{escape_string(faker.text(50))},{faker.date_time_this_decade()},{random.randint(1, 5)}")
    write_csv("cassandra_review.csv", reviews)

    # Atributos das reviews
    review_attributes = ["review_id,attribute_name,attribute_value"]
    for review_id in review_ids:
        for attr in random.sample(REVIEW_ATTRIBUTES, k=random.randint(2, 5)):
            review_attributes.append(f"{review_id},{attr},{random.choice(['low', 'medium', 'high'])}")
    write_csv("cassandra_review_attribute_classification.csv", review_attributes)


# Executar
if __name__ == "__main__":
    generate_dataset(DATASET_SIZE)
