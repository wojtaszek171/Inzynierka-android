package pl.pollub.shoppinglist.model.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import pl.pollub.shoppinglist.model.BuddyGroup;

import static pl.pollub.shoppinglist.model.BuddyGroup.TABLE_NAME;

/**
 * DAO of intermediate table, see {@link BuddyGroup}
 *
 * @author Adrian
 * @since 2017-10-09
 */
@Dao
public interface BuddyGroupDao extends BaseDao<BuddyGroup> {
    @Override
    @Query("SELECT * FROM " + TABLE_NAME)
    List<BuddyGroup> findAll();

    @Override
    @Query("SELECT * FROM " + TABLE_NAME + " WHERE id = :id")
    BuddyGroup findById(int id);

    @Override
    @Query("SELECT COUNT(*) FROM " + TABLE_NAME)
    int count();
}
