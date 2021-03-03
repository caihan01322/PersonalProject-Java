import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author caihan01322
 * @description 与WordCount相关的类库
 */
public class Lib {
    private static final int WORD_FREQ_LIMIT = 10;
    private static final Pattern NON_BLACK_PATTERN = Pattern.compile("^.*[^\\s]+.*$");
    private static final Pattern WORD_PATTERN = Pattern.compile("[A-Za-z]{4}[A-Za-z0-9]*");
    /**
     * @description 计算核心模块，只负责计算和返回计算结果
     */
    static class CoreModule {
        private final File INPUT_FILE;
        private final Map<String,Integer> WORD_FREQ;
        private boolean isCountWordFreq;
        /**
         * @description CoreModule类构造函数
         * @param inputPath
         */
        public CoreModule(String inputPath) {
            INPUT_FILE = new File(inputPath);
            WORD_FREQ = new HashMap<>();
            isCountWordFreq = false;
        }
        /**
         * @description 统计文件字符数
         * @return charNum
         */
        public int countChar() throws IOException {
            FileReader fileReader = new FileReader(INPUT_FILE);
            int fileChar;
            int charNum = 0;
            while((fileChar = fileReader.read()) != -1) {
                if (fileChar <= 127) {
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
            if(!isCountWordFreq){
                extractWord();
                isCountWordFreq = true;
            }
            int wordNum = 0;
            for(int value : WORD_FREQ.values()) {
                wordNum += value;
            }
            return wordNum;
        }
        /**
         * @description 统计文件行数
         * @return lineNum
         */
        public int countLine() throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(INPUT_FILE));
            String lineStr;
            int lineNum = 0;
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
        public LinkedHashMap<String,Integer> countWordFreq() throws IOException {
            if(!isCountWordFreq){
                extractWord();
                isCountWordFreq = true;
            }
            ArrayList<Map.Entry<String,Integer>> wordList = new ArrayList<>(WORD_FREQ.entrySet());
            LinkedHashMap<String,Integer> mostFreqWord = new LinkedHashMap<>();
            wordList.sort((map1, map2) -> {
                if (map1.getValue().equals(map2.getValue())) {
                    return map1.getKey().compareTo(map2.getKey());
                } else {
                    return map2.getValue().compareTo(map1.getValue());
                }
            });
            int count = 0;
            for(Map.Entry<String,Integer> mapping:wordList) {
                mostFreqWord.put(mapping.getKey(),mapping.getValue());
                count++;
                if(count == WORD_FREQ_LIMIT) {
                    break;
                }
            }
            return mostFreqWord;
        }
        /**
         * @description 从文件中提取单词，并统计所有单词词频
         * @return void
         */
        private void extractWord() throws IOException {
            FileInputStream fileInputStream = new FileInputStream(INPUT_FILE);
            int strSize = fileInputStream.available();
            byte[] strBuffer = new byte[strSize];
            //noinspection ResultOfMethodCallIgnored
            fileInputStream.read(strBuffer);
            String fileStr = new String(strBuffer,StandardCharsets.UTF_8);
            Matcher matcher = WORD_PATTERN.matcher(fileStr);
            while(matcher.find()) {
                if(WORD_FREQ.containsKey(matcher.group(0))) {
                    WORD_FREQ.put(matcher.group(0),(WORD_FREQ.get(matcher.group(0)) + 1));
                } else {
                    WORD_FREQ.put(matcher.group(0),1);
                }
            }
        }
    }
    /**
     * @description 回答问题模块，负责调用计算核心进行统计并将结果写入文件
     */
    static class AnswerModule {
        private final int CHAR_NUM;
        private final int WORD_NUM;
        private final int LINE_NUM;
        private final LinkedHashMap<String,Integer> MOST_FREQ_WORD;
        public AnswerModule(CoreModule core) throws IOException {
            CHAR_NUM = core.countChar();
            WORD_NUM = core.countWord();
            LINE_NUM = core.countLine();
            MOST_FREQ_WORD = core.countWordFreq();
        }
        public void writeToFile(String outputPath) throws IOException {
            File outputFile = new File(outputPath);
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
            StringBuilder resultStr = new StringBuilder();
            resultStr.append("characters: ").append(CHAR_NUM).append("\n");
            resultStr.append("words: ").append(WORD_NUM).append("\n");
            resultStr.append("lines: ").append(LINE_NUM).append("\n");
            for(Map.Entry<String,Integer> entry : MOST_FREQ_WORD.entrySet()) {
                resultStr.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
            bufferedWriter.write(resultStr.toString());
            bufferedWriter.close();
        }
    }
}
