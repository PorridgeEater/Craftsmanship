package leadroyal.porridge;

/**
 * Created by wintau on 15/12/22.
 */
public class CategoryHelper {
    static String[] NameList = new String[] {"tea","study","bag","silver","chan","bracelet","vase","redware","carve"};

    static String getNameWithIndex(int index){
        return NameList[index];
    }

    static int findIndexWithName(String name){
        for (int i = 0 ; i < NameList.length ; i ++ ){
            if (NameList[i] == name){
                return i;
            }
        }
        return Integer.parseInt(null);
    }

}
