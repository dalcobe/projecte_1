package Projecte;

import static Projecte.projecte_1.connectarBD;
import com.mysql.jdbc.Connection;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

public class projecte_1 {
    static String[] proveidor = new String[100];
    static int[] contprod= new int[100];
    static final int MAXSTOCK=400;

    static Connection connectarBD = null;

    public static void main(String[] args) throws SQLException, IOException {
        boolean sortir = false;
        connectarBD();
        Scanner teclat = new Scanner(System.in);

        do {
            System.out.println("^^^^MENU GESTOR PRODUCTES^^^^");
            System.out.println("1.Manteniment de productes A/B/M/C");
            System.out.println("2.Actualitzar stocks");
            System.out.println("3.Generar comanda als proveïdors");
            System.out.println("4.Consultar comandes del dia");
            System.out.println("5.Sortir");
            System.out.println("\nTria una de les opcions");

            int opcio = teclat.nextInt();

            switch (opcio) {
                case 1:
                    gestioProductes();
                    break;
                case 2:
                    actualitzarStocks();
                    break;
                case 3:
                    generarcomandav2();
                    break;
                case 4:
                    consultarComandes();
                    break;
                case 5:
                    sortir = true;
                    break;
                default:
                    System.out.println("L'Opció no és vàlida");
            }

            System.out.println(("opció: ") + opcio);

        } while (!sortir);
        desconnexioBD();
    }
    
     static void gestioProductes() throws SQLException {
        Scanner teclat = new Scanner(System.in);
        boolean enrere = false;
        do {
            System.out.println("^^^^MENU GESTOR PRODUCTES^^^^");
            System.out.println("1.Llista Productes");
            System.out.println("2.Alta de Productes");
            System.out.println("3.modificar Productes");
            System.out.println("4.Esborrar Productes");
            System.out.println("5.Enrere");
            System.out.println("\nTria una de les opcions");

            int opcio = teclat.nextInt();
            teclat.nextLine();

            switch (opcio) {
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
                    enrere = true;
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
        String consulta = "SELECT * FROM productes ORDER BY codi_prod";
        //preparem la consulta
        PreparedStatement ps = connectarBD.prepareStatement(consulta);
        //lencem la consulta
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println("codi_prod: " + rs.getInt("codi_prod"));
            System.out.println("model: " + rs.getString("model"));
            System.out.println("estoc: " + rs.getInt("estoc"));
            System.out.println("marca: " + rs.getString("marca"));

        }
    }

    static void altaProductes() throws SQLException {
        Scanner teclat = new Scanner(System.in);
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
        try {
            sentencia = connectarBD.prepareStatement(sentenciaSql);
            sentencia.setString(1, codi_prod);
            sentencia.setString(2, model);
            sentencia.setString(3, estoc);
            sentencia.setString(4, marca);
            sentencia.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentencia != null) {
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
            }
        }
    }

