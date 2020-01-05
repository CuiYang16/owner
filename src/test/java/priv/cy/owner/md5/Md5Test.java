package priv.cy.owner.md5;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * @author ：cuiyang
 * @description：md5测试
 * @date ：Created in 2020/1/4 14:18
 */
public class Md5Test {

  public static void main(String[] args) {
    String hashAlgorithName = "MD5";
    String password = "admin123";
    int hashIterations = 1024;
    ByteSource byteSource = ByteSource.Util.bytes("cy_salt");
    Object obj = new SimpleHash(hashAlgorithName, password, byteSource, hashIterations);
    System.out.println("加密之后的密码" + obj);
  }
}
