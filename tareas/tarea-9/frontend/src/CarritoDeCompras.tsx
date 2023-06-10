import {
  Button,
  Card,
  CardContent,
  CardHeader,
  CardMedia,
  Grid,
  TextField,
  Typography,
} from "@mui/material";
import axios from "axios";
import React, { useEffect, useState } from "react";
import Carrito from "./Carrito";
import { URL_FUNCIONES_AZURE } from "./Constantes";

const CarritoDeCompras: React.FC = () => {
  const [carritos, setCarritos]: [Carrito[], any] = useState([]);

  useEffect(() => {
    axios
      .get(`${URL_FUNCIONES_AZURE}/ObtenerCarrito`)
      .then((response) => setCarritos(response.data))
      .catch((error) => console.error("Error fetching products:", error));
  }, []);

  // Función para cuando el usuario modifica la cantidad de un artículo en el carrito.
  const handleModificarCantidad = async (
    carrito: Carrito,
    cantidad: number
  ) => {
    try {
      // Actualizamos la cantidad del artículo en el carrito.
      await axios.post(`${URL_FUNCIONES_AZURE}/EditarCarrito`, {
        articulo_id: carrito.articulo_id,
        cantidad,
      });

      // Actualizamos el estado de los carritos.
      setCarritos(
        await axios
          .get(`${URL_FUNCIONES_AZURE}/ObtenerCarrito`)
          .then((response) => response.data)
      );
    } catch (error) {
      console.error("Error fetching products:", error);
    }
  };

  // Función para cuando el usuario da click en el botón de eliminar un artículo del carrito.
  const handleEliminarArticulo = (
    carrito: Carrito,
    preconfirmacion: boolean = false
  ) => {
    //  Confirmación del usuario.
    const confirmacionUsuario: boolean =
      preconfirmacion ||
      window.confirm(
        "¿Estás seguro de que quieres eliminar este artículo del carrito?" +
          "\n" +
          carrito?.articulo?.nombre
      );

    if (confirmacionUsuario) {
      // Eliminamos el artículo del carrito.
      axios
        .post(`${URL_FUNCIONES_AZURE}/EliminarCarrito`, {
          articulo_id: carrito.articulo_id,
        })
        .then(() =>
          // Actualizamos el estado de los carritos.
          axios
            .get(`${URL_FUNCIONES_AZURE}/ObtenerCarrito`)
            .then((response) => setCarritos(response.data))
        )
        .catch((error) => console.error("Error fetching products:", error));
    }
  };

  // Función para cuando el usuario da click en el botón de eliminar todos los artículos del carrito.
  const handleEliminarCarrito = async () => {
    //  Confirmación del usuario.
    const confirmacionUsuario: boolean = window.confirm(
      "¿Estás seguro de que quieres eliminar todos los artículos del carrito?"
    );

    if (confirmacionUsuario) {
      carritos.forEach((carrito: Carrito) => {
        handleEliminarArticulo(carrito, true);
      });
    }
  };

  return (
    <Grid container spacing={2} mt={4} mb={12}>
      <Grid item xs={12}>
        <Typography variant="h4" component="h1" gutterBottom>
          Carrito de compras
        </Typography>
        <Typography variant="body1" gutterBottom>
          En esta sección podrás ver los artículos que has añadido al carrito de
          compras, la cantidad y el precio de cada uno.
        </Typography>
      </Grid>
      {carritos.map((carrito: Carrito) => (
        <Grid item textAlign="right" xs={12} key={carrito.articulo_id}>
          <Card>
            <CardMedia
              component="img"
              height="140"
              src={carrito?.articulo?.fotografia}
              alt={carrito?.articulo?.nombre}
            />
            <CardHeader title={carrito?.articulo?.nombre || "Desconocido"} />
            <CardContent>
              <Typography variant="body1" gutterBottom>
                Cantidad:{" "}
                <TextField
                  type="number"
                  variant="standard"
                  inputProps={{ min: 1, max: carrito?.articulo?.cantidad || 0 }}
                  value={carrito.cantidad}
                  onChange={(event) => {
                    handleModificarCantidad(
                      carrito,
                      parseInt(event.target.value)
                    );
                  }}
                />
              </Typography>
              <Typography variant="body1" gutterBottom>
                Precio unitario: ${carrito?.articulo?.precio || 0} MXN
              </Typography>
              <Typography variant="body1" gutterBottom>
                Total: ${(carrito?.articulo?.precio || 0) * carrito.cantidad}{" "}
                MXN
              </Typography>
              <Button
                onClick={() => handleEliminarArticulo(carrito)}
                color="warning"
                variant="contained"
              >
                Eliminar artículo del carrito
              </Button>
            </CardContent>
          </Card>
        </Grid>
      ))}

      <Grid item textAlign="center" xs={12}>
        <Typography variant="h5" component="h2" gutterBottom>
          Total: $
          {carritos
            .reduce((a, b) => a + b.cantidad * (b?.articulo?.precio || 0), 0)
            .toLocaleString("es-MX")}{" "}
          MXN{"     "}
          <Button
            variant="contained"
            color="error"
            onClick={handleEliminarCarrito}
          >
            Vaciar carrito de compras.
          </Button>
        </Typography>
        <Button variant="contained" color="primary" href="/articulos">
          Volver a listado de artículos
        </Button>
      </Grid>
    </Grid>
  );
};

export default CarritoDeCompras;
