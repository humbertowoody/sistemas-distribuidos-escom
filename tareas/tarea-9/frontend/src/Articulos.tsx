import React, { useState, useEffect, ChangeEvent } from "react";
import {
  Button,
  Grid,
  Typography,
  Card,
  CardContent,
  CardMedia,
  TextField,
  Input,
} from "@mui/material";
import axios from "axios";
import Articulo from "./Articulo";
import { URL_FUNCIONES_AZURE } from "./Constantes";
import Carrito from "./Carrito";

const Articulos: React.FC = () => {
  // Variables que manejaremos con el estado de react.
  const [carritos, setCarritos]: [Carrito[], any] = useState([]);

  //  Esta función convierte un arreglo de Artículos a un arreglo de Carritos.
  const articulosACarritos = (articulos: Articulo[], cantidad: number = 1) => {
    return articulos.map((articulo: Articulo) => {
      return {
        articulo_id: articulo.id,
        cantidad: cantidad,
        articulo: {
          id: articulo.id,
          nombre: articulo.nombre,
          descripcion: articulo.descripcion,
          precio: articulo.precio,
          cantidad: articulo.cantidad - cantidad,
          fotografia: articulo.fotografia,
        },
      };
    });
  };

  // Esta función se ejecuta en cuanto se carga el componente, podría ser como un
  // constructor, aquí hago el request para obtener los Artículos y formar
  // objetos Carrito para cada uno a modo de controlar el flujo más fácil.
  useEffect(() => {
    axios
      .get(`${URL_FUNCIONES_AZURE}/ObtenerArticulos`)
      .then((response) => {
        // Actualizamos el estado de los carritos.
        setCarritos(articulosACarritos(response.data));
      })
      .catch((error) => console.error("Error fetching products:", error));
  }, []);

  /**
   * Fnción para mostrar más información de un artículo.
   */
  const handleVerMasInformacion = (carrito: Carrito) => {
    alert(
      `Nombre: ${carrito?.articulo?.nombre}\nDescripción: ${carrito?.articulo?.descripcion}\nPrecio: ${carrito?.articulo?.precio}\nCantidad: ${carrito?.articulo?.cantidad}`
    );
  };

  // Función para buscar un artículo por nombre.
  const handleBuscarArticulo = (event: React.ChangeEvent<HTMLInputElement>) => {
    event.preventDefault();
    const nombre = event.target.value;
    axios
      .get(`${URL_FUNCIONES_AZURE}/ObtenerArticulos?nombre=${nombre}`)
      .then((response) => setCarritos(articulosACarritos(response.data)))
      .catch((error) => console.error("Error fetching products:", error));
  };

  const handleCantidadChange = (
    event: React.ChangeEvent<HTMLInputElement>,
    carrito: Carrito
  ) => {
    // Obtenemos la cantidad del input.
    const cantidad: number = parseInt(event.target.value);

    // Asignamos la cantidad seleccionada por el usuario.
    carrito.cantidad = cantidad;

    // Actualizamos el estado de los carritos.
    const carritosActualizados = carritos.map((carritoActual: Carrito) => {
      if (carritoActual.articulo_id === carrito.articulo_id) {
        return carrito;
      } else {
        return carritoActual;
      }
    });
    setCarritos(carritosActualizados);
  };

  // Función para añadir un artículo al carrito.
  const handleAddToCarrito = (carrito: Carrito) => {
    console.log("Carrito a enviar", carrito);
    if (!carrito.articulo) {
      alert("No se encontró el artículo, intenta de nuevo.");
    } else {
      // Validamos que la cantidad esté disponible en el artículo.
      if (carrito.cantidad <= carrito.articulo.cantidad) {
        axios
          .post(`${URL_FUNCIONES_AZURE}/AgregarCarrito`, {
            articulo_id: carrito.articulo_id,
            cantidad: carrito.cantidad,
          })
          .then((response) => {
            const articulo: Articulo = response.data;

            // Enviamos un mensaje al usuario.
            alert(
              `Se añadieron ${carrito.cantidad} ${carrito?.articulo?.nombre}(s) al carrito (quedan ${articulo.cantidad} disponibles).`
            );

            // Actualizamos el estado de los carritos.
            const carritosActualizados = carritos.map(
              (carritoActual: Carrito) => {
                if (carritoActual.articulo_id === carrito.articulo_id) {
                  return {
                    articulo_id: carrito.articulo_id,
                    cantidad: 1,
                    articulo,
                  };
                } else {
                  return carritoActual;
                }
              }
            );
            setCarritos(carritosActualizados);
          })
          .catch((error) => {
            if (error.response && error.response.status) {
              if (error.response.status === 404) {
                alert("El artículo no existe en la BD.");
              }
              if (error.response.status === 400) {
                alert("No hay suficiente cantidad de este artículo.");
              } else {
                alert(
                  "El artículo ya se encontraba en el carrito, modifíquelo ahí."
                );
              }
            } else {
              alert("Ocurrió un error al añadir el artículo al carrito.");
              console.error("Error añadiendo producto al carrito:", error);
            }
          });
      } else {
        alert(
          `No hay suficiente cantidad de este artículo, el máximo es ${carrito.articulo.cantidad}`
        );
      }
    }
  };

  return (
    <Grid container spacing={2} mt={4} mb={12}>
      <Grid item xs={12} sm={6}>
        <Typography variant="h4">Artículos disponibles</Typography>
        <Typography variant="body1" gutterBottom>
          En esta sección podrás ver los artículos disponibles para comprar.
        </Typography>
        <Button variant="contained" color="primary" href="/carrito">
          Ir al carrito de compras
        </Button>
      </Grid>
      <Grid item xs={12} sm={6}>
        <Typography variant="subtitle1">Búsqueda de artículos:</Typography>
        <Input
          fullWidth
          onChange={handleBuscarArticulo}
          placeholder={"Escribe el nombre del artículo a buscar..."}
        />
      </Grid>

      {carritos.map((carrito: Carrito) => (
        <Grid
          item
          textAlign="center"
          xs={12}
          sm={6}
          md={4}
          lg={3}
          key={carrito?.articulo?.id}
        >
          <Card>
            <CardMedia
              component="img"
              height="140"
              src={carrito?.articulo?.fotografia}
              alt={carrito?.articulo?.nombre}
            />
            <CardContent>
              <Typography variant="h5" component="div">
                {carrito?.articulo?.nombre} - ${carrito?.articulo?.precio} MXN
              </Typography>
              <Button
                color="primary"
                onClick={() => handleVerMasInformacion(carrito)}
              >
                Ver más información
              </Button>
              <TextField
                fullWidth
                type="number"
                defaultValue={1}
                inputProps={{
                  min: 1,
                  max: carrito?.articulo?.cantidad,
                }}
                onChange={(event: ChangeEvent<HTMLInputElement>) =>
                  handleCantidadChange(event, carrito)
                }
              />
              <hr />
              <Button
                variant="contained"
                color="primary"
                onClick={() =>
                  handleAddToCarrito(
                    carritos.find(
                      (carritoActual: Carrito) =>
                        carritoActual.articulo?.id === carrito.articulo?.id
                    ) || carrito
                  )
                }
              >
                Agregar al carrito
              </Button>
            </CardContent>
          </Card>
        </Grid>
      ))}

      <Grid item xs={12}>
        <Button variant="contained" color="primary" href="/">
          Regresar a menú principal
        </Button>
      </Grid>
    </Grid>
  );
};

export default Articulos;
