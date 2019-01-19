import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

class Operator {

    /*
    Вывести сообщение пользователю
    */
    private static void showMsg(String msg) {
        System.out.print(msg);
    }

    /*
    Получить данные от пользователя через консоль
    */
    private static String getConsolInfo() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    /*
    Получить данные (об источнике ввода/вывода, параметрах операции)
    */
    private static List<String> getData(String str, boolean isDataOp) {

        List<String> data = new ArrayList<>();

        String newStr = str.trim().replaceAll("[\\s]{2,}"," ");

        int n;
        if (isDataOp) n = newStr.split(" ").length;
        else n = 2;

        data.addAll(Arrays.asList(newStr.split(" ", n)));

        return data;
    }

    /*
    Проверить, существует ли файл
    */
    private static boolean isFileAvailable(String str) {

        File userFile = new File(str);
        if (!userFile.exists()) {
            showMsg("\n\nФайла "+str+" не существует.\n\n");
            return false;
        }

        return true;
    }

    /*
    Посчитать результат
    */
    private static int count(List<String> opData) {

        int result = 0;

        try {
            switch (opData.get(0)) {
                case "a":
                    for (int i = 1; i < opData.size(); i++) {
                        result = result + Integer.parseInt(opData.get(i));
                    }
                    break;

                case "b":
                    result = 1;
                    for (int i = 1; i < opData.size(); i++) {
                        result = result * Integer.parseInt(opData.get(i));
                    }
                    break;

                case "c":
                    if (opData.size() == 2) {
                        opData.add("1");
                        opData.add("0");
                    }
                    else if (opData.size() == 3)
                        opData.add("0");
                    result = Integer.parseInt(opData.get(1))
                            * Integer.parseInt(opData.get(2))
                            + Integer.parseInt(opData.get(3));
                    break;

                default:
                    showMsg("\n\nНекорректный оператор...\n\n");
                    return 0;
            }
        } catch (NumberFormatException e) {
            showMsg("\n\nНеобходимо использовать целые числа... \n\n");
            return 0;

        } catch (IndexOutOfBoundsException e) {
            showMsg("\n\nДанные не получены.\n\n");
        }

        return result;
    }

    /*
    Вывести результат
    */
    private static void showResult(List<String> dataInOut, int result) {

        if (dataInOut.get(1).equals("-"))
            showMsg("\n\nРезультат операции = " + result + "\n\n");
        else {
            if (isFileAvailable(dataInOut.get(1)))
            try(FileWriter writer = new FileWriter(dataInOut.get(1), false)) {
                String str = String.valueOf(result);
                writer.write(str);
                writer.flush();
                showMsg("\n\nРезультат записан в файл "+dataInOut.get(1)+"\n\n");
            } catch (IOException e) {
                showMsg("\n\nЧто-то пошло не так...\n\n");
            } else showMsg("\n\nФайла для записи результата не существует\n\n");
        }
    }

    /*
    Обработать данные
    */
    void operate() {

        while (true) {
            showMsg("\n\nВведите данные через пробел: 1. Вариант ввода 2. Вариант вывода\n"
                    + "Значения: '-' - консоль, 'file.txt' - имя файла\n\n");

            List<String> dataOp = new ArrayList<>();
            List<String> dataInOut = getData(getConsolInfo(), false);

            if (dataInOut.size() < 2) {
                showMsg("\n\nНе указаны все данные для продолжения работы... Программа завершена.\n\n");
                return;
            }

            if ((dataInOut.get(0).equals("-"))) {

                showMsg("\n\nВведите данные через пробел: 1. Вариант операции 2. Числа\nЗначения:" +
                        "'a' - сложение, " +
                        "'b' - умножение, " +
                        "'c' - умножение первых 2х чисел и сложение с 3м числом.\n\n");

                dataOp = getData(getConsolInfo(), true);
            } else {

                if (isFileAvailable(dataInOut.get(0))) {
                    try (FileReader reader = new FileReader(dataInOut.get(0))) {

                        String str = "";
                        int ch;
                        while ((ch = reader.read()) != -1) {
                            str += String.valueOf((char) ch);
                        }

                        str = str.replace('\n',' ');
                        dataOp = getData(str, true);

                    } catch (IOException e) {
                        showMsg("\n\nЧто-то пошло не так...\n\n");
                    }
                }
            }

            int result = count(dataOp);

            showResult(dataInOut, result);
        }
    }
}