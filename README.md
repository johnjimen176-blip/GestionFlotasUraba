# GestionFlotasUraba

Proyecto académico de **UNIREMINGTON** para demostrar la persistencia de datos de una flota de transporte intermunicipal en Urabá usando Hibernate y MySQL.

## Tecnologías

- Java 17
- Maven
- Hibernate ORM 6.4
- Jakarta Persistence (JPA)
- MySQL 8 y MySQL Connector/J

La base de datos configurada para la aplicación se llama `flotas_uraba`.

## Entidades

- **Vehiculo**: placa, capacidad, tipo y estado.
- **Ruta**: origen, destino, precio y distancia en kilómetros.
- **Conductor**: nombre, cédula, licencia y teléfono.

## Requisitos

- JDK 17
- Maven
- Servidor MySQL en ejecución

## Configuración de la base de datos

Crea la base de datos antes de ejecutar la aplicación:

```sql
CREATE DATABASE flotas_uraba;
```

Revisa la URL, el usuario y la contraseña de conexión en `src/main/resources/hibernate.cfg.xml` para que coincidan con tu instalación local de MySQL. Hibernate crea o actualiza las tablas de las entidades al iniciar.

## Ejecutar

Desde la raíz del proyecto:

```bash
mvn clean compile exec:java
```

El plugin de Maven ejecuta `com.flotasuraba.Main`. El programa realiza las operaciones **Create, Read, Update y Delete** sobre Vehiculo, Ruta y Conductor. Los registros de ejemplo se eliminan al final de la ejecución.
