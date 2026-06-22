# Integrar CraftKit Paper en zMenu

Este diseño agrega soporte para el tag MiniMessage de CraftKit Paper en todo el pipeline Paper de zMenu donde se parsean componentes Adventure. El objetivo es que `<craftkit_head:...>` funcione de forma consistente en nombres, lore, títulos de inventario, mensajes, actionbars, titles, libros y componentes Paper modernos, respetando el orden correcto: primero placeholders de zMenu/PlaceholderAPI, después MiniMessage con el resolver de CraftKit.

## Decisión Principal

Registrar `CraftKitMiniMessageTags.playerHead()` como `TagResolver` adicional de `ComponentMeta` cuando zMenu corre en modo Paper/Folia con MiniMessage activo. `craftkit-paper` se consumirá como librería embebida en zMenu, no como plugin externo.

No se debe crear un sistema paralelo para CraftKit ni parsear manualmente `<craftkit_head:...>` en cada feature. zMenu ya centraliza MiniMessage en `ComponentMeta`, y ese es el punto correcto de integración.

## Contrato Soportado

El tag soportado viene de `craftkit-paper`:

```text
<craftkit_head:texture[:hat]>
```

`texture` puede ser:

- Value base64 completo de la textura.
- Hash de `textures.minecraft.net`.
- URL completa `https://textures.minecraft.net/texture/<hash>` entre comillas si contiene `https://`.

`hat` es opcional y controla la capa externa de la cabeza. Default: `true`.

Ejemplos esperados en configs de zMenu:

```yaml
name: '<craftkit_head:%country_head_value%> VIP %player%'
lore:
  - '<gray>Region: <craftkit_head:%country_head_value%:false> %country_name%'
```

## Contexto Revisado

| Área | Evidencia | Impacto |
|---|---|---|
| CraftKit docs | `docs/CraftKit/craftkit-paper/README.md` | El contrato oficial es `CraftKitMiniMessageTags.playerHead()` y exige placeholders antes de MiniMessage. |
| MiniMessage central | `Hooks/Paper/.../ComponentMeta.java` | Todos los componentes Paper pasan por un `MiniMessage` configurable con `TagResolver`. |
| API extensible | `API/.../PaperMetaUpdater.java` | Ya existe `withTagResolver`, `buildMiniMessage` y `clearCache`. |
| Hook parecido | `Hooks/Nexo/.../NexoTagResolverLoader.java` | Nexo registra un resolver externo y reconstruye MiniMessage; CraftKit debe seguir este patrón. |
| Items | `Common/.../ZMenuItemStack.java` | Name/lore resuelven placeholders antes de `MetaUpdater`, por lo que el orden requerido se cumple. |
| Paper components | `PaperLoreComponent`, `PaperCustomNameComponent` | También usan `PaperMetaUpdater.getComponent(...)`, por lo que heredan el resolver central. |
| Build de hooks | `Hooks/build.gradle.kts` | El módulo `Hooks` expone todos los subhooks, y el root empaqueta `projects.hooks`. |
| Shadow root | `build.gradle.kts` | zMenu ya relocaliza librerías embebidas en `shadowJar`; CraftKit debe seguir ese patrón. |

## Alcance

Se debe soportar `<craftkit_head:...>` en estos lugares porque pasan por `ComponentMeta`:

- Item `name` y `lore` normales.
- Item components Paper como `custom_name` y `lore`.
- Títulos de inventario creados con `createInventory`.
- Mensajes enviados por `MetaUpdater.sendMessage`.
- Actionbar y title enviados por `MetaUpdater`.
- Libros abiertos por `MetaUpdater.openBook`.
- Logs/componentes que usen `ComponentLogger` si pasan por el mismo `ComponentMeta`.

No queda garantizado en lugares que creen su propio `MiniMessage` fuera de zMenu, por ejemplo `zAuctionHouse` tiene `PaperComponent` propio. Eso debe tratarse como otro módulo si el equipo quiere cubrirlo también.

## Cambios Propuestos

| Archivo | Cambio |
|---|---|
| `gradle/libs.versions.toml` | Agregar versión y alias `craftkit-paper = com.hera.craftkit:craftkit-paper:1.1.0`. |
| `Hooks/Paper/build.gradle.kts` | Agregar `implementation(libs.craftkit.paper)` para que el hook Paper compile y el root pueda empaquetar la librería. |
| `build.gradle.kts` | Agregar `relocate("com.hera.craftkit", "fr.maxlego08.menu.hooks.craftkit")`. |
| `Hooks/Paper/src/main/java/.../hooks/ComponentMeta.java` | Registrar el resolver CraftKit durante la construcción de `ComponentMeta`, junto con los standard tags. |
| Tests Paper | Agregar prueba de que `ComponentMeta` registra resolvers externos y reconstruye MiniMessage sin perder tags existentes. |

## Diseño Runtime

1. zMenu inicia y llama `loadMeta()`.
2. Si MiniMessage está habilitado y el runtime tiene Adventure, zMenu usa `ComponentMeta`.
3. `ComponentMeta` registra `StandardTags.defaults()` y `CraftKitMiniMessageTags.playerHead()` antes de construir su instancia `MiniMessage`.
4. Hooks externos como Nexo pueden seguir agregando resolvers después y reconstruir MiniMessage con `clearCache()` + `buildMiniMessage()`.
5. Cuando zMenu construye textos:
   - Primero resuelve placeholders internos y PlaceholderAPI.
   - Después `ComponentMeta` parsea MiniMessage con StandardTags + Nexo si existe + CraftKit.
