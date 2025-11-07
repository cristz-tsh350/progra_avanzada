Proyecto CGE Programación Avanzada S2
Integrantes: - Martín Fuentes
             - Cristóbal Herrera
             - Pablo Ortega


Descripción General

Este proyecto consiste en el desarrollo de un sistema de gestión de clientes y consumo eléctrico utilizando Kotlin Multiplatform. 
El programa permite registrar clientes, asociar medidores eléctricos, guardar lecturas de consumo mensual y generar boletas automáticas 
con el cálculo según el tipo de tarifa.

Objetivo

El objetivo principal es aplicar los principios de la programación orientada a objetos (POO) en un contexto real. 
El sistema demuestra el uso de herencia, polimorfismo, encapsulamiento y abstracción.

Funcionamiento del Programa

El usuario puede registrar nuevos clientes, agregar sus direcciones y asociarles medidores monofásicos o trifásicos. 
Luego se ingresan las lecturas de consumo mensual, y el sistema calcula el total a pagar aplicando una tarifa residencial o comercial. 
Finalmente, se genera una boleta con el detalle de consumo, subtotal, cargos, IVA y total.

El sistema también permite guardar la información de los clientes y medidores en un archivo JSON, lo que asegura la persistencia 
de los datos entre ejecuciones. Además, se incluye la opción de generar boletas en formato PDF en la versión de escritorio.

Estructura del Proyecto

El código está organizado en diferentes paquetes según su función. 
En el módulo domain se encuentran las clases principales como Cliente, Medidor, Tarifa y Boleta. 
En el módulo service se encuentra la lógica de facturación, en persistence se maneja el guardado y carga de datos, y en pdf se genera el 
documento de boleta en formato PDF.

Ejemplo de Uso

Un cliente puede registrarse con su RUT, nombre y dirección. 
Luego se le asigna un medidor y se registran las lecturas de consumo de un mes determinado. 
Al seleccionar la tarifa, el sistema genera automáticamente una boleta que puede guardarse como PDF.

Conclusión

El proyecto demuestra el uso correcto de la programación orientada a objetos y su aplicación práctica en un sistema de facturación eléctrica. 
La implementación cumple con los requerimientos del enunciado, mostrando un código modular, reutilizable y bien estructurado, 
capaz de gestionar datos y generar reportes de manera automática.

El proyecto es una aplicación hecha en Kotlin Multiplatform, lo que significa que puede funcionar
tanto en computadora como en la web usando el mismo código base. 
Usa Compose Multiplatform para crear la interfaz gráfica y está bien organizado en carpetas que separan el código común del específico de cada plataforma.

En cuanto a los criterios de la rúbrica de programación avanzada, el proyecto cumple con los puntos técnicos más importantes:
tiene buena organización, cuenta con el cambio de interfaz a modo oscuro, también genera las boletas en formato PDF y por último 
calcula el valor de las boletas