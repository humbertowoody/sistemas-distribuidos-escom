import React, { useState } from "react";
import { Button, TextField, Box, Container, Typography } from "@mui/material";
import { useFormik } from "formik";
import * as yup from "yup";
import axios from "axios";
import { URL_FUNCIONES_AZURE } from "./Constantes";
import Articulo from "./Articulo";

// Esquema de validación para cada Artículo en el formulario.
const validationSchema = yup.object({
  nombre: yup
    .string()
    .max(64, "El nombre debe contener máximo 64 caracteres")
    .required("El nombre es requerido"),
  descripcion: yup
    .string()
    .max(512, "La descripción del artículo no debe tener más de 512 caracteres")
    .optional(),
  precio: yup
    .number()
    .min(0, "El precio debe ser mayor a 0.0")
    .required("El precio es requerido")
    .typeError("Debes introducir un número"),
  cantidad: yup
    .number()
    .min(1, "La cantidad de elementos en el almacen debe ser más de 1")
    .required("La cantidad es requerida")
    .integer("La cantidad debe ser un entero")
    .typeError("La cantidad debe ser un número"),
  fotografia: yup.mixed().optional(),
});

// Componente funcional que renderiza el formulario para agregar un nuevo artículo.
const AgregarArticulo: React.FC = () => {
  const [imagePreview, setImagePreview] = useState(null);

  // Función para convertir un archivo a base64.
  const convertirABase64 = (file: File): Promise<string> => {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => resolve(reader.result as string);
      reader.onerror = (error) => reject(error);
    });
  };

  // Formik es una librería que nos permite manejar formularios de una manera más sencilla.
  const formik = useFormik({
    initialValues: {
      id: 0,
      nombre: "",
      descripcion: "",
      precio: 0.0,
      cantidad: 1,
    },
    validationSchema: validationSchema,
    // onSubmit es la función que se ejecuta cuando el usuario presiona el botón de submit.
    onSubmit: async (values: Articulo, { setSubmitting }) => {
      axios
        .post(`${URL_FUNCIONES_AZURE}/CrearArticulo`, values)
        .then((response) => {
          console.log(response);
          if (response.status === 201) {
            alert(`Artículo ${values.nombre} agregado con éxito`);
          }
          setSubmitting(false);
        })
        .catch((error) => {
          console.log(error);
          if (error.code === "ECONNREFUSED") {
            alert("No se pudo conectar con el servidor");
          } else if (error.code === "ECONNABORTED") {
            alert("Se agotó el tiempo de espera");
          } else if (error.code === "ECONNRESET") {
            alert("Se terminó la conexión con el servidor");
          } else if (error.code === "ENOENT") {
            alert("No se encontró el servidor");
          } else if (error.code === "ERR_NETWORK") {
            alert("Error de red");
          } else if (error.response) {
            if (error.response.status === 400) {
              alert("Los datos ingresados no son válidos");
            } else {
              alert("Ocurrió un error al agregar el artículo");
            }
          }
          setSubmitting(false);
        });
    },
  });

  const handleFileChange = async (event: any) => {
    console.log("Archivo");
    setImagePreview(URL.createObjectURL(event.target.files[0]) as any);
    formik.setFieldValue(
      "fotografia",
      await convertirABase64(event.target.files[0])
    );
  };

  return (
    <Container>
      <Box mt={4} mb={4}>
        <Typography variant="h4" component="h1" gutterBottom>
          Agregar un nuevo artículo
        </Typography>
        <Typography variant="body1" gutterBottom>
          En esta sección podrás agregar un nuevo artículo al sistema.
        </Typography>
        <form onSubmit={formik.handleSubmit}>
          <TextField
            fullWidth
            id="nombre"
            name="nombre"
            label="Nombre"
            value={formik.values.nombre}
            onChange={formik.handleChange}
            error={formik.touched.nombre && Boolean(formik.errors.nombre)}
            helperText={formik.touched.nombre && formik.errors.nombre}
            sx={{ mb: 4 }}
          />
          <TextField
            fullWidth
            id="descripcion"
            name="descripcion"
            label="Descripcion"
            value={formik.values.descripcion}
            onChange={formik.handleChange}
            error={
              formik.touched.descripcion && Boolean(formik.errors.descripcion)
            }
            helperText={formik.touched.descripcion && formik.errors.descripcion}
            sx={{ mb: 4 }}
          />
          <TextField
            type="number"
            fullWidth
            id="precio"
            name="precio"
            label="Precio"
            value={formik.values.precio}
            onChange={formik.handleChange}
            error={formik.touched.precio && Boolean(formik.errors.precio)}
            helperText={formik.touched.precio && formik.errors.precio}
            sx={{ mb: 4 }}
          />
          <TextField
            fullWidth
            id="cantidad"
            name="cantidad"
            label="Cantidad"
            value={formik.values.cantidad}
            onChange={formik.handleChange}
            error={formik.touched.cantidad && Boolean(formik.errors.cantidad)}
            helperText={formik.touched.cantidad && formik.errors.cantidad}
            sx={{ mb: 4 }}
          />
          <input
            id="foto"
            name="foto"
            type="file"
            onChange={handleFileChange}
          />
          {imagePreview && (
            <img src={imagePreview} alt="preview" width={200} height={200} />
          )}
          <Button color="primary" variant="outlined" fullWidth type="submit">
            Agregar Artículo
          </Button>
        </form>
      </Box>
      <Box mb={12}>
        <Button href="/" variant="contained">
          Volver a la página principal
        </Button>
      </Box>
    </Container>
  );
};

export default AgregarArticulo;
