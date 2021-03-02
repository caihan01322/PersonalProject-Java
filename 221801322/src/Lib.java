import java.io.*;
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
        private int charNum;
        private int lineNum;
        private static final Pattern NON_BLACK_PATTERN = Pattern.compile("^.*[^\\s]+.*$");
        /**
         * @description CoreModule类构造函数
         * @param inputPath
         */
        public CoreModule(String inputPath) throws FileNotFoundException {
            inputFile = new File(inputPath);
            charNum = 0;
            lineNum = 0;
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
         * @description 统计文件行数
         * @return lineNum
         */
        public int countLine() throws IOException {
            bufferedReader = new BufferedReader(new FileReader(inputFile));
            String lineStr = "";
            while ((lineStr = bufferedReader.readLine()) != null) {
                System.out.println(lineStr);
                if(NON_BLACK_PATTERN.matcher(lineStr).matches()) {
                    lineNum++;
                }
            }
            return lineNum;
        }
    }
}
