package Lesson3;


import java.io.File;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;


public class Main {
    static Scanner iScanner = new Scanner(System.in);

    public static void main(String[] args) {
        String userString = inputString("Введите данные через пробел: ");

        String[] arrayString = userString.split(" ");
        int kodError = checkEnteredDataByNumber(arrayString);
        if (kodError > 0) {
            throw new RuntimeException("Введены лишние данные");
        } else if (kodError < 0) {
            throw new RuntimeException("Введено недостаточно данных");
        }

        String[] parseData = parseUserData(arrayString);

        String line = newViewData(parseData);
        writeDataToFile(parseData[0], line);
    }

    private static String newViewData(String[] arr) {
        return "<" + arr[0] + "><" + arr[1] + "><" + arr[2] + "><" + arr[3] + "><" + arr[4] + "><"
                + arr[5] + ">\n";
    }

    private static String[] parseUserData(String[] arr) {
        String[] result = new String[6];
        boolean[] isInResult = new boolean[] { false, false, false, false, false, false };
        int fio = 3;
        for (int i = arr.length - 1; i >= 0; i--) {
            String record = arr[i];
            int len = record.length();
            if (len == 0) {
                throw new RuntimeException("Введена строка без данных");
            } else if (isNumeric(record.substring(0, 1))) {
                if (isNumeric(record)) {
                    if (isInResult[4]) {
                        throw new RuntimeException("Повторный ввод номера телефона");
                    } else {
                        result[4] = record;
                        isInResult[4] = true;
                    }
                } else if (len == 10) {
                    if (isDate(record)) {
                        if (isInResult[3]) {
                            throw new RuntimeException("Повторный ввод даты рождения");
                        } else {
                            result[3] = record;
                            isInResult[3] = true;
                        }
                    } else {
                        throw new RuntimeException("Дата рождения введено не верно, необходим формат dd.mm.yyyy");
                    }
                } else {
                    throw new RuntimeException("Длина даты рождения не соотеветсвует 10 символам");
                }
            } else if (len == 1) {
                if (record.equals("f") || record.equals("m")) {
                    if (!isInResult[5]) {
                        result[5] = record;
                        isInResult[5] = true;
                    } else {
                        if (fio > 0) {
                            result[--fio] = record;
                            isInResult[fio] = true;
                        }
                    }
                } else {
                    if (fio > 0) {
                        result[--fio] = record;
                        isInResult[fio] = true;
                    }
                }
            } else {
                if (fio > 0) {
                    result[--fio] = record;
                    isInResult[fio] = true;
                }
            }
        }
        if (!isInResult[3]) {
            throw new RuntimeException("Отсутствует дата рождения");
        }
        if (!isInResult[4]) {
            throw new RuntimeException("Отсутствует номер телефона");
        }
        if (!isInResult[5]) {
            throw new RuntimeException("Отсутствует пол");
        }

        return result;
    }

    private static boolean isDate(String dateString) {
        if (dateString == null) {
            return false;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        try {
            Date d = formatter.parse(dateString);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Integer i = Integer.parseInt(strNum);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private static int checkEnteredDataByNumber(String[] arrString) {
        return arrString.length - 6;
    }

    private static String inputString(String msg) {
        System.out.printf(msg);
        return iScanner.nextLine();
    }

    public static void writeDataToFile(String filename, String text) {
        File file = new File(filename);
        try (FileWriter fileWriter = new FileWriter(file, true)) {
            fileWriter.write(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}