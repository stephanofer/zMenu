# Estrategia de trabajo para el fork de zMenu

Este documento explica cómo mantener nuestro fork de zMenu sin perder las modificaciones internas del equipo y sin bloquear futuras actualizaciones del proyecto oficial.

La idea principal es simple:

```text
origin   = nuestro fork: git@github.com:stephanofer/zMenu.git
upstream = zMenu oficial: git@github.com:Maxlego08/zMenu.git
```

Trabajamos en ramas separadas, revisamos con Pull Requests y mantenemos `main` como la versión estable de nuestro fork.

---

## Mapa mental rápido

| Nombre | Qué representa | Para qué se usa |
|---|---|---|
| `origin` | Nuestro fork | Subir ramas, PRs y cambios propios |
| `upstream` | Repositorio oficial de zMenu | Traer updates oficiales |
| `main` | Versión estable de nuestro fork | Base limpia para producción y nuevas ramas |
| `feat/*` | Funcionalidades internas | Cambios nuevos hechos por nuestro equipo |
| `sync-upstream-*` | Sincronización con zMenu oficial | Actualizar nuestro fork con cambios oficiales |

---

## Regla de oro

No trabajar directo en `main`.

Usar ramas para separar intenciones:

- una rama para una feature interna;
- una rama para traer updates del zMenu oficial;
- una rama para hotfixes si hace falta.

Esto evita mezclar cambios y hace que los conflictos sean más fáciles de revisar.

---

## Verificar remotes

Antes de trabajar, revisar que los remotes estén bien:

```bash
git remote -v
```

Debe verse parecido a esto:

```text
origin   git@github.com:stephanofer/zMenu.git (fetch)
origin   git@github.com:stephanofer/zMenu.git (push)
upstream git@github.com:Maxlego08/zMenu.git (fetch)
upstream git@github.com:Maxlego08/zMenu.git (push)
```

Si `origin` apunta al repo oficial, está mal para nuestro flujo. `origin` debe ser nuestro fork.

---

## Flujo 1: agregar una funcionalidad interna al fork

Usar este flujo cuando el equipo quiere agregar algo propio a zMenu, por ejemplo:

- localización con `NetworkPlayerSettings`;
- integración con plugins internos;
- cambios de UX;
- ajustes para la network.

### 1. Actualizar `main` local

```bash
git checkout main
git pull origin main
```

Por qué: antes de crear una rama nueva, conviene empezar desde la última versión estable de nuestro fork.

### 2. Crear una rama de feature

```bash
git checkout -b feat/network-player-localization
```

Usar nombres claros:

```text
feat/<descripcion-corta>
fix/<descripcion-corta>
docs/<descripcion-corta>
refactor/<descripcion-corta>
```

Ejemplos:

```bash
git checkout -b feat/custom-rewards
git checkout -b fix/menu-title-refresh
git checkout -b docs/fork-workflow
```

### 3. Trabajar y revisar cambios

```bash
git status --short
git diff
```

Por qué:

- `git status --short` muestra qué archivos cambiaron.
- `git diff` muestra exactamente qué cambió.

### 4. Correr pruebas

Para este proyecto, como mínimo:

```bash
.\gradlew.bat :API:test :Common:test :compileJava :test
```

Si tocaste hooks de Paper o Bedrock:

```bash
.\gradlew.bat :Hooks:Paper:compileJava :Hooks:Bedrock:compileJava
```

### 5. Stagear solo lo que corresponde

```bash
git add API Common Hooks src build.gradle.kts docs
```

No usar `git add .` si hay carpetas o archivos que no deben entrar al commit.

Ejemplo real: `zAuctionHouse/` estaba untracked y no debía entrar al PR de zMenu.

### 6. Crear commit

```bash
git commit -m "feat(localization): add NetworkPlayerSettings language support"
```

Usar mensajes convencionales:

```text
feat(scope): add something
fix(scope): correct something
docs(scope): document something
refactor(scope): improve structure without behavior change
```

### 7. Subir la rama

```bash
git push -u origin feat/network-player-localization
```

Por qué: sube la rama a nuestro fork y deja configurado el tracking. Después de eso, futuros pushes pueden ser solo:

```bash
git push
```

### 8. Crear Pull Request hacia `main`

El PR debe ir:

```text
desde: feat/network-player-localization
hacia: main
repo: stephanofer/zMenu
```

El PR permite revisar:

- archivos cambiados;
- pruebas ejecutadas;
- posibles errores;
- si se coló algo que no debía;
- comentarios del equipo.

Cuando el PR se mergea, `main` recibe la funcionalidad.

### 9. Actualizar `main` local después del merge

Después de mergear en GitHub:

```bash
git checkout main
git pull origin main
```

Resultado:

