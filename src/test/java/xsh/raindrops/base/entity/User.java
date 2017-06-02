package xsh.raindrops.base.entity;
import org.msgpack.annotation.MessagePackBeans;

import java.util.concurrent.TimeUnit;

/**
 * Created by hzq on 16/7/5.
 */
@MessagePackBeans
public class User {

    private static User u = null;

    public static User getInstance() {
        if (u == null) {
            synchronized (User.class) {
                if (u == null) {
                    u = new User(5);
                }
            }

        }
        return u;
    }

    private Integer age;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public User(Integer age) {
        this.age = age;
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.age = 9;

    }

    public boolean isHigh() {
        return true;
    }

    public static String set01(String key1) {
        return null;
    }

    public static String set02(String key2) {
        return null;
    }

    public static String set03(String key3) {
        return null;
    }

    public static String set04(String key4) {
        return null;
    }


    @Override
    public String toString() {
        return "User{" +
                "age=" + age +
                '}';
    }


    public static void main(String[] args) {
        new Thread(()->{
            System.out.println(User.getInstance());
        }).start();

        new Thread(()->{
            System.out.println(User.getInstance());
        }).start();

    }
}
