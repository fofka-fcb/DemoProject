# Тестовое задание (Сервис по созданию заявок)

## Технологии
* Spring Boot 3.0 (Security, Oauth2, Data, Web, Validation)
* Spring Cloud (OpenFeign)
* JSON Web Tokens (JWT)
* Postgres
* Liquibase
* Maven

## Чтобы начать работу необходимо:
* Скачать репозиторий: `git clone https://github.com/fofka-fcb/DemoProject.git`
* Добавить в файле application.properties настройки для базы данных
* Чтобы заработала авторизация необходимо в сервисе DaData из личного кабинета взять два секретных ключа и добавить их в файле application.properties (myToken, secretToken)
* Запустить приложение через среду разработки

-> приложение будет работать по адресу http://localhost:9000

## Заранее зарегестрированные пользователи:
* Роль пользователя: Администратор (Login: admin, Password: password)
* Роль пользователя: Оператор (Login: operator, Password: password)
* Роль пользователя: Пользователь (Login: user_1, Password: password)
* Роль пользователя: Пользователь (Login: user_2, Password: password)
* Роль пользователя: Пользователь (Login: user_3, Password: password)
