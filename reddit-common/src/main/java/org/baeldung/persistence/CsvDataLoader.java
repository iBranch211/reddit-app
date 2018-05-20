package org.baeldung.persistence;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

@Component
public final class CsvDataLoader {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    // API

    public <T> List<T> loadObjectList(final Class<T> type, final String fileName) {
        try {
            final CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
            final CsvMapper mapper = new CsvMapper();
            final File file = new ClassPathResource(fileName).getFile();
            final MappingIterator<T> readValues = mapper.readerFor(type).with(bootstrapSchema).readValues(file);
            return readValues.readAll();
        } catch (final Exception e) {
            logger.error("Error ocurred while loading object list from file " + fileName, e);
            return Collections.emptyList();
        }
    }

    public List<String[]> loadManyToManyRelationship(final String fileName) {
        try {
            final CsvMapper mapper = new CsvMapper();
            final CsvSchema bootstrapSchema = CsvSchema.emptySchema().withSkipFirstDataRow(true);
            mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
            final File file = new ClassPathResource(fileName).getFile();
            final MappingIterator<String[]> readValues = mapper.readerFor(String[].class).with(bootstrapSchema).readValues(file);
            return readValues.readAll();
        } catch (final Exception e) {
            logger.error("Error ocurred while loading many to many relationship from file " + fileName, e);
            return Collections.emptyList();
        }
    }

}
