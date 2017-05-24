package com.zx;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by 97038 on 2017-05-10.
 */
public class RedisTest {

    public static void main(String[] args) {
        Set<HostAndPort> jedisClusterNode = new HashSet<HostAndPort>();
        jedisClusterNode.add(new HostAndPort("192.168.0.104",7001));
        jedisClusterNode.add(new HostAndPort("192.168.0.104",7002));
        jedisClusterNode.add(new HostAndPort("192.168.0.104",7003));
        jedisClusterNode.add(new HostAndPort("192.168.0.104",7004));
        jedisClusterNode.add(new HostAndPort("192.168.0.104",7005));
        jedisClusterNode.add(new HostAndPort("192.168.0.104",7006));
        jedisClusterNode.add(new HostAndPort("192.168.0.104",7007));

        JedisPoolConfig cfg = new JedisPoolConfig();
        cfg.setMaxTotal(100);

        JedisCluster jc =new JedisCluster(jedisClusterNode,6000,100,cfg);

        System.out.println(jc.set("a","111"));
        System.out.println(jc.set("b","222"));
        System.out.println(jc.set("c","333"));
        System.out.println(jc.set("d","444"));
        System.out.println(jc.set("e","555"));

        System.out.println(jc.get("a"));
        System.out.println(jc.get("b"));
        System.out.println(jc.get("c"));
        System.out.println(jc.get("d"));
        System.out.println(jc.get("e"));

    }
}
