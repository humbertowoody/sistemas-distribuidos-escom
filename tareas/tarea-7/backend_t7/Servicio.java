package backend_t7;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.QueryParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Response;

import java.sql.*;
import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import com.google.gson.*;

// la URL del servicio web es http://localhost:8080/Servicio/rest/ws
// donde:
//	"Servicio" es el dominio del servicio web (es decir, el nombre de archivo Servicio.war)
//	"rest" se define en la etiqueta <url-pattern> de <servlet-mapping> en el archivo WEB-INF\web.xml
//	"ws" se define en la siguiente anotación @Path de la clase Servicio

@Path("ws")
public class Servicio {
  static DataSource pool = null;
  static {
    try {
      Context ctx = new InitialContext();
      pool = (DataSource) ctx.lookup("java:comp/env/jdbc/datasource_Servicio");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  static Gson j = new GsonBuilder().registerTypeAdapter(byte[].class, new AdaptadorGsonBase64())
      .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();

  @POST
  @Path("articulos")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response altaArticulo(String json) throws Exception {
    ParamAltaArticulo p = (ParamAltaArticulo) j.fromJson(json, ParamAltaArticulo.class);
    Articulo articulo = p.articulo;

    Connection conexion = pool.getConnection();

    if (articulo.nombre == null || articulo.nombre.equals(""))
      return Response.status(400).entity(j.toJson(new Error("Se debe ingresar el nombre"))).build();

    if (articulo.descripcion == null || articulo.descripcion.equals(""))
      return Response.status(400).entity(j.toJson(new Error("Se debe ingresar la descripción"))).build();

    if (articulo.cantidad < 0)
      return Response.status(400).entity(j.toJson(new Error("La cantidad debe ser mayor a cero"))).build();

    if (articulo.precio < 0)
      return Response.status(400).entity(j.toJson(new Error("El precio debe ser mayor a cero"))).build();

    try {
      conexion.setAutoCommit(false);

      PreparedStatement stmt_1 = conexion.prepareStatement(
          "INSERT INTO articulos(id_articulo,nombre,descripcion,cantidad,precio) VALUES (0,?,?,?,?)");

      try {
        stmt_1.setString(1, articulo.nombre);
        stmt_1.setString(2, articulo.descripcion);
        stmt_1.setInt(3, articulo.cantidad);
        stmt_1.setDouble(4, articulo.precio);
        stmt_1.executeUpdate();
      } finally {
        stmt_1.close();
      }

      if (articulo.foto != null) {
        PreparedStatement stmt_2 = conexion.prepareStatement(
            "INSERT INTO fotos_articulos(id_articulo,foto) VALUES ((SELECT id_articulo FROM articulos WHERE nombre=?),?)");
        try {
          stmt_2.setString(0, articulo.nombre);
          stmt_2.setBytes(1, articulo.foto);
          stmt_2.executeUpdate();
        } finally {
          stmt_2.close();
        }
      }

      conexion.commit();
    } catch (Exception e) {
      conexion.rollback();
      return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
    } finally {
      conexion.setAutoCommit(true);
      conexion.close();
    }

    return Response.ok().build();
  }

  @GET
  @Path("articulos")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response consultaArticulo(String json) throws Exception {

    Connection conexion = pool.getConnection();

    try {
      PreparedStatement stmt_1 = conexion.prepareStatement(
          "SELECT a.id_articulo,a.nombre,a.descripcion,a.cantidad,a.precio,b.foto FROM articulos a LEFT OUTER JOIN fotos_articulos b ON a.id_articulo=b.id_articulo");

      try {
        ResultSet rs = stmt_1.executeQuery();
        try {
          ArrayList<Articulo> r = new ArrayList<Articulo>();
          if (rs.next()) {
            Articulo a = new Articulo();
            a.id_articulo = rs.getInt("id_articulo");
            a.nombre = rs.getString("nombre");
            a.descripcion = rs.getString("descripcion");
            a.cantidad = rs.getInt("cantidad");
            a.precio = rs.getDouble("precio");
            a.foto = rs.getBytes("foto");
            r.add(a);
          }
          return Response.ok(j.toJson(r)).build();
        } finally {
          rs.close();
        }
      } finally {
        stmt_1.close();
      }
    } catch (Exception e) {
      return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
    } finally {
      conexion.close();
    }
  }

  @GET
  @Path("carrito_articulos")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response consultaArticulo(String json) throws Exception {
    Connection conexion = pool.getConnection();

    try {
      PreparedStatement stmt_1 = conexion.prepareStatement(
          "SELECT a.cantidad,b.id_articulo,b.nombre,b.descripcion,b.cantidad,b.precio,c.foto FROM carrito a, LEFT OUTER JOIN articulos b ON a.id_articulo=b.id_articulo, LEFT OUTER JOIN fotos_articulos c ON a.id_articulo=b.id_articulo");

      try {
        ResultSet rs = stmt_1.executeQuery();
        try {
          ArrayList<RespuestaCarritoArticulo> r = new ArrayList<RespuestaCarritoArticulo>();
          if (rs.next()) {
            RespuestaCarritoArticulo a = new RespuestaCarritoArticulo();
            a.articulo = new Articulo();
            a.cantidad = rs.getInt(0);
            a.articulo.id_articulo = rs.getInt(1);
            a.articulo.nombre = rs.getString(2);
            a.articulo.descripcion = rs.getString(3);
            a.articulo.cantidad = rs.getInt(4);
            a.articulo.precio = rs.getDouble(5);
            a.articulo.foto = rs.getBytes(6);
            r.add(a);
          }
          return Response.ok(j.toJson(r)).build();
        } finally {
          rs.close();
        }
      } finally {
        stmt_1.close();
      }
    } catch (Exception e) {
      return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
    } finally {
      conexion.close();
    }
  }

  @POST
  @Path("alta_carrito_articulo")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response altaCarritoArticulo(String json) throws Exception {
    ParamAltaCarritoArticulo p = (ParamAltaCarritoArticulo) j.fromJson(json, ParamAltaCarritoArticulo.class);
    CarritoArticulo carrito_articulo = p.carrito_articulo;

    Connection conexion = pool.getConnection();

    if (carrito_articulo.id_articulo == 0)
      return Response.status(400).entity(j.toJson(new Error("Se debe ingresar el id del artículo"))).build();

    if (carrito_articulo.cantidad <= 0)
      return Response.status(400).entity(j.toJson(new Error("La cantidad debe ser mayor a cero"))).build();

    // Obtener la cantidad de artículos disponibles desde la base de datos y validar
    // si hay stock
    int cantidad_disponible = 0;
    try {
      PreparedStatement stmt_1 = conexion.prepareStatement(
          "SELECT cantidad FROM articulos WHERE id_articulo=?");
      try {
        stmt_1.setInt(1, carrito_articulo.id_articulo);
        ResultSet rs = stmt_1.executeQuery();
        try {
          if (rs.next()) {
            cantidad_disponible = rs.getInt("cantidad");
          }
        } finally {
          rs.close();
        }
      } finally {
        stmt_1.close();
      }
    } catch (Exception e) {
      return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
    }

    if (cantidad_disponible == 0)
      return Response.status(400).entity(j.toJson(new Error("No hay stock disponible"))).build();

    // Restamos la cantidad de artículos disponibles con la cantidad solicitada
    cantidad_disponible -= carrito_articulo.cantidad;

    // Actualizamos la cantidad de artículos disponibles en la base de datos
    try {
      PreparedStatement stmt_1 = conexion.prepareStatement(
          "UPDATE articulos SET cantidad=? WHERE id_articulo=?");
      try {
        stmt_1.setInt(1, cantidad_disponible);
        stmt_1.setInt(2, carrito_articulo.id_articulo);
        stmt_1.executeUpdate();
      } finally {
        stmt_1.close();
      }
    } catch (Exception e) {
      return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
    }

    try {
      conexion.setAutoCommit(false);

      PreparedStatement stmt_1 = conexion.prepareStatement(
          "INSERT INTO carrito(id_articulo,cantidad) VALUES (?,?)");

      try {
        stmt_1.setInt(2, carrito_articulo.id_articulo);
        stmt_1.setInt(3, carrito_articulo.cantidad);
        stmt_1.executeUpdate();
      } finally {
        stmt_1.close();
      }

      conexion.commit();
    } catch (Exception e) {
      conexion.rollback();
      return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
    } finally {
      conexion.setAutoCommit(true);
      conexion.close();
    }

    return Response.ok().build();
  }

  @POST
  @Path("borra_carrito_articulo")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response borraCarritoArticulo(String json) throws Exception {
    ParamBorraCarritoArticulo p = (ParamBorraCarritoArticulo) j.fromJson(json, ParamBorraCarritoArticulo.class);
    Carrito carrito_articulo = p.carrito_articulo;

    Connection conexion = pool.getConnection();

    if (carrito_articulo.id_articulo == 0)
      return Response.status(400).entity(j.toJson(new Error("Se debe ingresar el id del artículo"))).build();

    // Obtener la cantidad de artículos disponibles desde la base de datos
    int cantidad_disponible = 0;
    try {
      PreparedStatement stmt_1 = conexion.prepareStatement(
          "SELECT cantidad FROM articulos WHERE id_articulo=?");
      try {
        stmt_1.setInt(1, carrito_articulo.id_articulo);
        ResultSet rs = stmt_1.executeQuery();
        try {
          if (rs.next()) {
            cantidad_disponible = rs.getInt("cantidad");
          }
        } finally {
          rs.close();
        }
      } finally {
        stmt_1.close();
      }
    } catch (Exception e) {
      return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
    }

    // Sumamos la cantidad de artículos disponibles con la cantidad solicitada
    cantidad_disponible += carrito_articulo.cantidad;

    // Actualizamos la cantidad de artículos disponibles en la base de datos
    try {
      PreparedStatement stmt_1 = conexion.prepareStatement(
          "UPDATE articulos SET cantidad=? WHERE id_articulo=?");
      try {
        stmt_1.setInt(1, cantidad_disponible);
        stmt_1.setInt(2, carrito_articulo.id_articulo);
        stmt_1.executeUpdate();
      } finally {
        stmt_1.close();
      }
    } catch (Exception e) {
      return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
    }

    try {
      conexion.setAutoCommit(false);

      PreparedStatement stmt_1 = conexion.prepareStatement(
          "DELETE FROM carrito WHERE id_articulo=?");

      try {
        stmt_1.setInt(1, carrito_articulo.id_articulo);
        stmt_1.executeUpdate();
      } finally {
        stmt_1.close();
      }
    } catch (Exception e) {
      conexion.rollback();
      return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
    } finally {
      conexion.setAutoCommit(true);
      conexion.close();
    }
    return Response.ok().build();
  }

  @POST
  @Path("modifica_carrito_articulo")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response modificaCarritoArticulo(String json) throws Exception {
    ParamModificaCarritoArticulo p = (ParamModificaCarritoArticulo) j.fromJson(json,
        ParamModificaCarritoArticulo.class);
    Carrito carrito_articulo = p.carrito_articulo;

    Connection conexion = pool.getConnection();

    if (carrito_articulo.id_articulo == 0)
      return Response.status(400).entity(j.toJson(new Error("Se debe ingresar el id del artículo"))).build();

    if (carrito_articulo.cantidad == 0)
      return Response.status(400).entity(j.toJson(new Error("Se debe ingresar la cantidad"))).build();

    // Obtener la cantidad de artículos disponibles desde la base de datos
    int cantidad_disponible = 0;
    try {
      PreparedStatement stmt_1 = conexion.prepareStatement(
          "SELECT cantidad FROM articulos WHERE id_articulo=?");
      try {
        stmt_1.setInt(1, carrito_articulo.id_articulo);
        ResultSet rs = stmt_1.executeQuery();
        try {
          if (rs.next()) {
            cantidad_disponible = rs.getInt("cantidad");
          }
        } finally {
          rs.close();
        }
      } finally {
        stmt_1.close();
      }
    } catch (Exception e) {
      return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
    }

    // Sumamos la cantidad de artículos disponibles con la cantidad solicitada
    cantidad_disponible += carrito_articulo.cantidad;

    // Actualizamos la cantidad de artículos disponibles en la base de datos
    try {
      PreparedStatement stmt_1 = conexion.prepareStatement(
          "UPDATE articulos SET cantidad=? WHERE id_articulo=?");
      try {
        stmt_1.setInt(1, cantidad_disponible);
        stmt_1.setInt(2, carrito_articulo.id_articulo);
        stmt_1.executeUpdate();
      } finally {
        stmt_1.close();
      }
    } catch (Exception e) {
      return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
    }

    try {
      conexion.setAutoCommit(false);

      PreparedStatement stmt_1 = conexion.prepareStatement(
          "UPDATE carrito SET cantidad=? WHERE id_articulo=?");

      try {
        stmt_1.setInt(1, carrito_articulo.cantidad);
        stmt_1.setInt(2, carrito_articulo.id_articulo);
        stmt_1.executeUpdate();
      } finally {
        stmt_1.close();
      }
    } catch (Exception e) {
      conexion.rollback();
      return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
    } finally {
      conexion.setAutoCommit(true);
      conexion.close();
    }
    return Response.ok().build();
  }

  @POST
  @Path("borra_carrito")
  @Produces(MediaType.APPLICATION_JSON)
  public Response borraCarrito() throws Exception {
    Connection conexion = pool.getConnection();

    try {
      conexion.setAutoCommit(false);

      PreparedStatement stmt_1 = conexion.prepareStatement(
          "DELETE FROM carrito");

      try {
        stmt_1.executeUpdate();
      } finally {
        stmt_1.close();
      }
    } catch (Exception e) {
      conexion.rollback();
      return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
    } finally {
      conexion.setAutoCommit(true);
      conexion.close();
    }
    return Response.ok().build();
  }
}
