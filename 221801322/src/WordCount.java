import java.io.*;
import java.util.Map;

/**
 * @author caihan01322
 *
 */
public class WordCount {
    public String inputPath;
    public String outputPath;
    /**
     * @description WordCount类构造函数
     * @param inputPath,outputPath
     */
    public WordCount(String inputPath,String outputPath) {
        this.inputPath = inputPath;
        this.outputPath = outputPath;
    }
    /**
     * @description 运行主函数
     * @return void
     */
    public void running() throws IOException {
        Lib.CoreModule core = new Lib.CoreModule(inputPath);
        System.out.println("characters: " + core.countChar());
        System.out.println("words: " + core.countWord());
        System.out.println("lines: " + core.countLine());
        Map<String,Long> mostFreqWord = core.countWordFreq();
        for(Map.Entry<String,Long> entry: mostFreqWord.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
    /**
     * @description 程序运行入口
     * @param args
     */
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("参数不足");
            return ;
        }
        WordCount wordCount = new WordCount(args[0],args[1]);
        wordCount.running();
    }
}
