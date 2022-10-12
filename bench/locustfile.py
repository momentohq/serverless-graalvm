import random
from locust import HttpUser, task, between


class StartUser(HttpUser):
    wait_time = between(1, 2)

    @task
    def get_user(self):
        self.client.get("/hello")
