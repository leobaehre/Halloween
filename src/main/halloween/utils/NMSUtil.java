package jam.codedred.halloween.utils;

import java.lang.reflect.Field;

public class NMSUtil {

    public static Field getPrivateField(Class<?> clazz, String fieldName) {
        try {
         Field field = clazz.getDeclaredField(fieldName);
         field.setAccessible(true);

         return field;

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

}
