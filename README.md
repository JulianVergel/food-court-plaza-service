# Microservicio de Plaza (food-court-plaza-service)

## Descripción General

Este repositorio contiene el código fuente del **Microservicio de Plaza**, un componente vital de la arquitectura de la Plazoleta de Comidas. Su principal responsabilidad es la **gestión integral de la información de los restaurantes** que operan dentro de la plazoleta. Permite a los administradores registrar y gestionar los establecimientos, lo que a su vez habilita a los clientes a visualizar y seleccionar opciones para realizar sus pedidos.

## Funcionalidades Clave

### Gestión de Restaurantes:
* **Registro de Nuevos Restaurantes:** Permite a los administradores crear nuevas entradas para restaurantes en el sistema, asociándolos a un usuario propietario específico.
* **Validación de Datos:** Asegura la integridad y coherencia de la información del restaurante, aplicando reglas de negocio para campos como el NIT, teléfono, nombre y la validación del usuario propietario.

## Arquitectura

Este microservicio está diseñado siguiendo los principios de la **Arquitectura Hexagonal (Puertos y Adaptadores)**. Esto asegura una clara separación de la lógica de negocio del dominio de los detalles de infraestructura (bases de datos, frameworks web, comunicación entre microservicios, etc.), haciendo el código más modular, testable y adaptable a cambios futuros.

## Componentes Principales

* **Núcleo (Dominio y Servicios de Aplicación):** Contiene la lógica de negocio fundamental para la gestión de restaurantes, incluyendo las validaciones para la creación y las reglas de negocio específicas.
* **Puertos:** Interfaces que definen cómo el núcleo interactúa con el mundo exterior. Incluyen puertos de entrada (para la API REST) y puertos de salida (para persistencia y comunicación con otros microservicios).
* **Adaptadores de Entrada (Driving Adapters):** Implementaciones de las APIs REST que exponen las funcionalidades del servicio, permitiendo la interacción con clientes externos (ej. un frontend o un gateway API).
* **Adaptadores de Salida (Driven Adapters):** Implementaciones que conectan el núcleo con la base de datos (persistencia) y con otros microservicios (ej. el Microservicio de Usuarios para validar el propietario).

## Tecnologías Utilizadas

* **Lenguaje de Programación:** Java
* **Framework:** Spring Boot
* **Base de Datos:** PostgreSQL
* **Gestión de Dependencias:** Gradle

## Cómo Empezar

### Prerequisitos

* **Java Amazon Corretto 17** (o cualquier JDK compatible con Java 17)
* **Gradle** (preferiblemente la versión wrapper incluida en el proyecto)
* Una instancia de base de datos **PostgreSQL** en ejecución.
* Acceso al **Microservicio de Usuarios** (food-court-user-service) para la validación de propietarios.