6. El componente final puede contener `ObjectComponent` de player head.

## Decisiones Técnicas

| Decisión | Razón |
|---|---|
| Integrar en `Hooks/Paper`, no en `Common` | CraftKit Paper depende de APIs Paper/Adventure modernas; `Common` debe seguir siendo compatible con Spigot. |
| Usar `implementation` y shadow | CraftKit no es plugin; es librería consumida por zMenu y debe estar disponible dentro del jar final. |
| Relocalizar CraftKit | Evita conflictos si otro plugin también embebe otra versión de CraftKit o si zMenu actualiza su versión interna. |
| No tocar `plugin.yml` | No existe plugin CraftKit que declarar como `depend` o `softdepend`. |
| Registrar el resolver una sola vez en enable | El tag no necesita I/O, cache interno ni tareas. Mantiene el lifecycle simple. |
| Registrar CraftKit antes del primer `buildMiniMessage()` | Evita limpiar cache innecesariamente porque ningún componente fue parseado todavía. |
| No crear key de material nueva | CraftKit no produce `ItemStack`; produce un tag MiniMessage visual. Mezclarlo con `MaterialLoader` sería conceptualmente incorrecto. |

## Threading y Performance

No hay trabajo async necesario.

CraftKit, según la documentación local, no hace HTTP, I/O ni consultas a Mojang. El tag usa el value/hash/URL ya recibido y genera un componente Adventure. Por eso el parseo puede quedarse en el flujo actual de zMenu.

La cache existente de `ComponentMeta` sigue siendo válida porque CraftKit se registra antes de construir el primer `MiniMessage`. Si en el futuro se permite activar/desactivar CraftKit dinámicamente, ahí sí habría que limpiar cache y reconstruir MiniMessage como hace Nexo.

## Edge Cases

| Caso | Comportamiento Esperado |
|---|---|
| Librería CraftKit no empaquetada por error | zMenu fallaría al cargar `ComponentMeta`; por eso el build debe compilar y el jar final debe incluir la librería relocalizada. |
| MiniMessage deshabilitado | zMenu usa `ClassicMeta`; el tag no se soporta porque no hay parser MiniMessage. |
| Servidor no Paper/Folia compatible | No se registra el hook Paper. zMenu mantiene compatibilidad base. |
| Placeholder devuelve vacío | CraftKit recibirá textura vacía; no se debe inventar fallback silencioso porque ocultaría un error de configuración. |
| Placeholder devuelve URL con `https://` sin comillas | MiniMessage puede rechazar la sintaxis; se documentará que debe usarse `<craftkit_head:"https://...">`. |
| Cliente/servidor menor a `1.21.9+` | El componente puede no renderizar visualmente aunque el parseo exista. Debe documentarse como limitación de Minecraft, no bug de zMenu. |
| Nexo y CraftKit activos | Ambos resolvers deben coexistir porque `ComponentMeta` acumula resolvers y reconstruye un único `MiniMessage`. |
| Cache de items zMenu | Items sin placeholders pueden cachearse normalmente. Items con `%...%` ya desactivan cache de item stack por `needPlaceholderAPI`. |

## Validación

Checks mínimos después de implementar:

```bash
.\gradlew.bat :Hooks:Paper:compileJava :compileJava :test
```

Validación del jar:

1. Construir el shadow jar.
2. Confirmar que incluye clases relocalizadas de CraftKit.
3. Confirmar que no quedan clases públicas bajo el paquete original de CraftKit.

Pruebas manuales recomendadas en servidor Paper compatible:

1. Instalar zMenu y PlaceholderAPI.
2. Crear un menú con `name: '<craftkit_head:%country_head_value%> VIP %player%'`.
3. Confirmar que `%country_head_value%` se resuelve antes de MiniMessage.
4. Confirmar que el tag renderiza la cabeza en cliente compatible.
5. Probar `hat:false`.
6. Probar hash, base64 y URL entre comillas.
7. Probar Nexo activo junto con CraftKit para verificar coexistencia de resolvers.

## Fuera De Alcance Por Ahora

- No soportar `<craftkit_head:...>` en sistemas externos que creen su propio `MiniMessage`, como `zAuctionHouse`.
- No convertir `url:` de player heads existentes a CraftKit; esa feature ya crea `ItemStack` de skull y tiene otro objetivo.
- No agregar fallback HTTP/Mojang; CraftKit explícitamente evita resolver perfiles por red.
- No cachear dentro de CraftKit desde zMenu; la cache central de componentes ya cubre el caso normal.

## Paquete CraftKit Confirmado

La clase a usar es:

```java
com.hera.craftkit.paper.minimessage.CraftKitMiniMessageTags
```

Archivo fuente en CraftKit:

```text
craftkit-paper/src/main/java/com/hera/craftkit/paper/minimessage/CraftKitMiniMessageTags.java
```

Relocation definida:

```kotlin
relocate("com.hera.craftkit", "fr.maxlego08.menu.hooks.craftkit")
```

Esto cubre `com.hera.craftkit.paper.minimessage` y cualquier otro paquete interno bajo `com.hera.craftkit`.
