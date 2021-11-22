package Projecte;

import static Projecte.projecte_1.connectarBD;
import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class projecte_1 {
    static Connection connectarBD = null;
    public static void main (String[] args) throws SQLException {
        boolean sortir=false;
        connectarBD();
        Scanner teclat = new Scanner (System.in);
        
        do{
           System.out.println("^^^^MENU GESTOR PRODUCTES^^^^");
           System.out.println("1.Manteniment de productes A/B/M/C");
           System.out.println("2.Actualitzar stocks");
           System.out.println("3.Generar comanda als proveïdors");
           System.out.println("4.Consultar comandes del dia");
           System.out.println("5.Sortir");
           System.out.println("\nTria una de les opcions");
           
           int opcio=teclat.nextInt();
           
           switch (opcio){
               case 1:
                    gestioProductes ();
                    break;
               case 2:
                    actualitzarStocks();
                    break;
               case 3:
                    generarComanda();
                    break;
               case 4:
                    consultarComandes();
                    break;
               case 5:
                   sortir=true;
                   break;
               default:
                   System.out.println("L'Opció no és vàlida");
            }
           
      
          System.out.println(("opció: ")+ opcio); 
          
         
      } while (!sortir);
        desconnexioBD();
    }
          
    static void altaProducte (){
            System.out.println("Alta productes");
        }
    static void actualitzarStocks(){
           System.out.println("Actualitzar Stock");

    }
    static void generarComanda(){
           System.out.println("Generar comanda");
    }
    static void consultarComandes(){
           System.out.println("Consultar comanda");
    }
    
    static void gestioProductes () throws SQLException {
        Scanner teclat = new Scanner (System.in);
        boolean enrere=false;
        do{
           System.out.println("^^^^MENU GESTOR PRODUCTES^^^^");
           System.out.println("1.Llista Productes");
           System.out.println("2.Alta de Productes");
           System.out.println("3.modificar Productes");
           System.out.println("4.Esborrar Productes");
           System.out.println("5.Enrere");
           System.out.println("\nTria una de les opcions");

            int opcio=teclat.nextInt();
            teclat.nextLine();


           switch (opcio){
                case 1:
                    llistaProductes();
                    break;
                case 2:
                    altaProductes();
                    break;
                case 3:
                    modificarProductes();
                    break;
                case 4:
                    esborrarProductes();
                    break;
                case 5:
                   enrere=true;
                   break;
                default:
                   System.out.println("L'Opció no és vàlida");
            }
        } while (!enrere);
                  
    } 

    public static void desconnexioBD() {
        System.out.println("Desconnectat de la BD");
    }

    

    public static void llistaProductes() throws SQLException {
        System.out.println("Llistem productes");
        String consulta ="SELECT * FROM productes ORDER BY codi_prod";
        //preparem la consulta
        PreparedStatement ps = connectarBD.prepareStatement(consulta);
        //lencem la consulta
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            System.out.println("codi_prod: " + rs.getInt("codi_prod"));
            System.out.println("model: " + rs.getString("model"));
            System.out.println("estoc: " + rs.getInt("estoc"));
            System.out.println("marca: " + rs.getString("marca"));

        }
    }
    static void altaProductes() throws SQLException{
        Scanner teclat = new Scanner (System.in);
        System.out.println("ALTA PRODUCTES");
        System.out.println("Codi:");
        String codi_prod = teclat.nextLine();
        System.out.println("Model:");
        String model = teclat.nextLine();
        System.out.println("Estoc:");
        String estoc = teclat.nextLine();
        System.out.println("Marca:");
        String marca = teclat.nextLine();        
        String sentenciaSql = "INSERT INTO productes (codi_prod, model, estoc, marca) VALUES (?, ?, ?, ?)";
        PreparedStatement sentencia = null;
        try{   
            sentencia = connectarBD.prepareStatement(sentenciaSql);
            sentencia.setString(1, codi_prod);
            sentencia.setString(2, model);
            sentencia.setString(3, estoc);
            sentencia.setString(4, marca);
            sentencia.executeUpdate();
      } catch (SQLException sqle){
            sqle.printStackTrace();
      } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }
    

    public static void modificarProductes() {
        Scanner teclat = new Scanner (System.in);
        System.out.println("MODIFICAR PRODUCTES");
        System.out.println("Codi del producte a modificar:");
        int codi_prod = teclat.nextInt();
        teclat.nextLine();
        System.out.println("Nou model:");
        String model = teclat.nextLine();
        System.out.println("Nou estoc:");
        int estoc = teclat.nextInt();
        teclat.nextLine();
        System.out.println("Nova marca:");
        String marca = teclat.nextLine();
        String sentenciaSql = "UPDATE productes SET model = ?, estoc = ?, marca = ?" + "WHERE codi_prod = ?";
        PreparedStatement sentencia = null;
 
        try {
          sentencia = connectarBD.prepareStatement(sentenciaSql);
          sentencia.setInt(4, codi_prod);
          sentencia.setString(1, model);
          sentencia.setInt(2, estoc);
          sentencia.setString(3, marca);
          sentencia.executeUpdate();
            System.out.println("Producte modificat: " + model);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
          if (sentencia != null)
                try {
                  sentencia.close();
                } catch (SQLException sqle) {
                  sqle.printStackTrace();
                }
            }
    }

    public static void esborrarProductes() {
        Scanner teclat = new Scanner (System.in);
        System.out.println("ESBORRAR PRODUCTES");
        String sentenciaSql = "DELETE FROM productes WHERE codi_prod = ?";
        System.out.println("Quin es el codi del producte a elimina?");
        int codi_prod = teclat.nextInt();
        teclat.nextLine();
        PreparedStatement sentencia = null;
 
        try {
          sentencia = connectarBD.prepareStatement(sentenciaSql);
          sentencia.setInt(1, codi_prod);
          sentencia.executeUpdate();
        } catch (SQLException sqle) {
          sqle.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                  sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
            }
    }
    static void connectarBD(){

            String servidor="jdbc:mysql://localhost:3307/";
            String bbdd="electroimp";
            String user="root";
            String password="Alastor666Radio";
            
            try{

                connectarBD = (Connection) DriverManager.getConnection(servidor + bbdd, user, password);
            }
            catch (SQLException ex) {
                ex.printStackTrace();

            }
    } 
            
}
	

    

