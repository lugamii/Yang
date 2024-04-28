package me.tulio.yang.profile.category.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.tulio.yang.profile.category.Category;

import java.util.UUID;

@Getter @Setter
@AllArgsConstructor
public class CategoryEditorData {

    private final UUID uuid;
    private final Category category;
    private String data;
}
