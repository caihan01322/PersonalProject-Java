import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author caihan01322
 *
 */
public class Lib {
    static class CoreModule {
        private File inputFile;
        private FileReader fileReader;
        private BufferedReader bufferedReader;
        public FileInputStream fileInputStream;
        private int charNum;
        private int lineNum;
        private int wordNum;
        private static final Pattern NON_BLACK_PATTERN = Pattern.compile("^.*[^\\s]+.*$");
        private static final Pattern WORD_PATTERN = Pattern.compile("\\s*[A-Za-z]{4}[A-Za-z0-9]*\\s*");
        /**
         * @description CoreModule类构造函数
         * @param inputPath
         */
        public CoreModule(String inputPath) throws FileNotFoundException {
            inputFile = new File(inputPath);
            charNum = 0;
            lineNum = 0;
            wordNum = 0;
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
         * @description 从文件中提取单词
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
                wordNum++;
            }
        }
    }
}
