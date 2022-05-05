package fr.insy2s.commerce.mappers;

import fr.insy2s.commerce.dtos.tag.TagCreateDto;
import fr.insy2s.commerce.models.Tag;

public class TagMapper {

  private  TagMapper() {}

  /**
   * Convert a tagDTO into a tag
   * @param tagCreateDto is information of a tag
   * @return a tag entity
   */
  public static Tag tagCreateDtoToTag(TagCreateDto tagCreateDto) {
    Tag tag = new Tag();
    tag.setName(tagCreateDto.getName());
    tag.setProducts(tagCreateDto.getProducts());
    return tag;
    // TODO : may I have return with an id ?
  }

  /**
   * Convert a tag to a tagDTO
   * @param tag information of a tag
   * @return a tagDTO
   */
  public static TagCreateDto tagToTagCreateDto(Tag tag) {
    TagCreateDto tagCreateDto = new TagCreateDto();
    tagCreateDto.setName(tag.getName());
    tagCreateDto.setProducts(tag.getProducts());
    return tagCreateDto;
  }

}
