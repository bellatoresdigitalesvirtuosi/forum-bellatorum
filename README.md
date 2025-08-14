# Forum Bellatorum

**Forum Bellatorum** es un foro jerárquico y modular desarrollado por **Bellatores Digitales Virtuosi** por **HEAM**, donde los usuarios pueden crear y suscribirse a categorías, tópicos y mensajes. El sistema soporta roles y permisos granulares, moderación de contenido y jerarquía de categorías.

---

## 🛠️ Stack Tecnológico

* **Backend:** Java 21 + Spring Boot
* **Seguridad:** Spring Security con JWT y roles/permissions
* **Base de datos:** PostgreSQL 16
* **ORM:** Spring Data JPA + Hibernate
* **Migraciones:** Flyway
* **DTO & Validación:** Jakarta Validation + Lombok
* **Contenedores:** Docker & Docker Compose

---

## 📂 Estructura del Proyecto

```
src/main/java/virtuosi/digitales/bellatores/forumbellatorum/
│
├─ controller/       # Controladores REST (Category, Topic, Message, Auth)
├─ dto/              # Data Transfer Objects (Request/Response)
├─ entity/           # Entidades JPA (User, Role, Category, Topic, Message)
├─ repository/       # Repositorios Spring Data JPA
├─ services/         # Lógica de negocio
└─ security/         # Configuración de seguridad y JWT
```

---

## ⚙️ Configuración con Docker Compose

Archivo `docker-compose.yml`:

```yaml
version: "3.9"

services:
  bellaforum-postgre:
    image: postgres:16-alpine
    container_name: forum-db
    environment:
      POSTGRES_USER: ${DB_USER}
      POSTGRES_PASSWORD: ${DB_PASSWORD}
      POSTGRES_DB: ${DB_NAME}
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    healthcheck:
      test: [ "CMD-SHELL", "pg_isready -U ${DB_USER} -d ${DB_NAME}" ]
      interval: 5s
      timeout: 3s
      retries: 5
    networks:
      - forum-net

  app:
    build: .
    container_name: forum-app
    depends_on:
      bellaforum-postgre:
        condition: service_healthy
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://bellaforum-postgre:5432/${DB_NAME}
      SPRING_DATASOURCE_USERNAME: ${DB_USER}
      SPRING_DATASOURCE_PASSWORD: ${DB_PASSWORD}
      SPRING_JPA_HIBERNATE_DDL_AUTO: validate
      SPRING_JPA_DATABASE_PLATFORM: org.hibernate.dialect.PostgreSQLDialect
    networks:
      - forum-net

volumes:
  postgres_data:

networks:
  forum-net:
```

---

## 🚀 Levantar la aplicación

1. Crear archivo `.env` con tus credenciales:

```env
DB_USER=forum_user
DB_PASSWORD=forum_pass
DB_NAME=forum_db
ADMIN_USERNAME=admin
ADMIN_PASSWORD=admin123
```

> Nota: El usuario administrador por defecto será `admin` con contraseña `admin123`.

2. Ejecutar Docker Compose:

```bash
docker-compose up --build
```

3. Flyway ejecutará automáticamente la migración inicial `V1__init.sql`.

4. La aplicación estará disponible en:

```
http://localhost:8080/forum-bellatorum
```

---

## 🗄️ Base de datos

Tablas principales:

* **users**: usuarios registrados
* **roles**: roles (ADMIN, MODERATOR, USER)
* **permissions**: permisos granulares
* **user\_roles** y **role\_permissions**: relaciones muchos a muchos
* **categories**: categorías jerárquicas
* **topics**: tópicos por categoría
* **messages**: mensajes por tópico

---

## 📌 Endpoints principales

### **Categorías**

| Método | Ruta                       | Roles/Acceso        | Descripción                  |
| ------ | -------------------------- | ------------------- | ---------------------------- |
| POST   | `/categories`              | Usuario autenticado | Crear categoría              |
| PATCH  | `/categories/{id}/approve` | ADMIN               | Aprobar categoría            |
| GET    | `/categories`              | Público             | Listar categorías aprobadas  |
| GET    | `/categories/tree`         | Público             | Listar categorías en árbol   |
| GET    | `/categories/pending`      | ADMIN               | Listar categorías pendientes |

### **Tópicos**

| Método | Ruta           | Roles/Acceso               | Descripción     |
| ------ | -------------- | -------------------------- | --------------- |
| POST   | `/topics`      | Usuario con `TOPIC_CREATE` | Crear tópico    |
| GET    | `/topics/{id}` | Público                    | Obtener tópico  |
| DELETE | `/topics/{id}` | ADMIN / Moderador          | Eliminar tópico |

### **Mensajes**

| Método | Ruta                        | Roles/Acceso                 | Descripción                |
| ------ | --------------------------- | ---------------------------- | -------------------------- |
| POST   | `/messages`                 | Usuario con `MESSAGE_CREATE` | Crear mensaje              |
| GET    | `/messages/topic/{topicId}` | Público                      | Listar mensajes por tópico |
| DELETE | `/messages/{id}`            | ADMIN / Moderador            | Eliminar mensaje           |

---

## 🔐 Seguridad

* JWT para autenticación
* Roles: `ADMIN`, `MODERATOR`, `USER`
* Permisos granulares (`CATEGORY_CREATE`, `CATEGORY_APPROVE`, `TOPIC_CREATE`, `MESSAGE_CREATE`, `MESSAGE_DELETE`)
* Control de acceso mediante `@PreAuthorize`

---

## 📌 Notas

* Las categorías tienen jerarquía y pueden tener moderadores asignados.
* Los tópicos y mensajes están asociados a usuarios y categorías.
* Validación de DTOs con `@Valid` y restricciones de Jakarta Validation.
* Migración inicial `V1__init.sql` incluye roles, permisos y relaciones.
