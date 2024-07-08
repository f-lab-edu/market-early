package kr.flap.config;

public enum SeederRange {
  CART(1000),
  CATEGORY(100),
  DELIVERY(3000),
  ORDER(3000),
  PRODUCT(9000),
  RESERVE(5000),
  SELLER(9000),
  STORAGE(9000),
  SUBPRODUCT(18000),
  USER_ADDRESS(5000),
  USER(1000),
  DEFAULT_VALUE(1000);


  private int range;

  SeederRange(int defaultRange) {
    this.range = defaultRange;
  }

  public int getRange() {
    return range;
  }

  public void setRange(int range) {
    this.range = range;
  }

  public static void updateRange(String entityType, int range) {
    try {
      SeederRange.valueOf(entityType.toUpperCase()).setRange(range);
    } catch (IllegalArgumentException e) {
      System.err.println("Invalid entity type provided: " + entityType);
    }
  }
}
