/**
 * AgregarCarrito
 * Esta función permite al usuario agregar un artículo al carrito de compras.
 * Humberto Alejandro Ortega Alcocer
 */
import { AzureFunction, Context, HttpRequest } from "@azure/functions";
import { QueryTypes, Sequelize } from "sequelize";

// Interfaz para los datos que necesitamos.
interface AgregarArticuloCarritoDTO {
  articulo_id: number;
  cantidad: number;
}

// Interfaz para representar un artículo.
interface Articulo {
  id: number;
  nombre: string;
  descripcion?: string;
  precio: number;
  cantidad: number;
  fotografia?: string;
}

// Interfaz para representar un artículo en el carrito.
interface Carrito {
  articulo_id: number;
  cantidad: number;
}

const httpTrigger: AzureFunction = async function (
  context: Context,
  req: HttpRequest
): Promise<void> {
  context.log(
    "Se ha recibido una petición HTTP para Agregar un Artículo al Carrito"
  );

  // Validamos que hayamos recibido un body.
  if (req.body) {
    // Validamos los datos del Body.
    const agregarArticuloCarritoDto: AgregarArticuloCarritoDTO = req.body;

    try {
      // Iniciamos la conexión con la base de datos.
      const sequelize = new Sequelize(
        "mysql://root:root@localhost:3306/tarea9",
        {
          dialect: "mysql",
          dialectOptions: {
            multipleStatements: true,
          },
        }
      );

      // Obtenemos el artículo.
      const articulos: Articulo[] = await sequelize.query<Articulo>(
        "SELECT id, nombre, descripcion, precio, cantidad, CONVERT(fotografia using utf8) as fotografia FROM articulos WHERE id = ?",
        {
          type: QueryTypes.SELECT,
          mapToModel: true,
          replacements: [agregarArticuloCarritoDto.articulo_id],
        }
      );

      // Verificamos que se haya encontrado al menos uno.
      if (articulos.length === 0) {
        context.res = {
          status: 404,
          body: "No se encontró el artículo.",
        };

        return;
      }

      const articulo: Articulo = articulos[0];

      // Validamos que haya suficientes artículos.
      if (articulo.cantidad < agregarArticuloCarritoDto.cantidad) {
        context.res = {
          status: 400,
          body: "No hay suficientes artículos.",
        };

        return;
      }

      // Validamos que el artículo no esté ya en el carrito.
      const carritos: Carrito[] = await sequelize.query<Carrito>(
        "SELECT articulo_id, cantidad FROM carrito WHERE articulo_id = :articulo_id",
        {
          type: QueryTypes.SELECT,
          mapToModel: true,
          replacements: { articulo_id: articulo.id },
        }
      );

      // Verificamos que se haya encontrado al menos uno.
      if (carritos.length === 0) {
        // Creamos la transaccion.
        const transaction = await sequelize.transaction();

        // Agregamos el artículo al carrito.
        await sequelize.query(
          "INSERT INTO carrito (articulo_id, cantidad) VALUES (:articulo_id, :cantidad)",
          {
            type: QueryTypes.INSERT,
            replacements: {
              articulo_id: articulo.id,
              cantidad: agregarArticuloCarritoDto.cantidad,
            },
            transaction,
          }
        );

        // Quitamos la cantidad de artículos del inventario.
        await sequelize.query(
          "UPDATE articulos SET cantidad = cantidad - :cantidad WHERE id = :id",
          {
            type: QueryTypes.UPDATE,
            replacements: {
              id: articulo.id,
              cantidad: agregarArticuloCarritoDto.cantidad,
            },
            transaction,
          }
        );

        // Restamos la cantidad de artículos del inventario.
        articulo.cantidad -= agregarArticuloCarritoDto.cantidad;

        // Hacemos commit de la transacción.
        await transaction.commit();

        // Regresamos el artículo.
        context.res = {
          status: 200,
          body: articulo,
        };
      } else {
        // Informamos al usuario de que el artículo ya está en el carrito y que debe usar la función de actualizar.
        context.res = {
          status: 402,
          body: "El artículo ya está en el carrito. Use la función de actualizar.",
        };
      }

      // Cerrar la conexión con la base de datos.
      await sequelize.close();
    } catch (error) {
      // Si hubo un error en la base de datos regresamos un error 500.
      context.res = {
        status: 500,
        body: {
          mensaje: "Ocurrió un error en la base de datos.",
          error: error,
        },
      };
    }
  } else {
    // Si no recibimos un body, regresamos un error.
    context.res = {
      status: 400,
      body: "No se recibió nada en el body",
    };
  }
};

export default httpTrigger;
