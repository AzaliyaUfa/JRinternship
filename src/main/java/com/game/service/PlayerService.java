package com.game.service;

import com.game.PlayerDTO;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayersRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

@Service
public class PlayerService {

    private PlayersRepository playersRepository;

    public PlayerService(PlayersRepository playersRepository) {
        this.playersRepository = playersRepository;
    }

    //ModelMapper modelMapper;

    public Player createPlayer (PlayerDTO playerDTO) {

        Player player = new Player();

        player.setName(playerDTO.getName());
        player.setTitle(playerDTO.getTitle());
        player.setRace(playerDTO.getRace());
        player.setProfession(playerDTO.getProfession());
        player.setBirthday(Date.from(Instant.ofEpochMilli(playerDTO.getBirthday()).atZone(ZoneId.of("UTC")).toLocalDateTime().toInstant(ZoneOffset.UTC)));
        if (playerDTO.getBanned() == null) {
            player.setBanned(false);
        } else {
            player.setBanned(playerDTO.getBanned());
        }
        player.setExperience(playerDTO.getExperience());
        setLevelData(playerDTO, player);
        return playersRepository.save(player);
    }

    private void setLevelData(PlayerDTO playerDTO, Player player) {
        player.setLevel(Math.toIntExact((long) ((Math.sqrt(2500 + 200 * playerDTO.getExperience()) - 50) / 100.0)));
        player.setUntilNextLevel(50 * (player.getLevel() + 1) * (player.getLevel() + 2) - playerDTO.getExperience());
    }

    public Date longToDate(Long num) {
        if (num != null) {
            return new Date(num);
        } else {
            return null;
        }
    }

    public String raceToString(Race race) {
        if (race != null) {
            return race.name();
        } else {
            return null;
        }
    }

    public String profToString(Profession profession) {
        if (profession != null) {
            return profession.name();
        } else {
            return null;
        }
    }

    public Player updatePlayer(Player player, PlayerDTO playerDTO) {
        if (playerDTO.getName() != null) {
            player.setName(playerDTO.getName());
        }
        if (playerDTO.getTitle() != null) {
            player.setTitle(playerDTO.getTitle());
        }
        if (playerDTO.getExperience() != null) {
            player.setExperience(playerDTO.getExperience());
            setLevelData(playerDTO, player);
        }
        if (playerDTO.getBanned() != null) {
            player.setBanned(playerDTO.getBanned());
        }
        if (playerDTO.getBirthday() != null) {
            //System.out.println(playerDTO.getBirthday().getTime());
            player.setBirthday(Date.from(Instant.ofEpochMilli(playerDTO.getBirthday()).atZone(ZoneId.of("UTC")).toLocalDateTime().toInstant(ZoneOffset.UTC)));
            System.out.println(player.getBirthday());
        }
        if (playerDTO.getProfession() != null) {
            player.setProfession(playerDTO.getProfession());
        }
        if (playerDTO.getRace() != null) {
            player.setRace(playerDTO.getRace());
        }
        return playersRepository.save(player);
    }

    public int validateBeforeUpdate(PlayerDTO playerDTO) {
        int errorCount = 0;
        if (playerDTO.getName() != null && playerDTO.getName().length() > 12) {
            errorCount++;
        }
        if (playerDTO.getTitle() != null && playerDTO.getTitle().length() > 30) {
            errorCount++;
        }
        if (playerDTO.getExperience() != null && (playerDTO.getExperience() < 0 ||
                playerDTO.getExperience() > 10_000_000)) {
            errorCount++;
        }
        if (playerDTO.getBirthday() != null &&
                (playerDTO.getBirthday() < 0 ||
                        playerDTO.getBirthday() < (new GregorianCalendar(2000, Calendar.JANUARY, 1).getTimeInMillis()) ||
                        playerDTO.getBirthday()> (new GregorianCalendar(3000, Calendar.DECEMBER, 31).getTimeInMillis()))) {
            errorCount++;
        }
        return errorCount;
    }
}
