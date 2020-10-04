package jedis;

import redis.clients.jedis.Jedis;

public class Test {

    public static void main(String[] args) {
        //创建一个jedis的对象。
        Jedis jedis = new Jedis("10.224.230.74", 6379, 500000000);
//        //调用jedis对象的方法，方法名称和redis的命令一致。
//        jedis.set("key1", "jedis test");
//        String string = jedis.get("key1");
        System.out.println(jedis.ping());
        //关闭jedis。
        jedis.close();

    }
}
