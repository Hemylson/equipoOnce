<div align="center">

# 🍾 Pico Botella

### El clásico juego de la botella, ahora en tu bolsillo

*Forma el círculo, gira la botella… y que comiencen los retos.*

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)
![Hilt](https://img.shields.io/badge/DaggerHilt-2196F3?style=for-the-badge&logo=android&logoColor=white)

![Arquitectura](https://img.shields.io/badge/Arquitectura-MVVM%20%2B%20Repository-orange?style=flat-square)
![Tests](https://img.shields.io/badge/Tests-23%20passing-success?style=flat-square)
![Cobertura ViewModels](https://img.shields.io/badge/ViewModels-%E2%89%A550%25%20test-brightgreen?style=flat-square)
![Estado](https://img.shields.io/badge/Sprint%201-Completo-blueviolet?style=flat-square)

</div>

---

## 📖 ¿De qué trata?

**Pico Botella** es una aplicación **Android nativa** que digitaliza el juego de la botella:
los jugadores forman un círculo, colocan el dispositivo en el centro y giran la botella.
Al jugador que señale, la app le lanza un **reto aleatorio** que debe cumplir… o queda fuera.
Gana el último que siga en pie. 🏆

> Proyecto académico — **Desarrollo de Aplicaciones para Dispositivos Móviles**, Universidad del Valle.

---

## ✨ Características

| | Funcionalidad |
|--|--------------|
| 🔐 | **Autenticación** con Firebase (login, registro y sesión persistente) |
| 🎞️ | **Splash** animado de bienvenida |
| 🍾 | **Giro de botella** aleatorio con sonido y cuenta regresiva |
| 🎯 | **Retos personalizables** (crear, editar y eliminar) guardados en la nube |
| 🐉 | **Reto sorpresa** con un Pokémon aleatorio desde una API pública |
| 🎵 | **Audio de fondo** con interruptor on/off, coordinado entre pantallas |
| ⭐ | **Calificar** y **compartir** la app |
| 📜 | **Instrucciones** del juego con animación de triunfo |

---

## 🛠️ Tecnologías

```
Kotlin · MVVM + Repository Pattern · DaggerHilt
Firebase Authentication · Cloud Firestore
Retrofit · Coroutines · Navigation Component
RecyclerView · Fragments · JUnit + MockK
```

---

## 🧱 Arquitectura

```
┌──────────────┐   observa StateFlow   ┌──────────────┐   llama   ┌──────────────┐
│    Vista     │ ───────────────────►  │  ViewModel   │ ────────► │  Repository  │
│ Activity /   │ ◄───────────────────  │  (lógica +   │           │ (Firebase /  │
│ Fragment/XML │   repinta el estado   │   estado)    │           │     API)     │
└──────────────┘                       └──────────────┘           └──────────────┘
```

Estructura de paquetes:

```
com.example.equipoonce
├── view/         # Activities, Fragments, ViewModels, diálogos
├── repository/   # AuthRepository, RetoRepository, PokemonRepository
├── model/        # Modelo de dominio (Reto)
├── webservice/   # API de Pokémon (Retrofit) + DTOs
├── di/           # Módulos de DaggerHilt (Firebase, Network)
└── utils/        # Audio, constantes, utilidades
```

---

## 🚀 Cómo ejecutar

```bash
# 1. Clonar
git clone https://github.com/Hemylson/equipoOnce.git

# 2. Abrir en Android Studio y sincronizar Gradle
# 3. Ejecutar en un emulador o dispositivo (Run ▶)
```

Correr las pruebas unitarias:

```bash
./gradlew testDebugUnitTest
```

---

## ✅ Calidad

- **23 pruebas unitarias** con JUnit + MockK.
- **Los 6 ViewModels** superan el 50 % de métodos con prueba.
- Arquitectura **MVVM + Repository** con inyección de dependencias (**DaggerHilt**).

---

## 👥 Equipo Once

| Integrante | Rol |
|------------|-----|
| Juan Manuel Montenegro Cardona | Setup · Hilt · Firebase |
| Emilson Cossio Zambrano | Autenticación · Splash |
| Juan David Charry Medina | Home · Instrucciones · Integración |
| Alan Sebastián Leal Valencia | Firestore · Retos |
| Fabián Andrés Camayo Pesas | Reto aleatorio · Compartir |

---

<div align="center">

**Universidad del Valle** · Escuela de Ingeniería de Sistemas y Computación
Miniproyecto 2 · Sprint 1 · Junio 2026

*Solo los valientes lo juegan 🔥*

</div>
