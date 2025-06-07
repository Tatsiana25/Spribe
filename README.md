## Описание проекта для тестирования API

Этот проект предназначен для автоматизированного тестирования REST API, используя Java, TestNG, RestAssured и Allure для отчетности.

Тесты расположены в папке src\test\java\apiAutotests\playerController

Параллелизация настроена через testng.xml, который находится в src\test\resources

### Стек технологий:

Java 11 – основной язык программирования.

TestNG 7.10.2 – фреймворк тестирования.

RestAssured 5.4.0 – библиотека для тестирования REST API.

Log4j – логирование в тестах.

Jackson – обработка JSON.

Allure – генерация отчетов тестирования.

### Для запуска проекта необходимо:

Установить Maven и Java 11.


Запустить тесты: mvn clean test

Сгенерировать отчет: mvn allure:report

Просмотреть отчёт: mvn allure:serve
