package pl.pollub.android.shoppinglist.model.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import pl.pollub.android.shoppinglist.model.Buddy;

/**
 * @author Adrian
 * @since 2017-10-03
 */

@Dao
public interface BuddyDao extends BaseDao<Buddy> {
    @Override
    @Query("SELECT * FROM buddy")
    List<Buddy> findAll();

    @Override
    @Query("SELECT * FROM buddy where id = :id")
    Buddy findById(int id);

    @Query("SELECT * FROM buddy where name LIKE :name%")
    Buddy findByName(String name);

    @Query("SELECT * FROM buddy where nickname LIKE :nickname%")
    Buddy findByNickname(String nickname);

    @Query("SELECT * FROM buddy where email LIKE :email%")
    Buddy findByEmail(String email);
}
