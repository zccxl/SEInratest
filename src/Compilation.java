import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Hashtable;
public class Compilation {
    private ArrayList<String> directives;
    static Hashtable<String, String> map;
    static {
        map=new Hashtable<>();
        map.put("lui","0110111");
        map.put("lw","0000011");
        map.put("li","0010011");
    }
    public Compilation() {
        this.directives = new ArrayList<>();
    }
    public ArrayList<String> getDirectives() {
        return directives;
    }
    private static String ReadPath="C:\\Java-primer\\test\\compilation\\source.s";
    private static String WritePath="C:\\Java-primer\\test\\compilation\\binary.txt";
    public static void main(String[] args) throws Exception {
        Compilation compilation = new Compilation();
        ReadFile(compilation);
        Change(compilation);
        WriteFile(compilation);

    }
    private static void ReadFile(Compilation compilation) throws IOException {
        String str;
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ReadPath)));
        while((str=br.readLine())!=null) {
            String s =str.replaceAll(","," ");
            s=s.replaceAll("a","1");
            s=s.replaceAll("0x","");
            s=s.replaceAll("\\("," ");
            s=s.replaceAll("\\)","");
            compilation.getDirectives().add(s);
        }
    }
    private static void WriteFile(Compilation compilation) throws IOException {
        FileWriter fileWriter=new FileWriter(WritePath);
        fileWriter.write("");
        fileWriter.flush();
        fileWriter.close();
        FileWriter Writer = new FileWriter(WritePath, true);
        for (int i=0;i<compilation.getDirectives().size();i++){
            Writer.write( compilation.getDirectives().get(i)+ "\n");
        }
        Writer.flush();
        Writer.close();
    }
    private static void Change(Compilation compilation){
        for (int i=0;i<compilation.getDirectives().size();i++) {
            String[] s=compilation.getDirectives().get(i).split(" ");
            s[1]=TenToTwo(s[1],5);
            if (map.containsKey(s[0])){
                if (s[0].equals("lui")){
                    if (s[2].length()>5){
                        System.out.println("0x"+s[2]+"超出合法界限");
                        return;
                    }
                    s[2]=SixteenToTwo(s[2],20);
                    String str=s[2]+s[1]+map.get(s[0]);
                    compilation.getDirectives().set(i,TwoToSixteen(str,8));
                }
                if (s[0].equals("lw")){
                    s[2]=TenToTwo(s[2],12);
                    s[3]=TenToTwo(s[3],5);
                    String str=s[2]+s[3]+"010"+s[1]+map.get(s[0]);
                    compilation.getDirectives().set(i,TwoToSixteen(str,8));
                }
                if (s[0].equals("li")){
                    s[2]=TenToTwo(s[2],12);
                    String str=s[2]+"00000000"+s[1]+map.get(s[0]);
                    compilation.getDirectives().set(i,TwoToSixteen(str,8));
                }
            }else{
                System.out.println("语法错误，位置在："+s[0]);
            }
        }
    }
    private static String TenToTwo(String str,int length){
        Integer num =Integer.parseInt(str);
        String binary=Integer.toString(num, 2);
        str="";
        for (int i = 0; i < length-binary.length(); i++) {
            str+="0";
        }
        str+=binary;
        return str;
    }
    private static String SixteenToTwo(String str,int length){
        BigInteger saint = new BigInteger(str, 16);
        //10进制转2进制
        String binary = saint.toString(2);
        str="";
        for (int i = 0; i < length-binary.length(); i++) {
            str+="0";
        }
        str+=binary;
        return str;
    }
    private static String TwoToSixteen(String str,int length){
        int n = Integer.parseInt(str,2);
        String hex= Integer.toHexString(n);
        str="";
        for (int i = 0; i < length-hex.length(); i++) {
            str+="0";
        }
        str+=hex;
        return str;
    }
}
