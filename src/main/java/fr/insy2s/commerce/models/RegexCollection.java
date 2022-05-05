package fr.insy2s.commerce.models;

import lombok.*;
import java.util.regex.Pattern;

@Data
@RequiredArgsConstructor
public class RegexCollection {
    private final Pattern emailPattern;
    private final Pattern namePattern;
}
