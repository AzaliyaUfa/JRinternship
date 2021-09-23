package com.game.controller;

import com.game.PlayerDTO;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayersRepository;
import com.game.service.PlayerService;
import com.game.service.PlayerValidation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/rest/players")
public class PlayersController {

    PlayersRepository playersRepository;
    PlayerService playerService;
    PlayerValidation playerValidation;

    public PlayersController(PlayersRepository playersRepository,
                             PlayerService playerService,
                             PlayerValidation playerValidation) {
        this.playerValidation = playerValidation;
        this.playerService = playerService;
        this.playersRepository = playersRepository;
    }

    @GetMapping("")
    public ResponseEntity<List<Player>> getPlayersList(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "race", required = false) Race race,
            @RequestParam(value = "profession", required = false) Profession profession,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "banned", required = false) Boolean banned,
            @RequestParam(value = "minExperience", required = false) Integer minExperience,
            @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
            @RequestParam(value = "minLevel", required = false) Integer minLevel,
            @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
            @RequestParam(value = "order", required = false, defaultValue = "ID") PlayerOrder order,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize
    ) {
        List<Player> playerList = playersRepository.findPlayersBySomeAttributes(name, title,
                playerService.raceToString(race), playerService.profToString(profession),
                playerService.longToDate(after), playerService.longToDate(before),
                banned, minExperience, maxExperience, minLevel, maxLevel,
                PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName())));
        return new ResponseEntity<>(playerList, HttpStatus.OK);
    }

    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Player> createPlayer(@Validated @RequestBody PlayerDTO playerDTO,
                                               BindingResult bindingResult) {

        playerValidation.validate(playerDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            Player newPlayer = playerService.createPlayer(playerDTO);
            return new ResponseEntity<>(newPlayer, HttpStatus.OK);
        }


    }

    @GetMapping("/count")
    public Integer getPlayersCount(@RequestParam(value = "name", required = false) String name,
                                   @RequestParam(value = "title", required = false) String title,
                                   @RequestParam(value = "race", required = false) Race race,
                                   @RequestParam(value = "profession", required = false) Profession profession,
                                   @RequestParam(value = "after", required = false) Long after,
                                   @RequestParam(value = "before", required = false) Long before,
                                   @RequestParam(value = "banned", required = false) Boolean banned,
                                   @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                   @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                   @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                   @RequestParam(value = "maxLevel", required = false) Integer maxLevel) {
        return playersRepository.findPlayersBySomeAttributes(name, title,
                playerService.raceToString(race), playerService.profToString(profession),
                playerService.longToDate(after), playerService.longToDate(before), banned, minExperience, maxExperience, minLevel, maxLevel,
                PageRequest.of(0, Integer.MAX_VALUE)).size();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerBtId(@PathVariable("id") Long id) {
        if (id <= 0 || !id.toString().matches("\\d+")) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        if (!playersRepository.existsById(id)) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(playersRepository.findPlayerById(id), HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Player> updatePlayerBtId(@PathVariable("id") Long id, @RequestBody PlayerDTO playerDTO) {
        if (id <= 0 || !id.toString().matches("\\d+") || playerDTO == null ||
                playerService.validateBeforeUpdate(playerDTO) > 0) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        if (!playersRepository.existsById(id)) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        Player player = playerService.updatePlayer(playersRepository.findPlayerById(id), playerDTO);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Player> deletePlayerBtId(@PathVariable("id") Long id) {
        if (id <= 0 || !id.toString().matches("\\d+")) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        if (!playersRepository.existsById(id)) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        playersRepository.deleteById(id);
        return new ResponseEntity<>(playersRepository.findPlayerById(id), HttpStatus.OK);
    }


    @GetMapping("/test")
    public void testGet(@RequestBody PlayerDTO playerDTO) {
        System.out.println(playerDTO);
    }

    @PostMapping("/test")
    public void testPost(@RequestBody PlayerDTO playerDTO) {
        System.out.println(playerDTO);
    }

}
