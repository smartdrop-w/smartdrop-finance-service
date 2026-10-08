# smartdrop-finance-service

> **SmartDrop â€” IoT Liquid Monitoring & Quality Management**  
> UPC â€” Fundamentos de Arquitectura de Software (2026-20)  
> Autor: **Jean Niels Arizabal Condori**

## ðŸ“‹ Descripcion
SmartDrop Finance Microservice: Suscripciones SaaS, Metodos de pago y Billetera digital

## ðŸš€ Ejecucion Rapida (Zero Friction)
Para iniciar el servicio localmente:
``bash
# En Windows PowerShell
./mvnw spring-boot:run
``

* **Puerto Local:** $(System.Collections.Hashtable.Port)
* **Swagger UI:** [http://localhost:8085/swagger-ui/index.html](http://localhost:8085/swagger-ui/index.html)
* **OpenAPI Docs:** [http://localhost:8085/v3/api-docs](http://localhost:8085/v3/api-docs)
* **Health Check Probe:** [http://localhost:8085/api/v1/health](http://localhost:8085/api/v1/health)

## ðŸ§ª Pruebas Automatizadas
``bash
./mvnw test
``
