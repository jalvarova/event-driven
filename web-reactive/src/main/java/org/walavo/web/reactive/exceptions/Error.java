package org.walavo.web.reactive.exceptions;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Error {
    private String field;
    private String value;
    private String location;
    private String issue;
}
