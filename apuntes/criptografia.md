# Criptografía 

Existen tres características fundamentales de la criptografía:

- **Confidencialidad**: que los datos no se puedan leer _en tránsito_.
- **Integridad**: detectar cuando se modifican los datos.
- **Identidad**: garantizar el origen de los datos.

## Criptografía simétrica.

- Es cuando se utiliza la misma llave tanto para cifrar como para descifrar.
- Ventajas:
  - Es muy rápida.
  - AES es un algoritmo muy fuerte que se utiliza en internet.
  - Se utiilizan llaves de 128 y 256 bits.
  - DES es otro algoritmo simétrico o 3DES (triple DES).
- Desventajas:
  - No puedes compartir la llave de manera sencilla.

## Criptografía asimétrica.

- Criptografía de llave pública.
- PKI - Public Key Infrastructure.
- Se encripta con la llave pública y se desencripta con la llave privada.
- Ventajas:
  - La llave se puede compartir.
- Desventajas:
  - Es muy _muy_ lenta.

## Certificado Digital

¿Qué es un certificado digital?

- Documento electrónico, que contiene:
  - Llave pública.
  - Nombre del propietario de la llave púlica.
  - Fechas de vigencia del certificado.
  - (...)
  - Firma digital.

Existen dos tipos de certificados:

- Certificado Autofirmado: es aquél que se firma con la clave privada que
corresponde a la clave pública contenida en el certificado.
- Certificado firmado por una CA: aquí el certificado lo firma una _autoridad
certificadora_, la cual, se hace responsable por la existencia de dicho
certificado. Existen dos usos principales para los certificados:
   - Verificación de Dominio (DNS).
   - Verificación de empresa.
- Repositorio de certificados.

> Un *certificado autofirmado* es criptográficamente equivalente a un
*certificado firmado por una CA*, pero depende más bien de quién sea que lo
está consumiendo o que lo está utilizando.

## Creación de repositorios de certificados para Java

1. Crear un keystore (repositorio de certificados) incluyendo un certificado
auto-firmado y la clave privada:
   - `keytool -genkeypair -keyalg RSA -alias certificado_servidor -keystore
keystore_servidor.jks -storepass 1234567`
2. Obtener el certificado contenido en el keystore:
   - `keytool -exportcert keystore_servidor.jks -alias certificado_servidor -rfc
-file certificado_servidor.pem`
3. Crear un keystore para el cliente (repositorio de confianza) incluyendo
el certificado auto-firmado:
   - `keytool -import -alias certificado_servidor -file
certificado_servidor.pem -keystore keystore_cliente.jks -storepass 123456`

## Glosario de Términos

- _hash_ - Función de digestión **no invertible** con la cual podemos tener la
"firma" de un documento/archivo/payload.
- _Certificado X.509_ - Es el certificado que se utiliza para poder intercambiar
información de identidad.