```text
origin/main = main actualizado en GitHub
main local  = main actualizado en tu máquina
```

---

## Flujo 2: traer cambios nuevos del zMenu oficial

Usar este flujo cuando el equipo oficial de zMenu sacó commits nuevos y queremos traerlos a nuestro fork.

### 1. Actualizar nuestro `main`

```bash
git checkout main
git pull origin main
```

Por qué: primero aseguramos que nuestro `main` local está igual que el `main` del fork.

### 2. Crear una rama de sincronización

```bash
git checkout -b sync-upstream-2026-06
```

Usar nombres con fecha o versión:

```bash
git checkout -b sync-upstream-2026-06
git checkout -b sync-upstream-1.1.2.0
```

Por qué: traer cambios oficiales puede generar conflictos. Mejor resolverlos en una rama aislada.

### 3. Traer información del repo oficial

```bash
git fetch upstream
```

Esto no modifica tus archivos. Solo descarga la información nueva del zMenu oficial.

### 4. Mezclar `upstream/main` en la rama actual

```bash
git merge upstream/main
```

Esto intenta combinar:

```text
nuestro main + cambios nuevos del zMenu oficial
```

### 5. Si no hay conflictos

Correr pruebas:

```bash
.\gradlew.bat :API:test :Common:test :compileJava :test
```

Subir la rama:

```bash
git push -u origin sync-upstream-2026-06
```

Crear PR hacia `main`.

### 6. Si hay conflictos

Git puede mostrar algo así:

```text
CONFLICT (content): Merge conflict in Common/src/main/java/fr/maxlego08/menu/ZMenuItemStack.java
```

Significa:

> zMenu oficial cambió el mismo archivo que nosotros modificamos.

Hay que abrir el archivo y combinar manualmente:

- lo nuevo del zMenu oficial;
- nuestras modificaciones internas;
- cualquier ajuste necesario para que ambas cosas convivan.

Después de resolver:

```bash
git add Common/src/main/java/fr/maxlego08/menu/ZMenuItemStack.java
git commit
```

Luego correr pruebas y abrir PR.

---

## Qué pasa al final de una sincronización upstream

Antes:

```text
upstream/main: zMenu oficial con commits nuevos
origin/main:   nuestro fork estable, quizá más viejo
```

Después del PR de sync:

```text
origin/main: zMenu oficial actualizado + nuestras modificaciones internas
```

Ese es el objetivo.

---

## Qué NO hacer

No hacer esto:

```bash
git checkout main
git merge upstream/main
git push origin main
```

Por qué: mete cambios oficiales directo en `main` sin revisión.

Tampoco hacer esto si hay archivos ajenos:

```bash
git add .
```

Por qué: puede meter carpetas o archivos que no tienen relación con el cambio actual.

---

## Checklist para features internas

- [ ] Estoy en una rama `feat/*`, `fix/*`, `docs/*` o similar.
- [ ] `main` estaba actualizado antes de crear la rama.
- [ ] Revisé `git status --short`.
- [ ] No incluí archivos ajenos al cambio.
- [ ] Corrí pruebas relevantes.
- [ ] Hice commit convencional.
- [ ] Subí la rama a `origin`.
- [ ] Abrí PR hacia `main`.

---

## Checklist para updates del zMenu oficial

- [ ] Actualicé `main` local con `git pull origin main`.
- [ ] Creé una rama `sync-upstream-*`.
- [ ] Ejecuté `git fetch upstream`.
- [ ] Ejecuté `git merge upstream/main`.
- [ ] Resolví conflictos si aparecieron.
- [ ] Corrí pruebas relevantes.
- [ ] Abrí PR hacia `main`.

---

## Comandos más usados

```bash
# Ver remotes
git remote -v

# Ver rama actual
git branch --show-current

# Ver estado corto
git status --short

# Actualizar main local desde nuestro fork
git checkout main
git pull origin main

# Crear rama de feature
git checkout -b feat/example-feature

# Crear rama de sync con zMenu oficial
git checkout -b sync-upstream-2026-06

# Descargar información del oficial
git fetch upstream

# Mezclar cambios oficiales en la rama actual
git merge upstream/main

# Stagear archivos concretos
git add API Common Hooks src build.gradle.kts docs

# Commit convencional
git commit -m "feat(scope): describe the change"

# Subir rama al fork
git push -u origin nombre-de-la-rama
```

---

## Resumen corto

```text
Feature interna:
main actualizado -> feat/* -> commit -> push -> PR -> merge -> main actualizado

Update oficial:
main actualizado -> sync-upstream-* -> fetch upstream -> merge upstream/main -> tests -> PR -> merge -> main actualizado
```

Mantener este flujo evita perder nuestras modificaciones internas y permite seguir recibiendo mejoras del zMenu oficial con control.
