# Patrones de Diseño — Parcial

Repositorio académico dedicado a la implementación y análisis práctico de **patrones de diseño de software**, desarrollado como parte del curso de Patrones de Diseño.

El objetivo principal de este repositorio es aplicar los conceptos de **Programación Orientada a Objetos**, principios de diseño y patrones **GoF (Gang of Four)** mediante diferentes ejercicios y problemas prácticos.

---

## Descripción

Los patrones de diseño son soluciones reutilizables a problemas comunes que aparecen durante el diseño y desarrollo de software.

A través de este repositorio se presentan diferentes implementaciones de patrones de diseño, utilizando ejemplos prácticos para comprender:

* El problema que resuelve cada patrón.
* La estructura de sus componentes.
* Las relaciones entre las clases participantes.
* La forma correcta de aplicar el patrón.
* Las ventajas y limitaciones de cada solución.
* La diferencia entre una implementación acoplada y una basada en abstracciones.

El repositorio corresponde principalmente al trabajo realizado durante el **parcial del curso de Patrones de Diseño**.

---

# Patrones implementados

Actualmente el repositorio contiene implementaciones correspondientes a los siguientes patrones:

| Patrón        | Categoría   | Descripción                                                                                                             |
| ------------- | ----------- | ----------------------------------------------------------------------------------------------------------------------- |
| **Adapter**   | Estructural | Permite que clases con interfaces incompatibles puedan colaborar mediante un adaptador.                                 |
| **Bridge**    | Estructural | Separa una abstracción de su implementación para permitir que ambas evolucionen independientemente.                     |
| **Flyweight** | Estructural | Permite compartir objetos para reducir el consumo de memoria cuando existen grandes cantidades de instancias similares. |

---

# Estructura del proyecto

```text
Parcial-Patrones/
│
├── Parcial Patrones/
│   │
│   ├── Flyweight PARCIAL/
│   │
│   ├── adapter PARCIAL/
│   │   └── patron-adapter-empleados/
│   │
│   └── bridge PARCIAL/
│       └── PatronBridgeFactory/
│
├── .gitattributes
└── README.md
```

---

# Implementaciones

## Adapter

**Categoría:** Patrón estructural

El patrón **Adapter** permite que dos interfaces incompatibles puedan trabajar conjuntamente.

En esta implementación se utiliza el ejemplo de **empleados**, donde el adaptador actúa como intermediario entre diferentes interfaces, permitiendo que una implementación existente pueda ser utilizada sin modificar directamente su código.

### Problema

Un componente existente posee una interfaz diferente a la esperada por el código cliente.

Modificar directamente el componente original podría generar dependencias innecesarias o afectar código que ya funciona.

### Solución

Se introduce un **Adapter** que transforma la interfaz existente en la interfaz esperada por el cliente.

```text
┌──────────────┐
│    Cliente   │
└──────┬───────┘
       │
       ▼
┌──────────────┐
│ Target       │
│ Interface    │
└──────┬───────┘
       │
       ▼
┌──────────────┐
│   Adapter    │
└──────┬───────┘
       │
       ▼
┌──────────────┐
│ Adaptee      │
│ existente    │
└──────────────┘
```

### Ventajas

* Permite reutilizar código existente.
* Evita modificar clases que ya están implementadas.
* Reduce el acoplamiento entre componentes.
* Facilita la integración de sistemas con interfaces diferentes.

---

## Bridge

**Categoría:** Patrón estructural

El patrón **Bridge** permite separar una abstracción de su implementación para que ambas puedan evolucionar de manera independiente.

En esta implementación se trabaja la relación entre una abstracción y diferentes implementaciones, evitando crear una jerarquía de clases excesivamente rígida.

### Problema

Cuando una abstracción y sus implementaciones están fuertemente relacionadas mediante herencia, agregar nuevas variantes puede provocar una explosión de clases.

### Solución

Bridge divide la estructura en dos jerarquías independientes:

```text
        Abstracción
             │
             ▼
      ┌──────────────┐
      │ Abstraction  │
      └──────┬───────┘
             │
             ▼
      ┌──────────────┐
      │Implementor   │
      └──────┬───────┘
             │
       ┌─────┴─────┐
       ▼           ▼
 ImplementationA  ImplementationB
```

### Ventajas

