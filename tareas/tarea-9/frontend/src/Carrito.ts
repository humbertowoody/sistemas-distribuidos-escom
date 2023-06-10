/**
 * Interfaz para el carrito de compras.
 * Humberto Alejandro Ortega Alcocer.
 * 2016630495
 */

import Articulo from "./Articulo";

// Interfaz a utilizar.
interface Carrito {
  cantidad: number;
  articulo_id: number;
  articulo?: Articulo;
}

export default Carrito;
