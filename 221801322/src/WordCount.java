/**
 * @author caihan01322
 * @description 统计文件内容指标
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
    public void running() {
        Lib.CoreModule core = new Lib.CoreModule(inputPath);
        Lib.AnswerModule answer= new Lib.AnswerModule(core);
        answer.writeToFile(outputPath);
    }
    /**
     * @description 程序运行入口
     * @param args
     */
    public static void main(String[] args) {
        try {
            if (args.length < 2) {
                throw new Exception();
            }
        } catch (Exception e) {
            System.out.println("命令行参数个数不足2个！");
            e.printStackTrace();
            return ;
        }
        WordCount wordCount = new WordCount(args[0],args[1]);
        wordCount.running();
    }
}
