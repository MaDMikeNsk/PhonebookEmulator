/* [M5_L5] - Написать умный эмулятор телефонной книги. Если в неё ввести новое имя, она должна запросить номер телефона.
 Если в неё ввести новый номер телефона, должна запросить имя. Если введённое имя или номер телефона найдены,
 должна вывести дополнительную информацию: номер или имя, соответственно.
 Команда LIST должна выводить всех абонентов в алфавитном порядке с номерами телефонов.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    private static boolean isName (String name) {
        if (name.equals("STOP")||name.equals("LIST")) {
            return false;
        } else return name.matches(".+[a-zA-Z]+"); //Имя должно содержать хотя бы одну букву
    }

    private static boolean isNumber (String number) {
        return number.matches("[\\d-+()\\s]+");
    }

    private static void list (HashMap contacts) {
        if (!contacts.isEmpty()) {
            ArrayList<String> sortedContacts = new ArrayList<>();
            Object[] keySet = contacts.keySet().toArray();
            for (Object key : keySet) {
                sortedContacts.add(contacts.get(key) + " /" + key + "/");
            }
            Collections.sort(sortedContacts);
            for (String item : sortedContacts) {
                System.out.println(item);
            }
        } else {
            System.out.println("Список контактов пуст");
        }
    }

    private static <T, E> Set<T> getKeysByValue(Map<T, E> map, E value) {
        Set<T> keys = new HashSet<T>();
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                keys.add(entry.getKey());
            }
        }
        return keys;
    }
//
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String command = "";
        HashMap <String, String> phoneBook = new HashMap<>();
        final String ANSI_RED = "\u001B[31m";
        final String ANSI_RESET = "\u001B[0m";

        while (!command.equals("STOP")) {
            System.out.println("Введите имя или номер, или операционные команды (STOP,LIST):");
            command = reader.readLine().trim();
            String contactName = "";
            String contactNumber = "";

            if (!command.equals("STOP") && !command.equals("LIST")){
                //Если ввели номер
                if (isNumber(command)) {
                    contactNumber = command.replaceAll("[+\\-()\\s]*", "");
                    if (phoneBook.containsKey(contactNumber)) {
                        System.out.println(phoneBook.get(contactNumber) + " /" + contactNumber + "/");
                    } else {
                        while (!isName(contactName)) {
                            System.out.println("Введите имя абонента ");
                            contactName = reader.readLine().trim();
                            if (!isName(contactName)){
                                System.out.println(ANSI_RED + "Неверный формат имени" + ANSI_RESET);
                            }
                        }
                        phoneBook.put(contactNumber,contactName);
                    }

                //Если ввели имя
                } else if (isName(command)) {
                    contactName = command;
                    if (phoneBook.containsValue(contactName)) {
                        Set keys = getKeysByValue(phoneBook, contactName);
                        for (Object key : keys) {
                            System.out.println(contactName + " /" + key + "/");
                        }

                    } else {
                        while (!isNumber(contactNumber)) {
                            System.out.println("Введите номер абонента:");
                            contactNumber = reader.readLine().trim();
                            if (!isNumber(contactNumber)) {
                                System.out.println(ANSI_RED + "Неверный формат номера" + ANSI_RESET);
                            }
                        }
                        contactNumber = contactNumber.replaceAll("[+\\-()\\s]*", "");
                        phoneBook.put(contactNumber, contactName);
                    }
                }

                //Если ввели команду <LIST>
                } else if (command.equals("LIST")) {
                    list(phoneBook);

                //Если ввели команду <STOP>
                } else {
                    System.out.println("Программа звершила работу. До свидания!");
            }
        }
    }
}