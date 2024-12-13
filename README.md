
# Bank App - API RESTful

## **Descripción del Proyecto**

**Bank App** es una aplicación bancaria desarrollada con **Spring Boot** que permite gestionar clientes, productos financieros (como cuentas bancarias, tarjetas de crédito, préstamos) y realizar transacciones bancarias. El proyecto sigue el patrón **MVC (Modelo-Vista-Controlador)** para organizar el código, lo que facilita la escalabilidad y el mantenimiento.

### **Objetivo del Proyecto**

El objetivo principal es ofrecer una API RESTful que permita gestionar:

- **Clientes**: Registro, actualización, eliminación y consulta de clientes.
- **Productos Financieros**: Creación y gestión de productos como cuentas bancarias, tarjetas de crédito, préstamos.
- **Transacciones**: Consignaciones, retiros y transferencias entre cuentas.

### **Tecnologías Utilizadas**

- **Backend**: Spring Boot (Java 17)
- **Base de Datos**: PostgreSQL
- **Persistencia de Datos**: Spring Data JPA
- **Seguridad**: Spring Security (opcional, deshabilitado para desarrollo)
- **Dependencias**:
    - Hibernate Validator para validaciones
    - Lombok para evitar la escritura repetitiva de código
    - JUnit y Mockito para pruebas unitarias

### **Arquitectura Utilizada**

El proyecto sigue una arquitectura **MVC (Modelo-Vista-Controlador)** y está diseñado para adoptar una arquitectura **Hexagonal** en el futuro, lo que permite separar la lógica de negocio de los detalles técnicos (como la persistencia de datos).

---

## **Esquema del Proyecto**

```
com.bankapp
├── mvc                 # MVC (Modelo-Vista-Controlador) - Capa que gestiona las rutas RESTful
│   ├── controller      # Controladores que exponen los endpoints para interactuar con el frontend o consumidores
│   │   ├── ClienteController.java
│   │   ├── ProductoController.java
│   │   └── TransaccionController.java
│   ├── model           # Modelos de datos, entidades que corresponden a tablas en la base de datos
│   │   ├── Cliente.java
│   │   ├── Producto.java
│   │   └── Transaccion.java
│   ├── repository      # Repositorios que interactúan con la base de datos (JPA)
│   │   ├── ClienteRepository.java
│   │   ├── ProductoRepository.java
│   │   └── TransaccionRepository.java
│   ├── service         # Servicios que contienen la lógica de negocio
│   │   ├── ClienteService.java
│   │   ├── ProductoService.java
│   │   ├── TransaccionService.java
│   └── utils           # Utilidades generales, como validaciones
│       └── ValidacionesUtils.java
├── hexagonal           # Arquitectura Hexagonal para mantener la lógica de negocio desacoplada
│   ├── core            # Contendrá el modelo y la lógica de negocio core
│   ├── adapter         # Adaptadores para conectar con sistemas externos como bases de datos, servicios
│   └── service         # Servicios que conectan la capa core con adaptadores externos
└── resources           # Archivos de configuración
    ├── application.properties
```

---

## **Ciclo de Vida del Proyecto y Flujo de Datos**

### **Diagrama de Ciclo de Vida**

El flujo de interacción entre las capas del sistema sigue el siguiente esquema:

```
                       +------------------------+
                       | Cliente Controller     |
                       +------------------------+
                                |
             +------------------v------------------+
             |               Cliente Service      |
             +-------------------------------------+
                                |
                  +-------------v-------------+
                  |     Cliente Repository    |
                  +---------------------------+
                                |
                   +------------v------------+
                   |  Base de Datos (PostgreSQL)|
                   +--------------------------+
```

**Flujo**:

1. El **Cliente Controller** recibe una solicitud HTTP.
2. La solicitud es procesada por el **Cliente Service**, que maneja la lógica de negocio.
3. El **Cliente Service** interactúa con el **Cliente Repository** para almacenar o recuperar datos de la base de datos.
4. La base de datos (PostgreSQL) es actualizada o consultada según lo solicitado.
5. El **Controller** devuelve una respuesta HTTP adecuada al cliente.

## **Arquitectura MVC**

- **Modelo**: Representa las entidades de la base de datos (Clientes, Productos, Transacciones) y las operaciones que deben ejecutarse sobre estos datos.
- **Vista**: Al ser una API RESTful, no hay una vista en el sentido tradicional. Los controladores gestionan las respuestas para las rutas de la API.
- **Controlador**: Expone los endpoints REST que permiten realizar operaciones sobre los clientes, productos y transacciones.

El proyecto también está orientado a implementar una **arquitectura Hexagonal** para garantizar la separación de las capas de lógica de negocio y detalles de implementación.

---

## **Controladores**

### **ClienteController**

- **POST** `/mvc/clientes`: Crea un nuevo cliente.
- **PUT** `/mvc/clientes/{id}`: Actualiza un cliente por su ID.
- **DELETE** `/mvc/clientes/{id}`: Elimina un cliente por su ID.
- **GET** `/mvc/clientes/{id}`: Obtiene un cliente por su ID.

### **ProductoController**

- **POST** `/mvc/productos`: Crea un nuevo producto (cuenta bancaria).
- **PUT** `/mvc/productos/producto/{id}`: Actualiza un producto por su ID.
- **PUT** `/mvc/productos/cliente/producto/{idAccount}`: Actualiza un producto por el número de cuenta.
- **DELETE** `/mvc/productos/{id}`: Elimina un producto por su ID.
- **DELETE** `/mvc/productos/cliente/{idAccount}`: Elimina un producto por el número de cuenta.
- **GET** `/mvc/productos/{id}`: Obtiene un producto por su ID.

### **TransaccionController**

- **POST** `/mvc/transacciones/realizar`: Realiza una transacción (consignación, retiro, transferencia) entre productos.
- **GET** `/mvc/transacciones/producto/{productoId}`: Obtiene todas las transacciones asociadas a un producto.

---

## **Ejemplos de Uso con cURL**

### Crear un Cliente

```bash
curl --location 'http://localhost:8080/mvc/clientes' --header 'Content-Type: application/json' --data-raw '{
    "tipoIdentificacion": "CC",
    "numeroIdentificacion": "100722889",
    "nombres": "Pepito Andres",
    "apellidos": "Martinez",
    "correoElectronico": "pepito.martinez@fakemail.com",
    "fechaNacimiento": "1990-06-15"
}'
```

### Crear un Producto (Cuenta Bancaria) para un Cliente

```bash
curl --location 'http://localhost:8080/mvc/productos/cliente/producto/5300000001' --header 'Content-Type: application/json' --data '{
    "cliente": {
        "numeroIdentificacion": 100722889
    },
    "tipoCuenta": "ahorros",
    "saldo": 10000,
    "exentaGMF": false,
    "estado": "activa"
}'
```

### Realizar una Transacción (Consignación)

```bash
curl --location --request POST 'http://localhost:8080/mvc/transacciones/realizar?productoIdOrigen=5300000001&monto=500.00&tipo=consignacion'
```

---

## **Conclusión**

**Bank App** es una solución robusta y escalable para la gestión de clientes, productos financieros y transacciones bancarias. Utiliza tecnologías modernas como **Spring Boot** y **PostgreSQL**, y sigue principios de arquitectura sólida como **MVC**.