* Separa abstracción e implementación.
* Reduce el acoplamiento.
* Facilita agregar nuevas implementaciones.
* Evita jerarquías de herencia innecesariamente grandes.

---

## Flyweight

**Categoría:** Patrón estructural

El patrón **Flyweight** permite compartir objetos cuando existen grandes cantidades de instancias que contienen información repetida.

La idea principal consiste en separar el **estado intrínseco**, que puede ser compartido, del **estado extrínseco**, que depende del contexto particular de cada objeto.

### Problema

Crear una gran cantidad de objetos similares puede producir un consumo innecesario de memoria.

### Solución

Flyweight utiliza objetos compartidos que pueden ser reutilizados por múltiples contextos.

```text
              ┌───────────────┐
              │    Factory    │
              └───────┬───────┘
                      │
              ┌───────▼───────┐
              │ Flyweight Pool│
              └───────┬───────┘
                      │
          ┌───────────┼───────────┐
          ▼           ▼           ▼
       Cliente 1   Cliente 2   Cliente 3
          │           │           │
          └───────────┼───────────┘
                      ▼
               Objeto compartido
```

### Ventajas

* Reduce el consumo de memoria.
* Permite reutilizar objetos.
* Evita crear múltiples instancias equivalentes.
* Resulta útil cuando existe una gran cantidad de objetos similares.

---

# Conceptos trabajados

Durante la implementación de estos patrones se trabajaron diferentes conceptos fundamentales de diseño de software:

### Programación Orientada a Objetos

* Encapsulamiento.
* Herencia.
* Polimorfismo.
* Abstracción.
* Interfaces.
* Composición.

### Principios de diseño

* Bajo acoplamiento.
* Alta cohesión.
* Separación de responsabilidades.
* Programación orientada a abstracciones.
* Reutilización de componentes.

### Patrones GoF

Los ejercicios se basan en los patrones definidos por el catálogo **Gang of Four (GoF)**, agrupados en:

* Patrones creacionales.
* Patrones estructurales.
* Patrones de comportamiento.

Este repositorio se concentra en implementaciones correspondientes principalmente a la categoría de **patrones estructurales**.

---

# Objetivo académico

El propósito de estas implementaciones no es únicamente reproducir la estructura de un patrón, sino comprender **cuándo y por qué utilizarlo**.

Cada ejercicio busca demostrar cómo una determinada solución de diseño puede:

* Resolver un problema específico.
* Reducir dependencias innecesarias.
* Facilitar la reutilización.
* Mejorar la extensibilidad.
* Organizar las responsabilidades de las clases.
* Permitir que diferentes componentes trabajen mediante abstracciones.

---

# Tecnologías

* **Java**
* **Programación Orientada a Objetos**
* **Git**
* **GitHub**
* **IDE de desarrollo Java**

---

# Ejecución

Clonar el repositorio:

```bash
git clone https://github.com/Nauj93x/Parcial-Patrones.git
```

Ingresar al proyecto:

```bash
cd Parcial-Patrones
```

Cada patrón se encuentra organizado en su respectiva carpeta dentro de:

```text
Parcial Patrones/
```

Las implementaciones pueden ejecutarse de manera independiente según la estructura de cada ejercicio.

---

# Relación con otros proyectos

Este repositorio forma parte del trabajo académico realizado durante el estudio de **Patrones de Diseño de Software**.

Los conceptos y patrones trabajados aquí fueron posteriormente aplicados en proyectos de mayor escala, como **Unlock**, donde diferentes patrones de diseño fueron integrados dentro de una aplicación funcional.

---

# Estado del proyecto

**Finalizado — Proyecto académico**

Repositorio utilizado para el desarrollo, práctica e implementación de patrones de diseño durante el curso.

---

# Autor

**Juan Pablo**

Proyecto desarrollado con fines académicos.

---

# Licencia y derechos de autor

**Copyright © 2026 Juan Pablo. Todos los derechos reservados.**

Este repositorio corresponde a material académico desarrollado individualmente.

La publicación del repositorio no implica la concesión de una licencia para copiar, modificar, distribuir, sublicenciar, publicar o utilizar comercialmente el código, total o parcialmente.

Cualquier uso, reproducción, modificación o distribución del contenido requiere autorización previa y expresa del autor.

La disponibilidad pública del repositorio no constituye autorización para presentar el código o sus implementaciones como trabajo propio.