    public static void modificarProductes() {
        Scanner teclat = new Scanner(System.in);
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
            if (sentencia != null) {
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
            }
        }
    }

    public static void esborrarProductes() {
        Scanner teclat = new Scanner(System.in);
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
            if (sentencia != null) {
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
            }
        }
    }


    static void actualitzarStocks() throws FileNotFoundException, IOException, SQLException {
        System.out.println("Actualitzar Stock");
        /*Scanner teclat = new Scanner(System.in);

        String actualitza = "Update productes SET estoc = estoc + ? WHERE codi_prod = ?";
        System.out.println("Codi_prod:");
        int codi_prod = teclat.nextInt();
        teclat.nextLine();
        System.out.println("Estoc");
        int estoc = teclat.nextInt();
        PreparedStatement sentencia = null;
        try {
            sentencia = connectarBD.prepareStatement(actualitza);
            sentencia.setInt(2, codi_prod);
            sentencia.setInt(1, estoc);
            sentencia.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentencia != null) {
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
            }
        }*/

        File fitxers = new File("files2/ENTRADES PENDENTS");

        if (fitxers.isDirectory()) {
            File[] fitxers1 = fitxers.listFiles();

            for (int i = 0; i < fitxers1.length; i++) {
                System.out.println(fitxers1[i].getName());
                actualitzarFitxers(fitxers1[i]);
                moureFixterAENTRADESPROCESSADES(fitxers1[i]);
            }

        }
        File fitxer3 = new File("files2/ENTRADES PROCESSADES");

    }

    static void moureFixterAENTRADESPROCESSADES(File fitxer) throws FileNotFoundException, IOException {

        FileSystem sistemaFitxers = FileSystems.getDefault();
        Path origen = sistemaFitxers.getPath("files2/ENTRADES PENDENTS/" + fitxer.getName());
        Path desti = sistemaFitxers.getPath("files2/ENTRADES PROCESSADES/" + fitxer.getName());

        Files.move(origen, desti, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("S'ha mogut a PROCESSADES el fitxer: " + fitxer.getName());

    }

    static void actualitzarFitxers(File fitxer) throws FileNotFoundException, IOException, SQLException {
        FileReader reader = new FileReader(fitxer);
        BufferedReader buffer = new BufferedReader(reader);

        String linea;
        while ((linea = buffer.readLine()) != null) {
            System.out.println(linea);
            int posSep = linea.indexOf(":");
            int codi_prod = Integer.parseInt(linea.substring(0, posSep));
            System.out.println("El codi del producte es: " + codi_prod);
            int estoc = Integer.parseInt(linea.substring(posSep + 1));
            System.out.println("El numero de unitats es: " + estoc);
            actualitzaBD(codi_prod, estoc);

        }
        buffer.close();
        reader.close();
    }

    static void actualitzaBD(int codi_prod, int estoc) throws SQLException {

        int codi_prod2 = codi_prod;

        String actualitza = "UPDATE productes SET estoc = estoc + ? WHERE codi_prod = ?";
        PreparedStatement sentencia = null;

        try {
            sentencia = connectarBD.prepareStatement(actualitza);
            sentencia.setInt(1, estoc);
            sentencia.setInt(2, codi_prod2);
            sentencia.executeUpdate();
            System.out.println("S'han afegit " + estoc + " unitats al producte: " + codi_prod);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            System.out.println("Hi ha hagut un error al actualitzar el stock");
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }
    
        /*FileReader reader = new FileReader(fitxer);
        BufferedReader buffer = new BufferedReader(reader);
        String linea;
        while ((linea = buffer.readLine()) != null) {
            System.out.println(linea);
            String LIMITADOR = ":";
            int posLimit = linea.indexOf(LIMITADOR);
            String codi_prod = linea.substring(0, posLimit);
            int entradaStock = Integer.parseInt(linea.substring(posLimit + 1));
            System.out.println("codi_prod: " + codi_prod + " entradaStock: " + entradaStock);
        }

        buffer.close();
        reader.close();

    }*/

    /*static void generarComanda() throws SQLException {
         System.out.println("Generar comanda");
        String consulta = "SELECT P. model, P.estoc, R.nom, R.codi_prov from productes P, proveïdors R where P.estoc<=20 order by R.nom;";
        PreparedStatement ps = connectarBD.prepareStatement(consulta);
        ResultSet rs = ps.executeQuery();

        String proveidorAnt = "";
        while (rs.next()) {
            if (!proveidorAnt.equals(rs.getString("nom"))) {
                proveidorAnt = rs.getString("nom");
            }
            System.out.println("canvi de proveidor: " + rs.getString("nom"));
            System.out.print("model: " + rs.getString("model"));
            System.out.print(" estoc: " + rs.getInt("estoc"));
            System.out.print(" codi_prov: " + rs.getInt("codi_prov"));
            System.out.println(" nom: " + rs.getString("nom"));

        }
       
    }*/
        

    
    static void generarcomandav2() throws SQLException, IOException {
        System.out.println("Generar comanda");
        int contprov=0;
        int contprod1=0;
        PrintWriter escritor=null;
        
        String consulta = "SELECT P. model,P.codi_prod, P.estoc, P.marca, R.nom, R.codi_prov, R. nom from productes P, proveïdors R where P.estoc<=20 order by R.nom;";
        PreparedStatement ps = connectarBD.prepareStatement(consulta);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            String actproveidor=rs.getString("R.codi_prov");
            proveidor[contprov]=rs.getString("codi_prov");
            //ESCRIVIM CAPÇALERA DEL FITXER:
            //creem el fitxer:
            escritor=escriureCapçaleraComanda(rs.getString ("nom"), contprov);
            
            //escrivim linia descriptiva
            do{
                //comprovem si el proveidor ha canviat
                if (!actproveidor.equals(rs.getString("codi_prov"))){
                    contprod[contprov]=contprod1;
                    contprod1=0;
                    contprov++;
                    proveidor[contprov]=rs.getString("codi_prov");
                    //ha canviat el proveidor
                    //hem guardo el nou proveidor
                    escritor.close();
                    actproveidor=rs.getString("codi_prov");
                    contprod1++;
                    escritor=escriureCapçaleraComanda(rs.getString("nom"), contprov);
                }
               
                escritor.println(rs.getInt("codi_prod")+"            "+rs.getString("marca")+"           "+rs.getString("nom")+"            "+(MAXSTOCK - rs.getInt("estoc")));
                contprod1++;
            } while (rs.next());
            contprod[contprov]=contprod1;
            escritor.close();

            
}
    }
    
    static void comanda(){
        for(int i=0;i<contprod.length;i++){
            System.out.println(contprod[i] + proveidor[i]);
        }
                
    }
    
    static PrintWriter escriureCapçaleraComanda(String nom, int codi_prov) throws IOException{
        FileWriter fw=null;
        BufferedWriter bf=null;
        PrintWriter escritor=null;
        fw = new FileWriter ("files2/comandes/" + nom + "_" + LocalDate.now() + ".txt", false);
        bf = new BufferedWriter(fw);
        escritor = new PrintWriter(bf);
        //escrivim dades empresa
        escritor.println("^^^^^Electroimp^^^^^");
        escritor.println("Data pedit: 31/10/2021");
        escritor.println("Codi del pedit: 9706665");
        escritor.println("Direcció: Avinguda Catalunya, 25300");
        escritor.println("____________________________________");
        escritor.println("Codi client: 245710");
        escritor.println("Telèfon: 973537843");
        escritor.println("Correu: electro@gmail.com");
        escritor.println("____________________________________");
        escritor.println("Codi         Marca           Proveïdor            Unitats");
        proveidor[codi_prov]=nom;
        
        //escrivim linia descriptiva
        return escritor;
        
    }
    
    static void consultarComandes() {
        llistatProductes();
        maxim();
        minim();
        mitjana();

    }

    static void llistatProductes() {
        System.out.println("Llistat de Productes demanats");
        for (int i = 0; contprod[i] != 0; i++) {
            System.out.println(proveidor[i] + ": " + contprod[i]);
        }
    }

    static void maxim() {
        System.out.println("\nProveidor mes sol·licitat");
        int max = contprod[0];
        int imax = 0;

        for (int i = 0; i < contprod.length; i++) {
            if (contprod[i] > max) {
                max = contprod[i];
                imax = i;
            }
        }
        System.out.println(proveidor[imax] + " se sol·liciten : " + max);

    }

    static void minim() {
        System.out.println("\nProveidor menys sol·licitat");
        int min = contprod[0];
        int imin = 0;

        for (int i = 0; contprod[i] != 0; i++) {
            if (contprod[i] < min) {
                min = contprod[i];
                imin = i;
            }
        }
        System.out.println(proveidor[imin] + " se sol·liciten : " + min);
    }

    static void mitjana() {
        System.out.println("\nMitjana de productes sol·licitats");

        double mitjana = 0, suma = 0;
        int i;
        for ( i= 0; contprod[i] !=0; i++) {
            suma += contprod[i];

        }
        mitjana = suma / i;
        System.out.println("La mitjana es " + mitjana + "\n");

    }


    static void connectarBD() {

        String servidor = "jdbc:mysql://localhost:3307/";
        String bbdd = "electroimp";
        String user = "root";
        String password = "Alastor666Radio";

        try {

            connectarBD = (Connection) DriverManager.getConnection(servidor + bbdd, user, password);
        } catch (SQLException ex) {
            ex.printStackTrace();

        }
    }

}
