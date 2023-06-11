/**
 * ObtenerArticulos.ts
 * En este archiov se encuentra la funcion que se encarga de obtener los articulos de la base de datos.
 * Humberto Alejandro Ortega Alcocer
 */
import { AzureFunction, Context, HttpRequest } from "@azure/functions";
import { QueryTypes, Sequelize } from "sequelize";

// Interface para representar un artículo.
interface Articulo {
  id: number;
  nombre: string;
  descripcion?: string;
  precio: number;
  cantidad: number;
  fotografia?: string;
}

const httpTrigger: AzureFunction = async function (
  context: Context,
  req: HttpRequest
): Promise<void> {
  context.log("Se ha recibido una petición HTTP para Obtener Artículos");

  // Se revisa si se recibió un query parameter "nombre"
  const nombre: string = req.query.nombre || (req.body && req.body.nombre);

  if (nombre) {
    context.log("Se recibió el query parameter nombre: " + nombre);
  }

  try {
    // Abrimos la conexión a la base de datos.
    const sequelize = new Sequelize("mysql://humberto:Tacos12345@t9-bd-2016630495.mysql.database.azure.com:3306/tarea9", {
      dialect: "mysql",
      dialectOptions: {
        multipleStatements: true,
        ssl: {
          require: true,
          rejectUnauthorized: false,
        },
      },
    });

    // Variable para el resultado.
    let articulos: Articulo[] = [];

    // Si recibimos el nombre, usamos un query por nombre.
    if (nombre) {
      articulos = await sequelize.query<Articulo>(
        "SELECT id, nombre, descripcion, precio, cantidad, CONVERT(fotografia using utf8) as fotografia FROM articulos WHERE nombre LIKE :nombre OR descripcion LIKE :nombre",
        {
          type: QueryTypes.SELECT,
          // Mapeamos los resultados a la interface Articulo.
          mapToModel: true,
          // Reemplazamos el query parameter :nombre por el valor recibido.
          replacements: { nombre: `%${nombre}%` },
        }
      );
    } else {
      // Obtenemos los artículos de la base de datos.
      articulos = await sequelize.query<Articulo>(
        "SELECT id, nombre, descripcion, precio, cantidad, CONVERT(fotografia using utf8) as fotografia FROM articulos",
        {
          type: QueryTypes.SELECT,
          // Mapeamos los resultados a la interface Articulo.
          mapToModel: true,
        }
      );
    }

    // Cerramos la conexión
    await sequelize.close();

    // Enviamos la respuesta.
    context.res = {
      status: 200, 
      // Enviamos los artículos como respuesta.
      body: articulos,
    };
  } catch (error) {
    context.res = {
      status: 500,
      body: { mensaje: "Error al obtener los artículos.", error },
    };
  }
};

export default httpTrigger;
