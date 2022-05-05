package fr.insy2s.commerce.services.interfaces;

import fr.insy2s.commerce.dtos.tag.TagCreateDto;
import fr.insy2s.commerce.models.Tag;

import java.util.List;

public interface TagServiceInterface {

  public List<Tag> findAllTags();

  public Tag createTag(TagCreateDto tagCreateDto);

  public int customDeleteTagById( Long id);
}
