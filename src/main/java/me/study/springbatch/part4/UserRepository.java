package me.study.springbatch.part4;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByUpdatedDate(LocalDate updatedDate);

    @Query("select min(u.id) from User u")
    long findMinId();

    @Query("select max(u.id) from User u")
    long findMaxId();
}
