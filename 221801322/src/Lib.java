import java.io.*;
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
        private File inputFile;
        private Map<String,Integer> wordFreq;
        private boolean isCountWordFreq;
        /**
         * @description CoreModule类构造函数
         * @param inputPath
         */
        public CoreModule(String inputPath) {
            inputFile = new File(inputPath);
            wordFreq = new HashMap<>();
            isCountWordFreq = false;
        }
        /**
         * @description 统计文件字符数
         * @return charNum
         */
        public int countChar() throws IOException {
            FileReader fileReader = new FileReader(inputFile);
            int fileChar;
            int charNum = 0;
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
            if(!isCountWordFreq){
                extractWord();
                isCountWordFreq = true;
            }
            int wordNum = 0;
            for(int value : wordFreq.values()) {
                wordNum += value;
            }
            return wordNum;
        }
        /**
         * @description 统计文件行数
         * @return lineNum
         */
        public int countLine() throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));
            String lineStr = "";
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
            ArrayList<Map.Entry<String,Integer>> wordList = new ArrayList<>(wordFreq.entrySet());
            LinkedHashMap<String,Integer> mostFreqWord = new LinkedHashMap<>();
            Collections.sort(wordList,(map1,map2) -> {
                if(map1.getValue() == map2.getValue()) {
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
            FileInputStream fileInputStream = new FileInputStream(inputFile);
            int strSize = fileInputStream.available();
            byte[] strBuffer = new byte[strSize];
            fileInputStream.read(strBuffer);
            String fileStr = new String(strBuffer,"UTF-8");
            Matcher matcher = WORD_PATTERN.matcher(fileStr);
            while(matcher.find()) {
                if(wordFreq.containsKey(matcher.group(0))) {
                    wordFreq.put(matcher.group(0),(wordFreq.get(matcher.group(0)) + 1));
                } else {
                    wordFreq.put(matcher.group(0),1);
                }
            }
        }
    }
    /**
     * @description 回答问题模块，负责调用计算核心进行统计并将结果写入文件
     */
    static class AnswerModule {
        private File outputFile;
        private BufferedWriter bufferedWriter;
        private int charNum;
        private int lineNum;
        private int wordNum;
        private LinkedHashMap<String,Integer> mostFreqWord;
        public AnswerModule(CoreModule core) throws IOException {
            charNum = core.countChar();
            lineNum = core.countWord();
            wordNum = core.countLine();
            mostFreqWord = core.countWordFreq();
        }
        public void writeToFile(String outputPath) throws IOException {
            outputFile = new File(outputPath);
            bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
            StringBuilder resultStr = new StringBuilder();
            resultStr.append("characters: " + charNum + "\n");
            resultStr.append("words: " + wordNum + "\n");
            resultStr.append("lines: " + lineNum + "\n");
            for(Map.Entry<String,Integer> entry: mostFreqWord.entrySet()) {
                resultStr.append(entry.getKey() + ": " + entry.getValue() + "\n");
            }
            bufferedWriter.write(resultStr.toString());
            bufferedWriter.close();
        }
    }
}
