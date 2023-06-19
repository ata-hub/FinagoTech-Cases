
import java.io.*;
import java.util.*;
import java.util.stream.*;
        import static java.util.stream.Collectors.joining;
        import static java.util.stream.Collectors.toList;
class Result {
    public static int uniqueWolfs(List<Integer> arr) {
        // hasmap tanımla
        HashMap<Integer, Integer> wolfOccurenceTable = new HashMap<>();
        // Her bir kurt türünün kaç defa tespit edildiğini say, daha önce yoksa 0 a eşitle ve 1 arttır, varsa sadece 1 arttır
        for (int wolf : arr) {
            wolfOccurenceTable.put(wolf, wolfOccurenceTable.getOrDefault(wolf, 0) + 1);
        }

        int maxCount = Collections.max(wolfOccurenceTable.values());
        int minWolfId = Integer.MAX_VALUE; //başta initialize etmek için böyle, 1 den 5 e kadar bir sayı olmadığı sürece başka sayılar da olabilirdi

        // En çok tespit edilen en düşük ID'ye sahip kurt türünü bul
        for (Map.Entry<Integer, Integer> entry : wolfOccurenceTable.entrySet()) {
            if (entry.getValue() == maxCount) {
                minWolfId = Math.min(minWolfId, entry.getKey());
            }
        }

        return minWolfId;
    }
}
public class Solution {
    public static void main(String[] args) throws IOException {
        /*
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new
                FileWriter(System.getenv("OUTPUT_PATH")));
        int arrCount = Integer.parseInt(bufferedReader.readLine().trim());
        List<Integer> arr = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" ")).map(Integer::parseInt).collect(toList());
        int result = Result.uniqueWolfs(arr);
        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();
        bufferedReader.close();
        bufferedWriter.close();
         */
        Scanner scanner = new Scanner(System.in);
        int arrCount = Integer.parseInt(scanner.nextLine().trim());
        List<Integer> arr = Arrays.stream(scanner.nextLine().split(" "))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        int result = Result.uniqueWolfs(arr);
        System.out.println(result);

        scanner.close();
    }
}