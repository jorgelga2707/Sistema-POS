# 🧾 Sistema de Punto de Venta (POS) – Java Swing + MySQL

Sistema de Punto de Venta (POS) desarrollado para pequeños negocios, enfocado en la **gestión de inventario**, **ventas**, **usuarios** y **reportes**, utilizando **Java Swing**, **MySQL** y una **arquitectura modular MVC**. El IDE que se usa en el proyectos en Apache NetBeans IDE 23.

---

## 📌 Información del Proyecto

- **Proyecto:** Sistema POS para Pequeños Negocios  
- **Lenguaje:** Java (Swing)  
- **Base de Datos:** MySQL  
- **Arquitectura:** MVC (Model – View – Controller)  
- **Tipo:** Aplicación de escritorio  
- **Curso:** Lenguaje de Programación  
- **Institución:** Instituto de Educación Superior Tecnológico Público  

### 👨‍💻 Autores
- **Jorge García Alegre**  
- **Jezer Arellano Cárdenas**

---

## 🎯 Objetivo del Sistema

Desarrollar un sistema POS **robusto, intuitivo y eficiente** que permita a pequeños negocios:

- Controlar su inventario
- Registrar ventas
- Generar tickets en archivos `.txt`
- Gestionar usuarios con roles
- Obtener reportes clave para la toma de decisiones

---

## 🧩 Módulos del Sistema

### 📦 Módulo de Productos (Inventario)
Permite administrar los productos del negocio.

**Funciones:**
- Registrar productos
- Editar productos
- Eliminar productos
- Buscar por código o nombre
- Listar todos los productos
- Control automático de stock
- Validación de códigos duplicados

**Datos del Producto:**
- Código / ID
- Nombre
- Descripción
- Categoría
- Precio
- Stock

---

### 🛒 Módulo de Ventas
Gestiona el proceso completo de una venta.

**Funciones:**
- Selección de productos
- Cálculo automático de:
  - Subtotal
  - IGV (18%)
  - Total
- Registro de ventas en MySQL
- Actualización automática del stock
- Generación de ticket de venta en archivo `.txt`

**Cada venta almacena:**
- ID de venta
- Fecha y hora
- Productos vendidos
- Cantidades
- Totales

---

### 👤 Módulo de Usuarios
Controla el acceso al sistema.

**Funciones:**
- Login seguro
- Registro de usuarios
- Gestión de roles:
  - Administrador
  - Vendedor

---

### 📊 Módulo de Reportes
Brinda información clave para la toma de decisiones.

**Reportes disponibles:**
- 📉 Reporte de stock bajo (productos con bajo inventario)
- 📅 Reporte de ventas por día

**Características:**
- Visualización en tablas (Swing)
- Exportación a archivos `.txt`
- Consultas directas desde MySQL

---

## 🖥️ Interfaz Gráfica (UI)

La interfaz fue desarrollada con **Java Swing**, enfocándose en:

- Diseño limpio e intuitivo
- Uso de JTable para reportes
- Formularios claros
- Mensajes de validación al usuario

**Pantallas principales:**
- Login
- Menú Principal
- Productos
- Ventas
- Usuarios
- Reportes

---

## 🗂️ Estructura del Proyecto

```bash
src/
│
├── Database/
│ └── Conexion.java
│
├── dao/
│ ├── ProductoDAO.java
│ ├── VentaDAO.java
│ ├── UsuarioDAO.java
│ └── CorrelativoDAO.java
│
├── model/
│ ├── Producto.java
│ ├── Venta.java
│ ├── Usuario.java
│ └── DetalleVenta.java
│
├── Controllers/
│ ├── ProductoService.java
│ ├── SesionService.java
│ ├── UsuarioService.java
│ └── VentaService.java
│
├── Main/
│ └── Main.java
│
├── UI/
│ ├── FormMenu.java
│ ├── FrmLogin.java
│ ├── GestionProductos.java
│ ├── GestionUsuarios.java
│ ├── ListadoProductos.java
│ ├── MostrarTicket.java
│ ├── RegistrarU.java
│ ├── RegistroVentas.java
│ └── ReporteGeneral.java

```
---

## 🛠️ Tecnologías Utilizadas

- ☕ Java SE
- 🪟 Java Swing
- 🐬 MySQL
- 🔗 JDBC
- 🧱 Arquitectura MVC
- 📄 Archivos TXT para tickets y reportes

---

## 🚀 Ejecución del Proyecto

1. Clonar el repositorio
2. Importar el proyecto en **NetBeans**
3. Crear la base de datos MySQL (`posdb`)
4. Configurar la conexión en `Conexion.java`
5. Ejecutar el proyecto desde el `main`

---

## 📈 Conclusiones

- El sistema optimiza los procesos de venta
- Reduce errores manuales
- Facilita el control de inventario
- Es escalable para futuras mejoras
- Cumple con los requerimientos académicos y funcionales

---

## 🔮 Mejoras Futuras

- Exportación a PDF
- Gráficos estadísticos
- Control de permisos avanzado
- Soporte multi-sucursal
- Interfaz moderna (JavaFX)

---

## 📄 Licencia

Proyecto académico desarrollado con fines educativos.

---

✨ **Gracias por revisar nuestro Sistema POS** ✨  
Desarrollado con dedicación por **Jorge García Alegre & Jezer Arellano Cárdenas**


