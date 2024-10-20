package service;

import domain.Tag;
import exception.TagException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.TagRepositoryInterface;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TagServiceTest {

    @Mock
    private TagRepositoryInterface tagRepository;

    @InjectMocks
    private TagService tagService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testInsertTag_ValidTag() {
        Tag tag = new Tag();
        tag.setName("tag1");
        when(tagRepository.save(tag)).thenReturn(tag);
        Tag result = tagService.insertTag(tag);
        assertEquals(tag, result);
        verify(tagRepository, times(1)).save(tag);
    }

    @Test
    public void testInsertTag_EmptyName() {
        Tag tag = new Tag();
        tag.setName("");

        TagException exception = assertThrows(
                TagException.class,
                () -> tagService.insertTag(tag)
        );

        assertEquals("Tag name cannot be null or empty.", exception.getMessage());
        verify(tagRepository, never()).save(any(Tag.class));
    }

    @Test
    public void testInsertTag_LongName() {
        Tag tag = new Tag();
        tag.setName("a".repeat(51));

        TagException exception = assertThrows(
                TagException.class,
                () -> tagService.insertTag(tag)
        );
        assertEquals("Tag name cannot exceed 50 characters.", exception.getMessage());
        verify(tagRepository, never()).save(any(Tag.class));
    }

    @Test
    public void testUpsertTag_ValidTag() {
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("tag1");

        when(tagRepository.save(tag)).thenReturn(tag);

        Tag result = tagService.insertTag(tag);
        assertEquals(tag, result);
        verify(tagRepository, times(1)).save(tag);
    }

    @Test
    public void testUpdateTag_NullId(){
        Tag tag = new Tag();
        tag.setName("tag1");

        TagException exception = assertThrows(
                TagException.class,
                () -> tagService.updateTag(tag)
        );

        assertEquals("Tag ID cannot be null when updating a tag.", exception.getMessage());
        verify(tagRepository, never()).save(any(Tag.class));
    }

    @Test
    public void testUpdateTag_EmptyName(){
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("");

        TagException exception = assertThrows(
                TagException.class,
                () -> tagService.updateTag(tag)
        );
        assertEquals("Tag name cannot be null or empty.", exception.getMessage());
        verify(tagRepository, never()).save(any(Tag.class));
    }

}
