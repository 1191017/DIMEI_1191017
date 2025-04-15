from faker import Faker
import uuid
import random
from datetime import datetime, timedelta
import json

faker = Faker('pt_PT')

# Configuração do tamanho do dataset
DATASET_SIZE = 500  # Podes mudar para 5000 ou 5000000

REVIEW_ATTRIBUTES = ['cleanliness', 'wifi_speed', 'comfort', 'ambience', 'service']


def generate_bulk_json():
    # Utilizador único
    user_id = str(uuid.uuid4())
    users = [{
        "_id": user_id,
        "first_name": faker.first_name(),
        "last_name": faker.last_name(),
        "user_name": faker.user_name(),
        "mail": faker.email(),
        "birthdate": faker.date_of_birth().isoformat(),
        "gender": random.choice(["MALE", "FEMALE", "OTHER"]),
        "role": "USER_PREMIUM",
        "vat_number": str(faker.random_number(digits=9, fix_len=True)),
        "address": {
            "line1": faker.street_address(),
            "line2": "",
            "city": faker.city(),
            "district": faker.administrative_unit(),
            "country": faker.country(),
            "zip_code": faker.postcode()
        }
    }]

    # WiFi Spots
    wifi_spots = []
    wifi_spot_ids = []
    for _ in range(DATASET_SIZE):
        spot_id = str(uuid.uuid4())
        wifi_spot_ids.append(spot_id)
        wifi_spots.append({
            "_id": spot_id,
            "user_id": user_id,
            "name": faker.company(),
            "latitude": float(faker.latitude()),
            "longitude": float(faker.longitude()),
            "description": faker.text(60),
            "features": {
                "air_conditioning": random.choice([True, False]),
                "child_friendly": random.choice([True, False]),
                "covered_area": random.choice([True, False]),
                "crowded": random.choice([True, False]),
                "disabled_access": random.choice([True, False]),
                "good_view": random.choice([True, False]),
                "noise_level": random.randint(0, 3),
                "signal_strength": random.randint(0, 2),
                "wifi_quality": random.randint(0, 2),
                "bandwidth_limitations": random.choice([True, False]),
                "location_type": random.randint(0, 6),
                "management": random.randint(0, 2),
                "available_power_outlets": random.choice([True, False]),
                "charging_stations": random.choice([True, False]),
                "pet_friendly": random.choice([True, False]),
                "food_options": random.choice([True, False]),
                "drink_options": random.choice([True, False]),
                "restrooms_available": random.choice([True, False]),
                "parking_availability": random.choice([True, False]),
                "heated_in_winter": random.choice([True, False]),
                "open_during_heat": random.choice([True, False]),
                "open_during_rain": random.choice([True, False]),
                "outdoor_fans": random.choice([True, False]),
                "shaded_areas": random.choice([True, False])
            },
            "address": {
                "line1": faker.street_address(),
                "line2": "",
                "city": faker.city(),
                "district": faker.administrative_unit(),
                "country": faker.country(),
                "zip_code": faker.postcode()
            },
            "create_date_time": faker.date_time_this_decade().isoformat()
        })

    # Visitas
    wifi_spot_visits = []
    visit_ids = []
    for spot_id in wifi_spot_ids:
        num_visits = random.randint(0, max(1, DATASET_SIZE // 10))
        for _ in range(num_visits):
            visit_id = str(uuid.uuid4())
            visit_ids.append((visit_id, spot_id))
            start = faker.date_time_this_decade()
            end = start + timedelta(hours=random.randint(1, 5))
            wifi_spot_visits.append({
                "_id": visit_id,
                "user_id": user_id,
                "wifi_spot": {
                    "_id": spot_id,
                    "name": faker.company(),
                    "location": f"{faker.street_address()}, {faker.city()}"
                },
                "start_time": start.isoformat(),
                "end_time": end.isoformat()
            })

    # Reviews com atributos embutidos
    reviews = []
    review_ids = []
    for spot_id in wifi_spot_ids:
        review_id = str(uuid.uuid4())
        review_ids.append(review_id)
        review = {
            "_id": review_id,
            "wifi_spot_id": spot_id,
            "user_id": user_id,
            "comment": faker.text(60),
            "create_date_time": faker.date_time_this_decade().isoformat(),
            "overall_classification": random.randint(1, 5),
            "attributes": []
        }
        for attr in random.sample(REVIEW_ATTRIBUTES, k=random.randint(2, 5)):
            review["attributes"].append({
                "name": attr,
                "value": random.choice(["low", "medium", "high"])
            })
        reviews.append(review)

    # Transações de pontos
    points_transactions = []
    for _ in range(DATASET_SIZE // 10):
        points_transactions.append({
            "_id": str(uuid.uuid4()),
            "user_id": user_id,
            "amount": random.randint(10, 200),
            "datetime": faker.date_time_this_decade().isoformat(),
            "review_id": random.choice(review_ids),
            "wifi_spot_id": random.choice(wifi_spot_ids),
            "wifi_spot_visit_id": random.choice(visit_ids)[0] if visit_ids else None,
            "wifi_spot_visit_my_spot_id": random.choice(visit_ids)[0] if visit_ids else None
        })

    return {
        "users": users,
        "wifi_spot": wifi_spots,
        "wifi_spot_visit": wifi_spot_visits,
        "review": reviews,
        "points_earn_transaction": points_transactions
    }


# Guardar JSONs por coleção
if __name__ == "__main__":
    data = generate_bulk_json()
    for collection, docs in data.items():
        with open(f"{collection}.json", "w", encoding="utf-8") as f:
            json.dump(docs, f, indent=2)
