package com.auspos.suburb.service;

import com.auspos.suburb.domain.Suburb;
import com.auspos.suburb.dto.SuburbDto;
import com.auspos.suburb.exception.DuplicateSuburbException;
import com.auspos.suburb.repository.SuburbRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class SuburbService {

    private final SuburbRepository suburbRepository;
    private final SuburbDtoMapper suburbDtoMapper;

    /**
     * Fetch list of suburbs by <code>postCode</code>
     * @param postCode
     * @return a a list of {@link SuburbDto} matching the <code>postCode</code> or returns and empty list
     */
    public List<SuburbDto> getSuburbsByPostCode(long postCode) {
        log.debug("Getting all suburbs by postCode {}", postCode);
        List<Suburb> suburbs = suburbRepository.findByPostCode(postCode);

        if (CollectionUtils.isEmpty(suburbs)) {
            log.info("No suburbs found with postcode {}", postCode);
            return Collections.EMPTY_LIST;
        }

        log.debug("Found {} suburbs with postcode {}", suburbs.size(), postCode);
        return suburbDtoMapper.mapDomainToDto(suburbs);
    }

    /**
     * Fetch list of suburbs by <code>suburbName</code>
     * @param suburbName
     * @return a a list of {@link SuburbDto} matching the <code>suburbName</code> or returns and empty list
     */
    public List<SuburbDto> getSuburbsBySuburbName(String suburbName) {
        log.debug("Getting all suburbs by postCode {}", suburbName);
        List<Suburb> suburbs = suburbRepository.findBySuburbNameIgnoreCase(suburbName);

        if (CollectionUtils.isEmpty(suburbs)) {
            log.info("No suburbs found with suburbName {}", suburbName);
            return Collections.EMPTY_LIST;
        }

        log.debug("Found {} suburbs with suburbName {}", suburbs.size(), suburbName);
        return suburbDtoMapper.mapDomainToDto(suburbs);
    }

    /**
     * Creates a new suburb in the DB
     * @param suburbDto
     * @exception {@link DuplicateSuburbException} when a suburb exists with the same name and postcode
     */
    public void createSuburb(SuburbDto suburbDto) {
        List<Suburb> suburbs = suburbRepository.findBySuburbNameIgnoreCaseAndPostCode(suburbDto.getSuburbName(), suburbDto.getPostCode());

        // Throw exception if a suburb already exists with the same name and postcode.
        if (!CollectionUtils.isEmpty(suburbs)) {
            String msg = String.format("A suburb already present with name: %s and postCode: %s",  suburbDto.getSuburbName(), suburbDto.getPostCode());
            log.error(msg);
            throw new DuplicateSuburbException(msg);
        }

        log.debug("Creating a suburb with name {} and postcode {}", suburbDto.getSuburbName(), suburbDto.getPostCode());
        suburbRepository.save(suburbRepository.save(suburbDtoMapper.mapDtoToDomain(suburbDto)));

        log.info("Created a suburb with name {} and postcode {}", suburbDto.getSuburbName(), suburbDto.getPostCode());
    }

}
