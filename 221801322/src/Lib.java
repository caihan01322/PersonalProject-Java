import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author caihan01322
 *
 */
public class Lib {
    static class CoreModule {
        private File inputFile;
        private FileReader fileReader;
        private int charNum;
        /**
         * @description CoreModule类构造函数
         * @param inputPath
         */
        public CoreModule(String inputPath) throws FileNotFoundException {
            inputFile = new File(inputPath);
            charNum = 0;
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
    }
}
