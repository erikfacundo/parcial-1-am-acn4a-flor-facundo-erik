# 🚗 **Carental** 🚗

## 📱 Descripción del Proyecto

**Carental** es una aplicación móvil desarrollada en **Android** que permite a los usuarios alquilar vehículos de manera fácil y rápida. 🌟 Conecta con **Firebase Firestore** para almacenar todos los datos de los alquileres, y ofrece una experiencia intuitiva para seleccionar fechas, calcular precios y gestionar reservas. 🚙💨

## ✨ Características

- 🗓 **Seleccionar fechas de alquiler**: Elige las fechas de inicio y fin del alquiler.
- 💰 **Cálculo automático del precio**: Calcula el costo total según los días seleccionados.
- 📝 **Almacenamiento en Firestore**: Guarda la información del alquiler en la nube.
- ✔ **Confirmación del alquiler**: Recibe un mensaje de confirmación con los detalles y precio final.

## ⚙️ Funcionalidad

### 1. Selección de fechas de alquiler 📅
El usuario selecciona una fecha de inicio y una fecha de fin para el alquiler. Se valida que la fecha de fin sea posterior a la de inicio.

### 2. Cálculo del precio total 💵
El precio total del alquiler se calcula en función de los días seleccionados y la tarifa base por día del vehículo.

### 3. Guardar alquiler en Firestore ☁️
Cuando el usuario confirma el alquiler, se guarda toda la información (fechas, precio, ID del usuario) en **Firestore** para que pueda acceder a ella en cualquier momento.

### 4. Confirmación del alquiler ✅
Al finalizar el proceso, el usuario recibe una confirmación con los detalles del alquiler y el precio final calculado.

## 🛠 Tecnologías Utilizadas

- **Android (Java)** 📱: Lenguaje de desarrollo para la aplicación.
- **Firebase** 🔥:
  - **Firestore** ☁️: Almacenamiento en la nube.
  - **Firebase Authentication** 🔑 (opcional): Para gestionar usuarios.
- **DatePicker** 📅: Para seleccionar fechas de inicio y fin del alquiler.
- **ProgressBar** 🔄: Para mostrar el estado de carga mientras se procesan los datos.

## 📂 Estructura del Proyecto

### Clases principales

- **AlquilerToken.java**: Gestiona la creación y obtención de alquileres en **Firestore**.
- **DetallesVehiculoActivity.java**: Pantalla donde el usuario selecciona las fechas del alquiler y visualiza el precio total.
- **Alquiler.java**: Modelo que representa un alquiler con campos como `fechaInicio`, `fechaFin`, `precioFinal`, `diasSeleccionados` y `userId`.

### 🏃 Flujo de la Aplicación

1. **Selección del vehículo** 🚗: El usuario elige un vehículo de la lista disponible.
2. **Selección de fechas** 📅: El usuario elige las fechas de inicio y fin del alquiler.
3. **Cálculo del precio** 💰: La aplicación calcula el precio total según las fechas seleccionadas.
4. **Confirmación del alquiler** ✅: El usuario confirma los datos del alquiler y guarda la información en Firestore.
5. **Mensaje de éxito** 🎉: Se muestra un mensaje con el resumen y el precio final del alquiler.

### 📊 Estructura de Firestore

- **Colección: alquileres**
  - Cada documento representa un alquiler con los siguientes campos:
    - `fechaInicio`: La fecha de inicio del alquiler.
    - `fechaFin`: La fecha de finalización del alquiler.
    - `precioFinal`: El precio total calculado.
    - `diasSeleccionados`: Número de días del alquiler.
    - `userId`: ID del usuario que realiza el alquiler.
[![image.png](https://i.postimg.cc/cCq1yjM2/image.png)](https://postimg.cc/cgmGfFzB)
[![image.png](https://i.postimg.cc/k4RMH98W/image.png)](https://postimg.cc/njZtsy3L)
[![image.png](https://i.postimg.cc/bNhYm7v2/image.png)](https://postimg.cc/SjgpstCy)
[![image.png](https://i.postimg.cc/LXPRTDKT/image.png)](https://postimg.cc/Q9jRNpM9)
### 📊 Estructura de Firebase
Firebase Authentication (opcional): Autenticar a los usuarios para luego asociar los alquileres a un usuario específico.
[![image.png](https://i.postimg.cc/nLzhvqhR/image.png)](https://postimg.cc/sMkzzQS7)
[![Imagen-de-Whats-App-2024-11-26-a-las-21-33-40-45bb8a1f.jpg](https://i.postimg.cc/P5dx5gsk/Imagen-de-Whats-App-2024-11-26-a-las-21-33-40-45bb8a1f.jpg)](https://postimg.cc/sv0yn0gn)

