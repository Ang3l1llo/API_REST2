# API REST2

## Descripción del Proyecto
API REST2 API es una API REST segura para la gestión de tareas, que permite a los usuarios crear, gestionar y completar tareas, con diferentes niveles de acceso según el rol del usuario.

## Documentos del Proyecto

### Usuario (User)
- `id`: ObjectID
- `username`: String (único)
- `password`: String (encriptada)
- `email`: String
- `role`: String (USER/ADMIN)
- `address`: Object


### Tarea (Task)
- `id`: ObjectID
- `title`: String
- `description`: String
- `state`: Boolean
- `userId`: String (referencia al Usuario)
- `completed`: LocalDate

## Endpoints

### Autenticación
1. **Registro de Usuario**
    - Método: POST
    - Ruta: `/users/register`
    - Descripción: Permite registrar un nuevo usuario en el sistema
    - Acceso: Público

2. **Login**
    - Método: POST
    - Ruta: `/users/login`
    - Descripción: Autenticación de usuario y generación de token JWT
    - Acceso: Público

### Gestión de Tareas

1. **Crear Tarea**
    - Método: POST
    - Ruta: `/tasks/create`
    - Descripción: Crear una nueva tarea
    - Acceso: USER (solo para sí mismo), ADMIN (para cualquier usuario)

2. **Obtener Tareas**
    - Método: GET
    - Ruta: `/tasks/getAll`
    - Descripción: Obtener lista de tareas
    - Acceso: USER (solo sus tareas), ADMIN (todas las tareas)

3. **Obtener Tarea por ID**
    - Método: GET
    - Ruta: `/tasks/{id}`
    - Descripción: Obtener detalles de una tarea específica
    - Acceso: USER (solo sus tareas), ADMIN (cualquier tarea)

4. **Actualizar Tarea**
    - Método: PATCH
    - Ruta: `/tasks/{id}`
    - Descripción: Actualizar el estado de una tarea 
    - Acceso: USER (solo sus tareas), ADMIN (cualquier tarea)

5. **Eliminar Tarea**
    - Método: DELETE
    - Ruta: `/tasks/{id}`
    - Descripción: Eliminar una tarea
    - Acceso: USER (solo sus tareas), ADMIN (cualquier tarea)

## Lógica de Negocio

1. **Gestión de Usuarios**
    - Los usuarios se registran con email, username y password
    - Las contraseñas se almacenan encriptadas
    - Por defecto, los nuevos usuarios tienen rol USER
    - El rol ADMIN solo puede ser asignado por otro ADMIN

2. **Gestión de Tareas**
    - Cada tarea debe tener un título y descripción
    - Las tareas siempre pertenecen a un usuario
    - Solo el propietario o un admin pueden modificar/eliminar una tarea
    - Al completar una tarea, se registra la fecha de completado

## Manejo de Excepciones y Códigos de Estado

### Códigos de Éxito
- 200 OK: Petición exitosa
- 201 Created: Recurso creado exitosamente
- 204 No Content: Petición exitosa sin contenido de respuesta

### Códigos de Error
- 400 Bad Request
    - Datos de entrada inválidos
    - Formato de request incorrecto

- 401 Unauthorized
    - Token no proporcionado
    - Token expirado
    - Credenciales inválidas

- 403 Forbidden
    - Usuario no tiene permisos necesarios
    - Intento de modificar recursos de otro usuario

- 404 Not Found
    - Recurso no encontrado
    - Tarea o usuario inexistente

- 409 Conflict
    - Username ya existe
    - Email ya registrado


## Restricciones de Seguridad

1. **Autenticación**
    - Implementación de JWT para autenticación
    - Tokens con expiración de 24 horas
    - Refresh tokens para renovación de sesión

2. **Autorización**
    - Validación de propiedad de recursos
    - Control de acceso basado en roles

3. **Validación de Datos**
    - Comprobación de todas las entradas de usuario
    - Validación de formatos de email y contraseña
    - Límites de longitud en campos de texto


## PRUEBAS GESTIÓN USUARIOS

### Creación de Usuario

![Crear-user](Comprobaciones%2FCrear-user.png)
![Crear-user-bbdd](Comprobaciones%2FCrear-user-bbdd.png)


### Creación de Admin

![Crear-admin](Comprobaciones%2FCrear-admin.png)
![Crear-admin-bbdd](Comprobaciones%2FCrear-admin-bbdd.png)


### Login de Usuario

![Login-user](Comprobaciones%2FLogin-user.png)


### Crear un Usuario repetido

![Crear-user-repe](Comprobaciones%2FCrear-user-repe.png)


### No coinciden contraseñas al repetirla

![Crear-admin-incorrecto.png](Comprobaciones%2FCrear-admin-incorrecto.png)


### Credenciales Incorrectas

![Login-user-incorrecto.png](Comprobaciones%2FLogin-user-incorrecto.png)



### GUI Registro

![GUI-Registro-Valido.png](Comprobaciones%2FGUI-Registro-Valido.png)

![GUI-Registro-noValido.png](Comprobaciones%2FGUI-Registro-noValido.png)

![Crear-user-bbdd-gui.png](Comprobaciones%2FCrear-user-bbdd-gui.png)


### GUI Login

![GUI-Login.png](Comprobaciones%2FGUI-Login.png)

![GUI-Login-Fail.png](Comprobaciones%2FGUI-Login-Fail.png)


