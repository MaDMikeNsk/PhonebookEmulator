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
    private static boolean isValidInput (String inputStream) {
        return inputStream.matches("([a-zA-Z\\s])+.*|(\\D\\s)+|([\\d-+()\\s]+)");
    }

    private static boolean isName (String name) {
      return name.matches("([a-zA-Z\\s])+.*[^STOPLI]|(\\D[^STOPLI]\\s)+");
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

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String command = "";
        HashMap <String, String> phonebook = new HashMap<>();

        while (!command.equals("STOP")) {
            do {
                System.out.println("Введите что-нибудь:");
                command = reader.readLine().trim();
                if (!isValidInput(command)) {
                    System.out.println("Invalid input");
                }
            } while(!isValidInput(command)); //<STOP> и <LIST> тоже Name
            String contactName = "";
            String number = "";

            if (!command.equals("STOP") && !command.equals("LIST")){
                //Если ввели номер
                if (isNumber(command)) {
                    command = command.replaceAll("[+\\-()\\s]*", "");
                    if (phonebook.containsKey(command)) {
                        System.out.println(phonebook.get(command) + " /" + command + "/");
                    } else {
                        while (!isName(contactName)) {
                            System.out.println("Введите имя абонента для добавления в список контактов:");
                            contactName = reader.readLine().trim();
                            if (!isName(contactName)){
                                System.out.println("Неправильный формат ввода имени");
                            }
                        }
                        phonebook.put(command,contactName);
                    }
                //Если ввели имя
                } else if (isName(command)) {
                    if (phonebook.containsValue(command)) {
                        Set keys = getKeysByValue(phonebook, command);
                        for (Object key : keys) {
                            System.out.println(command + " /" + key + "/");
                        }

                    } else {
                        while (!isNumber(number)) {
                            System.out.println("Введите номер абонента:");
                            number = reader.readLine().trim();
                            if (!isNumber(number)) {
                                System.out.println("Неправильный формат ввода номера");
                            }
                        }
                        number = number.replaceAll("[+\\-()\\s]*", "");
                        phonebook.put(number, command);
                    }
                }

                //Если ввели команду <LIST>
                } else if (command.equals("LIST")) {
                    list(phonebook);
                //Если ввели команду <STOP>
                } else {
                    System.out.println("Программа звершила работу. До свидания!");
            }
        }
    }
}