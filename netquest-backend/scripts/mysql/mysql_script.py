from faker import Faker
import uuid
import random
from datetime import datetime, timedelta

# -----------------------------
# CONFIGURAÇÃO
# -----------------------------
faker = Faker('pt_PT')
DATASET_SIZE = 500  # Altera para 5000 ou 5000000 conforme necessário

REVIEW_ATTRIBUTES = ['cleanliness', 'wifi_speed', 'comfort', 'ambience', 'service']

# -----------------------------
# UTILITÁRIOS
# -----------------------------
def escape_string(value):
    if value is None:
        return '""'
    value = value.replace('"', '""')
    value = value.replace('\n', ' ').replace('\r', ' ')
    return f'"{value.strip()}"'

def write_csv(filename, lines):
    with open(f"{filename}", "w", encoding="utf-8") as f:
        f.write("\n".join(lines))

# -----------------------------
# GERADOR DE DADOS
# -----------------------------
def generate_dataset(size):
    # --- Utilizador único ---
    user_id = str(uuid.uuid4())
    users = ["user_id,first_name,last_name,mail"]
    users.append(f"{user_id},{escape_string(faker.first_name())},{escape_string(faker.last_name())},{faker.email()}")
    write_csv("users.csv", users)

    # --- Wi-Fi Spots (iguais ao tamanho do dataset) ---
    wifi_spots = ["wifi_spot_id,wifi_spot_name,wifi_spot_description,wifi_spot_latitude,wifi_spot_longitude,wifi_spot_create_date_time"]
    wifi_spot_ids = []
    for _ in range(size):
        spot_id = str(uuid.uuid4())
        wifi_spot_ids.append(spot_id)
        wifi_spots.append(
            f"{spot_id},{escape_string(faker.company())},{escape_string(faker.text(max_nb_chars=100))},{faker.latitude()},{faker.longitude()},{faker.date_time_this_decade()}"
        )
    write_csv("wifi_spot.csv", wifi_spots)

    # --- Visitas (0 até 1/10 do tamanho total, por cada spot) ---
    wifi_spot_visits = ["wifi_spot_visit_id,wifi_spot_visit_user_id,wifi_spot_visit_wifi_spot_id,wifi_spot_visit_start_datetime,wifi_spot_visit_end_datetime"]
    visit_ids = []
    for spot_id in wifi_spot_ids:
        num_visits = random.randint(0, max(1, size // 10))
        for _ in range(num_visits):
            visit_id = str(uuid.uuid4())
            visit_ids.append((visit_id, spot_id))
            start = faker.date_time_this_decade()
            end = start + timedelta(hours=random.randint(1, 5))
            wifi_spot_visits.append(f"{visit_id},{user_id},{spot_id},{start},{end}")
    write_csv("wifi_spot_visit.csv", wifi_spot_visits)

    # --- Reviews (1 por cada Wi-Fi spot) ---
    reviews = ["review_id,review_comment,review_create_date_time,review_overall_classification,review_user_id,review_wifi_spot_id"]
    review_ids = []
    for spot_id in wifi_spot_ids:
        review_id = str(uuid.uuid4())
        review_ids.append((review_id, spot_id))
        reviews.append(
            f"{review_id},{escape_string(faker.text(max_nb_chars=100))},{faker.date_time_this_decade()},{random.randint(1, 5)},{user_id},{spot_id}"
        )
    write_csv("review.csv", reviews)

    # --- Atributos da Review (2 a 5 atributos por review) ---
    review_attributes = ["review_id,review_attribute_classification_name,review_attribute_classification_value"]
    for review_id, _ in review_ids:
        for attr in random.sample(REVIEW_ATTRIBUTES, k=random.randint(2, 5)):
            review_attributes.append(
                f"{review_id},{attr},{random.choice(['low', 'medium', 'high'])}"
            )
    write_csv("review_attribute_classification.csv", review_attributes)

# -----------------------------
# EXECUÇÃO
# -----------------------------
if __name__ == "__main__":
    generate_dataset(DATASET_SIZE)
