import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
class Test{
    public static void main(String[] args){
        BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));

        while(true){
            try{
                String input = buff.readLine();
                if(input != null){
                    System.out.println(input);
                }else{
                    break;
                }
            }catch(IOException e){
                System.err.println("error");
            }
        }
    }
}


