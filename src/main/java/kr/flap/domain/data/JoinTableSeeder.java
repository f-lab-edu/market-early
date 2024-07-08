package kr.flap.domain.data;

import java.util.List;

public interface JoinTableSeeder<T> extends Seeder{
  public void setJoinTableList(List<T> joinTableList);
}
