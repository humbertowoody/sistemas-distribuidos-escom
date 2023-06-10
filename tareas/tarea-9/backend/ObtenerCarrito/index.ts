/**
 * ObtenerCarrito
 * Esta función permite obtener un listado de los elementos presentes en el carrito de compra.
 */
import { AzureFunction, Context, HttpRequest } from "@azure/functions";
import { QueryTypes, Sequelize } from "sequelize";

// Interfaz para el artículo.
interface Articulo {
  id: number;
  nombre: string;
  precio: number;
  cantidad: number;
  descripcion?: string;
  fotografia?: string;
}

// Interfaz para el carrito.
interface Carrito {
  articulo_id: number;
  cantidad: number;
  articulo?: Articulo;
}

// Interface para la respuesta de la DB.
interface RespuestaDB {
  articulo_id: number;
  cantidad: number;
  id: number;
  nombre: string;
  descripcion?: string;
  precio: number;
  cantidad_disponible: number;
  fotografia?: string;
}

const httpTrigger: AzureFunction = async function (
  context: Context,
  req: HttpRequest
): Promise<void> {
  context.log("Se ha recibido una petición HTTP para Obtener Carrito");

  try {
    // Abrimos la conexión a la base de datos.
    const sequelize = new Sequelize("mysql://root:root@localhost:3306/tarea9", {
      dialect: "mysql",
    });

    // Obtenemos los datos desde la base de datos.
    const respuesta: RespuestaDB[] = await sequelize.query<RespuestaDB>(
      // "SELECT * FROM carrito",
      "SELECT a.articulo_id,a.cantidad,b.id,b.nombre,b.descripcion,b.precio,b.cantidad as cantidad_disponible,CONVERT(b.fotografia using utf8) as fotografia FROM carrito a LEFT OUTER JOIN articulos b ON a.articulo_id=b.id",
      { type: QueryTypes.SELECT }
    );

    // Creamos el carrito.
    const carrito: Carrito[] = respuesta.map((articulo: RespuestaDB) => {
      return {
        articulo_id: articulo.articulo_id,
        cantidad: articulo.cantidad,
        articulo: {
          id: articulo.id,
          nombre: articulo.nombre,
          descripcion: articulo.descripcion,
          precio: articulo.precio,
          cantidad: articulo.cantidad_disponible,
          fotografia: articulo.fotografia,
        },
      };
    });

    // Cerramos la conexion a la base de datos.
    await sequelize.close();

    // Enviamos el carrito.
    context.res = {
      status: 200,
      body: carrito,
    };
  } catch (error) {
    context.res = {
      status: 500,
      body: {
        mensaje: "Error al obtener el carrito",
        error,
      },
    };
  }
};

export default httpTrigger;
