import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

enum RomanNumeral {
    I(1), IV(4), V(5), IX(9), X(10),
    XL(40), L(50), XC(90), C(100);
    final private int value;
    RomanNumeral(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
    public static List<RomanNumeral> getReverseSortedValues() {
        return Arrays.stream(values())
                .sorted(Comparator.comparing((RomanNumeral e) -> e.value).reversed())
                .collect(Collectors.toList());
    }
}

public class Main {
    public static int RomanToArabic(String input) {
        String romanNumeral = input.toUpperCase();
        int result = 0;

        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();

        int i = 0;

        while ((romanNumeral.length() > 0) && (i < romanNumerals.size())) {
            RomanNumeral symbol = romanNumerals.get(i);
            if (romanNumeral.startsWith(symbol.name())) {
                result += symbol.getValue();
                romanNumeral = romanNumeral.substring(symbol.name().length());
            } else {
                i++;
            }
        }

        if (romanNumeral.length() > 0) result = -1;

        return result;
    }

    public static String ArabicToRoman(int number) {
        if ((number <= 0) || (number > 101)) {
            throw new IllegalArgumentException(number + " вне допустимого диапазона (0,101]");
        }

        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();

        int i = 0;
        StringBuilder sb = new StringBuilder();

        while ((number > 0) && (i < romanNumerals.size())) {
            RomanNumeral currentSymbol = romanNumerals.get(i);
            if (currentSymbol.getValue() <= number) {
                sb.append(currentSymbol.name());
                number -= currentSymbol.getValue();
            } else {
                i++;
            }
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);


        while (true) {
            System.out.print("Введите выражение: ");
            String cmd = console.nextLine();
            if (cmd == "s") System.exit(0);
            System.out.println(Calc(cmd));
        }
    }

    private static int Calculate(int num1, int num2, char oper)
    {
        int result;
        switch (oper) {
            case '+':
                result = num1 + num2;
                break;
            case '-':
                result = num1 - num2;
                break;
            case '*':
                result = num1 * num2;
                break;
            case '/':
                try {
                    result = num1 / num2;
                } catch (Exception e) {
                    throw new RuntimeException("Нельзя делить на ноль ("+ e +")");
                }
                break;
            default:
                throw new RuntimeException("Неверная операция");
        }
        return result;
    }

    public static String Calc(String calcInput) {
        char oper = '+';
        int result;
        char[] uChar = new char[10];
        for (int i = 0; i < calcInput.length(); i++) {
            if (i >= uChar.length) break;
            uChar[i] = calcInput.charAt(i);
            if (uChar[i] == '+') oper = '+';
            else if (uChar[i] == '-') oper = '-';
            else if (uChar[i] == '*') oper = '*';
            else if (uChar[i] == '/') oper = '/';
        }
        String uCharString = String.valueOf(uChar).toUpperCase();
        String[] nums = uCharString.split("[+-/*]");
        if(nums.length > 2) throw new RuntimeException("Использование более одного оператора не допускается");
        if(nums.length < 2) throw new RuntimeException("Не найден второй операнд");
        String tmpNum1 = nums[0].trim();
        String tmpNum2 = nums[1].trim();
        int romanNum1 = RomanToArabic(tmpNum1);
        int romanNum2 = RomanToArabic(tmpNum2);
        if((romanNum1 > 10 || romanNum2 > 10)) throw new RuntimeException("Доступны операции с числами от 1(I) до 10(X)");
        if(!(romanNum1 < 0 && romanNum2 < 0))
        {
            if(romanNum1 < 0 || romanNum2 < 0) throw new RuntimeException("Разные системы счисления");
            result = Calculate(romanNum1, romanNum2, oper);
            if(result < 1) throw new RuntimeException("Недопустимый результат в выбранной системе счисления");
            return "Результат: " + ArabicToRoman(result);
        }
        int arabicNum1 = Integer.parseInt(tmpNum1);
        int arabicNum2 = Integer.parseInt(tmpNum2);
        if((arabicNum1 > 10 || arabicNum2 > 10)) throw new RuntimeException("Доступны операции с числами от 1 до 10");
        result = Calculate(arabicNum1, arabicNum2, oper);
        return "Результат: " + result;
    }
}

