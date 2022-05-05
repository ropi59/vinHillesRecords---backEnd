package fr.insy2s.commerce.controllers;

import fr.insy2s.commerce.dtos.tag.TagCreateDto;
import fr.insy2s.commerce.models.Tag;
import fr.insy2s.commerce.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost/")
@RequestMapping("tags")
@RequiredArgsConstructor
public class TagController {
  private final TagService tagService;

  /**
   * API to get all tags
   * @return List<TagDto> tagsDto
   */
  @GetMapping()
  public List<Tag> findAll() {
    return tagService.findAllTags();
  }

  /**
   * API to create a tag
   * @param tagCreateDto a tag to create
   * @return Tag tag
   */
  @PostMapping()
  public Tag create(@RequestBody TagCreateDto tagCreateDto) {
    return tagService.createTag(tagCreateDto);
    // TODO : change TagCreateDto to TagDtoNoId
  }

  /**
   * API to delete a tag
   * @param id of a tag
   * @return int count
   */
  @DeleteMapping("{id}")
  public int delete(@PathVariable Long id) {
    return tagService.customDeleteTagById(id);
  }
}

