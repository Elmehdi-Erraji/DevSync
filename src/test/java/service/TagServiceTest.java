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

}
