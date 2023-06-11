/**
 * EliminarCarrito
 * Esta función permite que el usuario elimine un artículo del carrito de compras.
 * Humberto Alejandro Ortega Alcocer
 */
import { AzureFunction, Context, HttpRequest } from "@azure/functions";
import { QueryTypes, Sequelize, Transaction } from "sequelize";

// Interfaz para representar un carrito.
interface Carrito {
  articulo_id: number;
  cantidad: number;
}

// Interfaz para representar un artículo.
interface Articulo {
  id: number;
  nombre: string;
  precio: number;
  descripcion: string;
  cantidad: number;
  fotografia?: string;
}

const httpTrigger: AzureFunction = async function (
  context: Context,
  req: HttpRequest
): Promise<void> {
  context.log(
    "Se recibió una petición HTTP para Eliminar un registro del Carrito de Compras"
  );

  const id = req.body.articulo_id;

  if (!id) {
    context.res = {
      status: 400,
      body: "Falta el ID del Artículo en el Carrito de Compras",
    };
  } else {
    try {
      // Conectarse a la base de datos
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

      // Obtener el carrito de compras.
      const carritos: Carrito[] = await sequelize.query<Carrito>(
        "SELECT * FROM carrito WHERE articulo_id = :articulo_id",
        {
          replacements: { articulo_id: id },
          type: QueryTypes.SELECT,
        }
      );

      // Validamos que el artículo exista.
      if (carritos.length <= 0) {
        context.res = {
          status: 404,
          body: "No existe el artículo en el carrito de compras",
        };
      } else {
        // Crear una transacción.
        const transaction: Transaction = await sequelize.transaction();

        // Agregar de vuelta las unidades del carrito al artículo.
        await sequelize.query(
          "UPDATE articulos SET cantidad = cantidad + :cantidad WHERE id = :id",
          {
            replacements: {
              id: carritos[0].articulo_id,
              cantidad: carritos[0].cantidad,
            },
            type: QueryTypes.UPDATE,
            transaction,
          }
        );

        // Eliminar el artículo del carrito de compras.
        await sequelize.query(
          "DELETE FROM carrito WHERE articulo_id = :articulo_id",
          {
            replacements: { articulo_id: id },
            type: QueryTypes.DELETE,
            transaction,
          }
        );

        // Ejecutar la transacción.
        await transaction.commit();

        // Responder con un 200.
        context.res = {
          status: 200,
          body: "Artículo eliminado del carrito de compras",
        };
      }
      // Cerrar la conexion a la base de datos.
      await sequelize.close();
    } catch (error) {
      context.res = {
        status: 500,
        body: {
          mensaje: "Error al eliminar el artículo del carrito de compras",
          error,
        },
      };
    }
  }
};

export default httpTrigger;
