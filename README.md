# smartdrop-finance-service

> **SmartDrop - IoT Liquid Monitoring & Quality Management**  
> *UPC - Fundamentos de Arquitectura de Software (2026-20)*  
> *Autor Responsable:* **Jean Niels Arizabal Condori**

---

## Descripcion General

Microservicio de facturacion y suscripciones SaaS, encargado de la gestion de planes residenciales y comerciales, metodos de pago, balance de billetera digital y facturacion recurrente.

---

## Ejecucion en Entorno Local

Para compilar y ejecutar el proyecto localmente sin preconfiguraciones externas:

```powershell
# Compilacion y arranque con Maven Wrapper
./mvnw spring-boot:run
```

## Configuracion de Puertos y Endpoints

* **Puerto Local:** 8085
* **Swagger UI:** [http://localhost:8085/swagger-ui/index.html](http://localhost:8085/swagger-ui/index.html)
* **OpenAPI Especificacion JSON:** [http://localhost:8085/v3/api-docs](http://localhost:8085/v3/api-docs)
* **Health Check Liveness Probe:** [http://localhost:8085/api/v1/health](http://localhost:8085/api/v1/health)

---

## Pruebas Automatizadas

Para validar la suite de pruebas unitarias y de integracion:

```powershell
./mvnw test
```