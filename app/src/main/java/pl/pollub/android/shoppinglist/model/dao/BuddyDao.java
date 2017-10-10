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
    @Query("SELECT * FROM buddy WHERE id = :id")
    Buddy findById(int id);

    @Override
    @Query("SELECT COUNT(*) FROM buddy")
    int count();

    // TODO: to search only part of the string, consult this: https://stackoverflow.com/a/44185385/5982796
    @Query("SELECT * FROM buddy WHERE name LIKE :name")
    Buddy findByName(String name);

    @Query("SELECT * FROM buddy WHERE nickname LIKE :nickname")
    Buddy findByNickname(String nickname);

    @Query("SELECT * FROM buddy WHERE email LIKE :email")
    Buddy findByEmail(String email);
}
