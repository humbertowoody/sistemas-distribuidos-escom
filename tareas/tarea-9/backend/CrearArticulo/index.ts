/**
 * CrearArticulo.ts
 * En este archivo se encuentra la función que se ejecuta cuando se hace una petición HTTP POST a la ruta /api/CrearArticulo.
 * Esta función recibe un body con los datos del artículo a crear y los valida.
 * Si los datos son válidos, se inserta el artículo en la base de datos.
 * Si los datos no son válidos, se retorna un código 400 (Bad Request) y un mensaje de error.
 * Si ocurre un error al insertar el artículo en la base de datos, se retorna un código 500 (Internal Server Error) y un mensaje de error.
 * Si ocurre un error al conectarse a la base de datos, se retorna un código 500 (Internal Server Error) y un mensaje de error.
 * Si el artículo se inserta correctamente, se retorna un código 201 (Created) y el artículo creado.
 * Si el artículo no se inserta correctamente, se retorna un código 500 (Internal Server Error) y un mensaje de error.
 */
import { AzureFunction, Context, HttpRequest } from "@azure/functions";
import { QueryTypes, Sequelize } from "sequelize";

// Interface para el DTO de crear artículo.
interface CrearArticuloDTO {
  nombre: string;
  descripcion?: string;
  precio: number;
  cantidad: number;
  fotografia?: string;
}

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
  context.log("Se ha recibido una petición HTTP para Crear Artículo.");

  // Validamos que hayamos recibido un body.
  if (req.body) {
    // Creamos el objeto a partir del body.
    const articuloDto: CrearArticuloDTO = req.body;

    // Validamos los campos obligatorios.
    if (articuloDto.nombre && articuloDto.precio && articuloDto.cantidad) {
      // Validar que los datos sean correctos
      let articuloValido: boolean = true;

      if (articuloDto.nombre.length < 3 || articuloDto.nombre.length > 50) {
        articuloValido = false;
      } else if (articuloDto.precio < 0) {
        articuloValido = false;
      } else if (articuloDto.cantidad < 0) {
        articuloValido = false;
      }

      // Validar campos opcionales.
      if (articuloDto.descripcion && articuloDto.descripcion.length > 200) {
        articuloValido = false;
      }

      // Si el artículo es válido, insertar en la base de datos.
      if (articuloValido) {
        // A partir de aquí todo dentro de un try/catch.
        try {
          // Creamos objeto del ORM.
          const sequelize = new Sequelize(
            "mysql://humberto:Tacos12345@t9-bd-2016630495.mysql.database.azure.com:3306/tarea9",
            {
              dialect: "mysql",
              dialectOptions: {
                multipleStatements: true,
                ssl: {
                  require: true,
                  rejectUnauthorized: false,
                },
              },
            }
          );

          // Insertamos usando un query raw en mysql
          await sequelize.query(
            `INSERT INTO articulos (nombre, descripcion, precio, cantidad, fotografia) VALUES (?, ?, ?, ?, ?);`,
            {
              type: QueryTypes.INSERT,
              replacements: [
                articuloDto.nombre,
                articuloDto.descripcion || null,
                articuloDto.precio,
                articuloDto.cantidad,
                articuloDto.fotografia || null,
              ],
            }
          );

          // Obtenemos el artículo que acabamos de insertar.
          const articulos: Articulo[] = await sequelize.query(
            `SELECT * FROM articulos WHERE nombre = ?;`,
            {
              type: QueryTypes.SELECT,
              replacements: [articuloDto.nombre],
            }
          );

          // Cerramos la conexión.
          await sequelize.close();

          // Retornamos el código 201 (Created) y el articulo creado.
          context.res = {
            status: 201,
            body: articulos[0],
          };
        } catch (error) {
          context.log(error);
          // Retornamos el código 500 (Internal Server Error) y un mensaje de error.
          context.res = {
            status: 500,
            body: {
              message: "Error al crear el artículo",
              error: error,
            },
          };
        }
      } else {
        // Retornamos el código 400 (Bad Request) y un mensaje de error.
        context.res = {
          status: 400,
          body: "Los datos del artículo no son válidos",
        };
      }
    } else {
      // Retornamos el código 400 (Bad Request) y un mensaje de error.
      context.res = {
        status: 400,
        body: "No se recibieron todos los datos necesarios para crear el articulo",
      };
    }
  } else {
    // Retornamos el código 400 (Bad Request) y un mensaje de error.
    context.res = {
      status: 400,
      body: "No se recibieron datos",
    };
  }
};

export default httpTrigger;
