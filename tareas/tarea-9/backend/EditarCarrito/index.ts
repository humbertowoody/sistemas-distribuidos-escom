/**
 * EditarCarrito
 * Esta función permite que el usuario modifique un elemento del carrito en cantidad.
 * Humberto Alejandro Ortega Alcocer
 */
import { AzureFunction, Context, HttpRequest } from "@azure/functions";
import { QueryTypes, Sequelize } from "sequelize";

// Interfaz para la solicitud.
interface Solicitud {
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

// Interfaz para representar un elemento en el carrito.
interface Carrito {
  articulo_id: number;
  cantidad: number;
  articulo?: Articulo;
}

const httpTrigger: AzureFunction = async function (
  context: Context,
  req: HttpRequest
): Promise<void> {
  context.log("Se ha recibido una petición HTTP para Modificar al Carrito");

  // Extraemos el cuerpo de la solicitud.
  const solicitud: Solicitud = req.body;

  if (solicitud) {
    try {
      // Abrimos la conexión a la base de datos.
      const sequelize = new Sequelize(
        "mysql://root:root@localhost:3306/tarea9",
        {
          dialect: "mysql",
          dialectOptions: {
            multipleStatements: true,
          },
        }
      );

      // Obtenemos el carrito de compras.
      const carritos: Carrito[] = await sequelize.query<Carrito>(
        "SELECT * FROM carrito WHERE articulo_id = :articulo_id",
        {
          replacements: { articulo_id: solicitud.articulo_id },
          type: QueryTypes.SELECT,
        }
      );

      // Validamos que el artículo exista.
      if (carritos.length <= 0) {
        context.res = {
          status: 404,
          body: "El artículo no existe en el carrito de compras.",
        };
      } else {
        // Obtenemos el carrito.
        const carrito: Carrito = carritos[0];

        // Obtenemos el artículo.
        const articulos: Articulo[] = await sequelize.query<Articulo>(
          "SELECT * FROM articulos WHERE id = :id",
          {
            replacements: { id: solicitud.articulo_id },
            type: QueryTypes.SELECT,
          }
        );

        // Validamos que el artículo exista.
        if (articulos.length <= 0) {
          context.res = {
            status: 404,
            body: "El artículo no existe.",
          };
        } else {
          // Obtenemos el artículo.
          const articulo: Articulo = articulos[0];

          if (carrito.cantidad < solicitud.cantidad) {
            // El usuario agregó más artículos al carrito.
            // Validamos que haya suficientes artículos en existencia.
            if (articulo.cantidad < solicitud.cantidad) {
              context.res = {
                status: 400,
                body: "No hay suficientes artículos en existencia.",
              };
            } else {
              // Creamos una transacción.
              const transaction = await sequelize.transaction();

              // Actualizamos la cantidad de artículos en el carrito.
              await sequelize.query(
                "UPDATE carrito SET cantidad = :cantidad WHERE articulo_id = :articulo_id",
                {
                  replacements: {
                    cantidad: solicitud.cantidad,
                    articulo_id: solicitud.articulo_id,
                  },
                  type: QueryTypes.UPDATE,
                  transaction,
                }
              );

              // Actualizamos la cantidad de artículos en existencia.
              await sequelize.query(
                "UPDATE articulos SET cantidad = :cantidad WHERE id = :id",
                {
                  replacements: {
                    cantidad:
                      articulo.cantidad -
                      (solicitud.cantidad - carrito.cantidad),
                    id: solicitud.articulo_id,
                  },
                  type: QueryTypes.UPDATE,
                  transaction,
                }
              );

              // Confirmamos la transacción.
              await transaction.commit();

              // Obtenemos el carrito de compras.
              const carritoRespuesta: Carrito = {
                articulo_id: solicitud.articulo_id,
                cantidad: solicitud.cantidad,
                articulo: articulo,
              };

              // Regresamos el carrito de compras.
              context.res = {
                status: 200,
                body: carritoRespuesta,
              };
            }
          } else {
            // El usuario quitó artículos del carrito.
            // Creamos una transacción.
            const transaction = await sequelize.transaction();

            // Actualizamos la cantidad de artículos en el carrito.
            await sequelize.query(
              "UPDATE carrito SET cantidad = :cantidad WHERE articulo_id = :articulo_id",
              {
                replacements: {
                  cantidad: solicitud.cantidad,
                  articulo_id: solicitud.articulo_id,
                },
                type: QueryTypes.UPDATE,
                transaction,
              }
            );

            // Actualizamos la cantidad de artículos en existencia.
            await sequelize.query(
              "UPDATE articulos SET cantidad = :cantidad WHERE id = :id",
              {
                replacements: {
                  cantidad:
                    articulo.cantidad + (carrito.cantidad - solicitud.cantidad),
                  id: solicitud.articulo_id,
                },
                type: QueryTypes.UPDATE,
                transaction,
              }
            );

            // Confirmamos la transacción.
            await transaction.commit();

            // Obtenemos el carrito de compras.
            const carritoRespuesta: Carrito = {
              articulo_id: solicitud.articulo_id,
              cantidad: solicitud.cantidad,
              articulo: articulo,
            };

            // Regresamos el carrito de compras.
            context.res = {
              status: 200,
              body: carritoRespuesta,
            };
          }
        }
      }
      // Cerramos la conexion a la base de datos.
      await sequelize.close();
    } catch (error) {
      context.res = {
        status: 500,
        body: "Error al conectarse a la base de datos.",
      };
    }
  } else {
    context.res = {
      status: 400,
      body: "No se recibieron datos.",
    };
  }
};

export default httpTrigger;
