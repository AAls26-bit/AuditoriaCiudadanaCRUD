# CRUD Auditoria Ciudadana - Java + MySQL

Este proyecto esta basado en la estructura del ejemplo `universidadUT`: paquetes `config`, `dao`, `modelo`, `vista` y clase `Main`.

## Antes de ejecutar

1. Ejecuta primero el script `auditoriaciudadana.sql` en MySQL.
2. Abre esta carpeta como proyecto Maven en IntelliJ IDEA.
3. En `src/main/java/org/example/config/Conexion.java`, cambia `USER` y `PASSWORD` si tu MySQL no usa `root` sin contrasena.
4. Ejecuta `org.example.Main`.

## Que hace cada archivo

- `pom.xml`: configura Maven, Java 17 y el conector de MySQL.
- `Main.java`: punto de entrada; crea el menu y lo inicia.
- `Conexion.java`: centraliza la conexion a MySQL para que los DAO no repitan URL, usuario y contrasena.
- `Validaciones.java`: clase utilitaria para validar textos, fechas, horas, correos e IDs. Ayuda a cumplir los requerimientos no funcionales de datos correctos.
- `Validable.java`: interfaz que obliga a las clases importantes a implementar `esValido()`.
- `PermisosSolicitud.java`: interfaz para definir si un usuario puede administrar solicitudes.
- `RegistroAuditoria.java`: clase abstracta base para registros con fecha. Tiene metodos abstractos `getIdentificador()` y `resumenCorto()`.
- `UsuarioSistema.java`: clase abstracta para usuarios. Encapsula id, nombre, correo y estatus.
- `Administrador.java`: hereda de `UsuarioSistema`; es el unico usuario con permisos para el CRUD.
- `UsuarioConsulta.java`: hereda de `UsuarioSistema`; queda creado, pero su modulo aparece como pendiente.
- `TipoSolicitud.java`, `EstadoSolicitud.java`, `TipoEntrega.java`, `EvidenciaEntrega.java`: enums que limitan las opciones a las permitidas por la base de datos.
- `Solicitud.java`: modelo principal del CRUD. Tiene encapsulamiento, constructor que llama setters, getters con formato, setters con validacion y `toString()` que llama getters.
- `OperacionesCrud.java`: interfaz generica con metodos CRUD.
- `SolicitudDAO.java`: implementa el CRUD contra MySQL usando `PreparedStatement`, transacciones y `try-catch`.
- `UsuarioDAO.java`: crea o recupera usuarios demo para que exista un `idUsuario` valido al registrar solicitudes.
- `Menu.java`: menu por consola. Permite agregar, editar, eliminar, listar y filtrar solicitudes.

## Campos usados

El requerimiento pide datos de solicitud y entrega en el mismo registro. En la base esos datos estan separados en dos tablas:

- `Solicitud`: folio, fecha, lugar, asunto, tipo, estado e id de usuario.
- `Entrega`: tipo de entrega, fecha, hora, area, cargo, correo, evidencia y vencimiento.

Por eso el modelo Java `Solicitud` junta ambos grupos de datos y el DAO guarda en ambas tablas dentro de una transaccion.

## POO que cumple

- Clases: todos los archivos `.java`.
- Encapsulamiento: atributos privados y acceso por getters/setters.
- Constructor llama setters: `Solicitud` y `UsuarioSistema`.
- Getters con formato: fechas salen como `dd/MM/yyyy`, horas como `HH:mm`, textos capitalizados y folios en mayusculas.
- Setters con validaciones: no permite campos obligatorios vacios, IDs invalidos, correos mal formados ni vencimientos anteriores a la entrega.
- `toString()`: imprime usando getters.
- Herencia: `Solicitud` hereda de `RegistroAuditoria`; `Administrador` y `UsuarioConsulta` heredan de `UsuarioSistema`.
- Polimorfismo: el menu trata a `Administrador` y `UsuarioConsulta` como `UsuarioSistema`.
- Clases abstractas: `RegistroAuditoria` y `UsuarioSistema`.
- Metodos abstractos: `getIdentificador()`, `resumenCorto()`, `getIdRol()`, `getNombreRol()`, `mostrarMenuPermitido()`.
- Interfaces: `Validable`, `PermisosSolicitud`, `OperacionesCrud`.
- Manejo de errores: uso de `try-catch` en `Menu`, `UsuarioDAO` y `SolicitudDAO`.
- Menu: `Menu.java` contiene el flujo completo por consola.
