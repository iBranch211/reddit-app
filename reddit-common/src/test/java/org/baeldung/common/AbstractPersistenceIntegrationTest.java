
package org.baeldung.common;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.baeldung.persistence.model.IEntity;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class AbstractPersistenceIntegrationTest<T extends IEntity> {

    // tests

    // find - one

    @Test
    public final void givenEntityDoesNotExist_whenEntityIsRetrieved_thenNoEntityIsReceived() {
        // When
        final T createdEntity = getApi().findById(IDUtil.randomPositiveLong()).get();

        // Then
        assertNull(createdEntity);
    }

    @Test
    public void givenEntityExists_whenEntityIsRetrieved_thenNoExceptions() {
        final T existingEntity = persistNewEntity();
        getApi().findById(existingEntity.getId());
    }

    @Test
    public void givenEntityDoesNotExist_whenEntityIsRetrieved_thenNoExceptions() {
        getApi().findById(IDUtil.randomPositiveLong());
    }

    @Test
    public void givenEntityExists_whenEntityIsRetrieved_thenTheResultIsNotNull() {
        final T existingEntity = persistNewEntity();
        final Optional<T> retrievedEntity = getApi().findById(existingEntity.getId());
        assertTrue(retrievedEntity.isPresent());
    }

    @Test
    public void givenEntityExists_whenEntityIsRetrieved_thenEntityIsRetrievedCorrectly() {
        final T existingEntity = persistNewEntity();
        final T retrievedEntity = getApi().findById(existingEntity.getId()).get();
        assertEquals(existingEntity, retrievedEntity);
    }

    // find - all

    @Test
    public void whenAllEntitiesAreRetrieved_thenNoExceptions() {
        getApi().findAll();
    }

    @Test
    public void whenAllEntitiesAreRetrieved_thenTheResultIsNotNull() {
        final List<T> entities = getApi().findAll();

        assertNotNull(entities);
    }

    @Test
    public void givenAtLeastOneEntityExists_whenAllEntitiesAreRetrieved_thenRetrievedEntitiesAreNotEmpty() {
        persistNewEntity();

        // When
        final List<T> allEntities = getApi().findAll();

        // Then
        assertThat(allEntities, not(Matchers.<T> empty()));
    }

    @Test
    public void givenAnEntityExists_whenAllEntitiesAreRetrieved_thenTheExistingEntityIsIndeedAmongThem() {
        final T existingEntity = persistNewEntity();

        final List<T> entities = getApi().findAll();

        assertThat(entities, hasItem(existingEntity));
    }

    @Test
    public void whenAllEntitiesAreRetrieved_thenEntitiesHaveIds() {
        persistNewEntity();

        // When
        final List<T> allEntities = getApi().findAll();

        // Then
        for (final T entity : allEntities) {
            assertNotNull(entity.getId());
        }
    }

    // create

    @Test(expected = RuntimeException.class)
    public void whenNullEntityIsCreated_thenException() {
        getApi().save((T) null);
    }

    @Test
    public void whenEntityIsCreated_thenNoExceptions() {
        persistNewEntity();
    }

    @Test
    public void whenEntityIsCreated_thenEntityIsRetrievable() {
        final T existingEntity = persistNewEntity();

        assertTrue(getApi().findById(existingEntity.getId()).isPresent());
    }

    @Test
    public void whenEntityIsCreated_thenSavedEntityIsEqualToOriginalEntity() {
        final T originalEntity = createNewEntity();
        final T savedEntity = getApi().save(originalEntity);

        assertEquals(originalEntity, savedEntity);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public final void whenInvalidEntityIsCreated_thenDataException() {
        getApi().save(createInvalidNewEntity());
    }

    // update

    @Test(expected = RuntimeException.class)
    public void whenNullEntityIsUpdated_thenException() {
        getApi().save((T) null);
    }

    @Test
    public void givenEntityExists_whenEntityIsUpdated_thenNoExceptions() {
        // Given
        final T existingEntity = persistNewEntity();

        // When
        getApi().save(existingEntity);
    }

    /**
     * - can also be the ConstraintViolationException which now occurs on the
     * update operation will not be translated; as a consequence, it will be a
     * TransactionSystemException
     */
    @Test(expected = RuntimeException.class)
    public void whenEntityIsUpdatedWithFailedConstraints_thenException() {
        final T existingEntity = persistNewEntity();
        invalidate(existingEntity);

        getApi().save(existingEntity);
    }

    @Test
    public void givenEntityExists_whenEntityIsUpdated_thenUpdatesArePersisted() {
        // Given
        final T existingEntity = persistNewEntity();

        // When
        change(existingEntity);
        getApi().save(existingEntity);

        final T updatedEntity = getApi().findById(existingEntity.getId()).get();

        // Then
        assertEquals(existingEntity, updatedEntity);
    }

    protected abstract T createNewEntity();

    protected final T createInvalidNewEntity() {
        final T newEntity = createNewEntity();
        invalidate(newEntity);
        return newEntity;
    }

    protected abstract JpaRepository<T, Long> getApi();

    protected void invalidate(final T entity) {
        //
    }

    private final void change(final T entity) {
        //
    }

    protected T persistNewEntity() {
        return getApi().save(createNewEntity());
    }

}
