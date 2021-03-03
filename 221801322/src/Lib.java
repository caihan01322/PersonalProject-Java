import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author caihan01322
 *
 */
public class Lib {
    static class CoreModule {
        private File inputFile;
        private File outputFile;
        private FileReader fileReader;
        private BufferedReader bufferedReader;
        private BufferedWriter bufferedWriter;
        private FileInputStream fileInputStream;
        private int charNum;
        private int lineNum;
        private int wordNum;
        private Map<String,Long> wordFreq;
        private LinkedHashMap<String,Long> mostFreqWord;
        private static final int WORD_FREQ_LIMIT = 10;
        private static final Pattern NON_BLACK_PATTERN = Pattern.compile("^.*[^\\s]+.*$");
        private static final Pattern WORD_PATTERN = Pattern.compile("[A-Za-z]{4}[A-Za-z0-9]*");
        /**
         * @description CoreModule类构造函数
         * @param inputPath
         */
        public CoreModule(String inputPath) throws FileNotFoundException {
            inputFile = new File(inputPath);
            charNum = 0;
            lineNum = 0;
            wordNum = 0;
            wordFreq = new HashMap<String,Long>();
        }
        /**
         * @description 统计文件字符数
         * @return charNum
         */
        public int countChar() throws IOException {
            fileReader = new FileReader(inputFile);
            int fileChar;
            while((fileChar = fileReader.read()) != -1) {
                if ((0 <= fileChar) && (127 >= fileChar)) {
                    charNum++;
                }
            }
            return charNum;
        }
        /**
         * @description 统计文件单词数
         * @return wordNum
         */
        public int countWord() throws IOException {
            extractWord();
            return wordNum;
        }
        /**
         * @description 统计文件行数
         * @return lineNum
         */
        public int countLine() throws IOException {
            bufferedReader = new BufferedReader(new FileReader(inputFile));
            String lineStr = "";
            while ((lineStr = bufferedReader.readLine()) != null) {
                if(NON_BLACK_PATTERN.matcher(lineStr).matches()) {
                    lineNum++;
                }
            }
            return lineNum;
        }
        /**
         * @description 统计频率最高的10个单词
         * @return mostFreqWord
         */
        public LinkedHashMap<String,Long> countWordFreq() {
            ArrayList<Map.Entry<String, Long>> wordList = new ArrayList<Map.Entry<String,Long>>(wordFreq.entrySet());
            mostFreqWord = new LinkedHashMap<String,Long>();
            Collections.sort(wordList,(map1,map2) -> {
                if(map1.getValue() == map2.getValue()) {
                    return map1.getKey().compareTo(map2.getKey());
                } else {
                    return map2.getValue().compareTo(map1.getValue());
                }
            });
            int count = 0;
            for(Map.Entry<String, Long> mapping:wordList) {
                mostFreqWord.put(mapping.getKey(),mapping.getValue());
                count++;
                if(count == 10) {
                    break;
                }
            }
            return mostFreqWord;
        }
        /**
         * @description 将统计结果写入文件
         * @return void
         */
        public void writeToFile(String outputPath) throws IOException {
            outputFile = new File(outputPath);
            bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
            StringBuilder resultStr = new StringBuilder();
            resultStr.append("characters: " + charNum + "\n");
            resultStr.append("words: " + wordNum + "\n");
            resultStr.append("lines: " + lineNum + "\n");
            for(Map.Entry<String,Long> entry: mostFreqWord.entrySet()) {
                resultStr.append(entry.getKey() + ": " + entry.getValue() + "\n");
            }
            System.out.println(resultStr.toString());
            bufferedWriter.write(resultStr.toString());
            bufferedWriter.close();
        }
        /**
         * @description 从文件中提取单词，并统计所有单词词频
         * @return void
         */
        private void extractWord() throws IOException {
            fileInputStream = new FileInputStream(inputFile);
            int strSize = fileInputStream.available();
            byte[] strBuffer = new byte[strSize];
            fileInputStream.read(strBuffer);
            String fileStr = new String(strBuffer,"UTF-8");
            Matcher matcher = WORD_PATTERN.matcher(fileStr);
            while(matcher.find()) {
                if(wordFreq.containsKey(matcher.group(0))) {
                    wordFreq.put(matcher.group(0),(wordFreq.get(matcher.group(0)) + 1L));
                } else {
                    wordFreq.put(matcher.group(0),1L);
                }
                wordNum++;
            }
        }
    }
}
