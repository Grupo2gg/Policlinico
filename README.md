# Sistema web de citas medicas

Proyecto base en Java con arquitectura en capas usando patron DAO.

## Estructura de carpetas

```text
src/
├─ main/
│  ├─ java/com/example/demo/
│  │  ├─ config/
│  │  │  └─ WebConfig.java
│  │  ├─ controller/
│  │  │  └─ CitaController.java
│  │  ├─ mapper/
│  │  │  ├─ CitaRowMapper.java
│  │  │  ├─ HorarioDisponibleRowMapper.java
│  │  │  └─ PacienteRowMapper.java
│  │  ├─ model/
│  │  │  ├─ Cita.java
│  │  │  ├─ HorarioDisponible.java
│  │  │  └─ Paciente.java
│  │  ├─ repository/
│  │  │  ├─ CitaRepository.java
│  │  │  └─ impl/
│  │  │     └─ CitaRepositoryImpl.java
│  │  ├─ service/
│  │  │  ├─ CitaService.java
│  │  │  └─ impl/
│  │  │     └─ CitaServiceImpl.java
│  │  └─ DemoApplication.java
│  ├─ resources/
│  │  ├─ application.properties
│  │  ├─ schema.sql
│  │  └─ data.sql
│  └─ webapp/WEB-INF/views/
│     └─ citas/
│        ├─ formulario.jsp
│        └─ lista.jsp
└─ test/
```

## Explicacion de cada capa

### Usuario

El usuario interactua con las pantallas JSP. En este proyecto el flujo principal es registrar una cita desde `formulario.jsp` y luego verla en `lista.jsp`.

### Controller

El `CitaController` recibe las peticiones HTTP.

- `GET /citas`: consulta y muestra las citas registradas.
- `GET /citas/nueva`: carga el formulario con horarios disponibles y pacientes desde BD.
- `POST /citas`: recibe los datos del formulario y delega la creacion de la cita al servicio.

El controller no accede directamente a SQL. Su responsabilidad es traducir la peticion web al caso de uso de la aplicacion.

### Service

El `CitaService` concentra la logica de negocio.

- valida que exista paciente
- valida que exista horario
- valida que el motivo no este vacio
- verifica que el horario siga disponible
- coordina la reserva del horario y el registro de la cita dentro de una transaccion

Esta capa desacopla las reglas del negocio respecto al controlador y al acceso a datos.

### Repository

El `CitaRepository` implementa el patron DAO.

- usa `JdbcTemplate` para ejecutar SQL
- usa `RowMapper` para transformar filas en POJOs simples
- encapsula `SELECT`, `INSERT` y `UPDATE`

Aqui vive todo el acceso a la base de datos H2. No hay JPA ni entidades avanzadas.

### Base de datos

H2 corre en memoria y se inicializa al arrancar la aplicacion.

- `schema.sql` crea las tablas `paciente`, `horario_disponible` y `cita`
- `data.sql` inserta datos de prueba

## Flujo completo de una peticion: crear cita

Ejemplo: el usuario crea una nueva cita medica.

1. El usuario abre `/citas/nueva`.
2. `CitaController` llama a `CitaService.listarHorariosDisponibles()`.
3. `CitaService` delega al `CitaRepository`.
4. `CitaRepositoryImpl` ejecuta el `SELECT` con `JdbcTemplate`.
5. `HorarioDisponibleRowMapper` convierte cada fila del resultado en un POJO `HorarioDisponible`.
6. El controller envia esos datos a `formulario.jsp`.
7. El usuario completa el formulario y envia `POST /citas`.
8. `CitaController` recibe `pacienteId`, `horarioId`, `motivo` y `observaciones`, arma el objeto `Cita` y llama a `CitaService.crearCita(cita)`.
9. `CitaServiceImpl` valida los datos y confirma que el horario sigue disponible.
10. `CitaServiceImpl` llama al repository para marcar el horario como `RESERVADO`.
11. `CitaServiceImpl` llama al repository para insertar la nueva fila en `cita`.
12. H2 guarda la informacion y el service devuelve el control al controller.
13. El controller redirige a `/citas`.
14. `lista.jsp` muestra la nueva cita registrada.

## Relacion entre capas

```text
Usuario -> JSP -> Controller -> Service -> Repository -> H2
```

## Ejecutar

```bash
./mvnw spring-boot:run
```

En Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

Rutas utiles:

- `http://localhost:8080/citas`
- `http://localhost:8080/h2-console`
