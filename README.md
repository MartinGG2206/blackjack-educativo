# Blackjack Educativo

Aplicacion educativa web en Java 17 para analizar blackjack desde probabilidad, estrategia basica teorica y seguimiento de cartas usadas. Ahora corre como servicio web, se puede desplegar en Render y la interfaz esta optimizada para smartphones, tablets y escritorio.

## Que incluye

- registro exacto de cartas por valor y palo,
- carta visible del dealer y cartas adicionales del dealer,
- cartas abiertas de otros jugadores,
- cartas ya vistas del mismo zapato,
- calculo de probabilidad de pasarse y de mejorar,
- recomendacion educativa basada en estrategia basica,
- historial persistente de rondas en CSV,
- UI responsive con botones grandes y flujo comodo para tactil.

## Stack

- Java 17
- Spring Boot 3
- Thymeleaf
- Maven
- Docker para despliegue en Render

## Ejecutar localmente

### Opcion rapida

En Windows:

```powershell
cmd /c ejecutar.bat
```

o:

```powershell
.\ejecutar.ps1
```

El script compila, levanta el servidor y abre:

```text
http://localhost:8080
```

### Opcion Maven

```powershell
.\mvnw.cmd spring-boot:run
```

### Empaquetar

```powershell
.\mvnw.cmd clean package
java -jar target\blackjack-educativo-1.0.0.jar
```

## Historial

Por defecto el historial se guarda en:

```text
data/historial_resultados.csv
```

Puedes cambiar la ruta con esta variable de entorno:

```text
BLACKJACK_HISTORIAL_PATH
```

Ejemplo:

```powershell
$env:BLACKJACK_HISTORIAL_PATH="C:\datos\historial_resultados.csv"
.\mvnw.cmd spring-boot:run
```

## Despliegue en Render

El repositorio ya incluye:

- `Dockerfile`
- `.dockerignore`
- `render.yaml`

### Opcion 1. Blueprint con `render.yaml`

1. Sube el proyecto a GitHub.
2. En Render, crea un nuevo Blueprint.
3. Conecta el repositorio.
4. Render detectara `render.yaml` y levantara un `Web Service` Docker.
5. Cuando termine el deploy, abre la URL `.onrender.com`.

### Opcion 2. Web Service manual

1. New `Web Service`
2. Connect repository
3. Environment: `Docker`
4. Render usara el `Dockerfile` del proyecto
5. Health check path: `/health`

### Persistencia en Render

Importante: si dejas la ruta por defecto, el historial quedara dentro del filesystem del contenedor. En Render eso no es adecuado para persistencia real entre redeploys o reinicios.

Si quieres conservar el historial:

1. crea un Persistent Disk en Render,
2. montalo por ejemplo en `/var/data`,
3. configura `BLACKJACK_HISTORIAL_PATH=/var/data/historial_resultados.csv`.

Si no montas disco, la aplicacion funciona igual, pero el historial puede perderse al redeployar.

## Pruebas

```powershell
.\mvnw.cmd test
```

## Estructura relevante

```text
src/
  main/
    java/com/example/blackjackeducativo/
      config/
      modelo/
      servicio/
      web/
    resources/
      static/css/app.css
      templates/index.html
      application.properties
Dockerfile
render.yaml
```

## Notas de uso

- `Nueva partida` mueve la ronda actual a cartas vistas y limpia la mesa.
- `Reiniciar zapato` borra todo y reconstruye el zapato con el numero de mazos elegido.
- `Marcar gane`, `Marcar perdi` y `Marcar empate` guardan la ronda y actualizan el historial.
- La interfaz recalcula el analisis despues de cada accion.
