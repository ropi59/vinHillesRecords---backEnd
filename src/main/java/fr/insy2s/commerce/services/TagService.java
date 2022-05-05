package fr.insy2s.commerce.services;

import fr.insy2s.commerce.mappers.TagMapper;
import fr.insy2s.commerce.dtos.tag.TagCreateDto;
import fr.insy2s.commerce.models.Tag;
import fr.insy2s.commerce.repositories.TagRepository;
import fr.insy2s.commerce.services.interfaces.TagServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TagService implements TagServiceInterface {
  private final TagRepository tagRepository;

  @Override
  public List<Tag> findAllTags() {
    return tagRepository.findAll();
  }

  @Override
  public Tag createTag(TagCreateDto tagCreateDto) {
    return tagRepository.save(TagMapper.tagCreateDtoToTag(tagCreateDto));
  }

  @Override
  public int customDeleteTagById(Long id) {
    return tagRepository.deleteTagById(id);
  }
}
