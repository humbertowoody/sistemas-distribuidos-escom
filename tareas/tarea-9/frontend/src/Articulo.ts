/**
 * Articulo.ts
 * Este artículo contiene la definición de la interfaz para nuestros Artículos.
 * Humberto Alejandro Ortega Alcocer
 */

// Artículo
interface Articulo {
  id: number;
  nombre: string;
  descripcion?: string;
  precio: number;
  cantidad: number;
  fotografia?: string;
}

export default Articulo;
