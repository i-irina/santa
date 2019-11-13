import java.io.*;
import java.util.*;


public class santa {

    private static String[] dirContent;
    private static String[] newContent;
    private static File content;
   // private static Path f_input= Paths.get(System.getProperty("user.dir")+"\\input\\");
    //private static Path f_output= Paths.get(System.getProperty("user.dir")+"\\output\\");
    private static long delay = 1 * 1000;

    public static void main(String[] args) {
       //Standardization st= new Standardization();
        //st.cleanName();

        // загрузка параметров
        // пути к входной и выходной директориям, которые будет использовать утилита

        try (InputStream input = santa.class.getClassLoader().getResourceAsStream("config.properties")) {

            Properties prop = new Properties();

            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }

            //load a properties file from class path, inside static method
            prop.load(input);
            String p_input= prop.getProperty("input");
            String p_output= prop.getProperty("output");

            //get the property value and start present

            MakePresent givepresent = new MakePresent(p_input, p_output);
            new Reminder(delay);


        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }

}
