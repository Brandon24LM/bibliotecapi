# Proyecto 1: Biblioteca
Nombre: BRANDON LOZANO


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
