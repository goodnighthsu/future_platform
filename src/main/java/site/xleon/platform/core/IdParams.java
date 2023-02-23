package site.xleon.platform.core;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class IdParams {
    @NotBlank
    private List<String> ids;
}