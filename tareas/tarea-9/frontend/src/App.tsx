import React from "react";
import { Container, AppBar, Toolbar, Typography } from "@mui/material";
import Inicio from "./Inicio";
import CarritoDeCompras from "./CarritoDeCompras";
import { Route, Routes } from "react-router-dom";
import Footer from "./Footer";
import Articulos from "./Articulos";
import AgregarArticulo from "./AgregarArticulo";

const App: React.FC = () => {
  return (
    <React.Fragment>
      <AppBar position="relative">
        <Toolbar>
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            🛒 Tarea 9 - Carrito de Compras 🧾
          </Typography>
        </Toolbar>
      </AppBar>
      <Container>
        <Routes>
          <Route path="/" element={<Inicio />} />
          <Route path="/articulos" element={<Articulos />} />
          <Route path="/carrito" element={<CarritoDeCompras />} />
          <Route path="/agregar-articulo" element={<AgregarArticulo />} />
        </Routes>
      </Container>
      <Footer />
    </React.Fragment>
  );
};

export default App;
