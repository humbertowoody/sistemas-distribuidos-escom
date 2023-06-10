import React from "react";
import { Typography, Button, Container, Box } from "@mui/material";

const Inicio: React.FC = () => {
  return (
    <Container>
      <Box mt={4} mb={4}>
        <Typography variant="h4" component="h1" gutterBottom>
          Carrito de Compras 🧾
        </Typography>
        <Typography variant="body1" component="p" gutterBottom>
          Este es un carrito de compras simple basado en una arquitectura de
          backend serverless con Azure Functions y una base de datos MySQL.
        </Typography>
        <Typography variant="body1" component="p" gutterBottom>
          Desarrollado por: Humberto Alejandro Ortega Alcocer
        </Typography>
        <Typography variant="body1" component="p" gutterBottom>
          Sistemas Distribuidos - 4CV13
        </Typography>
      </Box>
      <Box mt={4} mb={4}>
        <Button href="/agregar-articulo" variant="contained" color="primary">
          Agregar Artículo
        </Button>
      </Box>
      <Box mt={4} mb={4}>
        <Button href="/articulos" variant="contained" color="primary">
          Visualizar Artículos
        </Button>
      </Box>
    </Container>
  );
};

export default Inicio;
