# Proyecto 1: Biblioteca

API en Spring Boot para registrar libros, prestarlos y recibirlos de vuelta.

- Dependencias: Spring Web, Spring Data JPA, H2 Database, Lombok
- Java 17 o superior
- Puerto: **9090**
- Base de datos: H2 en modo archivo (`./data/biblioteca`), los datos se quedan al reiniciar

## Cómo correrlo

```bash
mvn spring-boot:run
```

O abre la carpeta en IntelliJ / VS Code y ejecuta `BibliotecaApplication`.

Consola de H2: http://localhost:9090/h2-console
- JDBC URL: `jdbc:h2:file:./data/biblioteca`
- Usuario: `sa` (sin contraseña)

## Estructura

```
com.example.biblioteca
├── model/Libro.java                 -> entidad
├── repository/LibroRepository.java  -> acceso a datos
├── service/LibroService.java        -> reglas del negocio
└── controller/LibroController.java  -> endpoints
```

## Campos

| Campo      | Tipo    | Ejemplo     |
|------------|---------|-------------|
| id         | Long    | 1 (auto)    |
| titulo     | String  | Huasipungo  |
| autor      | String  | Jorge Icaza |
| anio       | int     | 1934        |
| disponible | boolean | true        |

## Endpoints

| Método | Ruta                               | Qué hace                                  | Respuesta     |
|--------|------------------------------------|-------------------------------------------|---------------|
| GET    | /api/libro                         | Lista todos                               | 200           |
| GET    | /api/libro/{id}                    | Trae uno                                  | 200 o 404     |
| POST   | /api/libro                         | Crea uno                                  | 201           |
| PUT    | /api/libro/{id}                    | Edita uno                                 | 200 o 404     |
| DELETE | /api/libro/{id}                    | Elimina uno                               | 204 o 404     |
| GET    | /api/libro/disponibles             | Solo los disponibles                      | 200           |
| GET    | /api/libro/autor/{texto}           | Autor contiene el texto (sin mayúsculas)  | 200           |
| GET    | /api/libro/antiguos?antes=1950     | Publicados antes de ese año (def. 1950)   | 200           |
| PUT    | /api/libro/{id}/prestar            | Marca como no disponible                  | 200, 404, 409 |
| PUT    | /api/libro/{id}/devolver           | Marca como disponible                     | 200, 404, 409 |

## JSON de ejemplo

```json
{
  "titulo": "Huasipungo",
  "autor": "Jorge Icaza",
  "anio": 1934,
  "disponible": true
}
```

## Postman

Importa `Biblioteca.postman_collection.json` en Postman y ejecuta las peticiones en orden.

## Pruebas con curl

```bash
curl -X POST http://localhost:9090/api/libro \
  -H "Content-Type: application/json" \
  -d '{"titulo": "Huasipungo", "autor": "Jorge Icaza", "anio": 1934, "disponible": true}'

curl http://localhost:9090/api/libro/autor/icaza
curl "http://localhost:9090/api/libro/antiguos?antes=1950"

curl -i -X PUT http://localhost:9090/api/libro/1/prestar    # 200
curl -i -X PUT http://localhost:9090/api/libro/1/prestar    # 409
curl -i -X PUT http://localhost:9090/api/libro/1/devolver   # 200
```
