# PruebaAndroid1

## Enlace del repositorio --> [https://github.com/HugoSanchezGallego/PruebaAndroid1.git)

Una aplicación de gestión de tareas para Android desarrollada en Kotlin, usando la arquitectura Jetpack Compose y Firestore como base de datos en la nube para el almacenamiento de tareas.

## Descripción del Proyecto

Esta aplicación permite a los usuarios crear, listar y gestionar sus tareas de forma eficiente. Las tareas se dividen en dos categorías: **Pendientes** y **Hechas**. Los usuarios pueden cambiar el estado de una tarea y eliminar tareas completadas. La interfaz está diseñada con Jetpack Compose para una experiencia visual y funcional moderna.

## Características

- **Añadir Tareas:** Permite crear una nueva tarea y añadirla a la lista de tareas pendientes.
- **Visualizar Tareas:** Visualiza las tareas, categorizadas en tareas pendientes o completadas.
- **Actualizar Estado de la Tarea:** Permite cambiar el estado de una tarea a “Hecha” o “Pendiente”.
- **Eliminar Tareas:** Posibilidad de eliminar una tarea, ya sea desde la lista de pendientes o hechas.
- **Conexión en Tiempo Real con Firestore:** Las actualizaciones se reflejan en tiempo real usando Firestore como backend.

## Tecnologías Usadas

- **Kotlin:** Lenguaje de programación principal.
- **Jetpack Compose:** Para crear una interfaz de usuario moderna y reactiva.
- **Firebase Firestore:** Base de datos en la nube para almacenar y sincronizar las tareas.
- **Material Design 3:** Estilo visual y componentes de interfaz de usuario para una experiencia intuitiva y atractiva.

## Estructura del Proyecto

- `MainActivity`: Actividad principal que inicia la conexión con Firestore y define la vista principal.
- `TaskManagerScreen`: Composable que representa la interfaz principal de la gestión de tareas.
- `TaskItem`: Composable que representa cada tarea en la lista, con opciones para cambiar el estado o eliminarla.
- `Task`: Data class que define el modelo de tarea, con campos como `id`, `description` e `isDone`.

## Uso de la Aplicación
- **Añadir una Nueva Tarea:
- **Escribe una descripción en el campo correspondiente y pulsa el botón “Añadir Tarea”.
- **Visualizar Tareas Pendientes y Completadas:
- **Usa los botones “Tareas Pendientes” y “Tareas Hechas” para alternar entre las dos listas.
- **Cambiar el Estado de una Tarea:
- **Pulsa en una tarea para expandirla. Usa el botón “Marcar como Hecho” o “Marcar como Pendiente” según corresponda.
- **Eliminar una Tarea:
- **Pulsa en una tarea para expandirla y selecciona el botón “Borrar”.
