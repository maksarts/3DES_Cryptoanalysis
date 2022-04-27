package artsishevskiy.rz_3des;

import java.util.ArrayList;

public class Cryptoanalysis {
    public static final String latin = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    public static final String digits = "0123456789";
    public static final String symbols = "=+-_)(*&?^:%$;#№@!~.,";

    private int length;
    private int stepSize;
    //ArrayList<char[]> matrix;
    private char[] chars;
    private String cryptedText;
    private ArrayList<Integer> indexes;


    public Cryptoanalysis(int len, boolean isLat, boolean isDig, boolean isSymb, String crypt, int step){
        stepSize = step;
        length = len;
        cryptedText = crypt;
        String temp = "";
        if(isLat) temp += latin;
        if(isDig) temp += digits;
        if(isSymb) temp += symbols;
        chars = temp.toCharArray();

        indexes = new ArrayList<Integer>();

        for(int i = 0; i < length; i++){
            indexes.add(0);
        }
    }

    public String GetCryptedText(){
        return cryptedText;
    }

    private boolean UpdateIndexes(){
        int i = indexes.size()-1;
        while(i >= 0){
            if(indexes.get(i) < chars.length - 1){
                indexes.set(i, indexes.get(i) + 1);
                return true;
            }
            else{
                indexes.set(i, 0);
                i--;
            }
        }
        return false;
    }

    public StringBuilder NextPermutation(){
        StringBuilder perm = new StringBuilder();
        for(int i = 0; i < length; i++){
            perm.append(chars[indexes.get(i)]); // получаем на каждую позицию символ по индексу
        }
        return perm;
    }

    // возвращаем список из очередных stepSize перестановок
    public ArrayList<String> MakeStep(){
        int i = stepSize - 1;
        ArrayList<String> result = new ArrayList<String>();

        result.add(new String(NextPermutation()));

        while(i > 0 && UpdateIndexes()){
            result.add(new String(NextPermutation()));
            i--;
        }
        return result;
    }
}
