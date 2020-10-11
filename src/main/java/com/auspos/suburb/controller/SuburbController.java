package com.auspos.suburb.controller;


import com.auspos.suburb.dto.SuburbDto;
import com.auspos.suburb.service.SuburbService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path = "/v{version}/suburb", produces = MediaType.APPLICATION_JSON_VALUE)
public class SuburbController {

    private SuburbService suburbService;

    @GetMapping("/postcode/{postCode}")
    public List<SuburbDto> getSuburbsByPostCode(@PathVariable long postCode, @PathVariable int version) {
        log.info("Received request to get suburbs by postcode {}", postCode);
        return suburbService.getSuburbsByPostCode(postCode);
    }

    @GetMapping("/name/{suburbName}")
    public List<SuburbDto> getSuburbsByName(@PathVariable String suburbName, @PathVariable int version) {
        log.info("Received request to get suburbs by postcode {}", suburbName);
        return suburbService.getSuburbsBySuburbName(suburbName);
    }

    @PostMapping
    public void createSuburb(@Valid @RequestBody SuburbDto suburbDto, @PathVariable int version) {
        log.info("Received request to create new suburb with name {}", suburbDto.getSuburbName());
        suburbService.createSuburb(suburbDto);
    }
}
