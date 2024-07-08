package kr.flap.domain.data;

public interface BaseSeeder extends Seeder {
  void seed();
  boolean isDataAlreadySeeded() ;
}
