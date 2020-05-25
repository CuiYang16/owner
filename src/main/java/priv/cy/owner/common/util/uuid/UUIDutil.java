package priv.cy.owner.common.util.uuid;

import java.util.UUID;

public class UUIDutil {
  public static String getUUID() {
    return UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
  }
}
