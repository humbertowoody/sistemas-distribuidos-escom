/**
 * Constantes.ts
 * En este archivo guardaremos algunas constantesque usaremos en la aplicación
 * Humberto Alejandro Ortega Alcocer
 * 2016630495
 */

// URL de la API de Artículos.
export const URL_FUNCIONES_AZURE: string = process.env.URL_FUNCIONES_AZURE ||  "http://localhost:7071/api"; //"https://t9-af-2016630495.azurewebsites.net/api" ||

// Código y Client ID de la API de Artículos.
export const CODIGO_FUNCIONES_AZURE: string = process.env.CODIGO_FUNCIONES_AZURE || "ST8thJkEqLWEBsqW0J2LHk68qAlqZeb73fvNcfDUAwuUAzFusFiteg==";
export const CLIENT_ID_FUNCIONES_AZURE: string = process.env.CLIENT_ID_FUNCIONES_AZURE || "default";