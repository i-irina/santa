import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

import java.math.BigDecimal;
import static java.nio.file.Files.probeContentType;
import static java.nio.file.Files.readAllLines;

public class MakePresent {
    private static String[] dirContent; //папка с входными файлами
    private static String[] newContent; //список файлов
    private static File content; //новый файл
    private static String finput, foutput; //путь к входной и выходой директориям
    private static Standardization st=new Standardization();

    public MakePresent(String f_input, String f_output) {
        finput = f_input;
        foutput = f_output;
        content = new File(finput);
        dirContent = content.list();
        st.accountBalance();

    }

    //сканировать папку с файлами, проверить текстовый ли файл
    public static void checkDir() {
        newContent = content.list();

        for (int f = 0; f < newContent.length; f++) {
            if (!checkString(newContent[f], dirContent)) {
                String fil = "";
                Path file = Paths.get(finput + "\\" + newContent[f]);

                try {
                    //тип содержимого файла
                    fil = probeContentType(Paths.get(file.toString()));

                    String type = fil.split("/")[0];

                    if (type.equals("text")) {

                        List<String> data = new ArrayList();

                        try {
                            data = readAllLines(file, StandardCharsets.UTF_8);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //если file пуст
                        if ((data.size() <=1)||(file.toFile().length() == 0))
                        {
                            System.out.println("Входной файл "+file+" не соотвествует заданным параметрам системы.");
                        }
                        else {

                            //формируем список подарков и записываем в файл
                            givepodar(file, f, data);
                            // удаляем файл после завершения обработки
                            File finput = new File(Paths.get(file.toString()).toString());
                            System.out.println(file.toString() + " Обработан файл");
                            finput.delete();
                        }

                    }
                } catch (FileNotFoundException e) {
                System.out.println("error "+file+" file NOT FOUND");
                }catch (IOException e) {
                    e.printStackTrace();
                }



                refresh();
            }
        }
    }

    //функция стандартизации строки в ФИО
    public static List<String> updatefio(List<String> sp) {
        for (int i = 0; i <= sp.size() - 1; i++) {
            String temp3=(sp.get(i)).replaceAll("\\d", "").replaceAll("[\\s\\p{Z}]+", " ").trim();

            if ((temp3!=null)&& (temp3!=" ")&& (temp3!="")&& (!temp3.isEmpty())){
                temp3 = st.cleanName2(temp3);

                if (temp3!=null) { //
                    sp.set(i, temp3.trim());
                }
                else
                {
                    System.out.println("В списке дарителей подозрительный участник: "+sp.get(i)+" требуется ручная проверка");
                }
            }
            else
            {
                sp.set(i, (sp.get(i)).replaceAll("\\d", "").replaceAll("[\\s\\p{Z}]+", " ").trim());
            }

        }

        return sp;
    }

    public static void givepodar(Path adressfile, int n, List<String> data) {
        Path file = adressfile;

            data.toString().replaceAll("[\\s]{2,}", " ").trim();
            Collections.sort(data);
            data.removeAll(Arrays.asList("", null, " ", "\r\n"));


            for (int i = 0; i < data.size() - 1; i++) {
                //если 2 рядом стоящих супруги, разделяем их следующим за ними в очереди
                String temp1 = (data.get(i)).replaceAll("[^0-9]+", "");
                String temp2 = (data.get(i + 1)).replaceAll("[^0-9]+", "");
                String temp3 = "";

                if (temp1 != null) {
                    if ((temp1.equals(temp2))) { //если дарители супруги
                        if ((data.size() - 1)>2) {
                            if ((i + 1) >= (data.size() - 1)) {
                                temp3 = data.get(i + 1);
                                data.set(i + 1, data.get(i - 2));
                                data.set(i - 2, temp3.trim());

                            } else {
                                temp3 = data.get(i + 1);
                                data.set(i + 1, data.get(i + 2));
                                data.set(i + 2, temp3.trim());
                            }
                        }
                        else // если дарители супруги и всего дарителей 2
                        {
                            System.out.println("Входной файл "+file+" не соотвествует заданным параметрам системы. Невозможно определить дарителей");
                        }
                    }
                }

            }

            //если 1-й и последний супруги
            if ((data.get(0)).replaceAll("[^0-9]+", "").equals((data.get(data.size() - 1)).replaceAll("[^0-9]+", ""))) {
                String temp3 = data.get(1);
                data.set(1, data.get(0));
                data.set(0, temp3.trim());
            }

            //стандартифизировать список ФИО
            if (st.accountBalance().doubleValue()>(0.10*data.size()))
            {
                    data=updatefio(data);
            }
            else
            {
                System.out.println("Данные Дарителей не могут быть отформатированы. Необходимо пополнить баланс.");
            }

            //еще раз очищваем список если остались пустые строки
            data.removeAll(Arrays.asList("", null, " ", "\r\n"));

            /*for (int i = 0; i <= data.size() - 1; i++) {
                System.out.println(data.get(i));
            }*/


            SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY_MM_dd_HH_mm_SS_mmm");
            String fname = (newContent[n]).replaceAll(".txt", "") + "_" + dateFormat.format(new Date());

//записываем итоговый список в файл
            Writer sr = null;
            try {
                sr = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(foutput + "\\" + fname + ".txt"), "utf-8"));

                try {
                    for (int i = 0; i != data.size() - 1; ++i) {
                        //for (int i = 0; i < data.size() - 1; i++) {
                        if ((data.get(i) != null) && (data.get(i) != " ") && (data.get(i) != "") && (!data.get(i).isEmpty())) {

                            sr.write((String) data.get(i).trim() + " -> " + (String) data.get(i + 1).trim() + "\r\n");
                        }
                    }

                    sr.write((String) data.get(data.size() - 1).trim() + " -> " + (String) data.get(0).trim() + "\r\n");
                    sr.close();
                } finally {
                    if (sr != null) {
                        sr.close();
                    }

                }
            } catch (FileNotFoundException e) {
                System.out.println("error write file ");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


    }



    public static boolean checkString(String a,String[] Content)
    {
        for (int i=0;i<Content.length;i++)
        {
            if (a.equals(Content[i])) return true;
        }
        return false;
    }

    public static void refresh()
    {
        dirContent=content.list();
    }
}


