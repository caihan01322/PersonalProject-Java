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
    private static final Pattern WORD_PATTERN = Pattern.compile("[a-z]{4}[a-z0-9]*");
    private static final String IO_EXCEPTION = "文件I/0出现异常！";
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
        public int countChar() {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(INPUT_FILE));
                int fileChar;
                int charNum = 0;
                while((fileChar = bufferedReader.read()) != -1) {
                    if (fileChar <= 127) {
                        charNum++;
                    }
                }
                return charNum;
            } catch (IOException e) {
                System.out.println(IO_EXCEPTION);
                e.printStackTrace();
                return 0;
            }
        }
        /**
         * @description 统计文件单词数
         * @return wordNum
         */
        public int countWord() {
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
        public int countLine() {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(INPUT_FILE));
                String lineStr;
                int lineNum = 0;
                while ((lineStr = bufferedReader.readLine()) != null) {
                    if(NON_BLACK_PATTERN.matcher(lineStr).matches()) {
                        lineNum++;
                    }
                }
                return lineNum;
            } catch (IOException e) {
                System.out.println(IO_EXCEPTION);
                e.printStackTrace();
                return 0;
            }
        }
        /**
         * @description 统计频率最高的10个单词
         * @return mostFreqWord
         */
        public LinkedHashMap<String,Integer> countWordFreq() {
            if(!isCountWordFreq) {
                extractWord();
                isCountWordFreq = true;
            }
            ArrayList<Map.Entry<String,Integer>> wordList = new ArrayList<>(WORD_FREQ.entrySet());
            LinkedHashMap<String,Integer> mostFreqWord = new LinkedHashMap<>();
            wordList.sort((map1,map2) -> {
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
        private void extractWord() {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(INPUT_FILE));
                StringBuilder strBuilder = new StringBuilder();
                String fileStr;
                while ((fileStr = bufferedReader.readLine()) != null) {
                    strBuilder.append(fileStr.toLowerCase()).append("\n");
                }
                Matcher matcher = WORD_PATTERN.matcher(strBuilder.toString());
                while(matcher.find()) {
                    if(WORD_FREQ.containsKey(matcher.group(0))) {
                        WORD_FREQ.put(matcher.group(0),(WORD_FREQ.get(matcher.group(0)) + 1));
                    } else {
                        WORD_FREQ.put(matcher.group(0),1);
                    }
                }
            } catch (IOException e) {
                System.out.println(IO_EXCEPTION);
                e.printStackTrace();
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
        public AnswerModule(CoreModule core) {
            CHAR_NUM = core.countChar();
            WORD_NUM = core.countWord();
            LINE_NUM = core.countLine();
            MOST_FREQ_WORD = core.countWordFreq();
        }
        public void writeToFile(String outputPath) {
            try {
                File outputFile = new File(outputPath);
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
                StringBuilder resultStr = new StringBuilder();
                resultStr.append("characters: ").append(CHAR_NUM).append("\n");
                resultStr.append("words: ").append(WORD_NUM).append("\n");
                resultStr.append("lines: ").append(LINE_NUM).append("\n");
                for (Map.Entry<String, Integer> entry : MOST_FREQ_WORD.entrySet()) {
                    resultStr.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                }
                bufferedWriter.write(resultStr.toString());
                bufferedWriter.close();
            } catch (IOException e) {
                System.out.println(IO_EXCEPTION);
                e.printStackTrace();
            }
        }
    }
}
