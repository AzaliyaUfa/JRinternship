package com.game.repository;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface PlayersRepository extends JpaRepository<Player, Long> {

    @Query(value = "SELECT * FROM player where (:name IS NULL OR name LIKE CONCAT('%',:name,'%')) AND\n" +
            "    (:title IS NULL OR title LIKE CONCAT('%',:title,'%')) AND\n" +
            "    (:race IS NULL OR race = :race) AND\n" +
            "    (:profession IS NULL OR profession = :profession) AND\n" +
            "    (:after IS NULL OR birthday > :after) AND\n" +
            "    (:before IS NULL OR birthday < :before) AND\n" +
            "    (:banned IS NULL OR banned = :banned) AND\n" +
            "    (:minExperience IS NULL OR experience >= :minExperience) AND\n" +
            "    (:maxExperience IS NULL OR experience <= :maxExperience) AND\n" +
            "    (:minLevel IS NULL OR level >= :minLevel) AND\n" +
            "    (:maxLevel IS NULL OR level <= :maxLevel)", nativeQuery = true)
    List<Player> findPlayersBySomeAttributes(@Param("name") String name, @Param("title") String title,
                                             @Param("race") String race,
                                             @Param("profession") String profession,
                                             @Param("after") Date after, @Param("before") Date before,
                                             @Param("banned") Boolean banned,
                                             @Param("minExperience") Integer minExperience,
                                             @Param("maxExperience") Integer maxExperience,
                                             @Param("minLevel") Integer minLevel,
                                             @Param("maxLevel") Integer maxLevel,
                                             Pageable pageable);
    boolean existsById(Long id);

    Player findPlayerById(Long id);
}
