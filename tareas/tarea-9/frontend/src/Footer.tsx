import React from "react";
import { Container, Typography, Box } from "@mui/material";

const Footer = () => {
  const year = new Date().getFullYear();

  return (
    <Box
      sx={{
        bgcolor: "#f7f7f7",
        p: 1,
        position: "fixed",
        bottom: 0,
        width: "100%",
      }}
    >
      <Container>
        <Typography variant="body1" align="center">
          🎓 Humberto Alejandro Ortega Alcocer
        </Typography>
        <Typography variant="body2" color="text.secondary" align="center">
          {"Copyright © "}
          {year} {" | "}
          {"Boleta: 2016630495 | Grupo: 4CV13"}
        </Typography>
      </Container>
    </Box>
  );
};

export default Footer;
